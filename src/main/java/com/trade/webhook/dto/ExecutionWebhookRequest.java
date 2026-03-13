package com.trade.webhook.dto;

import java.math.BigDecimal;

/**
 * 증권사 체결 웹훅 요청 DTO.
 */
public class ExecutionWebhookRequest {

    private Long orderId;          // 주문 PK
    private Long memberKey;        // 회원 고유키
    private String stockCd;        // 종목 코드
    private BigDecimal execPrice;  // 체결 가격
    private int execQty;           // 체결 수량
    private String brokerExecId;   // 증권사 체결 고유번호

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getMemberKey() { return memberKey; }
    public void setMemberKey(Long memberKey) { this.memberKey = memberKey; }

    public String getStockCd() { return stockCd; }
    public void setStockCd(String stockCd) { this.stockCd = stockCd; }

    public BigDecimal getExecPrice() { return execPrice; }
    public void setExecPrice(BigDecimal execPrice) { this.execPrice = execPrice; }

    public int getExecQty() { return execQty; }
    public void setExecQty(int execQty) { this.execQty = execQty; }

    public String getBrokerExecId() { return brokerExecId; }
    public void setBrokerExecId(String brokerExecId) { this.brokerExecId = brokerExecId; }
}
