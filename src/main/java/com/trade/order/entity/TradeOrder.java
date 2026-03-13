package com.trade.order.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "t_trade_order")
public class TradeOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "member_key")
    private Long memberKey;

    @Column(name = "stock_cd")
    private String stockCd;

    @Column(name = "order_type_cd")
    private String orderTypeCd;

    @Column(name = "order_src_cd")
    private String orderSrcCd;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "total_amt")
    private BigDecimal totalAmt;

    @Column(name = "order_status_cd")
    private String orderStatusCd;

    @Column(name = "leverage_yn")
    private String leverageYn;

    @Column(name = "ordered_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderedAt;

    @Column(name = "filled_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date filledAt;

    protected TradeOrder() {}

    public static TradeOrder createBuyOrder(Long memberKey, String stockCd,
                                            int quantity, BigDecimal price) {
        TradeOrder order = new TradeOrder();
        order.memberKey = memberKey;
        order.stockCd = stockCd;
        order.orderTypeCd = "BUY";
        order.orderSrcCd = "MANUAL";
        order.quantity = quantity;
        order.price = price;
        order.totalAmt = price.multiply(BigDecimal.valueOf(quantity));
        order.orderStatusCd = "PENDING";
        order.leverageYn = "N";
        order.orderedAt = new Date();
        return order;
    }

    public Long getOrderId() { return orderId; }
    public Long getMemberKey() { return memberKey; }
    public String getStockCd() { return stockCd; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getTotalAmt() { return totalAmt; }
    public String getOrderStatusCd() { return orderStatusCd; }
}
