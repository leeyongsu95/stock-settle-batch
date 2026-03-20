package com.trade.settlement.batch;

import com.trade.settlement.dto.SettlementInsertDto;
import com.trade.settlement.repository.SettlementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 정산 결과 일괄 저장 + 잔고 반영.
 * 쓰기는 전부 MyBatis — JPA 대비 대량 처리 시 메모리 부하가 적다.
 */
public class SettlementWriter implements ItemWriter<SettlementInsertDto> {

    private static final Logger log = LoggerFactory.getLogger(SettlementWriter.class);

    private final SettlementMapper settlementMapper;

    public SettlementWriter(SettlementMapper settlementMapper) {
        this.settlementMapper = settlementMapper;
    }

    @Override
    public void write(List<? extends SettlementInsertDto> items) throws Exception {
        if (items.isEmpty()) return;

        List<SettlementInsertDto> list = new ArrayList<>(items);

        // 1) 정산 결과 Bulk Insert
        settlementMapper.bulkInsertSettlements(list);

        // 2) 체결 내역 정산 완료 처리
        List<Long> execSeqs = new ArrayList<>();
        for (SettlementInsertDto item : list) {
            execSeqs.add(item.getExecSeq());
        }
        settlementMapper.updateSettledYn(execSeqs);

        // 3) 회원별 잔고 반영 + 변동 이력 기록
        Map<Long, BigDecimal> balanceMap = new HashMap<>();
        for (SettlementInsertDto item : list) {
            BigDecimal current = balanceMap.getOrDefault(item.getMemberKey(), BigDecimal.ZERO);
            balanceMap.put(item.getMemberKey(), current.add(item.getBalanceChange()));
        }

        for (Map.Entry<Long, BigDecimal> entry : balanceMap.entrySet()) {
            Long memberKey = entry.getKey();
            BigDecimal changeAmt = entry.getValue();

            BigDecimal beforeBal = settlementMapper.selectMemberBalance(memberKey);
            settlementMapper.updateMemberBalance(memberKey, changeAmt);
            BigDecimal afterBal = beforeBal.add(changeAmt);

            settlementMapper.insertBalanceHistory(
                    memberKey, "SETTLEMENT", changeAmt,
                    beforeBal, afterBal, null, "SETTLEMENT");
        }

        log.info("정산 처리 완료 — {}건 처리, 회원 {}명 잔고 반영 (이력 기록 완료)",
                list.size(), balanceMap.size());
    }
}
