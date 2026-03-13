package com.trade.order.service;

import com.trade.common.exception.BusinessException;
import com.trade.order.dto.OrderRequest;
import com.trade.order.dto.OrderResponse;
import com.trade.order.entity.TradeOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 매수 주문 흐름 제어.
 * Redis 중복 방지 --> 트랜잭션(차감+주문) --> 증권사 API 순서로 처리한다.
 */
@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderDuplicateChecker duplicateChecker;

    @Autowired
    private OrderTransactionService transactionService;

    @Autowired
    private BrokerApiClient brokerApiClient;

    public OrderResponse placeBuyOrder(OrderRequest request) {

        // 1) Redis 중복 방지
        if (duplicateChecker.isDuplicate(request.getMemberKey(), request.getStockCd())) {
            throw new BusinessException("중복 요청입니다. 잠시 후 다시 시도해주세요.");
        }

        // 2) 트랜잭션: 예수금 잠금 --> 차감 --> 주문 생성
        TradeOrder order = transactionService.execute(request);

        // 3) 트랜잭션 밖에서 증권사 API 호출 — 락 점유 시간에 영향 없음
        try {
            brokerApiClient.sendBuyOrder(order);
        } catch (Exception e) {
            log.error("증권사 API 호출 실패 — 주문번호: {}", order.getOrderId(), e);
        }

        return OrderResponse.from(order);
    }
}
