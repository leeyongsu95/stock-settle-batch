package com.trade.order.dto;

import java.math.BigDecimal;

public class OrderRequest {

    private Long memberKey;
    private String stockCd;
    private int quantity;
    private BigDecimal price;

    public Long getMemberKey() { return memberKey; }
    public void setMemberKey(Long memberKey) { this.memberKey = memberKey; }

    public String getStockCd() { return stockCd; }
    public void setStockCd(String stockCd) { this.stockCd = stockCd; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}
