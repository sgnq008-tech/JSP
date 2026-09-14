package com.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


//@WebServlet("/Hello")// 자동 설정 -> 어노테이션()
// 웹페이지로 접속하면 주소창 오른쪽 끝에있다
public class HelloServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  response.setContentType("text/html;charset=utf-8");
//                                                                     문자인코딩(한,영 호환)
	  PrintWriter out = response.getWriter();
	  Date date = new Date();
	  out.println("<html>"); 
	  out.println("<body>"); 
	  out.println("HelloSeverlet 요청 !!!");
	  out.println("<br>");
	  out.println(date.toString());
	  out.println("</body>"); 
	  out.println("</html>"); 
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	System.out.println();
	}

}
