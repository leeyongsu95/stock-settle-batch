package com.trade.order.dto;

import com.trade.order.entity.TradeOrder;

/**
 * 매수 응답 DTO.
 */
public class OrderResponse {

    private Long orderId;          // 주문 PK
    private String orderStatusCd;  // 주문 상태
    private String stockCd;        // 종목 코드
    private int quantity;          // 주문 수량

    private OrderResponse() {}

    public static OrderResponse from(TradeOrder order) {
        OrderResponse res = new OrderResponse();
        res.orderId = order.getOrderId();
        res.orderStatusCd = order.getOrderStatusCd().name();
        res.stockCd = order.getStockCd();
        res.quantity = order.getQuantity();
        return res;
    }

    public Long getOrderId() { return orderId; }
    public String getOrderStatusCd() { return orderStatusCd; }
    public String getStockCd() { return stockCd; }
    public int getQuantity() { return quantity; }
}
