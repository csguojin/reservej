package com.example.reserve.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.BitFieldArgs;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import static io.lettuce.core.BitFieldArgs.unsigned;

@Service
public class Redis {
    @Autowired
    private RedisCommands<String, String> syncCommands;
    @Autowired
    private RedisTemplate<String, String> strRedisTemplate;

    @Autowired
    private RedisTemplate<String, Object> objRedisTemplate;

    public void set(String key, String value) {
        syncCommands.set(key, value);
    }

    public void set(String key, Object value) {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        objRedisTemplate.setValueSerializer(serializer);
        objRedisTemplate.opsForValue().set(key, value);
    }

    public void testtest(String classpath) {
        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(classpath)));
        redisScript.setResultType(String.class);
        System.out.println("####################");
        System.out.println(redisScript.getScriptAsString());
        System.out.println("####################");
    }

    public String get(String key) {
        return syncCommands.get(key);
    }

    public <T> T get(String key, Class<T> valueType) {
        String jsonValue = syncCommands.get(key);
        if (jsonValue != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(jsonValue, valueType);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Object getObj(String key) {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        objRedisTemplate.setValueSerializer(serializer);
        return objRedisTemplate.opsForValue().get(key);
    }

    public Long del(String key) {
        return syncCommands.del(key);
    }

    public boolean setnx(String key, String value) {
        return syncCommands.setnx(key, value);
    }

    public boolean expire(String key, int seconds) {
        return syncCommands.expire(key, seconds);
    }

    public boolean exists(String key) {
        return syncCommands.exists(key) == 1;
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

    public List<Long> bitfieldSetU1(String key, Integer[][] argsArr) {
        if (argsArr.length <= 0) {
            return new ArrayList<>();
        }
        for (int i = 0; i < argsArr.length; i++) {
            if (argsArr[i].length <= 2) {
                return new ArrayList<>();
            }
        }
        BitFieldArgs args = BitFieldArgs.Builder.set(unsigned(1), argsArr[0][0], argsArr[0][1]);
        for (int i = 1; i < argsArr.length; i++) {
            args = args.set(unsigned(1), argsArr[i][0], argsArr[i][1]);
        }
        return syncCommands.bitfield(key, args);
    }

    public Integer bitfieldGetU1(String key, Integer[] argsArr) {
        if (argsArr.length <= 0) {
            return null;
        }
        BitFieldArgs args = BitFieldArgs.Builder.get(unsigned(1), argsArr[0]);
        for (int i = 1; i < argsArr.length; i++) {
            args = args.get(unsigned(1), argsArr[i]);
        }
        List<Long> result = syncCommands.bitfield(key, args);
        Integer ans = 0;
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i) > 0) {
                ans++;
            }
        }
        return ans;
    }







    public String executeLuaScript(String classpath, String key1, String key2, String arg1, String arg2) {
        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(classpath)));
        redisScript.setResultType(String.class);

        redisScript.getScriptAsString();

        List<String> keys = Arrays.asList(key1, key2);
        return strRedisTemplate.execute(redisScript, keys, arg1, arg2);
    }

    public String executeLuaScript2(String classpath, String... keysAndArgs) {
        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(classpath)));
        redisScript.setResultType(String.class);

        return syncCommands.eval(redisScript.getScriptAsString(), ScriptOutputType.VALUE, keysAndArgs);
    }

    private void test() {
        String seatKey = "seatKey";
        String userKey = "userKey";
        long startOffset = 0;
        long endOffset = 10;


        String luaScript = "local seatKey = KEYS[1]\n" +
                "local userKey = KEYS[2]\n" +
                "local startOffset = tonumber(ARGV[1])\n" +
                "local endOffset = tonumber(ARGV[2])\n" +
                "\n" +
                "local seatBitCount = redis.call('BITCOUNT', seatKey, startOffset, endOffset, 'BIT')\n" +
                "local userBitCount = redis.call('BITCOUNT', userKey, startOffset, endOffset, 'BIT')\n" +
                "\n" +
                "if seatBitCount == 0 and userBitCount == 0 then\n" +
                "    local bitfieldArgs = {}\n" +
                "\n" +
                "    for i = startOffset, endOffset do\n" +
                "        table.insert(bitfieldArgs, \"SET\")\n" +
                "        table.insert(bitfieldArgs, \"u1\")\n" +
                "        table.insert(bitfieldArgs, i)\n" +
                "        table.insert(bitfieldArgs, 1)\n" +
                "    end\n" +
                "\n" +
                "    redis.call('BITFIELD', seatKey, unpack(bitfieldArgs))\n" +
                "    redis.call('BITFIELD', userKey, unpack(bitfieldArgs))\n" +
                "\n" +
                "    return 'OK'\n" +
                "else\n" +
                "    return 'BITCOUNT not equal to 0'\n" +
                "end";

        String result = syncCommands.eval(luaScript, // Lua 脚本
                ScriptOutputType.VALUE, // 返回值类型
                "2", // KEY数量
                "seatKey", "userKey", // KEY列表
                "0", "10" // ARGV列表
        );
    }
}
