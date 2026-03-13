package com.trade.member.repository;

import com.trade.member.entity.MemberBalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberBalanceHistoryRepository extends JpaRepository<MemberBalanceHistory, Long> {
}
