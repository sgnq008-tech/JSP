<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="com.boardone.BoardDAO" %>
<%@ page import="com.boardone.BoardVO" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ include file="view/color.jsp" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
<link href="style.css" rel="stylesheet" type="text/css">
</head>

<%
   // 1. 이전 페이지(목록 등)에서 전달된 글 번호(num)와 페이지 번호(pageNum)를 받아옵니다.
   int num = Integer.parseInt(request.getParameter("num"));
   String pageNum = request.getParameter("pageNum");

   // 2. 날짜 출력 형태를 예쁘게 꾸며줄 SimpleDateFormat 객체를 생성합니다. (년-월-일 시:분)
   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

   try {
          // 3. DAO 객체를 통해 선택한 글 번호의 상세 정보를 가져오고 조회수를 1 증가시킵니다.
          BoardDAO dbPro = BoardDAO.getInstance();
          BoardVO article = dbPro.getArticle(num);

          // 4. 나중에 '답글쓰기' 기능을 사용할 때 필요한 계층형 정보(ref, step, depth)를 미리 변수에 담습니다.
          int ref   = article.getRef();
          int step  = article.getStep();
          int depth = article.getDepth();
%>

<body bgcolor="<%=bodyback_c%>">

<div align="center">
<b>글 상세보기</b><br><br>

<form action="">
<table width="500" border="1" cellpadding="0" cellspacing="0"
bgcolor="<%=bodyback_c%>" align="center">

<!-- [1행] 글번호 / 조회수 출력 -->
<tr height="30">
    <td align="center" width="125" bgcolor="<%=value_c%>">글번호</td>
    <td align="center" width="125" ><%=article.getNum()%></td>
    <td align="center" width="125" bgcolor="<%=value_c%>">조회수</td>
    <td align="center" width="125" ><%=article.getReadcount()%></td>
</tr>

<!-- [2행] 작성자 / 작성일 출력 -->
<tr height="30">
    <td align="center" width="125" bgcolor="<%=value_c%>">작성자</td>
    <td align="center" width="125" ><%=article.getWriter()%></td>
    <td align="center" width="125" bgcolor="<%=value_c%>">작성일</td>
    <td align="center" width="125" ><%=sdf.format(article.getRegdate())%></td>
</tr>

<!-- [3행] 글제목 출력 -->
<tr height="30">
    <td align="center" width="125" bgcolor="<%=value_c%>">글제목</td>
    <td align="center" width="375" colspan="3"><%=article.getSubject()%></td>
</tr>

<!-- [4행] 글내용 출력 (<pre> 태그를 사용하여 줄바꿈과 공백을 유지합니다) -->
<tr height="30">
    <td align="center" width="125" bgcolor="<%=value_c%>">글내용</td>
    <td align="left" width="375" colspan="3">
    <pre><%=article.getContent()%></pre></td>
</tr>

<!-- [5행] 하단 버튼 영역 (글수정, 글삭제, 답글쓰기, 글목록) -->
<tr height="30">
    <td colspan="4" bgcolor="<%=value_c%>" align="right">

    <!-- 글 수정 페이지로 이동 (글 번호와 현재 페이지 번호를 함께 넘김) -->
    <input type="button" value="글수정"
    onclick="document.location.href='updateForm.jsp?num=<%=article.getNum()%>&pageNum=<%=pageNum%>'">
    &nbsp;&nbsp;&nbsp;&nbsp;

    <!-- 글 삭제 페이지로 이동 -->
    <input type="button" value="글삭제"
    onclick="document.location.href='deleteForm.jsp?num=<%=article.getNum()%>&pageNum=<%=pageNum%>'">
    &nbsp;&nbsp;&nbsp;&nbsp;

    <!-- 답글쓰기 페이지로 이동 (원글의 ref, step, depth 정보를 함께 넘겨 계층 구조를 맞춤) -->
    <input type="button" value="답글쓰기"
    onclick="document.location.href='writeForm.jsp?num=<%=num%>&ref=<%=ref%>&step=<%=step%>&depth=<%=depth%>'">
    &nbsp;&nbsp;&nbsp;&nbsp;

    <!-- 목록으로 돌아가기 -->
    <input type="button" value="글목록"
    onclick="document.location.href='list.jsp?pageNum=<%=pageNum%>'">
    </td>

</tr>

</table>
<%
   // try 블록 끝 (데이터베이스 조회 중 예외가 발생하면 콘솔에 에러 스택을 출력합니다)
   }catch(Exception e){e.printStackTrace();}
%>
</form>

</div>
</body>
</html>