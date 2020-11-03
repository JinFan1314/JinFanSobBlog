package net.sunofbeach.blog.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @program: SobBlogSystem
 * @description: 邮件异步发送
 * @author: JinFan
 * @create: 2020-09-18 14:45
 **/
@Configuration
@EnableAsync
public class AsyncConfiguration {

    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(10);
        executor.setThreadNamePrefix("sob_blog_task_worker-");
        executor.setQueueCapacity(30);
        executor.initialize();
        return executor;
    }

}