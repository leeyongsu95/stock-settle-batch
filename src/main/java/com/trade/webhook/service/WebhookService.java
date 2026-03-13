package com.trade.webhook.service;

import com.trade.common.constants.OrderStatus;
import com.trade.common.exception.BusinessException;
import com.trade.order.entity.TradeExecution;
import com.trade.order.entity.TradeOrder;
import com.trade.order.repository.TradeExecutionRepository;
import com.trade.order.repository.TradeOrderRepository;
import com.trade.webhook.dto.ExecutionWebhookRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 증권사 체결 웹훅 처리.
 * Redis 중복 방지 --> 체결 저장 --> 주문 상태 변경 (자동 감지)
 */
@Service
public class WebhookService {

    private static final Logger log = LoggerFactory.getLogger(WebhookService.class);

    @Autowired
    private WebhookDuplicateChecker duplicateChecker;

    @Autowired
    private TradeExecutionRepository executionRepository;

    @Autowired
    private TradeOrderRepository orderRepository;

    @Transactional
    public void processExecution(ExecutionWebhookRequest request) {

        // 1) Redis 중복 수신 방지
        if (duplicateChecker.isDuplicate(request.getBrokerExecId())) {
            log.info("중복 웹훅 무시 — brokerExecId: {}", request.getBrokerExecId());
            return;
        }

        // 2) 체결 결과 저장
        TradeExecution execution = TradeExecution.of(request);
        executionRepository.save(execution);

        // 3) 주문 상태 변경 — 값만 바꾸면 JPA가 알아서 UPDATE 날림
        TradeOrder order = orderRepository.findOne(request.getOrderId());
        if (order == null) {
            throw new BusinessException("주문 정보를 찾을 수 없습니다. orderId: " + request.getOrderId());
        }

        int totalExecQty = executionRepository.sumExecQtyByOrderId(request.getOrderId());
        if (totalExecQty >= order.getQuantity()) {
            order.updateStatus(OrderStatus.FILLED);
        } else {
            order.updateStatus(OrderStatus.PARTIAL);
        }

        log.info("체결 처리 완료 — orderId: {}, 상태: {}, 누적체결: {}/{}",
                order.getOrderId(), order.getOrderStatusCd(), totalExecQty, order.getQuantity());
    }
}
