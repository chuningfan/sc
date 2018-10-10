package vic.sc.annotation.impl;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

public class ApplicationContextProvider implements ApplicationContextAware {
	
	static ConfigurableApplicationContext CXT;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		CXT= (ConfigurableApplicationContext) applicationContext;
		CXT.refresh();
	}

}
