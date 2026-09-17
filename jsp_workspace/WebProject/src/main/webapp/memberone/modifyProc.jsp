<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.memberone.*" %>

<%request.setCharacterEncoding("utf-8");%>

<jsp:useBean id="dao" class="com.memberone.studentDAO"/>
<jsp:useBean id="vo" class="com.memberone.studentVO">
<jsp:setProperty name="vo" property="*"/>
</jsp:useBean>

<%
String loginID = (String)session.getAttribute("loginID");

vo.setId(loginID);

dao.updateMember(vo);

//response.sendRedirect("login.jsp");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" http-equiv="refresh" content="3;url='login.jsp'">
<title>회원정보 수정</title>
</head>
<body>
<div align="center">
<font size="5" face="궁서체">
입력하신 내용대로 <b>회원정보가 수정되었습니다.</b><br>
Login Page로 이동합니다.
</font>
</div>

</body>
</html>