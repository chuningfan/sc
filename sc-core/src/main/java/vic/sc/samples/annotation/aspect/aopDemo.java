package vic.sc.samples.annotation.aspect;

import org.aspectj.lang.JoinPoint;

import vic.sc.annotation.AOP;
import vic.sc.annotation.api.AspectSupport;

@AOP(expression="execution(* vic.sc.samples.controller.LoginController.*(..))")
public class aopDemo extends AspectSupport {

	@Override
	public void doBefore(JoinPoint jp) {
		System.out.println("before");
	}

	@Override
	public void doAfter() {
		System.out.println("after");
	}

//	@Override
//	public Object doAfterReturning(Object result) {
//		// TODO Auto-generated method stub
//		return result;
//	}
//
//	@Override
//	public Object doAround(ProceedingJoinPoint pjp) {
//		try {
//			return pjp.proceed();
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	@Override
//	public void doAfterThrowing(JoinPoint joinPoint, Throwable ex) {
//		// TODO Auto-generated method stub
//		
//	}

}
