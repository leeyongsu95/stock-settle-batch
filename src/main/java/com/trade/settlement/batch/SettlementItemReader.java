package com.trade.settlement.batch;

import com.trade.settlement.dto.ExecutionSettlementDto;
import com.trade.settlement.repository.SettlementMapper;
import org.springframework.batch.item.ItemReader;

import java.util.List;

/**
 * 미정산 체결 조회 — 한 번 조회 후 소진되면 null 반환하여 Step 종료.
 * 짧은 주기로 반복 실행되는 마이크로 배치라 매회 100건씩만 처리한다.
 */
public class SettlementItemReader implements ItemReader<ExecutionSettlementDto> {

    private static final int FETCH_SIZE = 100;

    private final SettlementMapper settlementMapper;
    private List<ExecutionSettlementDto> buffer;
    private int index = 0;

    public SettlementItemReader(SettlementMapper settlementMapper) {
        this.settlementMapper = settlementMapper;
    }

    @Override
    public ExecutionSettlementDto read() {
        if (buffer == null) {
            buffer = settlementMapper.selectUnsettledExecutions(FETCH_SIZE);
        }
        if (index >= buffer.size()) {
            return null;
        }
        return buffer.get(index++);
    }
}
