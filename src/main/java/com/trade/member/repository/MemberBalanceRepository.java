package com.trade.member.repository;

import com.trade.member.entity.MemberBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;

public interface MemberBalanceRepository extends JpaRepository<MemberBalance, Long> {

    /**
     * FOR UPDATE — 동시 차감 방지.
     * 락 범위를 t_member_bal로 한정하여 t_member 조회에는 영향 없도록 설계.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT mb FROM MemberBalance mb WHERE mb.memberKey = :memberKey")
    MemberBalance findByMemberKeyForUpdate(@Param("memberKey") Long memberKey);
}
