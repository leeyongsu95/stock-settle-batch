package com.trade.member.entity;

import com.trade.common.exception.BusinessException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "t_member_bal")
public class MemberBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bal_seq")
    private Long balSeq;

    @Column(name = "member_key")
    private Long memberKey;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    protected MemberBalance() {}

    public Long getMemberKey() { return memberKey; }
    public BigDecimal getBalance() { return balance; }

    /**
     * 예수금 차감 — 동시 차감 방지를 위해 호출 전 FOR UPDATE 필수.
     */
    public void deduct(BigDecimal amount) {
        if (this.balance.compareTo(amount) < 0) {
            throw new BusinessException("예수금이 부족합니다. 현재 잔액: " + this.balance);
        }
        this.balance = this.balance.subtract(amount);
    }
}
