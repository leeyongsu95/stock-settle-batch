package com.trade.order.entity;

import com.trade.webhook.dto.ExecutionWebhookRequest;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "t_trade_execution")
public class TradeExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exec_seq")
    private Long execSeq;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "member_key")
    private Long memberKey;

    @Column(name = "stock_cd")
    private String stockCd;

    @Column(name = "exec_price")
    private BigDecimal execPrice;

    @Column(name = "exec_qty")
    private Integer execQty;

    @Column(name = "exec_amt")
    private BigDecimal execAmt;

    @Column(name = "broker_exec_id")
    private String brokerExecId;

    @Column(name = "settled_yn")
    private String settledYn;

    @Column(name = "received_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date receivedAt;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    protected TradeExecution() {}

    public static TradeExecution of(ExecutionWebhookRequest req) {
        TradeExecution exec = new TradeExecution();
        exec.orderId = req.getOrderId();
        exec.memberKey = req.getMemberKey();
        exec.stockCd = req.getStockCd();
        exec.execPrice = req.getExecPrice();
        exec.execQty = req.getExecQty();
        exec.execAmt = req.getExecPrice().multiply(BigDecimal.valueOf(req.getExecQty()));
        exec.brokerExecId = req.getBrokerExecId();
        exec.settledYn = "N";
        exec.receivedAt = new Date();
        exec.createdAt = new Date();
        return exec;
    }

    public Long getExecSeq() { return execSeq; }
    public Long getOrderId() { return orderId; }
    public Integer getExecQty() { return execQty; }
}
