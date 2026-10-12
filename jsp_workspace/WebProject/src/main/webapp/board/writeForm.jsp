<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="view/color.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MY Board</title>

<link href="style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="script.js"></script>

</head>
<!-- 새글인지 아니면 답변글인지를 구분해야함 -->


<body bgcolor="${bodyback_c}">
	<div align="center">
		<b>글쓰기</b><br>

		<form action="/board/writeForm.bdo" method="post" name="writeForm"
			onsubmit="return writeSave()">

			<input type="hidden" name="num" value="${num}"> 
			<input type="hidden" name="ref" value="${ref}"> 
			<input type="hidden" name="step" value="${step}"> 
		    <input type="hidden" name="depth" value="${depth}">

			<table width="400" border="1" cellpadding="0" cellspacing="0"
				align="center" bgcolor="${bodyback_c}">
				<tr>
					<td align="right" colspan="2" bgcolor="${value_c}"><a
						href="/board/list.bdo">글 목록</a>
					</td>
				</tr>

				<tr>
					<td width="70" bgcolor="${value_c}">이름</td>
					<td width="330">
					<input type="text" size="12" maxlength="12" name="writer"></td>
				</tr>

				<tr>
					<td width="70" bgcolor="${value_c}">이메일</td>
					<td width="330">
					<input type="text" size="40" maxlength="40" name="email">
					</td>
				</tr>

				<tr>
					<td width="70" bgcolor="${value_c}">제목</td>
					<td width="330">
					
					<c:if test="${num ==0}">
					<input type="text" size="50" maxlength="50" name="subject">
					</c:if>
					
					<c:if test="${num !=0}">
					<input type="text" size="50" maxlength="50" name="subject" value="[답변글]"> 
					</c:if>
					
					</td>
				</tr>

				<tr>
					<td width="70" bgcolor="${value_c}">내용</td>
					<td width="330"><textarea rows="13" cols="50" name="content"></textarea>
					</td>
				</tr>

				<tr>
					<td width="70" bgcolor="${value_c}">비밀번호</td>
					<td width="330">
					<input type="password" size="10"
						maxlength="10" name="pass"></td>
				</tr>

				<tr>
					<td colspan="2" bgcolor="${value_c}" align="center">
					    <input type="submit" value="글쓰기"> 
						<input type="reset"  value="다시작성"> 
						<input type="button" value="글목록"
						onclick="window.location='/board/list.bdo'">
					</td>
				</tr>
			</table>
		
		</form>
		
	</div>
</body>
</html>