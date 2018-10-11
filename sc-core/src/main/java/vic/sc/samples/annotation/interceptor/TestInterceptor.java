package vic.sc.samples.annotation.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import vic.sc.annotation.WebComponent;
import vic.sc.annotation.constant.WebComponentType;

@WebComponent(urlPatterns="/test/*", type = WebComponentType.INTERCEPTOR, order=10)
public class TestInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("TestInterceptor");
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
}
