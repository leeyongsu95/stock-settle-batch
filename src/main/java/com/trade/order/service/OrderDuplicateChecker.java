package com.trade.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 매수 중복 방지 — 같은 회원이 같은 종목 연타하는 케이스 차단.
 */
@Component
public class OrderDuplicateChecker {

    private static final long LOCK_SECONDS = 3;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * @return true면 중복 요청 (차단 대상)
     */
    public boolean isDuplicate(Long memberKey, String stockCd) {
        String key = "order:dup:" + memberKey + ":" + stockCd;
        Boolean firstSet = redisTemplate.opsForValue().setIfAbsent(key, "1");
        if (Boolean.TRUE.equals(firstSet)) {
            redisTemplate.expire(key, LOCK_SECONDS, TimeUnit.SECONDS);
            return false;
        }
        return true;
    }
}
