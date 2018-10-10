package vic.sc.component.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import vic.sc.annotation.WebComponent;
import vic.sc.constant.WebComponentType;

@WebComponent(type=WebComponentType.LISTENER)
public class TestListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("TestListener");
	}
	
	
	
}
