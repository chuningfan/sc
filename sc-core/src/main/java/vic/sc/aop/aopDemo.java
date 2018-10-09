package vic.sc.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import vic.sc.annotation.AOP;
import vic.sc.annotation.api.AspectSupport;

@AOP(expression="execution(* vic.sc.controller.*.*(..))")
public class aopDemo implements AspectSupport {

	@Override
	public void doBefore(JoinPoint jp) {
		System.out.println("before");
	}

	@Override
	public void doAfter() {
		System.out.println("after");
	}

	@Override
	public void doAfterReturning(Object result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doAround(ProceedingJoinPoint pjp) {
//		try {
//			pjp.proceed();
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
		
	}

	@Override
	public void doAfterThrowing(JoinPoint joinPoint, Throwable ex) {
		// TODO Auto-generated method stub
		
	}

}
