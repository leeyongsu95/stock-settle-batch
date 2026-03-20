package com.trade.margin.batch;

import com.trade.margin.dto.MarginCallTargetDto;
import com.trade.margin.repository.MarginCallMapper;
import org.springframework.batch.item.ItemReader;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 반대매매 대상 조회 — MyBatis로 인덱스 기반 집계 쿼리 실행.
 * 한 번에 전체 대상을 조회한 뒤 Queue에서 순차 반환한다.
 */
public class MarginCallReader implements ItemReader<MarginCallTargetDto> {

    private static final int FETCH_SIZE = 500;

    private final MarginCallMapper marginCallMapper;
    private Queue<MarginCallTargetDto> buffer;

    public MarginCallReader(MarginCallMapper marginCallMapper) {
        this.marginCallMapper = marginCallMapper;
    }

    @Override
    public MarginCallTargetDto read() {
        if (buffer == null) {
            List<MarginCallTargetDto> targets = marginCallMapper.selectMarginCallTargets(FETCH_SIZE);
            buffer = new LinkedList<>(targets);
        }
        return buffer.poll();
    }
}
