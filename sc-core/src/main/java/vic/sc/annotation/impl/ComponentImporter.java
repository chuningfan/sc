package vic.sc.annotation.impl;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import vic.sc.annotation.WebComponent;
import vic.sc.constant.WebComponentType;
import vic.sc.util.Utils;

public final class ComponentImporter implements ImportBeanDefinitionRegistrar {
	
	private static final Logger LOG = LoggerFactory.getLogger(ComponentImporter.class);
	
	private Set<Map<HandlerInterceptor, WebComponent>> interceptors = Sets.newHashSet();
	
	{
		Thread.currentThread().setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				LOG.error(e.getMessage());
			}
		});
	}
	
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
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
	
	private void doReg(Set<String> scanSet, BeanDefinitionRegistry registry) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SecurityException, NoSuchFieldException {
		Set<Class<?>> registrableClasses = Sets.newHashSet();
		String basePackage = ComponentImporter.class.getResource("/").getPath();
		if (scanSet.isEmpty()) {
			Utils.scan(basePackage, basePackage, registrableClasses, WebComponent.class);
		} else {
			for (String path: scanSet) {
				Utils.scan(basePackage, path, registrableClasses, WebComponent.class);
			}
		}
		Iterator<Class<?>> it = registrableClasses.iterator();
		BeanDefinitionBuilder builder = null;
		String[] params = null;
		Map<String, String> paramMap = null;
		while (it.hasNext()) {
			Class<?> clazz = it.next();
			WebComponent wc = clazz.getAnnotation(WebComponent.class);
			WebComponentType type = wc.type();
			switch(type) {
				case FILTER:
					builder = BeanDefinitionBuilder.rootBeanDefinition(FilterRegistrationBean.class);
					builder.addPropertyValue("asyncSupported", wc.asyncSupported());
					builder.addPropertyValue("filter", clazz.newInstance());
					builder.addPropertyValue("name", clazz.getSimpleName());
					builder.addPropertyValue("urlPatterns", wc.urlPatterns());
					params = wc.initParam();
					paramMap = Maps.newHashMap();
					if (params.length > 0) {
						paramMap = getInitParams(params);
						builder.addPropertyValue("initParameters", paramMap);
					}
					break;
				case SERVLET: 
					builder = BeanDefinitionBuilder.rootBeanDefinition(ServletRegistrationBean.class);
					builder.addPropertyValue("asyncSupported", wc.asyncSupported());
					builder.addPropertyValue("name", clazz.getSimpleName());
					builder.addPropertyValue("urlMappings", wc.urlPatterns());
					builder.addPropertyValue("servlet", clazz.newInstance());
					builder.addPropertyValue("loadOnStartup", wc.loadOnStartUp());
					params = wc.initParam();
					if (params.length > 0) {
						paramMap = getInitParams(params);
						builder.addPropertyValue("initParameters", paramMap);
					}
					break;
				case LISTENER:
					builder = BeanDefinitionBuilder.rootBeanDefinition(ServletListenerRegistrationBean.class);
					builder.addPropertyValue("listener", clazz.newInstance());
					break;
				case INTERCEPTOR:
					Map<HandlerInterceptor, WebComponent> map = Maps.newHashMap();
					map.put((HandlerInterceptor) clazz.newInstance(), wc);
					interceptors.add(map);
					break;
				default: 
					LOG.info("Unsupported component type for class " + clazz.getName());
					break;
			}
			if (type != WebComponentType.LISTENER && type != WebComponentType.INTERCEPTOR) {
				builder.addPropertyValue("order", wc.order());
			}
			if (type != WebComponentType.INTERCEPTOR) {
				registry.registerBeanDefinition(Utils.lowerCaseFirstLetter(clazz.getSimpleName()), builder.getBeanDefinition());
			}
		}
		// register interceptors
		builder = BeanDefinitionBuilder.genericBeanDefinition();
		AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
		beanDefinition.setBeanClassName("vic.sc.annotation.impl.ComponentImporter.MvcBuilder");
		ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
		constructorArgumentValues.addGenericArgumentValue(this);
		beanDefinition.setConstructorArgumentValues(constructorArgumentValues);
		registry.registerBeanDefinition("mvcBuilder", beanDefinition);			
	}
	
	private Map<String, String> getInitParams(String[] params) {
		Map<String, String> paramMap = Maps.newHashMap();
		for (String s: params) {
			String[] k_v = s.trim().split("=");
			paramMap.put(k_v[0], k_v[1]);
		}
		return paramMap;
	}
	
	final class MvcBuilder implements WebMvcConfigurer {
		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			interceptors.stream().forEach(m -> {
				HandlerInterceptor hi = m.keySet().iterator().next();
				WebComponent wc = m.get(hi);
				InterceptorRegistration ir = registry.addInterceptor(hi);
				if (wc.urlPatterns().length > 0) {
					ir.addPathPatterns(wc.urlPatterns());
				}
				if (wc.excludeUrlPatterns().length > 0) {
					ir.excludePathPatterns(wc.excludeUrlPatterns());
				}
				ir.order(wc.order());
			});
		}
	}

}
