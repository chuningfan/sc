package vic.sc.annotation.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import com.google.common.collect.Sets;

import vic.sc.annotation.AOP;
import vic.sc.annotation.api.AspectRunner;
import vic.sc.annotation.api.AspectSupport;
import vic.sc.util.Utils;

public class AOPImporter implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		String[] packages = (String[]) importingClassMetadata.getAnnotationAttributes("vic.sc.annotation.EnableWebComponent").get("scanPackage");
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
				throw new RuntimeException("All @AOP classes must implement vic.sc.annotation.api.AbstractAspectSupport");
			}
			Class<AspectRunner> runnerClass = AspectRunner.class;
			registrableClasses.stream().forEach(clazz -> {
				try {
					AOP aop = clazz.getAnnotation(AOP.class);
					String expression = aop.expression();
					String argNames = aop.argNames();
					Method method = runnerClass.getDeclaredMethod("pointcut");
					Pointcut p = method.getAnnotation(Pointcut.class);
					InvocationHandler h = Proxy.getInvocationHandler(p);
					Field f = h.getClass().getDeclaredField("memberValues");
					f.setAccessible(true);
					@SuppressWarnings("unchecked")
					Map<String, Object> fMap = (Map<String, Object>) f.get(h);
					fMap.put("value", expression);
					if (!StringUtils.isEmpty(argNames.trim())) {
						fMap.put("argNames", argNames);
					}
					BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(runnerClass);
					builder.setScope("prototype");
					builder.addPropertyValue("aspectSupport", clazz.newInstance());
					registry.registerBeanDefinition(Utils.lowerCaseFirstLetter(runnerClass.getSimpleName() + "$" + System.nanoTime()), builder.getRawBeanDefinition());
				} catch (NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
			});
		}
	}
	
	private Set<Class<?>> filter(Set<Class<?>> registrableClasses) {
		return registrableClasses = registrableClasses.stream().
				filter(c -> Arrays.asList(c.getInterfaces()).contains(AspectSupport.class)).collect(Collectors.toSet());
	}

	
}
