package vic.sc.samples.annotation.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vic.sc.annotation.WebComponent;
import vic.sc.annotation.constant.WebComponentType;

@WebComponent(type=WebComponentType.SERVLET, order=0, asyncSupported=true, urlPatterns="/test.do", initParam = {"a=1"})
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 530260238516390266L;

	
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("servlet");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}
	
}
