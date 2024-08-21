package com.hmall.gateway.filters;

import cn.hutool.core.text.AntPathMatcher;
import com.hmall.common.exception.UnauthorizedException;
import com.hmall.gateway.config.AuthProperties;
import com.hmall.gateway.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final JwtTool jwtTool;
    private final AuthProperties authProperties;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.获取请求路径
        String path = exchange.getRequest().getURI().getPath();
        // 2.判断是否需要校验
        if (isExcludedPath(path)) {
            // 3.不需要校验
            return chain.filter(exchange);
        }
        // 4.需要校验
        // 4.1.获取token
        log.error("path:{}", path);
        log.error("headers:{}", exchange.getRequest().getHeaders());
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        // 4.2.校验token
        Long userId;
        try {
            userId = jwtTool.parseToken(token);
        } catch (UnauthorizedException e) {
            // 4.3.校验失败
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        // 4.4.校验成功
        // 4.4.1.将userId放入请求头
        ServerWebExchange build = exchange.mutate()
                .request(builder -> builder.header("user-info", String.valueOf(userId)))
                .build();
        return chain.filter(build);
    }

    private boolean isExcludedPath(String path) {
        for (String ignorePath : authProperties.getExcludePaths()) {
            if (antPathMatcher.match(ignorePath, path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
