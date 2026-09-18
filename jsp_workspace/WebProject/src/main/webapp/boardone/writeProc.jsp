<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.boardone.BoardDAO"%>
<%@ page import="java.sql.Timestamp"%>

<%
    // 한글 인코딩
    request.setCharacterEncoding("utf-8");
%>

<jsp:useBean id="article" class="com.boardone.BoardVO">

	<jsp:setProperty name="article" property="*" />

</jsp:useBean>

<%
    // 작성 날짜
    article.setRegdate(
        new Timestamp(System.currentTimeMillis())
    );

    // 작성자 IP
    article.setIp(
        request.getRemoteAddr()
    );

    // DAO 객체 생성
    BoardDAO dbPro = BoardDAO.getInstance();

    // 게시글 저장
    dbPro.insertArticle(article);

    // 게시글 목록으로 이동
    response.sendRedirect("list.jsp");
%>