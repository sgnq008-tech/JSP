<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*"  %>

<%
   HashMap<String, Object> mapData =
   new HashMap<>();

   mapData.put("name", "홍길동");
   mapData.put("today", new Date());
%>
<c:set var="intArray" value="<%=new int[]{1,2,3,4,5} %>"/>
<c:set var="map" value="<%=mapData%>"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h3>1부터 100까지 홀수의 합</h3>
<c:set var="sum" value="0"/>

<c:forEach var="i" begin="1" end="100">
         <c:set var="sum" value="${sum + i }"/>
</c:forEach>
결과: ${sum}

<h3> 구구단: 7단</h3>
<ul>
<c:forEach var="i" begin="1" end="9">
   <li>7 * ${i}= ${7*i}</li>
</c:forEach>
</ul>

<h3>정수형 배열</h3>
<c:forEach var="i" items="${intArray}" begin="2" end="4">
[${i}]
</c:forEach>

<h3>Map</h3>
<c:forEach var="i" items="${map}">
${i.key}=${i.value}<br>
</c:forEach>
</body>
</html>