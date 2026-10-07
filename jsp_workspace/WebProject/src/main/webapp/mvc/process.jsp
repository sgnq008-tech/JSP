<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>요청 파라미터로 명령어 전달</title>
</head>
<body>
처리 결과:
<c:set var="message" value="${message}"/>
<c:out value="${message}"/>

<!-- http://localhost:9090/WebProject/mvc/message.cdo 확인 url -->

</body>
</html>