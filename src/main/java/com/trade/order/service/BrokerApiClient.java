package com.trade.order.service;

import com.trade.order.entity.TradeOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 증권사 매수 API 호출 — 현재는 로그만 남기는 모의 구현.
 */
@Component
public class BrokerApiClient {

    private static final Logger log = LoggerFactory.getLogger(BrokerApiClient.class);

    public void sendBuyOrder(TradeOrder order) {
        log.info("[증권사 API] 매수 요청 — 주문번호: {}, 종목: {}, 수량: {}",
                order.getOrderId(), order.getStockCd(), order.getQuantity());
    }
}
