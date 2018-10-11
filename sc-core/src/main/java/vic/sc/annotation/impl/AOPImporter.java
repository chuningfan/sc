package vic.sc.annotation.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import com.google.common.collect.Sets;

import vic.sc.annotation.AOP;
import vic.sc.annotation.api.AbstractAspectSupport;
import vic.sc.annotation.api.AspectSupport;
import vic.sc.annotation.dto.DynamicClassDefinition;
import vic.sc.util.Utils;

public class AOPImporter implements ImportBeanDefinitionRegistrar {

	private static final Logger LOG = LoggerFactory.getLogger(AOPImporter.class);
	
	private static final Set<Class<?>> aspectClasses = Sets.newHashSet();
	
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		String[] packages = (String[]) importingClassMetadata.getAnnotationAttributes("vic.sc.annotation.EnableAOP").get("scanPackage");
		Set<String> set = Sets.newHashSet();
		for (String p: packages) {
			String pkg = p.trim();
			set.add(pkg);
		}
		try {
			doReg(set, registry);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
	}

	private void doReg(Set<String> scanSet, BeanDefinitionRegistry registry) throws ClassNotFoundException {
		Set<Class<?>> registrableClasses = Sets.newHashSet();
		String basePackage = ComponentImporter.class.getResource("/").getPath();
		if (scanSet.isEmpty()) {
			Utils.scan(basePackage, basePackage, registrableClasses, AOP.class);
		} else {
			for (String path: scanSet) {
				Utils.scan(basePackage, path, registrableClasses, AOP.class);
			}
		}
		if (!registrableClasses.isEmpty()) {
			registrableClasses = filter(registrableClasses);
			if (registrableClasses == null || registrableClasses.isEmpty()) {
				LOG.info("No customized AOP classes");
				return;
			}
			registrableClasses.stream().forEach(clazz -> {
				try {
					AOP aop = clazz.getAnnotation(AOP.class);
					String expression = aop.expression();
					String dynamicClassName = clazz.getSimpleName() + "Aspect";
					LOG.info("Created dynamic class " + dynamicClassName + " for customized AOP class " + clazz.getName());
					Class<?> runnerClass = createRunnerClass(dynamicClassName, clazz.getName(), expression);
					aspectClasses.add(runnerClass);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			aspectClasses.stream().forEach(a -> {
				BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(a);
				BeanDefinition df = builder.getBeanDefinition();
				registry.registerBeanDefinition(Utils.lowerCaseFirstLetter(a.getSimpleName()), df);
			});
		}
	}
	
	private Set<Class<?>> filter(Set<Class<?>> registrableClasses) {
		return registrableClasses = registrableClasses.stream().
				filter(c -> c.getSuperclass() == AspectSupport.class || Arrays.asList(c.getInterfaces()).contains(AbstractAspectSupport.class)).collect(Collectors.toSet());
	}

	
	private synchronized Class<?> createRunnerClass(String className, String injection, String expression) throws ClassNotFoundException, IOException {
		DynamicClassDefinition definition = new DynamicClassDefinition();
		definition.setPkg("package vic.sc.annotation.impl");
		String[] imports = new String[] {
				"import org.springframework.stereotype.Component",
				"import org.aspectj.lang.JoinPoint",
				"import org.aspectj.lang.ProceedingJoinPoint",
				"import org.aspectj.lang.annotation.After",
				"import org.aspectj.lang.annotation.AfterReturning",
				"import org.aspectj.lang.annotation.AfterThrowing",
				"import org.aspectj.lang.annotation.Around",
				"import org.aspectj.lang.annotation.Aspect",
				"import org.aspectj.lang.annotation.Before",
				"import org.aspectj.lang.annotation.Pointcut",
				"import vic.sc.annotation.api.AbstractAspectSupport"
		};
		definition.setImports(imports);
		String body = "@Aspect " + 
				" public class " + className + " { " +
				" private AbstractAspectSupport aspectSupport = new " + injection + "(); " +
				" @Before(\"" + expression + "\")" + 
				" public void doBefore(JoinPoint jp) { " + 
				" aspectSupport.doBefore(jp); " +
				" }" + 
				" @After(\"" + expression + "\") " + 
				" public void doAfter() { " + 
				" aspectSupport.doAfter(); " +
				" } " +
				" @AfterReturning(value = \"" + expression + "\", returning = \"result\") " +
				" public Object doAfterReturning(Object result) { " +
				" return aspectSupport.doAfterReturning(result); " + 
				" } " + 
				" @Around(\"" + expression + "\") " + 
				" public Object doAround(ProceedingJoinPoint pjp) { " +
				" return aspectSupport.doAround(pjp); " +
				" } " +
				" @AfterThrowing(value=\"" + expression + "\",throwing=\"ex\") " +
				" public void doAfterThrowing(JoinPoint joinPoint,Throwable ex) { " +
				" aspectSupport.doAfterThrowing(joinPoint, ex); " +
				" } " +
				" public AbstractAspectSupport getAspectSupport() { " + 
				" return aspectSupport; " +
				" } " +
				" public void setAspectSupport(AbstractAspectSupport aspectSupport) { " +
				" this.aspectSupport = aspectSupport; " + 
				" } " +
				" } ";
		definition.setBody(body);
		String source = definition.toString();
		String filePath = AOPImporter.class.getResource("/").toString().replaceFirst("file:", "");
		if (Utils.createClassDynamic(source, className, filePath)) {
			return Class.forName("vic.sc.annotation.impl." + className);
		} else {
			throw new RuntimeException("Failed to create a dynamic class");
		}
	}
	
}
