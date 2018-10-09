package vic.sc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import vic.sc.constant.WebComponentType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface WebComponent {
	
	public WebComponentType type();
	
	public int order() default 0;
	
	public String[] urlPatterns() default {"/*"};
	
	public String[] excludeUrlPatterns() default {};
	
	public boolean asyncSupported() default false;
	
	public String[] initParam() default {};
	
	public int loadOnStartUp() default -1;
	
}
