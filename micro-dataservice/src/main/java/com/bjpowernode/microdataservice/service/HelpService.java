package com.bjpowernode.microdataservice.service;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class HelpService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * @param type 字典中产品类型
     * @return  是否存在该产品 true 存在
     */
    public boolean checkProductType(String type){
        HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
        //operations.values();返回key对应的所有value
        return operations.values("DIC:PRODUCT:TYPE").contains(type);
    }

}
