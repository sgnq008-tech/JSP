<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%

 String[] movieList = {"프리즌 브레이크","스파르타쿠스","히어로즈"}; 
 request.setAttribute("movieList",movieList);

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
인기 미드:

<ul>
<c:forEach var="movie" items="${movieList}">
                        <!-- EL -->
<li>${movie}</li>
</c:forEach>


</ul>
</body>
</html>