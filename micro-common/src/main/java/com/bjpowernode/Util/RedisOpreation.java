package com.bjpowernode.Util;

import com.bjpowernode.Consts.YLBKEY;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisOpreation {
    private StringRedisTemplate stringRedisTemplate;
    private RedisTemplate redisTemplate;

    public RedisOpreation(StringRedisTemplate stringRedisTemplate, RedisTemplate redisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisTemplate = redisTemplate;
    }

    /**
     * @param key 自增加一
     * @return
     */
    public long incre(String key){

        return stringRedisTemplate.opsForValue().increment(key);
    }

    /**累加score
     * @param key  redis key
     * @param value redis value key值
     * @param score redis score 排序依据
     */
    public void incrScoreZSet(String key,String value,Double score){
        ZSetOperations<String, String> zset = stringRedisTemplate.opsForZSet();
        zset.incrementScore(key,value,score);

    }

    //反向排序，获取索引范围的数据
    public Set<ZSetOperations.TypedTuple<String>> reverseRangeZSet(String key,int begin,int end){
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, begin, end);
        return typedTuples;

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
    public void setKey(String key, Object value){
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(key,value);
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
    public void setStringKey(String key,String value){
        stringRedisTemplate.opsForValue().set(key,value);
    }


    /**
     * @param key 被删除的无效信息的key
     */
    public void deleteRedisKey(String key) {
        stringRedisTemplate.delete(key);

    }

    /**订单号添加到zset集合中去
     * @param rechargeNoList
     * @param rechargeNo
     * @param time
     */
    public void zset(String rechargeNoList, String rechargeNo, long time) {
        stringRedisTemplate.opsForZSet().add(rechargeNoList,rechargeNo,time);
    }

    public void removeValueforZset(String rechargeNoList, String orderId) {
        stringRedisTemplate.opsForZSet().remove(rechargeNoList,orderId);
    }

    public  Set<ZSetOperations.TypedTuple<String>>  getAllZSet(String key) {
        Set<ZSetOperations.TypedTuple<String>> zsts=  stringRedisTemplate.opsForZSet().rangeWithScores(key,0,-1);

        return zsts;
    }

    public Set<String> getStringKeys(String key) {
        Set<String> keys = stringRedisTemplate.keys(key);
        return keys;

    }

    public void removeStringByKeys(Set<String> set) {
         long count=stringRedisTemplate.delete(set);
    }
}

