package yurp.model;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.annotation.Resource;

@Configuration
public class MyInterConfig implements WebMvcConfigurer {
	
	@Resource
	MyInterceptor myInterceptor;
	

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		System.out.println("addInterceptors() 실행");
		
		registry.addInterceptor(myInterceptor)
		.addPathPatterns("/**")
		.excludePathPatterns("/index")
		.excludePathPatterns("/over")
		.excludePathPatterns("/inc/alert")
		.excludePathPatterns("/dashboard");

		
		//WebMvcConfigurer.super.addInterceptors(registry);
	}
}
