<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.memberone.*"%>

<!-- studentDAO 객체를 생성합니다 -->
<jsp:useBean id="dao" class="com.memberone.studentDAO"/>

<%
    // 세션에 저장된 로그인 아이디를 가져온 후, DAO를 통해 해당 회원의 정보를 studentVO 객체(vo)로 불러옵니다.
    String loginID = (String)session.getAttribute("loginID");
    studentVO vo = dao.getMember(loginID);
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원정보 수정</title>
<link href="style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="script.js"></script>
</head>

<body>

<!-- 수정된 회원 정보를 modifyProc.jsp로 전송하는 폼 -->
<form action="modifyProc.jsp" name="regForm" method="post">

<table border="1">

    <!-- 타이틀 영역 -->
    <tr>
       <td colspan="2" align="center">회원 수정 정보 입력</td>
    </tr>

    <!-- 아이디 출력 (수정 불가) -->
    <tr>
       <td align="right">아이디 :</td>
       <td>
          <%=vo.getId()%>
       </td>
    </tr>

    <!-- 비밀번호 입력 -->
    <tr>
       <td align="right">비밀번호 :</td>
       <td>
          <input type="password" name="pass"
             value="<%=vo.getPass()%>">
       </td>
    </tr>

    <!-- 비밀번호 확인 입력 -->
    <tr>
       <td align="right">비밀번호 확인 :</td>
       <td>
          <input type="password" name="repass"
             value="<%=vo.getPass()%>">
       </td>
    </tr>

    <!-- 이름 출력 (수정 불가) -->
    <tr>
       <td align="right">이름 :</td>
       <td>
          <%=vo.getName()%>
       </td>
    </tr>

    <!-- 전화번호 입력 (3분할) -->
    <tr>
       <td align="right">전화번호 :</td>
       <td>
          <input type="text" name="phone1" size="5"
             value="<%=vo.getPhone1()%>"> -
          <input type="text" name="phone2" size="5"
             value="<%=vo.getPhone2()%>"> -
          <input type="text" name="phone3" size="5"
             value="<%=vo.getPhone3()%>">
       </td>
    </tr>

    <!-- 이메일 입력 -->
    <tr>
       <td align="right">이메일 :</td>
       <td>
          <input type="text" name="email"
             value="<%=vo.getEmail()%>">
       </td>
    </tr>

    <!-- 우편번호 입력 및 주소 찾기 버튼 -->
    <tr>
       <td align="right">우편번호 :</td>
       <td>
          <input type="text" name="zipcode"
             value="<%=vo.getZipcode()%>">
          <input type="button" value="찾기" onclick="zipChect()">
       </td>
    </tr>

    <!-- 기본 주소 입력 -->
    <tr>
       <td align="right">주소 :</td>
       <td>
          <input type="text" name="address1" size="50"
             value="<%=vo.getAddress1()%>">
       </td>
    </tr>

    <!-- 상세 주소 입력 -->
    <tr>
       <td align="right">상세주소 :</td>
       <td>
          <input type="text" name="address2" size="30"
             value="<%=vo.getAddress2()%>">
       </td>
    </tr>

    <!-- 하단 버튼 영역 (정보수정 유효성 검사 및 취소 후 로그인 페이지 이동) -->
    <tr>
       <td colspan="2" align="center">
          <input type="button" value="정보수정"
             onclick="updateCheck()">&nbsp;&nbsp;

          <input type="button" value="취소"
             onclick="javascript:window.location='login.jsp'">
       </td>
    </tr>

</table>

</form>

</body>
</html>