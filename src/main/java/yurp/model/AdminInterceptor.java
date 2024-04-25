package yurp.model;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AdminInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		StoreDTO id = (StoreDTO)request.getSession().getAttribute("loginStore");
		
		if(id == null) {
			response.sendRedirect("/over");
			
			return false;
		}else{
			String auth = id.getAuth();
			if(!auth.equals("관리자")) {
				response.sendRedirect("/noauth");
				return false;
			}
			return true;
		}
		
	}
}
