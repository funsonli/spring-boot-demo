package com.funsonli.springbootdemo330memcache.config;

import com.funsonli.springbootdemo330memcache.config.properties.MemcachedProperties;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Memcached Client 配置
 *
 * @author Funsonli
 * @date 2019/11/12
 */
@Slf4j
@Configuration
public class MemcachedConfig {

    @Resource
    private MemcachedProperties memcachedProperties;

    @Bean
    public MemcachedClient getMemcachedClinet(){
        MemcachedClient memcachedClient = null;
        try {
            MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(memcachedProperties.getServers()));
            builder.setConnectionPoolSize(memcachedProperties.getPoolSize());
            builder.setOpTimeout(memcachedProperties.getOpTimeout());
            memcachedClient = builder.build();
        }catch (IOException e){
            log.error("init MemcachedClient failed" + e);
        }
        return memcachedClient;
    }
}
