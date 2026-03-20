package com.trade.settlement.repository;

import com.trade.settlement.dto.ExecutionSettlementDto;
import com.trade.settlement.dto.SettlementInsertDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface SettlementMapper {

    List<ExecutionSettlementDto> selectUnsettledExecutions(@Param("limit") int limit);

    void bulkInsertSettlements(@Param("list") List<SettlementInsertDto> list);

    void updateSettledYn(@Param("execSeqs") List<Long> execSeqs);

    void updateMemberBalance(@Param("memberKey") Long memberKey, @Param("amount") BigDecimal amount);

    BigDecimal selectMemberBalance(@Param("memberKey") Long memberKey);

    void insertBalanceHistory(@Param("memberKey") Long memberKey,
                              @Param("changeTypeCd") String changeTypeCd,
                              @Param("changeAmt") BigDecimal changeAmt,
                              @Param("beforeBal") BigDecimal beforeBal,
                              @Param("afterBal") BigDecimal afterBal,
                              @Param("refId") Long refId,
                              @Param("refTypeCd") String refTypeCd);
}
