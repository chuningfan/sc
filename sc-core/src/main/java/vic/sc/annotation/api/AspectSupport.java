package vic.sc.annotation.api;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

public interface AspectSupport {
	
	public abstract void doBefore(JoinPoint jp);
	
	public abstract void doAfter();
	
	public abstract Object doAfterReturning(Object result);
	
	public abstract Object doAround(ProceedingJoinPoint pjp);
	
	public abstract void doAfterThrowing(JoinPoint joinPoint,Throwable ex);
	
}
