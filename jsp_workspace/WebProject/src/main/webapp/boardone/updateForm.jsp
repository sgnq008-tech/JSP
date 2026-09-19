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
   // 글 번호와 페이지 번호 파라미터 받기
   int num = Integer.parseInt(request.getParameter("num"));
   String pageNum = request.getParameter("pageNum");

   try {
     // DAO 객체를 통해 기존 글 정보 가져오기
     BoardDAO dbPro = BoardDAO.getInstance();
     BoardVO article = dbPro.updateGetArticle(num);
%>
<body bgcolor="<%=bodyback_c%>">
    <div align="center">
       <b>글 수정</b> <br>
       <!-- 수정 처리 페이지로 이동하는 폼 -->
       <form action="updateProc.jsp?pageNum=<%=pageNum%>" name="writeForm"
          method="post" onsubmit="return writeSave()">
          <table width="400" bgcolor="<%=bodyback_c%>" border="1"
             cellpadding="0" cellspacing="0" align="center">

             <!-- 작성자 입력 및 글 번호 hidden 처리 -->
             <tr>
                <td width="70" bgcolor="<%=value_c%>" align="center">이름</td>
                <td width="330" align="left">
                <input type="text" size="12" maxlength="12"
                   name="writer" value="<%=article.getWriter()%>">

                <input type="hidden" name="num" value="<%=article.getNum()%>"></td>
             </tr>

             </tr>

             <!-- 제목 입력 -->
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

             <!-- 이메일 입력 -->
             <tr>
                <td width="70" bgcolor="<%=value_c%>" align="center">이메일</td>
                <td width="330" align="left">
                <input type="text" size="40"
                   maxlength="40" name="email" value="<%=article.getEmail()%>">
                </td>
             <!-- 내용 입력 -->
             <tr>
                <td width="70" bgcolor="<%=value_c%>" align="center">내용</td>
                <td width="330" align="left">
                <textarea rows="13" cols="50" name="content" <%=article.getContent()%>>
                </textarea>
                </td>
             </tr>

             <!-- 비밀번호 입력 -->
             <tr>
                <td width="70" bgcolor="<%=value_c%>" align="center">비밀번호</td>
                <td width="330" align="left">
                <input type="password" size="10" maxlength="10" name="pass">
                </td>
             </tr>

             <!-- 버튼 영역 -->
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