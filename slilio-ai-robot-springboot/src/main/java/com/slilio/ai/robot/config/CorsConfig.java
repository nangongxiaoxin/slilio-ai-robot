package com.slilio.ai.robot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: slilio
 * @CreateTime: 2025-07-04
 * @Description:
 * @Version: 1.0
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //运行匹配所有域名
                .allowedOriginPatterns("*") // 允许所有域名（生产环境应指定具体域名）
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的请求方法
                .allowedHeaders("*") //允许所有请求头
                .allowCredentials(true) // 运行发送Cookie
                .maxAge(3600); //允许请求的有效期

    }


}
