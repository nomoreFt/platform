package com.platform.platform.security;

import javax.annotation.Resource;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valusOps;

    public Long getVisitCount() {
        Long count = 0L;
        try {
            valusOps.increment("spring:redis:visitcount", 1);
            count = Long.valueOf(valusOps.get("spring:redis:visitcount"));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return count;
    }
}