package com.bjpowernode.Util;

import com.bjpowernode.Consts.YLBKEY;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class RedisOpreation {
    private StringRedisTemplate stringRedisTemplate;
    private RedisTemplate redisTemplate;

    public RedisOpreation(StringRedisTemplate stringRedisTemplate, RedisTemplate redisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisTemplate = redisTemplate;
    }


    /**
     * @param type  产品类型
     * @return true 包含该产品类型
     */
    public boolean checkProductType(String type){

        //operations.values();返回key对应的所有value
      List<String> values=getRedisValues(YLBKEY.DIC_PRODUCT_KEY);
        return values.contains(type);
    }

    /**
     * @param key redis中的key
     * @return   key中所有元素的集合
     */
    public List<String> getRedisValues(String key){
        HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();

        return operations.values(key);
    }

    /**
     * @param key redis中的key
     * @return 返回对应的value
     */
    public Object getKey(String key){
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * @param key 要存入reids中的key
     * @param value 要存入的value
     * @param minute 在内存的时间，分钟
     */
    public void setKey(String key, Object value,long minute){
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(key,value,minute, TimeUnit.MINUTES);
    }

    /**
     * @param key 字符串型
     * @return 获取值
     */
    public String getStringKey(String key){
        return stringRedisTemplate.opsForValue().get(key);
    }
    public void setStringKey(String key,String value,long minute){
        stringRedisTemplate.opsForValue().set(key,value,minute,TimeUnit.MINUTES);
    }


}

