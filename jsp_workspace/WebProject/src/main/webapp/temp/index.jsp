<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<% request.setAttribute("PAGETITLE", "정보보기"); %>
<jsp:forward page = "/temp/template/template.jsp">
	<jsp:param value = "info_view.jsp" name="CONTENTPAGE"/>
</jsp:forward>
</body>
</html>