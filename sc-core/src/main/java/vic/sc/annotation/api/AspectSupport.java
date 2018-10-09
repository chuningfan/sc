package vic.sc.annotation.api;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;

public interface AspectSupport {
	
	public abstract void doBefore(JoinPoint jp);
	
	@After("")
	public abstract void doAfter();
	
	public abstract void doAfterReturning(Object result);
	
	public abstract void doAround(ProceedingJoinPoint pjp);
	
	public abstract void doAfterThrowing(JoinPoint joinPoint,Throwable ex);
	
}
