package com.atguigu.gmall.gateway.filter;

import com.atguigu.gmall.common.constant.SysRedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.gateway.properties.AuthUrlProperties;
import com.atguigu.gmall.model.user.UserInfo;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.google.common.net.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 网关的全局过滤器
 */
@Slf4j
@Component
public class GlobalAuthFilter implements GlobalFilter {

    AntPathMatcher matcher = new AntPathMatcher();

    @Autowired
    AuthUrlProperties authUrlProperties;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //1 前置拦截
        String path = exchange.getRequest().getURI().getPath();
        String uri = exchange.getRequest().getURI().toString();
        log.info("{} 请求开始", path);


        boolean match = false;
        //静态资源直接放行
        //ant风格路径,
        for (String url : authUrlProperties.getNoAuthUrl()) {
            match = matcher.match(url, path);
            //直接放行
            if (match) {

                return chain.filter(exchange);
            }
        }
        //走到这, 说明不是直接放行的资源
        //不能放行的请求 /api/inner feign内部调的
        for (String url : authUrlProperties.getDenyUrl()) {
            match = matcher.match(url, path);
            if (match) {
                //直接拒绝
                Result<String> result = Result.build("",
                        ResultCodeEnum.PERMISSION);
                return responseResult(result, exchange);
            }
        }

        //需要登录的请求 ; 进行权限验证
        for (String url : authUrlProperties.getLoginAuthUrl()) {
            match = matcher.match(url, path);
            if (match) {
                //登录验证
                // 获取token 信息
                String tokenValue = getTokenValue(exchange);

                //校验token
                UserInfo info = getTokenUserInfo(tokenValue);

                //判断用户信息是否正确
                if (info != null) {
                    //redis中有用户的信息
                    ServerWebExchange webExchange = userIdOrTempIdTransport(info, exchange);
                    return chain.filter(webExchange);
                } else {
                    //跳到登录页
                    return redirectToCustomPage(authUrlProperties.getLoginPage() + "?originUrl=" + uri, exchange);
                }
            }
        }

            //既不是静态资源直接放行，也不是必须登录才能访问的,就一个普通请求
            //透传用户id
            String tokenValue = getTokenValue(exchange);
            UserInfo info = getTokenUserInfo(tokenValue);
            if (!StringUtils.isEmpty(tokenValue) && info == null) {
                //带了token但是没有用户的信息
                //说明是个伪令牌
                return redirectToCustomPage(authUrlProperties.getLoginPage() + "?originUrl=" + uri, exchange);
            }

             exchange = userIdOrTempIdTransport(info, exchange);

        return chain.filter(exchange);


        //对登录后的请求进行user_id透传
//        Mono<Void> filter = chain.filter(exchange)
//                .doFinally((signalType)->{
//                    log.info("{} 请求结束",path);
//                });
 }



    /**
     * 生成Mono返回结果
     * @param result
     * @param exchange
     * @return
     */
    private Mono<Void> responseResult(Result<String> result, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        String jsonStr = Jsons.toStr(result);

        DataBuffer dataBuffer = response.bufferFactory()
                .wrap(jsonStr.getBytes(StandardCharsets.UTF_8));

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return response.writeWith(Mono.just(dataBuffer));
    }

    /**
     * 用户id透传
     * 透传一个临时id
     * @param info
     * @param exchange
     * @return
     */
    private ServerWebExchange userIdOrTempIdTransport(UserInfo info, ServerWebExchange exchange) {
        //请求一旦发过来, 只读
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest.Builder newReqBuilder = exchange.getRequest().mutate();

        //用户登录了
        if (info != null){
            newReqBuilder.header(SysRedisConst.USERID_HEADER,info.getId().toString());
        }
        //用户没有登录
        //获取前端带来的临时id
         String userTempId = getUserTempId(exchange);
        newReqBuilder.header(SysRedisConst.USERTEMPID_HEADER,userTempId);

             //放行时传新的exchange
             ServerWebExchange webExchange = exchange
                     .mutate()
                     .request(newReqBuilder.build())
                     .response(exchange.getResponse())
                     .build();
             return webExchange;
    }

    /**
     * 获取临时id
     * @param exchange
     * @return
     */
    private String getUserTempId(ServerWebExchange exchange) {

        ServerHttpRequest request = exchange.getRequest();
        String userTempId = request.getHeaders().getFirst("userTempId");

        if (StringUtils.isEmpty(userTempId)){
            HttpCookie httpCookie = request.getCookies().getFirst("userTempId");
            if (httpCookie != null){
                userTempId = httpCookie.getValue();
            }

        }
        return userTempId;

    }

    /**
     * 重定向到登录页面
     * @return
     */
    private Mono<Void> redirectToCustomPage(String location,ServerWebExchange exchange) {

        ServerHttpResponse response = exchange.getResponse();

        //重定向:状态码设置302 + location
        response.setStatusCode(HttpStatus.FOUND);
        response.getHeaders().add(HttpHeaders.LOCATION ,location);

        //清除旧的cookie
        ResponseCookie tokenCookie = ResponseCookie
                .from("token","777")
                .maxAge(0)
                .path("/")
                .domain(".gmall.com")
                .build();

        response.getCookies().set("token",tokenCookie);

        //响应结束
        return response.setComplete();
    }

    /**
     * 校验token,获取info
     * @param tokenValue
     * @return
     */
    private UserInfo getTokenUserInfo(String tokenValue) {
        String json = redisTemplate.opsForValue().get(SysRedisConst.LOGIN_USER + tokenValue);

        if (!StringUtils.isEmpty(json)){
          return  Jsons.toObj(json, UserInfo.class);
        }
        return null ;
    }

    /**
     * 获取token信息
     * @param exchange
     * @return
     */
    private String getTokenValue(ServerWebExchange exchange) {

        String tokenValue = "";
        HttpCookie token = exchange.getRequest().getCookies().getFirst("token");

        //先检查cookie中有没有token
        if (token != null ){
            tokenValue= token.getValue();
            return tokenValue;
        }
        //说明cookie中没有,从请求投中获取
        tokenValue = exchange.getRequest().getHeaders().getFirst("token");
        return tokenValue;
    }
}
