package com.trade.margin.batch;

import com.trade.order.entity.TradeOrder;
import com.trade.order.repository.TradeOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * 반대매매 매도 주문 일괄 생성 — JPA 활용.
 * Chunk 단위로 TradeOrder 엔티티를 persist 한다.
 */
public class MarginCallWriter implements ItemWriter<TradeOrder> {

    private static final Logger log = LoggerFactory.getLogger(MarginCallWriter.class);

    private final TradeOrderRepository tradeOrderRepository;

    public MarginCallWriter(TradeOrderRepository tradeOrderRepository) {
        this.tradeOrderRepository = tradeOrderRepository;
    }

    @Override
    public void write(List<? extends TradeOrder> items) throws Exception {
        if (items.isEmpty()) return;

        for (TradeOrder order : items) {
            tradeOrderRepository.save(order);
        }

        log.info("반대매매 매도 주문 생성 완료 — {}건", items.size());
    }
}
