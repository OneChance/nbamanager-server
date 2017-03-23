package com.zhstar.nbamanager;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.zhstar.nbamanager.account.entity.Account;
import com.zhstar.nbamanager.account.service.AccountService;
import com.zhstar.nbamanager.common.JsonTool;
import com.zhstar.nbamanager.common.NetMessage;

@Configuration
public class MyConfiguration extends WebMvcConfigurerAdapter {
	
	@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:8888").allowCredentials(true);
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
				
				if(request.getRequestURL().indexOf("/login/")==-1){
					Account account = accountService.getLoginAccount(request, response);
			    	if(account==null){
			    		response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With");
			    		response.setHeader("Access-Control-Allow-Credentials", "true");
			    		response.setHeader("Access-Control-Allow-Origin", "http://localhost:8888");
			    		response.getWriter().write(JsonTool.toString(new NetMessage(NetMessage.STATUS_LOGIN_STATUS_ERROR, NetMessage.DANGER)));
			    		return false;
			    	}
					
				}			
				
				return true;
			}
		}).addPathPatterns("/**");
	}
	
	@Resource
    AccountService accountService;
}
