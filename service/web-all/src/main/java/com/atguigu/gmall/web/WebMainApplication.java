package com.atguigu.gmall.web;

import com.atguigu.gmall.common.annotation.EnableAutoFeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableAutoFeignInterceptor
@EnableFeignClients(basePackages = {
        "com.atguigu.gmall.feign"
})
@SpringCloudApplication
public class WebMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebMainApplication.class,args);
    }
}
