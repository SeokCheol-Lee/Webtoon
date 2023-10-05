package com.example.webtoon.global.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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

    public Long getLongValue(Long key){
        return Long.valueOf(Objects.requireNonNull(redisTemplate.opsForValue().get(key)).toString());
    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }
}
