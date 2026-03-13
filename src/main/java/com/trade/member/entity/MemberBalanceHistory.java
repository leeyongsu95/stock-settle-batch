package com.trade.member.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "t_member_bal_his")
public class MemberBalanceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "his_seq")
    private Long hisSeq;

    @Column(name = "member_key")
    private Long memberKey;

    @Column(name = "change_type_cd")
    private String changeTypeCd;

    @Column(name = "change_amt")
    private BigDecimal changeAmt;

    @Column(name = "before_bal")
    private BigDecimal beforeBal;

    @Column(name = "after_bal")
    private BigDecimal afterBal;

    @Column(name = "ref_id")
    private Long refId;

    @Column(name = "ref_type_cd")
    private String refTypeCd;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    protected MemberBalanceHistory() {}

    public static MemberBalanceHistory ofBuyDeduct(Long memberKey, BigDecimal amount,
                                                   BigDecimal beforeBal, BigDecimal afterBal,
                                                   Long orderId) {
        MemberBalanceHistory his = new MemberBalanceHistory();
        his.memberKey = memberKey;
        his.changeTypeCd = "BUY";
        his.changeAmt = amount.negate();
        his.beforeBal = beforeBal;
        his.afterBal = afterBal;
        his.refId = orderId;
        his.refTypeCd = "ORDER";
        his.createdAt = new Date();
        return his;
    }
}
