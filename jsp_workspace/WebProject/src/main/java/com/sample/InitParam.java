package com.sample;

import java.io.*;
import java.util.Enumeration;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

/* 초기 파라미터
 * 
 *   1. ServletConfig
 *      1. ServletConfig 객체는 Container가 서블릿을 생성할떄 생성되는 객체이다    
 *      2. web.xml을 읽어서 이름/쌍으로 된 초기화 파라미터를 읽어서 저장한다.
 *      3. ServletConfig 객체는 Servlet 객체당 한갰기 생성된다.
 *      4. Servlet에서 getServletConfig() 메소드를 이용해서 Servlet과 관련된 ServletConfig 객체를 얻음
 *      
 *   2. ServletContext
 *       - ServletContext 객체 web application당 하나씩 생성된다.
 *       - web application 전체에서 참조할 수 있는 초기화 파라미터를 저장함
 *       - Servlet에서 getServletContext()를 메소드를 이용해서 ServletContext 객체를 얻음
 */

@WebServlet(name = "InitParam", urlPatterns = {"/InitParam"},
initParams = {@WebInitParam(name="tel", value = "010-1111-2222"),
		@WebInitParam(name="email", value = "kim@naver.com")})
public class InitParam extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String company;
	private String manager;
	private String tel; // ServletConfig
	private String email;
	
	public void init() throws ServletException {
		// web.xml에 설정한 초기 파라미터 값을 얻어옴
		
		System.out.println("초기화 메소드 수행함");
		// ServletContext의 초기 파라미터 값을 얻기
		
		company = getServletContext().getInitParameter("company");
		manager = getServletContext().getInitParameter("manager");
		
		// ServletConifg의 초기 파라미터 값을 얻기
		tel = getServletConfig().getInitParameter("tel");
		email = getServletConfig().getInitParameter("email");

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request,response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request,response);
	}
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		   
		response.setContentType("text/html;charset=utf-8");
		   PrintWriter out = response.getWriter();
		   
		   out.println("<html><body>");
		   out.println("<li> 회사명 : " +company+"</li>");
		   out.println("<li> 관리자 : " +manager+"</li>");
		   out.println("<li> 전화번호 : " +tel+"</li>");
		   out.println("<li> 이메일 : " +email+"</li>");
		   out.println("</body></html>");
	
	}
}
