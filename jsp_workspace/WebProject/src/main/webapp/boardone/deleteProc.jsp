<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.boardone.BoardDAO"%>
<%@ page import="java.sql.Timestamp"%>

<%   
    request.setCharacterEncoding("utf-8");
%>

<%
      int num = Integer.parseInt(request.getParameter("num"));
      String pageNum = request.getParameter("pageNum");
      String pass = request.getParameter("pass");
      BoardDAO dbPro = BoardDAO.getInstance();
      int check = dbPro.deletArticle(num, pass);
      if(check == 1){
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" http-equiv="refresh" content="0;url=list.jsp?pageNum=<%=pageNum%>">
<title>Insert title here</title>
</head>
<body>
<%}else{ %>
<script type="text/javascript">
alert("비밀번호가 맞지않습니다.");
history.go(-1);
</script>

<%} %>
</body>
</html>