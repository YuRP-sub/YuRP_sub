package yurp.model;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class MyInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		StoreDTO id = (StoreDTO)request.getSession().getAttribute("loginStore");
		
		if(id == null) {
			response.sendRedirect("/over");
			
			return false;
		}

		
		return true;
	}
}
