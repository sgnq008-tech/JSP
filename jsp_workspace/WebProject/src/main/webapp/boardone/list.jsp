```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%-- 게시판에서 사용할 클래스 import --%>
<%@ page import="com.boardone.BoardDAO"%>
<%@ page import="com.boardone.BoardVO"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>

<%-- 색상 변수 가져오기 --%>
<%@ include file="view/color.jsp"%>


<%
/* ================================
   게시판 기본 설정
   ================================ */

int pageSize = 5; // 한 페이지에 보여줄 글 개수

// 날짜 출력 형식
SimpleDateFormat sdf =
    new SimpleDateFormat("yyyy-MM-dd HH:mm");

// 한글 검색어가 깨지지 않도록 UTF-8 설정
request.setCharacterEncoding("UTF-8");


/* ================================
   페이지 / 검색 정보 가져오기
   ================================ */

// 현재 페이지 번호
String pageNum = request.getParameter("pageNum");

// 검색 기준 (writer, subject, content)
String searchWhat = request.getParameter("searchWhat");

// 검색어
String searchText = request.getParameter("searchText");

// 페이지 번호가 없으면 1페이지
if(pageNum == null) {
    pageNum = "1";
}

int currentPage = Integer.parseInt(pageNum);


/* ================================
   DB에서 가져올 글 범위 계산
   ================================ */

// 1페이지 → 1~5
// 2페이지 → 6~10
// 3페이지 → 11~15
int startRow = (currentPage - 1) * pageSize + 1;
int endRow = currentPage * pageSize;


// 글 개수와 게시글 목록을 저장할 변수
int count = 0;
int number = 0;
List<BoardVO> articleList = null;

// BoardDAO 객체 가져오기
BoardDAO dbPro = BoardDAO.getInstance();


/* ================================
   검색 여부에 따라 DB 조회
   ================================ */

if(searchText == null) {

    // 검색하지 않은 경우 → 전체 글 조회
    count = dbPro.getArticleCount();

    if(count > 0) {
        articleList = dbPro.getArticles(startRow, endRow);
    }

} else {

    // 검색한 경우 → 검색 결과만 조회
    count = dbPro.getArticleCount(searchWhat, searchText);

    if(count > 0) {
        articleList =
            dbPro.getArticles(
                searchWhat,
                searchText,
                startRow,
                endRow
            );
    }
}


/* ================================
   게시글 번호 계산
   ================================ */

// 최신 글부터 번호를 표시하기 위한 값
number = count - (currentPage - 1) * pageSize;

%>


<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">

<title>게시판</title>

<!-- CSS / JavaScript 연결 -->
<link href="style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="script.js"></script>

</head>


<body bgcolor="<%=bodyback_c%>">

<div align="center">


<!-- 전체 글 개수 표시 -->
<b>글목록(전체 글:<%=count%>)</b>


<table width="700">

<tr>
<td align="right" bgcolor="<%=value_c%>">

<!-- 글쓰기 페이지로 이동 -->
<a href="writeForm.jsp">글쓰기</a>

</td>
</tr>

</table>


<%
/* ================================
   게시글이 없는 경우
   ================================ */

if(count == 0) {
%>


<table width="700" border="1">

<tr>
<td align="center">
게시판에 저장된 글이 없습니다.
</td>
</tr>

</table>


<%
} else {
%>


<!-- ================================
     게시글이 있는 경우
     ================================ -->

<table width="700" border="1"
       cellpadding="0" cellspacing="0">


<!-- 게시판 제목 -->
<tr height="30" bgcolor="<%=value_c%>">

<td align="center" width="50">번호</td>
<td align="center" width="250">제목</td>
<td align="center" width="100">작성자</td>
<td align="center" width="150">작성일</td>
<td align="center" width="50">조회</td>
<td align="center" width="100">IP</td>

</tr>


<%
/* ================================
   게시글 하나씩 출력
   ================================ */

for(int i = 0; i < articleList.size(); i++) {

    // List에서 게시글 하나를 꺼냄
    BoardVO article =
        (BoardVO)articleList.get(i);
%>


<tr height="30">


<!-- 게시글 번호 -->
<td align="center">
    <%=number--%>
</td>


<td width="250">

<%
/* ================================
   답글 들여쓰기
   ================================ */

int wid = 0;

if(article.getDepth() > 0) {

    // 답글 깊이에 따라 들여쓰기
    wid = 5 * article.getDepth();
%>

<img src="img/level.gif"
     width="<%=wid%>" height="16">

<!-- 답글 표시 -->
<img src="img/re.gif">

<%
} else {
%>

<!-- 원글 -->
<img src="img/level.gif"
     width="0" height="16">

<%
}
%>


<!-- 제목 클릭 → 글 내용 페이지 -->
<a href="content.jsp?num=<%=article.getNum()%>
&pageNum=<%=currentPage%>">

    <%=article.getSubject()%>

</a>

</td>


<!-- 작성자 -->
<td align="center">

<a href="mailto:<%=article.getEmail()%>">
    <%=article.getWriter()%>
</a>


<%
/* 조회수가 5 이상이면 HOT 표시 */
if(article.getReadcount() >= 5) {
%>

<img src="img/hot.gif"
     border="0" height="16">

<%
}
%>

</td>


<!-- 작성일 -->
<td align="center">

<%=sdf.format(article.getRegdate())%>

</td>


<!-- 조회수 -->
<td align="center">

<%=article.getReadcount()%>

</td>


<!-- IP -->
<td align="center">

<%=article.getIp()%>

</td>


</tr>


<%
}
%>

</table>


<%
}
%>


<%
/* ================================
   페이징 처리
   ================================ */

if(count > 0) {

    // 한 번에 보여줄 페이지 번호 개수
    int pageBlock = 5;

    // 전체 페이지 수 계산
    int pageCount =
        count / pageSize +
        (count % pageSize == 0 ? 0 : 1);

    // 현재 페이지가 속한 페이지 블록의 시작
    int startPage =
        ((currentPage - 1) / pageBlock)
        * pageBlock + 1;

    // 페이지 블록의 마지막
    int endPage =
        startPage + pageBlock - 1;

    if(endPage > pageCount) {
        endPage = pageCount;
    }


    // 검색 중이라면 페이지 이동 시 검색 조건 유지
    String searchParam = "";

    if(searchText != null) {
        searchParam =
            "&searchWhat=" + searchWhat +
            "&searchText=" + searchText;
    }


    // 이전 페이지 블록
    if(startPage > pageBlock) {
%>

<a href="list.jsp?pageNum=<%=startPage - pageBlock%>
<%=searchParam%>">
[이전]
</a>

<%
    }


    // 페이지 번호 출력
    for(int i = startPage; i <= endPage; i++) {

        if(i == currentPage) {
%>

<!-- 현재 페이지 -->
<b>[<%=i%>]</b>

<%
   } else {
%>

<!-- 다른 페이지 -->
<a href="list.jsp?pageNum=<%=i%><%=searchParam%>">
[<%=i%>]
</a>

<%
        }
    }


    // 다음 페이지 블록
    if(endPage < pageCount) {
%>

<a href="list.jsp?pageNum=<%=startPage + pageBlock%>
<%=searchParam%>">[다음]
</a>

<%
    }
}
%>


<!-- ================================
     검색창
     ================================ -->

<form action="list.jsp" method="post">

<!-- 검색 기준 -->
<select name="searchWhat">

<option value="writer">작성자</option>
<option value="subject">제목</option>
<option value="content">내용</option>

</select>

<!-- 검색어 -->
<input type="text" name="searchText">

<!-- 검색 버튼 -->
<input type="submit" value="검색">

</form>


</div>

</body>
</html>
