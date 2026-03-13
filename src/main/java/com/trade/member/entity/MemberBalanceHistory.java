package com.trade.member.entity;

import com.trade.common.constants.ChangeType;
import com.trade.common.constants.RefType;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "change_type_cd")
    private ChangeType changeTypeCd;

    @Column(name = "change_amt")
    private BigDecimal changeAmt;

    @Column(name = "before_bal")
    private BigDecimal beforeBal;

    @Column(name = "after_bal")
    private BigDecimal afterBal;

    @Column(name = "ref_id")
    private Long refId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ref_type_cd")
    private RefType refTypeCd;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    protected MemberBalanceHistory() {}

    public static MemberBalanceHistory ofBuyDeduct(Long memberKey, BigDecimal amount,
                                                   BigDecimal beforeBal, BigDecimal afterBal,
                                                   Long orderId) {
        MemberBalanceHistory his = new MemberBalanceHistory();
        his.memberKey = memberKey;
        his.changeTypeCd = ChangeType.BUY;
        his.changeAmt = amount.negate();
        his.beforeBal = beforeBal;
        his.afterBal = afterBal;
        his.refId = orderId;
        his.refTypeCd = RefType.ORDER;
        his.createdAt = new Date();
        return his;
    }

    /**
     * 매수 취소 시 예수금 복구 내역 — changeAmt가 양수면 복구, 음수면 차감.
     */
    public static MemberBalanceHistory ofBuyRollback(Long memberKey, BigDecimal amount,
                                                     BigDecimal beforeBal, BigDecimal afterBal,
                                                     Long orderId) {
        MemberBalanceHistory his = new MemberBalanceHistory();
        his.memberKey = memberKey;
        his.changeTypeCd = ChangeType.BUY;
        his.changeAmt = amount;
        his.beforeBal = beforeBal;
        his.afterBal = afterBal;
        his.refId = orderId;
        his.refTypeCd = RefType.ORDER;
        his.createdAt = new Date();
        return his;
    }
}
