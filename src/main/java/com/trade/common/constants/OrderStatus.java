package com.trade.common.constants;

/**
 * 주문 상태.
 */
public enum OrderStatus {
    PENDING,                      // 대기
    FILLED,                       // 체결 완료
    PARTIAL,                      // 부분 체결
    CANCELLED,                    // 취소
    REJECTED                      // 거부
}
