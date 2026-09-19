<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="com.boardone.BoardDAO"%>
<%@ page import="java.sql.Timestamp"%>

<%
    // 한글이 깨지지 않도록 요청 인코딩을 utf-8로 설정합니다.
    request.setCharacterEncoding("utf-8");
%>

<%
      // 삭제 폼에서 넘어온 글 번호(num), 페이지 번호(pageNum), 입력한 비밀번호(pass)를 받아옵니다.
      int num = Integer.parseInt(request.getParameter("num"));
      String pageNum = request.getParameter("pageNum");
      String pass = request.getParameter("pass");

      // DAO 객체를 불러와 삭제 메소드(deletArticle)를 실행하고 결과값(1 또는 0)을 받습니다.
      BoardDAO dbPro = BoardDAO.getInstance();
      int check = dbPro.deletArticle(num, pass);

      // 삭제가 성공했을 때 (비밀번호가 일치하여 DB에서 정상 삭제된 경우)
      if(check == 1){
%>

<!DOCTYPE html>
<html>
<head>
<!-- 성공적으로 삭제되었다면 목록 페이지(list.jsp)로 이동합니다 -->
<meta charset="UTF-8" http-equiv="refresh" content="0;url=list.jsp?pageNum=<%=pageNum%>">
<title>Insert title here</title>
</head>
<body>
<%}else{ // 삭제가 실패했을 때 (비밀번호가 틀렸거나 오류가 난 경우%>
<script type="text/javascript">
// 경고창을 띄우고 이전 삭제 폼 화면으로 되돌아갑니다.
alert("비밀번호가 맞지않습니다.");
history.go(-1);
</script>

<%} %>
</body>
</html>