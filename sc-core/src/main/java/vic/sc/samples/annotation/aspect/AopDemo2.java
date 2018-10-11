package vic.sc.samples.annotation.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import vic.sc.annotation.AOP;
import vic.sc.annotation.api.AbstractAspectSupport;

@AOP(expression="execution(* vic.sc.samples.controller.TestController.*(..))")
public class AopDemo2 implements AbstractAspectSupport {

	@Override
	public void doBefore(JoinPoint jp) {
		System.out.println("AopDemo2 before");
	}

	@Override
	public void doAfter() {
		System.out.println("AopDemo2 after");
	}

	@Override
	public Object doAfterReturning(Object result) {
		return result;
	}

	@Override
	public Object doAround(ProceedingJoinPoint pjp) {
		try {
			return pjp.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void doAfterThrowing(JoinPoint joinPoint, Throwable ex) {
		// TODO Auto-generated method stub
		
	}

}
