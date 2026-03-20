package com.trade.margin.dto;

import java.math.BigDecimal;

/**
 * 반대매매 대상 — MyBatis 집계 쿼리 결과.
 * 회원별 보유 종목 단위로 1건씩 조회된다.
 */
public class MarginCallTargetDto {

    private Long memberKey;            // 회원 고유키
    private String stockCd;            // 보유 종목 코드
    private int holdQty;               // 보유 수량
    private BigDecimal avgPrice;       // 평균 매입 단가
    private BigDecimal currentPrice;   // 종목 현재가
    private BigDecimal overdueAmt;     // 연체 미수금 합계

    public Long getMemberKey() { return memberKey; }
    public void setMemberKey(Long memberKey) { this.memberKey = memberKey; }

    public String getStockCd() { return stockCd; }
    public void setStockCd(String stockCd) { this.stockCd = stockCd; }

    public int getHoldQty() { return holdQty; }
    public void setHoldQty(int holdQty) { this.holdQty = holdQty; }

    public BigDecimal getAvgPrice() { return avgPrice; }
    public void setAvgPrice(BigDecimal avgPrice) { this.avgPrice = avgPrice; }

    public BigDecimal getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }

    public BigDecimal getOverdueAmt() { return overdueAmt; }
    public void setOverdueAmt(BigDecimal overdueAmt) { this.overdueAmt = overdueAmt; }
}
