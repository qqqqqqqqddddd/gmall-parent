package com.atguigu.gmall.user.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.LogRecord;

/**
 * 拦截器demo
 */
public class HelloFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        //目标方法执行之前
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.sendRedirect("http://www.baidu.com");

       // chain.doFilter(request,response); //放行

        //目标方法执行之后

    }
}
