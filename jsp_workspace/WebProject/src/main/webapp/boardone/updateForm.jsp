<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.boardone.BoardDAO"%>
<%@ page import="com.boardone.BoardVO"%>
<%@ include file="view/color.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="script.js"></script>
<title>게시판</title>
</head>
<%
   int num = Integer.parseInt(request.getParameter("num"));
   String pageNum = request.getParameter("pageNum");
   
   try {
	 BoardDAO dbPro = BoardDAO.getInstance();
	 BoardVO article = dbPro.updateGetArticle(num);   
%>
<body bgcolor="<%=bodyback_c%>">
	<div align="center">
		<b>글 수정</b> <br>
		<form action="updateProc.jsp?pageNum=<%=pageNum%>" name="writeForm"
			method="post" onsubmit="return writeSave()">
			<table width="400" bgcolor="<%=bodyback_c%>" border="1"
				cellpadding="0" cellspacing="0" align="center">

				<tr>
					<td width="70" bgcolor="<%=value_c%>" align="center">이름</td>
					<td width="330" align="left">
					<input type="text" size="12" maxlength="12"
						name="writer" value="<%=article.getWriter()%>"> 
					
					<input type="hidden" name="num" value="<%=article.getNum()%>"></td>
				</tr>

				</tr>

				<tr>
					<td width="70" bgcolor="<%=value_c%>" align="center">제목</td>
					<td width="330" align="left">
						<%
						if (request.getParameter("num") == null) {// 새글일떄
						%> 
						<input type="text" size="50" maxlength="50" name="subject"
						value="<%=article.getSubject()%>"> 
						<%} else {// 답변글일떄%> 
						<input type="text" size="50" maxlength="50" name="subject" value="[답변글]"> <%}%>
					</td>
				</tr>

				<tr>
					<td width="70" bgcolor="<%=value_c%>" align="center">이메일</td>
					<td width="330" align="left">
					<input type="text" size="40"
						maxlength="40" name="email" value="<%=article.getEmail()%>">
					</td>
				<tr>
					<td width="70" bgcolor="<%=value_c%>" align="center">내용</td>
					<td width="330" align="left">
					<textarea rows="13" cols="50" name="content" <%=article.getContent()%>>
					</textarea>
					</td>
				</tr>

				<tr>
					<td width="70" bgcolor="<%=value_c%>" align="center">비밀번호</td>
					<td width="330" align="left">
					<input type="password" size="10" maxlength="10" name="pass">
					</td>
				</tr>

				<tr>
					<td colspan="2" bgcolor="<%=value_c%>" align="center"><input
						type="submit" value="글수정"> 
						
						<input type="reset" value="다시작성"> 
						
						<input type="button" value="목록" onclick="document.location.href='list.jsp?pageNum
						=<%=pageNum%>'"></td>
				</tr>


			</table>
		</form>
	</div>
	<%}catch(Exception e){e.printStackTrace();} %>
</body>
</html>