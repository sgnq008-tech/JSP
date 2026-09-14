<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Vector, com.jdbc.TempMemberVO" %>

<jsp:useBean id="dao" class="com.jdbc.TempMemberDAO" scope="page"/>

<%
    // DAO에서 회원 목록 조회
    Vector<TempMemberVO> vlist = dao.getMemberList();
    int counter = (vlist != null) ? vlist.size() : 0;
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>DBCP를 이용한 데이터베이스</title>
<link href="style.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#ffffcc">
<h2>DBCP를 사용한 데이터베이스</h2>
<h3>회원정보</h3>

<table bordercolor="#0000ff" border="1">
    <!-- 헤더 행 (총 9칸) -->
    <tr>
        <td><strong>ID</strong></td>
        <td><strong>PASSWD</strong></td>
        <td><strong>NAME</strong></td>
        <td><strong>MEM_NUM1</strong></td>
        <td><strong>MEM_NUM2</strong></td>
        <td><strong>E_MAIL</strong></td>
        <td><strong>PHONE</strong></td>
        <td><strong>ZIPCODE/ADDRESS</strong></td>
        <td><strong>JOB</strong></td>
    </tr>

<%
    if (vlist != null && !vlist.isEmpty()) {
        for (int i = 0; i < vlist.size(); i++) {
            TempMemberVO vo = vlist.elementAt(i);
%>
    <!-- 데이터 행 (헤더와 동일하게 9칸) -->
    <tr>
        <td><%= vo.getId() %></td>
        <td><%= vo.getPasswd() %></td>
        <td><%= vo.getName() %></td>
        <td><%= vo.getMem_num1() %></td>
        <td><%= vo.getMem_num2() %></td>
        <td><%= vo.getE_mail() %></td>
        <td><%= vo.getPhone() %></td>
        <!-- 우편번호와 주소를 한 칸에 묶어서 출력 -->
        <td><%= (vo.getZipcode() != null ? vo.getZipcode() : "") + " " + (vo.getAddress() != null ? vo.getAddress() : "") %></td>
        <td><%= vo.getJob() %></td>
    </tr>
<%
        } // end for
    } else {
%>
    <tr>
        <td colspan="9" align="center">등록된 회원 정보가 없습니다.</td>
    </tr>
<%
    }
%>
</table>
<br>
total records : <%= counter %>

</body>
</html>