package com.trade.order.service;

import com.trade.common.exception.BusinessException;
import com.trade.member.entity.MemberBalance;
import com.trade.member.entity.MemberBalanceHistory;
import com.trade.member.repository.MemberBalanceHistoryRepository;
import com.trade.member.repository.MemberBalanceRepository;
import com.trade.order.dto.OrderRequest;
import com.trade.order.entity.TradeOrder;
import com.trade.order.repository.TradeOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 매수 트랜잭션 처리 — 락 획득부터 주문 생성까지 하나의 트랜잭션.
 * OrderService와 분리한 이유: 같은 클래스 내부 호출은 @Transactional 프록시가 안 탄다.
 */
@Service
public class OrderTransactionService {

    @Autowired
    private MemberBalanceRepository memberBalanceRepository;

    @Autowired
    private TradeOrderRepository tradeOrderRepository;

    @Autowired
    private MemberBalanceHistoryRepository memberBalanceHistoryRepository;

    /**
     * 동시 차감 방지 범위 내에서 예수금 차감 + 주문 생성만 수행.
     * 증권사 API 같은 외부 통신은 여기서 하면 안 된다 — 락 점유 시간이 늘어나서 병목.
     */
    @Transactional
    public TradeOrder execute(OrderRequest request) {
        BigDecimal totalAmt = request.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));

        // FOR UPDATE로 예수금 조회
        MemberBalance balance = memberBalanceRepository.findByMemberKeyForUpdate(request.getMemberKey());
        if (balance == null) {
            throw new BusinessException("계좌 정보를 찾을 수 없습니다.");
        }

        BigDecimal beforeBal = balance.getBalance();

        // 예수금 차감 (부족하면 예외)
        balance.deduct(totalAmt);

        // 주문 생성
        TradeOrder order = TradeOrder.createBuyOrder(
                request.getMemberKey(), request.getStockCd(),
                request.getQuantity(), request.getPrice()
        );
        tradeOrderRepository.save(order);

        // 잔액 변동 내역
        MemberBalanceHistory history = MemberBalanceHistory.ofBuyDeduct(
                request.getMemberKey(), totalAmt, beforeBal, balance.getBalance(), order.getOrderId()
        );
        memberBalanceHistoryRepository.save(history);

        return order;
    }
}
