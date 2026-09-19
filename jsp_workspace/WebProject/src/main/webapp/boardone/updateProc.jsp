<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="com.boardone.BoardDAO"%>
<%@ page import="java.sql.Timestamp"%>

<%
    // 한글이 깨지지 않도록 요청 인코딩을 utf-8로 설정합니다.
    request.setCharacterEncoding("utf-8");
%>

<!-- 전달된 파라미터(수정된 제목, 내용, 비밀번호, 글 번호 등)를 BoardVO 객체에 자동으로 쏙쏙 담아줍니다 -->
<jsp:useBean id="article" class="com.boardone.BoardVO">
    <jsp:setProperty name="article" property="*" />
</jsp:useBean>

<%
      // 이전 페이지 번호 파라미터를 받아옵니다.
      String pageNum = request.getParameter("pageNum");

      // DAO 객체를 불러와 글 수정 메소드(updateArticle)를 실행하고 결과값(1 또는 0)을 받습니다.
      BoardDAO dbPro = BoardDAO.getInstance();
      int check = dbPro.updateArticle(article);

      // 글 수정이 성공했을 때 (비밀번호가 일치하여 DB가 정상 수정된 경우)
      if(check == 1){
%>
<!DOCTYPE html>
<html>
<head>
<!-- 성공적으로 수정되었다면 목록 페이지(list.jsp)로 이동합니다 -->
<meta charset="UTF-8" http-equiv="refresh" content="0;url=list.jsp?pageNum=<%=pageNum%>">
<title>Insert title here</title>
</head>
<body>
<%}else{ // 글 수정이 실패했을 때 (비밀번호가 틀렸거나 오류가 난 경우%>
<script type="text/javascript">
// 경고창을 띄우고 이전 수정 폼 화면으로 되돌아갑니다.
alert("비밀번호가 맞지않습니다.");
history.go(-1);
</script>

<%} %>
</body>
</html>