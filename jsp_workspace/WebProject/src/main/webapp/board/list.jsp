<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:if test="${empty bodyback_c}"><c:set var="bodyback_c" value="#e0ffff" /></c:if>
<c:if test="${empty value_c}"><c:set var="value_c" value="#b0e0e6" /></c:if>

<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<title>게시판</title>

<link href="${ctx}/style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${ctx}/script.js"></script>

<style>
    body, td, select, div, form, textarea, input, a {
        font-family: 궁서, verdana, tahoma !important;
        font-size: 8pt !important;
    }
</style>

<script type="text/javascript">
// 페이지 번호 클릭: 검색 조건(find, find_box)을 유지한 채 해당 페이지로 이동
function frm_sub(pageNum) {
    document.i_frm.action = "${ctx}/board/list.bdo?pageNum=" + pageNum;
    document.i_frm.submit();
}

// 검색 버튼 클릭 시 검사
function check() {
    if (document.find_frm.find_box.value == "") {
        alert("검색어를 입력하세요.");
        document.find_frm.find_box.focus();
        return false;
    }
    return true;
}
</script>

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

<table width="700" border="0" bgcolor="${value_c}">
<tr>
    <td align="left">
        <a href="${ctx}/board/list.bdo">전체목록</a>
    </td>
    <td align="right">
        <a href="${ctx}/board/writeForm.bdo">글쓰기</a>
    </td>
</tr>
</table>

<!-- 글이 없을 경우 -->
<c:if test="${count == 0}">
<table width="700" border="1" cellpadding="0" cellspacing="0">
<tr height="40" bgcolor="${value_c}">
    <td align="center">게시판에 저장된 글이 없습니다.</td>
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
            <img src="${ctx}/img/level.gif" width="${5 * article.depth}" height="14">
            <img src="${ctx}/img/re.gif">
        </c:if>
        <c:if test="${article.depth == 0}">
            <img src="${ctx}/img/level.gif" width="0" height="14">
        </c:if>

        <a href="${ctx}/board/content.bdo?num=${article.num}&pageNum=${currentPage}">
            ${article.subject}
        </a>
    </td>

    <!-- 작성자 및 이메일 -->
    <td align="center">
        <a href="mailto:${article.email}">${article.writer}</a>
    </td>

    <!-- 작성일 -->
    <td align="center">
        <fmt:formatDate value="${article.regdate}" pattern="yyyy-MM-dd HH:mm"/>
    </td>

    <!-- 조회수 -->
    <td align="center">${article.readcount}</td>

    <!-- IP -->
    <td align="center">${article.ip}</td>

</tr>
</c:forEach>

</table>
</c:if>

<br>

<!-- ================================
     페이징 처리
     ================================ -->
<c:if test="${count > 0}">
    <c:set var="pageBlock" value="5" />

    <!-- 전체 페이지 수 (올림 계산 후 정수로 변환) -->
    <fmt:parseNumber var="pageCount" value="${(count + pageSize - 1) / pageSize}" integerOnly="true" />

    <!-- 현재 페이지 블록 번호 -->
    <fmt:parseNumber var="result" value="${(currentPage - 1) / pageBlock}" integerOnly="true" />

    <!-- 블록의 시작/마지막 페이지 -->
    <c:set var="startPage" value="${result * pageBlock + 1}" />
    <c:set var="endPage" value="${startPage + pageBlock - 1}" />

    <c:if test="${endPage > pageCount}">
        <c:set var="endPage" value="${pageCount}" />
    </c:if>

    <!-- 이전 블록 -->
    <c:if test="${startPage > pageBlock}">
        <a href="javascript:frm_sub(${startPage - pageBlock})">[이전]</a>
    </c:if>

    <!-- 페이지 번호 -->
    <c:forEach var="i" begin="${startPage}" end="${endPage}">
        <a href="javascript:frm_sub(${i})">[${i}]</a>
    </c:forEach>

    <!-- 다음 블록 -->
    <c:if test="${endPage < pageCount}">
        <a href="javascript:frm_sub(${startPage + pageBlock})">[다음]</a>
    </c:if>

</c:if>
<br>

<!-- 페이지 이동 시 검색 조건을 전달하기 위한 폼 -->
<form action="" method="post" name="i_frm">
    <input type="hidden" name="find_box" value="${find_box}">
    <input type="hidden" name="find" value="${find}">
</form>

<!-- 검색 폼 -->
<form action="${ctx}/board/list.bdo" method="post" name="find_frm" onsubmit="return check()">
    <select name="find" size="1">
        <option value="writer"  ${find == 'writer'  ? 'selected' : ''}>작성자</option>
        <option value="subject" ${find == 'subject' ? 'selected' : ''}>제목</option>
        <option value="content" ${find == 'content' ? 'selected' : ''}>내용</option>
    </select>
    <input type="text" name="find_box" value="${find_box}">
    <input type="submit" value="검색">
</form>

</div>

</body>
</html>