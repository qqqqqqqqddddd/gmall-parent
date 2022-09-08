package com.atguigu.gmall.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 配置过滤的请求路径
 */
@Data
@ConfigurationProperties(prefix ="app.auth" )
@Component
public class AuthUrlProperties {

    List<String> noAuthUrl; //无需登录的请求路径

    List<String>  loginAuthUrl; //需登录的请求路径

    String loginPage; //登录页地址

    List<String> denyUrl; //永远拒绝浏览器访问

}
