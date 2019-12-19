package com.demo.distributed.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author oulei
 * @Description
 * @date 2019/12/14 14:53
 */
@Component
public class RedisStringUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取缓存值
     * @param key
     */
    public String getString(String key){
        return  key==null?null:stringRedisTemplate.opsForValue().get(key);
    }


    /**
     * 存入缓存值
     * @param key
     * @param value
     */
    public boolean setString(String key,String value){
        try {
            stringRedisTemplate.opsForValue().set(key,value);
            return true;
        }catch (Exception e){
           e.printStackTrace();
           return false;
        }
    }

    /**
     * 删除缓存值
     * @param key
     */
    public void  delString(String key){
        if(!StringUtils.isEmpty(key)){
            stringRedisTemplate.delete(key);
        }
    }

    /**
     * 设置缓存的过期时间
     * @param key
     * @param time
     */
    public boolean expireString(String key, long time) {
        try {
            if (time > 0) {
              stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
           e.printStackTrace();
        }
        return  false;
    }



}
