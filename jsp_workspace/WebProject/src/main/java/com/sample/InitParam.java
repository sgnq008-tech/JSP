package com.sample;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;


/* 초기 파라미터
 * 
 * 1.ServletConfig
 * 		1.ServletConfig 객체는 Container(tomcat) 가 서블릿을 생성할때
 * 			생성되는 객체이다. 
 * 		2. web.xml 을 읽어서 이름/쌍으로 된 초기화 파라미터를 읽어서 저장한다.
 * 		3. ServletConfig 객체는 Servlet 객체당 한개씩 생성된다.
 * 		4. Serlvet에서는 getServletConfig() 메소드를 이용해서 Servlet과
 * 			관련된 ServletConfig 객체를 얻을 수있다.
 * 
 * 		2. ServletContext
 * 			-ServletContext 객체 web application 당 하나씩 생성된다.
 * 			-web application 전체에서 참조할 수 있는 초기화 
 * 			파라미터를 저장할 수 있다.
 * 			-Servlet에서 getServletConext() 메소드를 이용해서 
 * 			ServletContext 객체를 얻을 수 있다.
 */



@WebServlet(
	    name="initParam",
	    urlPatterns = {"/initParam"},
	    initParams = {
	        @WebInitParam(name="tel", value="010-1111-2222"),
	        @WebInitParam(name="email", value="kim@naver.com")
	    }
	)
public class initParam extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String company; //ServletContext
	private String manager;
	private String tel;//ServletConfig
	private String email;
	
	public void init() throws ServletException{
		// web.xml에 설정한 초기 파라미터 값을 얻어옴
		System.out.println("초기화 메소드 수행");
		// ServletContext 의 초기 파라미터 값을 얻겠습니다.
		company = getServletContext().getInitParameter("company");
		manager = getServletContext().getInitParameter("company");
		// ServletContext 의 초기 파라미터 값을 얻겠습니다.
		tel = getServletConfig().getInitParameter("tel");
		email = getServletConfig().getInitParameter("email");
	}
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		processRequest(request, response);
	}
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		out.println("<html><body>");
		out.println("<li> 회사명:"+company+"</li>");
		out.println("<li> 관리자:"+manager+"</li>");
		out.println("<li> 전화번호:"+tel+"</li>");
		out.println("<li> 이메일:"+email+"</li>");
		out.println("</body></html>");
	}
}
