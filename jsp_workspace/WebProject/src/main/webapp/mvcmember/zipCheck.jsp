<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>우편번호 검색</title>
<link href="style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="script.js"></script>
</head>
<body bgcolor="#ffffcc">
<div align="center">
<b>우편번호 찾기</b>

<form action="member.mdo?cmd=zipCheck" method="post" name="zipForm">
    <table>
        <tr>
            <td>동 이름 입력:
                <input type="text" name="dong">
                <input type="button" value="검색" onclick="dongCheck()">
            </td>
        </tr>
    </table>
    <input type="hidden" name="check" value="n">
</form>

<table border="1">
    <c:if test="${param.check eq 'n'}">

        <%-- 검색 결과 없음 --%>
        <c:if test="${empty zipcodeList}">
            <tr>
                <td align="center"><br>검색된 결과가 없습니다.</td>
            </tr>
        </c:if>

        <%-- 검색 결과 있음 --%>
        <c:if test="${not empty zipcodeList}">
            <tr>
                <td align="center"><br>
                    ※ 검색 후, 아래 우편번호를 클릭하면 자동으로 주소가 입력됩니다.
                </td>
            </tr>
            <c:forEach var="vo" items="${zipcodeList}">
                <tr>
                    <td>
                        <a href="javascript:sendAddress('${vo.zipcode}','${vo.sido}','${vo.gugun}','${vo.dong}','${vo.ri}','${vo.bunji}')">
                            ${vo.zipcode}&nbsp;${vo.sido}&nbsp;${vo.gugun}&nbsp;${vo.dong}&nbsp;${vo.ri}&nbsp;${vo.bunji}
                        </a><br>
                    </td>
                </tr>
            </c:forEach>
        </c:if>

    </c:if>
    <tr>
        <td align="center">
            <a href="javascript:window.close()">닫기</a>
        </td>
    </tr>
</table>
</div>
</body>
</html>