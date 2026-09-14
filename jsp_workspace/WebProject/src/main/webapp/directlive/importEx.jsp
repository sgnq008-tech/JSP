<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.Calendar" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Calendar</title>
</head>
<body>
<% 
    Calendar cal = Calendar.getInstance(); 
%>

<%= cal.get(Calendar.YEAR) %>년
<%= cal.get(Calendar.MONTH) + 1 %>월
<%= cal.get(Calendar.DATE) %>일 입니다.

</body>
</html>