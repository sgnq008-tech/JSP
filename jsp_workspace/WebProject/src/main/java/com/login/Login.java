package com.login;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

// 매핑 주소를 "/Login"으로 설정한 서블릿 클래스입니다.
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// GET 방식으로 요청이 들어왔을 때 processRequest 메소드로 전달합니다.
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	// POST 방식으로 요청이 들어왔을 때 processRequest 메소드로 전달합니다.
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	// 실제 요청을 처리하는 핵심 공통 메소드입니다.
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// 응답 형식 및 인코딩을 HTML, UTF-8로 설정합니다.
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			// 기존 세션을 가져옵니다 (없으면 null 반환)
			HttpSession session = request.getSession(false);

			if(session != null) { // 로그인이 성공(유지)된 경우
				String sessionId = session.getId();
				System.out.println("세션아이디:"+sessionId);
				String user = (String)session.getAttribute("user");

				// 로그인 환영 화면 출력 (HTML)
				out.println("<html>");
				out.println("<body>");
				out.println("<table border = '1' width ='300'>");
				out.println("<tr>");
				out.println("<td width ='300' ='center'>");
				out.println(user +"님이 로그인 하셨습니다.</td>");
				out.println("</tr>");

				out.println("<tr>");
				out.println("<td align ='center'>");
				out.println("<a href='#'>회원정보</a>");
				out.println("<a href='Logout'>로그아웃</a>");
				out.println("</td>");
				out.println("</tr>");

				out.println("</tr>");
				out.println("</table>");
				out.println("</body>");
				out.println("</html>");
			}
			else { // 로그인이 안 된 경우 (로그인 폼 출력)
				out.println("<html>");
				out.println("<body>");
				// 로그인 처리를 담당할 'LoginChectk' 서블릿(또는 페이지)으로 데이터를 전송합니다.
				out.println("<form action='LoginChectk' method='post'>");
				out.println("<table border = '1' width ='300'>");

				// 아이디 입력 칸
				out.println("<tr>");
				out.println("<th width='100'>아이디</th>");
				out.println("<td width='200'>&nbsp;"
						+"<input type='text' name ='id'></td>");
				out.println("</tr>");

				// 비밀번호 입력 칸
				out.println("<tr>");
				out.println("<th width='100'>비밀번호</th>");
				out.println("<td width='200'>&nbsp;"
						+"<input type='password' name ='pwd'></td>");
				out.println("</tr>");

				// 하단 버튼 영역 (로그인 제출, 회원가입 버튼)
				out.println("<tr>");
				out.println("<td align ='center' colspan='2'>");
				out.println("<input type ='submit' value ='로그인'>");
				out.println("<input type ='button' value ='회원가입'>");
				out.println("</td>");
				out.println("</tr>");

				out.println("</table>");
				out.println("</form>");
				out.println("</body>");
				out.println("</html>");
			}
		}finally {
			// 출력 스트림을 닫아 자원을 해제합니다.
			out.close();
		}
	}
}