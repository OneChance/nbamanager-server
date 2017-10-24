package com.zhstar.nbamanager;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.zhstar.nbamanager.account.entity.Account;
import com.zhstar.nbamanager.account.service.AccountService;
import com.zhstar.nbamanager.common.CookieTool;
import com.zhstar.nbamanager.common.JsonTool;
import com.zhstar.nbamanager.common.NetMessage;

@Configuration
public class MyConfiguration extends WebMvcConfigurerAdapter {

    @Value("${system.env}")
    private String env;

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                //http://www.nbamanager.win http://localhost:8888
                registry.addMapping("/**").allowedOrigins("http://www.nbamanager.win").allowCredentials(true);
            }
        };
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                    throws Exception {
                request.getContextPath();
                request.setCharacterEncoding("UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Access-Control-Allow-Headers",
                        "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With");
                response.setHeader("Access-Control-Allow-Credentials", "true");
                response.setHeader("Access-Control-Allow-Origin", "http://www.nbamanager.win");

                if (request.getRequestURL().indexOf("/signIn/") == -1
                        && request.getRequestURL().indexOf("/signOut/") == -1
                        && request.getRequestURL().indexOf("/signUp/") == -1) {
                    boolean isLogin = true;
                    Account account = accountService.getLoginAccount(request, response);
                    if (account == null) {
                        String cookie = CookieTool.getCookies(request, "nbam_c");
                        if (cookie == null) {
                            isLogin = false;
                        } else {
                            String name = cookie.split(":")[0];
                            String token = cookie.split(":")[1];
                            Account accountDB = accountService.getByName(name);
                            if (accountDB == null) {
                                isLogin = false;
                            } else {
                                if (!accountDB.getToken().equals(token)) {
                                    isLogin = false;
                                } else {
                                    request.getSession().setAttribute("login_account", accountDB.getId());
                                }
                            }
                        }
                    }
                    if (!isLogin) {

                        if (!request.getRequestURI().equals("/")) {
                            response.getWriter().write(JsonTool
                                    .toString(new NetMessage(NetMessage.STATUS_LOGIN_STATUS_ERROR, NetMessage.DANGER)));
                            return false;
                        }
                    }

                }

                return true;
            }
        }).addPathPatterns("/**");
    }

    @Resource
    AccountService accountService;
}
