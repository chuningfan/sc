package vic.sc.samples.annotation.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import vic.sc.annotation.WebComponent;
import vic.sc.annotation.constant.WebComponentType;

@WebComponent(type=WebComponentType.LISTENER)
public class LoginListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("listener");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

}
