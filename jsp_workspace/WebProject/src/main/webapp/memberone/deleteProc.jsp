<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.memberone.*" %>

<jsp:useBean id="dao" class="com.memberone.studentDAO"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" http-equiv="refresh" content="3;url='login.jsp'">
<title>회원탈퇴</title>
</head>
<%
     String loginID = (String)session.getAttribute("loginID");
     String pass = request.getParameter("pass");
     
     int check = dao.deleteMember(loginID, pass);
     if(check == 1){
    	 session.invalidate();
%>
<body>
<div align="left">
<font size="5" face="궁서체">
회원정보가 삭제되었습니다.<br>
너무나 아쉽습니다.<br>
바이 바이<br>
3초후 로그인 페이지로 이동합니다.
</font>
<%}else{ %>
<script type = "text/javascript">
alert("비밀번호가 틀렸습니다.");
history.go(-1);
</script>
<%} %>
</div>
</body>
</html>