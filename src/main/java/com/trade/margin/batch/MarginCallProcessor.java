package com.trade.margin.batch;

import com.trade.margin.dto.MarginCallTargetDto;
import com.trade.order.entity.TradeOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * 반대매매 대상 --> 시장가 매도 주문 엔티티 변환.
 * 보유 수량 전량을 현재 시장가로 강제 매도한다.
 */
public class MarginCallProcessor implements ItemProcessor<MarginCallTargetDto, TradeOrder> {

    private static final Logger log = LoggerFactory.getLogger(MarginCallProcessor.class);

    @Override
    public TradeOrder process(MarginCallTargetDto target) {
        if (target.getHoldQty() <= 0 || target.getCurrentPrice() == null) {
            log.warn("반대매매 스킵 — memberKey={}, stockCd={}, 보유수량={}, 현재가={}",
                    target.getMemberKey(), target.getStockCd(),
                    target.getHoldQty(), target.getCurrentPrice());
            return null;
        }

        log.info("반대매매 대상 — memberKey={}, stockCd={}, 수량={}, 현재가={}, 연체금액={}",
                target.getMemberKey(), target.getStockCd(),
                target.getHoldQty(), target.getCurrentPrice(), target.getOverdueAmt());

        return TradeOrder.createMarginCallOrder(
                target.getMemberKey(),
                target.getStockCd(),
                target.getHoldQty(),
                target.getCurrentPrice()
        );
    }
}
