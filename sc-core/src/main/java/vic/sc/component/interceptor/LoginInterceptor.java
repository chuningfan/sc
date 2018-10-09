package vic.sc.component.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import vic.sc.annotation.WebComponent;
import vic.sc.constant.WebComponentType;

@WebComponent(urlPatterns="/login/*", type = WebComponentType.INTERCEPTOR)
public class LoginInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("interceptor");
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
}
