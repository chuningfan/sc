package vic.sc.annotation.api;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AspectRunner {
	
	private AspectSupport aspectSupport;
	
	@Pointcut("")
	public void pointcut() {
	}
	
	@Before("pointcut()")
	public void doBefore(JoinPoint jp) {
		aspectSupport.doBefore(jp);
	}
	
	@After("pointcut()")
	public void doAfter() {
		aspectSupport.doAfter();
	}
	
	@AfterReturning(value = "pointcut()", returning = "result")
	public void doAfterReturning(Object result) {
		aspectSupport.doAfterReturning(result);
	}
	
	@Around("pointcut()")
	public void doAround(ProceedingJoinPoint pjp) {
		aspectSupport.doAround(pjp);
	}
	
	@AfterThrowing(value="pointcut()",throwing="ex")
	public void doAfterThrowing(JoinPoint joinPoint,Throwable ex) {
		aspectSupport.doAfterThrowing(joinPoint, ex);
	}

	public AspectSupport getAspectSupport() {
		return aspectSupport;
	}

	public void setAspectSupport(AspectSupport aspectSupport) {
		this.aspectSupport = aspectSupport;
	}
	
	
}
