package com.example.webtoon.global.redis;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisDao {
    private final RedisTemplate<Object, Object> redisTemplate;

    public void setLongValue(Long key, Long data){
        ValueOperations<Object, Object> value = redisTemplate.opsForValue();
        value.set(key,data);
    }

    public void setValuesHash(String key, String hashkey, Long data) {
        redisTemplate.opsForHash().put(key,hashkey,data);
    }

    public Map<Object, Object> getValuesHash(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public void countView(Long key){
        redisTemplate.opsForValue().increment(key);
    }
}
