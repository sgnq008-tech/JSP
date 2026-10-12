<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

<body bgcolor="${bodyback_c}">

<div a align="center">
<b>글 상세보기</b><br>

<form action="">
<table width="500" border="1" cellpadding="0" cellspacing="0" 
bgcolor="${bodyback_c}" align="center">

<tr height="30">
    <td align="center" width="125" bgcolor="${value_c}">글번호</td>
    <td align="center" width="125" >${article.Num}</td>
    <td align="center" width="125" bgcolor="${value_}">조회수</td>
    <td align="center" width="125" >${article.readcount}></td>
</tr>

<tr height="30">
    <td align="center" width="125" bgcolor="${value_c}">작성자</td>
    <td align="center" width="125" >${article.Num}</td>
    <td align="center" width="125" bgcolor="${value_c}">작성일</td>
    <td align="center" width="125" >${article.getRegdate}</td>
</tr>

<tr height="30">
    <td align="center" width="125" bgcolor="${value_c}">글제목</td>
    <td align="center" width="375" colspan="3">${article.subject}</td>
</tr>

<tr height="30">
    <td align="center" width="125" bgcolor="${value_c}">글내용</td>
    <td align="center" width="375" colspan="3">
    <pre>${article.content}</pre></td>
</tr>

<tr height="30">
    <td colspan="4" bgcolor="${value_c}" align="right">
    
    <input type="button" value="글수정" 
    onclick="document.location.href='/board/updateForm.bdo?num=${article.Num}&pageNum=${pageNum}'">
    &nbsp;&nbsp;&nbsp;&nbsp;
    
    <input type="button" value="글삭제" 
    onclick="document.location.href='/board/deleteForm.bdo?num=${article.Num}&pageNum=${pageNum}'">
    &nbsp;&nbsp;&nbsp;&nbsp;
    
    <input type="button" value="답글쓰기" 
    onclick="document.location.href='/board/writeForm.bdo?num=${article.num}&ref=${article.ref}&step=${article.step}&depth=${article.depth}'">
    &nbsp;&nbsp;&nbsp;&nbsp;
    
    <input type="button" value="글목록" 
    onclick="document.location.href='/board/list.bdo?pageNum=${pageNum}'">
    </td>
</tr>

</table>

</form>

</div>
</body>
</html>