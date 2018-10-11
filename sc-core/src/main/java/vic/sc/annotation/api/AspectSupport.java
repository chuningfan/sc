package vic.sc.annotation.api;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

public abstract class AspectSupport implements AbstractAspectSupport {
	
	public void doBefore(JoinPoint jp) {
		
	}
	
	public void doAfter() {
		
	}
	
	public Object doAfterReturning(Object result) {
		return result;
	}
	
	public Object doAround(ProceedingJoinPoint pjp) {
		try {
			return pjp.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void doAfterThrowing(JoinPoint joinPoint,Throwable ex) {
		
	}
	
}
