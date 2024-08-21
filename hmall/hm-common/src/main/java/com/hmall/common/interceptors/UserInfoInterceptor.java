package com.hmall.common.interceptors;

import cn.hutool.core.util.StrUtil;
import com.hmall.common.exception.UnauthorizedException;
import com.hmall.common.utils.UserContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserInfoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userInfo = request.getHeader("user-info");
        System.out.println("userInfo: " + userInfo);
        System.out.println("request.getRequestURI(): " + request.getRequestURI());
        if (StrUtil.isNotBlank(userInfo)) {
            UserContext.setUser(Long.parseLong(userInfo));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.removeUser();
    }

}
