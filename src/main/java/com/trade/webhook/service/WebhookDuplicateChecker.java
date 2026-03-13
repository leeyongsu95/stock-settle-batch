package com.trade.webhook.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 웹훅 중복 수신 방지 — broker_exec_id 기준으로 24시간 내 재전송 차단.
 * Redis가 1차 방어, DB의 uk_broker_exec UNIQUE 제약이 2차 방어.
 */
@Component
public class WebhookDuplicateChecker {

    private static final Logger log = LoggerFactory.getLogger(WebhookDuplicateChecker.class);
    private static final long TTL_HOURS = 24;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public boolean isDuplicate(String brokerExecId) {
        try {
            String key = "webhook:exec:" + brokerExecId;
            Boolean firstSet = redisTemplate.opsForValue().setIfAbsent(key, "1");
            if (Boolean.TRUE.equals(firstSet)) {
                redisTemplate.expire(key, TTL_HOURS, TimeUnit.HOURS);
                return false;
            }
            return true;
        } catch (Exception e) {
            // Redis 장애 시 통과 — DB UNIQUE 제약이 최종 방어선
            log.warn("Redis 접속 실패, 중복 방지 없이 진행: {}", e.getMessage());
            return false;
        }
    }
}
