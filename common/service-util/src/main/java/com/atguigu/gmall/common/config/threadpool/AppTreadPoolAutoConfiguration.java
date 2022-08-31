package com.atguigu.gmall.common.config.threadpool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * 线程池配置类
 */
//将AppTreadPoolProperties自动放到容器中 ,, 开启自动属性绑定配置
@EnableConfigurationProperties(AppTreadPoolProperties.class)
@Configuration
public class AppTreadPoolAutoConfiguration {

    @Autowired
    AppTreadPoolProperties treadPoolProperties;

    @Value("${spring.application.name}")
    String applicationName;


    @Bean
    public ThreadPoolExecutor coreExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                treadPoolProperties.getCore(),
                treadPoolProperties.getMax(),
                treadPoolProperties.getKeepAliveTime(),
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(treadPoolProperties.getQueueSize()),
                new ThreadFactory() { //线程工厂负责给线程池创建线程
                    int i = 0;

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName(applicationName+"[core-thread-"+ i++ +"]");
                        return thread;
                    }
                },
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        return  executor;
    }
}
