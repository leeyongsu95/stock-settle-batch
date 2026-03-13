package com.trade.order.dto;

import java.math.BigDecimal;

/**
 * 매수 요청 DTO.
 */
public class OrderRequest {

    private Long memberKey;        // 회원 고유키
    private String stockCd;        // 종목 코드
    private int quantity;          // 주문 수량
    private BigDecimal price;      // 주문 가격

    public Long getMemberKey() { return memberKey; }
    public void setMemberKey(Long memberKey) { this.memberKey = memberKey; }

    public String getStockCd() { return stockCd; }
    public void setStockCd(String stockCd) { this.stockCd = stockCd; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
