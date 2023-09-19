package com.example.reserve.cache;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

@Service
public class Redis {
    @Autowired
    private RedisTemplate<String, String> strRedisTemplate;

    @Autowired
    private RedisTemplate<String, Object> objRedisTemplate;

    public void set(String key, Object value) {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        objRedisTemplate.setValueSerializer(serializer);
        objRedisTemplate.opsForValue().set(key, value);
    }

    public Object getObj(String key) {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        objRedisTemplate.setValueSerializer(serializer);
        return objRedisTemplate.opsForValue().get(key);
    }
    public void deleteKey(String key) {
        objRedisTemplate.delete(key);
    }

    public void set(String key, String value) {
        strRedisTemplate.opsForValue().set(key, value);
    }

    public String getStr(String key) {
        return strRedisTemplate.opsForValue().get(key);
    }

    public boolean lock(String key, String value, long timeoutSeconds) {
        Boolean lockAcquired = strRedisTemplate.opsForValue().setIfAbsent(key, value, timeoutSeconds, TimeUnit.SECONDS);
        return lockAcquired != null && lockAcquired;
    }

    public boolean unlock(String key, String value) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        Long result = strRedisTemplate.execute(redisScript, Collections.singletonList(key), value);
        return result != null && result == 1;
    }
}
