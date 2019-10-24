package com.funsonli.springbootdemo230guavacache.config;

import com.google.common.cache.CacheBuilder;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;


@Configuration
public class GuavaConfig {

    /**
     * spring缓存配置，使用guava
     * @author Funson
     * @date 2019/10/24
     * @return
     */
    @Bean
    public CacheManager cacheManager(){
        GuavaCacheManager cacheManager = new GuavaCacheManager();
        // 设置过期时间为7200秒
        cacheManager.setCacheBuilder(CacheBuilder.newBuilder().expireAfterWrite(7200, TimeUnit.SECONDS));
        return cacheManager;
    }
}
