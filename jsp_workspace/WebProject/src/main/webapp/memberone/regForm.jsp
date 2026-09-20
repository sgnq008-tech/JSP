<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<!-- 공통 스타일 시트와 유효성 검사 자바스크립트 연결 -->
<link href = "style.css" rel = "stylesheet" type= "text/css">
<script type="text/javascript" src="script.js"></script>
</head>
<body>

</body>
<!-- 작성된 회원가입 정보를 regProc.jsp로 전달하는 폼 -->
<form action="regProc.jsp" name="regForm" method="post">
<table border="1">
    <!-- 상단 타이틀 영역 -->
    <tr>
       <td colspan="2" align="center"> 회원 가입 정보 입력</td>
    </tr>

    <!-- 아이디 입력 및 중복확인 버튼 -->
    <tr>
       <td align="right" >아이디 :</td>
       <td>
          <input type = "text" name = "id">&nbsp;
          <input type = "button" value = "중복확인"
          onclick="idCheck(this.form.id.value)">
       </td>
    </tr>

    <!-- 비밀번호 입력 -->
    <tr>
       <td align="right"> 비밀번호 :</td>
       <td>
          <input type = "password" name = "pass">
       </td>
    </tr>

    <!-- 비밀번호 확인 입력 -->
    <tr>
       <td align="right"> 비밀번호 확인:</td>
       <td>
          <input type = "password" name = "repass">
       </td>
    </tr>

    <!-- 이름 입력 -->
    <tr>
       <td align = "right"> 이름 : </td>
       <td>
          <input type="text" name="name">
       </td>
    </tr>

    <!-- 전화번호 입력 (셀렉트 박스 + 텍스트 박스 조합) -->
    <tr>
          <td align = "right"> 전화번호 : </td>
          <td>
             <select name= "phone1">
                <option value = "02">02 </option>
                <option value = "031">031 </option>
                <option value = "010">010 </option>
             </select>
             <input type="text" name="phone2" size= "5"> -
             <input type="text" name="phone3" size= "5">
          </td>
    </tr>

    <!-- 이메일 입력 -->
    <tr>
       <td align = "right"> 이메일 : </td>
       <td>
          <input type="text" name="email">
       </td>
    </tr>

    <!-- 우편번호 입력 및 찾기 버튼 -->
    <tr>
       <td align="right" >우편번호 :</td>
       <td>
          <input type = "text" name = "zipcode">&nbsp;
          <input type = "button" value = "찾기" onclick="zipChect()">
       </td>
    </tr>

    <!-- 기본 주소 입력 -->
    <tr>
       <td align = "right"> 주소 : </td>
       <td>
          <input type="text" name="address1" size="50">
       </td>
    </tr>

    <!-- 상세 주소 입력 -->
    <tr>
       <td align = "right"> 상세주소 : </td>
       <td>
          <input type="text" name="address2" size="30">
       </td>
    </tr>

    <!-- 하단 버튼 영역 (회원가입 유효성 검사 버튼, 다시입력 버튼) -->
    <tr>
       <td colspan= "2" align="center">
          <input type="button" value="회원가입" onclick="inputCheck()" >&nbsp;&nbsp;
          <input type= "reset" value = "다시입력">
       </td>
    </tr>

</table>
</form>
</html>