package com.lmk.server.config;

import com.lmk.api.constants.RedisConstant;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @auth: lmk
 * @Description:
 * @date: 2021/3/23
 */
@WebFilter("/*")
public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //字符集
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        //拦截
        String uri = request.getRequestURI();
        HttpSession session = request.getSession();
//        System.out.println("这是过滤器获取的:"+session);
        if (!(uri.endsWith("login") || uri.endsWith("email") || uri.endsWith("getCode") || uri.endsWith("forget"))) {
            if (session == null) {
                throw new RuntimeException("非法登录");
            }
            Object keyaaaaa = session.getAttribute(RedisConstant.SESSION_KEY);
            if (keyaaaaa == null) {
                throw new RuntimeException("非法登录");
            }
        }
        filterChain.doFilter(request, response);
    }
}
