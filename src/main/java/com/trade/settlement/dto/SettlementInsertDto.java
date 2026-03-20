package com.trade.settlement.dto;

import java.math.BigDecimal;

/**
 * 정산 결과 — MyBatis Bulk Insert + 잔고 반영용.
 */
public class SettlementInsertDto {

    private Long execSeq;               // 체결 PK
    private Long orderId;               // 주문 PK
    private Long memberKey;             // 회원 고유키
    private String settlementTypeCd;    // 정산 유형 (NORMAL/MARGIN_CALL)
    private BigDecimal settlementAmt;   // 정산 대상 금액
    private BigDecimal commissionRate;  // 적용 수수료율
    private BigDecimal commission;      // 수수료
    private BigDecimal tax;             // 세금
    private BigDecimal netAmt;          // 최종 정산 금액
    private String leverageYn;          // 레버리지 여부
    private BigDecimal balanceChange;   // 잔고 변동 금액 (매수: -수수료, 매도: +순입금액)

    public Long getExecSeq() { return execSeq; }
    public void setExecSeq(Long execSeq) { this.execSeq = execSeq; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getMemberKey() { return memberKey; }
    public void setMemberKey(Long memberKey) { this.memberKey = memberKey; }

    public String getSettlementTypeCd() { return settlementTypeCd; }
    public void setSettlementTypeCd(String settlementTypeCd) { this.settlementTypeCd = settlementTypeCd; }

    public BigDecimal getSettlementAmt() { return settlementAmt; }
    public void setSettlementAmt(BigDecimal settlementAmt) { this.settlementAmt = settlementAmt; }

    public BigDecimal getCommissionRate() { return commissionRate; }
    public void setCommissionRate(BigDecimal commissionRate) { this.commissionRate = commissionRate; }

    public BigDecimal getCommission() { return commission; }
    public void setCommission(BigDecimal commission) { this.commission = commission; }

    public BigDecimal getTax() { return tax; }
    public void setTax(BigDecimal tax) { this.tax = tax; }

    public BigDecimal getNetAmt() { return netAmt; }
    public void setNetAmt(BigDecimal netAmt) { this.netAmt = netAmt; }

    public String getLeverageYn() { return leverageYn; }
    public void setLeverageYn(String leverageYn) { this.leverageYn = leverageYn; }

    public BigDecimal getBalanceChange() { return balanceChange; }
    public void setBalanceChange(BigDecimal balanceChange) { this.balanceChange = balanceChange; }
}
