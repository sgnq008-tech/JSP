<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<title>게시판</title>

<!-- CSS / JavaScript 연결 -->
<link href="${pageContext.request.contextPath}/style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${pageContext.request.contextPath}/script.js"></script>

<!-- 글씨체 크기를 살짝 줄임 (9pt -> 8pt) -->
<style>
    body, td, select, div, form, textarea, input, a {
        font-family: 궁서, verdana, tahoma !important;
        font-size: 8pt !important;
    }
</style>

</head>
<body bgcolor="${bodyback_c}">

<div align="center">

<br>
<!-- 전체 글 개수 표시 -->
<table width="700" border="1" cellpadding="0" cellspacing="0">
    <tr height="25" bgcolor="${value_c}">
        <td align="center"><b>글목록 (전체 글 : ${count})</b></td>
    </tr>
</table>
<br>

<table width="700">
<tr>
<td align="right">
<!-- 글쓰기 페이지로 이동 -->
<a href="${pageContext.request.contextPath}/board/writeForm.bdo">글쓰기</a>
</td>
</tr>
</table>

<!-- 글이 없을 경우 -->
<c:if test="${count == 0}">
<table width="700" border="1" cellpadding="0" cellspacing="0">
<tr height="40" bgcolor="${value_c}">
<td align="center">
게시판에 저장된 글이 없습니다.
</td>
</tr>
</table>
</c:if>

<!-- 글이 있을 경우 -->
<c:if test="${count > 0}">

<table width="700" border="1" cellpadding="0" cellspacing="0">

<!-- 게시판 헤더 -->
<tr height="25" bgcolor="${value_c}">
<td align="center" width="50">번호</td>
<td align="center" width="250">제목</td>
<td align="center" width="100">작성자</td>
<td align="center" width="150">작성일</td>
<td align="center" width="50">조회</td>
<td align="center" width="100">IP</td>
</tr>

<c:forEach var="article" items="${articleList}">
<tr height="25">

<!-- 게시글 번호 -->
<td align="center" width="50">
    <c:out value="${number}"/>
    <c:set var="number" value="${number - 1}"/>
</td>

<!-- 제목 (답글 들여쓰기 및 링크) -->
<td width="250">
    <c:if test="${article.depth > 0}">
        <img src="${pageContext.request.contextPath}/img/level.gif" width="${5 * article.depth}" height="14">
        <img src="${pageContext.request.contextPath}/img/re.gif">
    </c:if>
    <c:if test="${article.depth == 0}">
        <img src="${pageContext.request.contextPath}/img/level.gif" width="${5 * article.depth}" height="14">
    </c:if>

    <a href="${pageContext.request.contextPath}/board/content.bdo?num=${article.num}&pageNum=${currentPage}">
        ${article.subject}
    </a>
</td>

<!-- 작성자 및 이메일 -->
<td align="center">
    <a href="mailto:${article.email}">${article.writer}</a>
</td>

<!-- 작성일 (JSTL fmt 태그로 날짜 포맷팅) -->
<td align="center">
    <fmt:formatDate value="${article.regdate}" pattern="yyyy-MM-dd HH:mm"/>
</td>

<!-- 조회수 -->
<td align="center">
    ${article.readcount}
</td>

<!-- IP -->
<td align="center">
    ${article.ip}
</td>

</tr>
</c:forEach>

</table>
</c:if>

<br>

<!-- ================================
     페이징 처리 (JSTL / EL 활용)
     ================================ -->
<c:if test="${count > 0}">
    <c:set var="pageBlock" value="5" />

    <!-- 전체 페이지 수 계산 -->
    <c:set var="pageCount" value="${count / pageSize + (count % pageSize == 0 ? 0 : 1)}" />
    
    <fmt:parseNumber var="result" value="${(currentPage - 1) / pageBlock}" integerOnly="true"/>
    
    <!-- 현재 페이지 블록의 시작 페이지 -->
    <c:set var="startPage" value="${result * pageBlock + 1}" />
    
    <!-- 현재 페이지 블록의 마지막 페이지 -->
    <c:set var="endPage" value="${startPage + pageBlock - 1}" />
    
    <c:if test="${endPage > pageCount}">
        <c:set var="endPage" value="${pageCount}" />
    </c:if>

    <!-- 이전 페이지 블록 -->
    <c:if test="${startPage > pageBlock}">
        <a href="${pageContext.request.contextPath}/board/list.bdo?pageNum=${startPage - pageBlock}">[이전]</a>
    </c:if>

    <!-- 페이지 번호 출력 -->
    <c:forEach var="i" begin="${startPage}" end="${endPage}">
        <c:if test="${i == currentPage}">
            <b>[${i}]</b>
        </c:if>
        <c:if test="${i != currentPage}">
            <a href="${pageContext.request.contextPath}/board/list.bdo?pageNum=${i}">[${i}]</a>
        </c:if>
    </c:forEach>
     
    <!-- 다음 페이지 블록 -->
    <c:if test="${endPage < pageCount}">
        <a href="${pageContext.request.contextPath}/board/list.bdo?pageNum=${startPage + pageBlock}">[다음]</a>
    </c:if>
</c:if>

<br><br>

<form action="${pageContext.request.contextPath}/board/list.bdo" method="post">
<select name="searchWhat">
    <option value="writer">작성자</option>
    <option value="subject">제목</option>
    <option value="content">내용</option>
</select>
<input type="text" name="searchText">
<input type="submit" value="검색">
</form>

</div>

</body>
</html>