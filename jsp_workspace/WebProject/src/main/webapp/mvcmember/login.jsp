<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Log in</title>
<link href = "style.css" rel = "stylesheet" type= "text/css">
</head>
<body>
<c:choose>
<c:when test="${loginID ne null}">
<table border="1" width= "300">
<tr>
	<td colspan="3" align ="center">
	<c:out value="${loginID}"/> </td>
	
</tr>
<tr>
	<td align ="center" width = "100">
	<a href = "member.mdo?cmd=modifyForm"> 정보수정</a>
	</td>
	<td align = "center" width = "100">
		<a href="member.mdo?cmd=deleteForm">회원탈퇴</a>
	</td>
	<td align="center" width ="100">
		<a href="member.mdo?cmd=logout">로그아웃</a>
	</td>
</tr>
</table>
</c:when>
<c:otherwise>

<c:if test="${requestScope.check eq 0}">
  <script type="text/javascript">
     alert("비밀번호가 틀렸습니다.");
  </script>
</c:if>

<c:if test="${requestScope.check eq -1}">
  <script type="text/javascript">
     alert("ID가 존재하지않습니다.");
  </script>
</c:if>

<c:if test="${requestScope.check eq -1}">
  <script type="text/javascript">
     alert("PASS가 존재하지않습니다.");
  </script>
</c:if>

<form action = "member.mdo?cmd=loginProc" method="post">
<table border="1" width= "300">
	<tr>
		<td colspan="2" align="center">회원 로그인</td>
	</tr>
	
	<tr>
		<td align="right" width = "100">아이디 :</td>
		<td width = "200">
			<input type = "text" name = "id" size = "20">
		</td>
	</tr>
	
	<tr>
		<td align="right" width = "100"> 비밀번호 :</td>
		<td width = "200">
			<input type = "password" name = "pass" size = "20">
		</td>
	</tr>
	<tr>
		<td colspan= "2" align="center">
			<input type="submit" value="로그인">&nbsp;&nbsp;
			<input type= "button" value = "회원가입"
			onclick= "javascript:window.location='member.mdo?cmd=regForm'">
	</tr>
</table>
</form>
</c:otherwise>
</c:choose>
</body>
</html>