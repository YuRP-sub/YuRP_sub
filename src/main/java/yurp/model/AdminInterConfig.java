package yurp.model;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.annotation.Resource;

@Configuration
public class AdminInterConfig implements WebMvcConfigurer {
	
	@Resource
	AdminInterceptor adminInterceptor;
	

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		System.out.println("addInterceptors() 실행");
		
		registry.addInterceptor(adminInterceptor)
		.addPathPatterns("/brandOrder/**")
		.addPathPatterns("/notice/insert")
		.addPathPatterns("/stock/product/**")
		.addPathPatterns("/manage/**");

		
		//WebMvcConfigurer.super.addInterceptors(registry);
	}
}
