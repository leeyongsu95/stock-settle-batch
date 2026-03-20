package com.trade.settlement.dto;

import java.math.BigDecimal;

/**
 * 미정산 체결 조회 결과 — MyBatis Reader용.
 */
public class ExecutionSettlementDto {

    private Long execSeq;               // 체결 PK
    private Long orderId;               // 주문 PK
    private Long memberKey;             // 회원 고유키
    private String stockCd;             // 종목 코드
    private String orderTypeCd;         // 주문 유형 (BUY/SELL)
    private BigDecimal execPrice;       // 체결 가격
    private int execQty;                // 체결 수량
    private BigDecimal execAmt;         // 체결 금액
    private String leverageYn;          // 레버리지 여부
    private BigDecimal commissionRate;  // 회원별 수수료율

    public Long getExecSeq() { return execSeq; }
    public void setExecSeq(Long execSeq) { this.execSeq = execSeq; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getMemberKey() { return memberKey; }
    public void setMemberKey(Long memberKey) { this.memberKey = memberKey; }

    public String getStockCd() { return stockCd; }
    public void setStockCd(String stockCd) { this.stockCd = stockCd; }

    public String getOrderTypeCd() { return orderTypeCd; }
    public void setOrderTypeCd(String orderTypeCd) { this.orderTypeCd = orderTypeCd; }

    public BigDecimal getExecPrice() { return execPrice; }
    public void setExecPrice(BigDecimal execPrice) { this.execPrice = execPrice; }

    public int getExecQty() { return execQty; }
    public void setExecQty(int execQty) { this.execQty = execQty; }

    public BigDecimal getExecAmt() { return execAmt; }
    public void setExecAmt(BigDecimal execAmt) { this.execAmt = execAmt; }

    public String getLeverageYn() { return leverageYn; }
    public void setLeverageYn(String leverageYn) { this.leverageYn = leverageYn; }

    public BigDecimal getCommissionRate() { return commissionRate; }
    public void setCommissionRate(BigDecimal commissionRate) { this.commissionRate = commissionRate; }
}
