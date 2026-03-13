package com.trade.common.constants;

/**
 * 잔액 변동 유형.
 */
public enum ChangeType {
    DEPOSIT,                      // 입금
    WITHDRAW,                     // 출금
    BUY,                          // 매수 차감
    SELL,                         // 매도 입금
    SETTLEMENT,                   // 정산 반영
    MARGIN_CALL                   // 반대매매
}
