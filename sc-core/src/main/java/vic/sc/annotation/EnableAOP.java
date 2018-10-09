package vic.sc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import vic.sc.annotation.impl.AOPImporter;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(AOPImporter.class)
public @interface EnableAOP {
	
	public String[] scanPackage() default {};
	
}
