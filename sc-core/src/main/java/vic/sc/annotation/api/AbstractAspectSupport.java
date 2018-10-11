package vic.sc.annotation.api;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

public interface AbstractAspectSupport {
	
	void doBefore(JoinPoint jp);
	
	void doAfter();
	
	Object doAfterReturning(Object result);
	
	Object doAround(ProceedingJoinPoint pjp);
	
	void doAfterThrowing(JoinPoint joinPoint,Throwable ex);
	
}
