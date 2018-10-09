package vic.sc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import vic.sc.annotation.impl.ComponentImporter;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ComponentImporter.class)
@Documented
public @interface EnableWebComponent {
	
	public String[] scanPackage() default {};
	
	
}
