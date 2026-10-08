<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입 확인</title>
<link href="style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="script.js"></script>
</head>
<body style="background-color: #ffffcc;">
<br>

<div align="center">
    <c:choose>
        <%-- flag가 true일 때 (회원가입 성공) --%>
        <c:when test="${flag}">
            <b>회원 가입을 축하드립니다.</b><br><br>
            <a href="member.mdo?cmd=login">로그인</a>
        </c:when>
        <%-- flag가 false이거나 비어있을 때 (회원가입 실패) --%>
        <c:otherwise>
            <b>다시 입력해주세요.</b><br><br>
            <a href="member.mdo?cmd=regForm">다시입력</a>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>