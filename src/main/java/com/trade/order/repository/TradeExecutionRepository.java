package com.trade.order.repository;

import com.trade.order.entity.TradeExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TradeExecutionRepository extends JpaRepository<TradeExecution, Long> {

    /**
     * 해당 주문의 누적 체결 수량 — 부분 체결 판정용.
     */
    @Query("SELECT COALESCE(SUM(te.execQty), 0) FROM TradeExecution te WHERE te.orderId = :orderId")
    int sumExecQtyByOrderId(@Param("orderId") Long orderId);
}
