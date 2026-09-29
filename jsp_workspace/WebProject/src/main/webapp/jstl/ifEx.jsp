<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/sample/first.jsp" var="url"></c:import>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<c:if test="${3 > 4}">
이 내용은 화면에 나타나지 않습니다. 
</c:if>

<c:if test="${ param.type eq 'guest'}">
나의 홈에 오신 여러분을 열렬히 환영합니다.<br>
좀 더 많은 내용을 보시려면 회원가입을 하시고, 즐기시기 바랍니다. 
</c:if>

<c:if test="${ param.type eq 'member'}">
회원님을 열렬히 환영합니다.<br>
즐거운 쇼핑이 되시기를 바랍니다. 
</c:if>

<!-- http://localhost:9090/jstl/ifEx.jsp?type=guest      확인용
     http://localhost:9090/jstl/ifEx.jsp?type=member     확인용 -->
</body>
</html>