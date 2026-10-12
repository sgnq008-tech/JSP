<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${check == 1}">
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" http-equiv="refresh" content="0;url=/board/list.bdo?pageNum=${pageNum}">
<title>Insert title here</title>
</head>
<body>
</c:if>

<c:if test="${check == 0}">
<script type="text/javascript">
alert("비밀번호가 맞지않습니다.");
history.go(-1);
</script>
</c:if>



</body>
</html>