package com.trade.order.dto;

import com.trade.order.entity.TradeOrder;

public class OrderResponse {

    private Long orderId;
    private String orderStatusCd;
    private String stockCd;
    private int quantity;

    private OrderResponse() {}

    public static OrderResponse from(TradeOrder order) {
        OrderResponse res = new OrderResponse();
        res.orderId = order.getOrderId();
        res.orderStatusCd = order.getOrderStatusCd();
        res.stockCd = order.getStockCd();
        res.quantity = order.getQuantity();
        return res;
    }

    public Long getOrderId() { return orderId; }
    public String getOrderStatusCd() { return orderStatusCd; }
    public String getStockCd() { return stockCd; }
    public int getQuantity() { return quantity; }
}
