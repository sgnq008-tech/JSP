<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="view/color.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MY Board</title>

<!-- 공통 스타일 시트와 유효성 검사 자바스크립트 불러오기 -->
<link href="style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="script.js"></script>

</head>
<%
// 새 글인지, 아니면 기존 글의 답변 글인지를 구분하기 위한 변수 초기화
int num = 0, ref = 1, step = 0, depth = 0;
try {
    // 전달된 num 파라미터가 존재한다면 답변 글쓰기로 판단하고 관련 계층 정보를 받아옴
    if (request.getParameter("num") != null) {
       num = Integer.parseInt(request.getParameter("num"));
       ref = Integer.parseInt(request.getParameter("ref"));
       step = Integer.parseInt(request.getParameter("step"));
       depth = Integer.parseInt(request.getParameter("depth"));
    }
%>

<body bgcolor="<%=bodyback_c%>">
    <div align="center">
       <b>글쓰기</b><br>

       <!-- 입력된 데이터를 writeProc.jsp로 전송하며, 스크립트 함수로 유효성 검사 수행 -->
       <form action="writeProc.jsp" method="post" name="writeForm"
          onsubmit="return writeSave()">

          <!-- 새 글 또는 답변 글 처리를 위한 숨겨진(hidden) 계층 정보 필드 -->
          <input type="hidden" name="num" value="<%=num%>">
          <input type="hidden" name="ref" value="<%=ref%>">
          <input type="hidden" name="step" value="<%=step%>">
          <input type="hidden" name="depth" value="<%=depth%>">

          <table width="400" border="1" cellpadding="0" cellspacing="0"
             align="center" bgcolor="<%=bodyback_c%>">
             <!-- 상단 글 목록 이동 링크 -->
             <tr>
                <td align="right" colspan="2" bgcolor="<%=value_c%>"><a
                   href="list.jsp">글 목록</a></td>
             </tr>

             <!-- 작성자 이름 입력 -->
             <tr>
                <td width="70" bgcolor="<%=value_c%>">이름</td>
                <td width="330">
                <input type="text" size="12" maxlength="12" name="writer"></td>
             </tr>

             <!-- 이메일 입력 -->
             <tr>
                <td width="70" bgcolor="<%=value_c%>">이메일</td>
                <td width="330">
                <input type="text" size="40" maxlength="40" name="email">
                </td>
             </tr>

             <!-- 제목 입력 (새 글인지 답변 글인지에 따라 기본 값 다르게 설정) -->
             <tr>
                <td width="70" bgcolor="<%=value_c%>">제목</td>
                <td width="330">

                   <%if (request.getParameter("num") == null) {// 새 글일 때 %>

                <input type="text" size="50" maxlength="50" name="subject">
                   <% } else {// 답변 글일 때 (제목에 [답변글] 기본 표시) %>

                <input type="text" size="50" maxlength="50" name="subject"
                   value="[답변글]"> <%
 }
 %>
                </td>
             </tr>

             <!-- 내용 입력 -->
             <tr>
                <td width="70" bgcolor="<%=value_c%>">내용</td>
                <td width="330"><textarea rows="13" cols="50" name="content"></textarea>
                </td>
             </tr>

             <!-- 비밀번호 입력 -->
             <tr>
                <td width="70" bgcolor="<%=value_c%>">비밀번호</td>
                <td width="330">
                <input type="password" size="10"
                   maxlength="10" name="pass"></td>
             </tr>

             <!-- 하단 버튼 영역 (글쓰기 전송, 다시작성, 목록 이동) -->
             <tr>
                <td colspan="2" bgcolor="<%=value_c%>" align="center"><input
                   type="submit" value="글쓰기">

                   <input type="reset"
                   value="다시작성">
                   <input type="button" value="목록"
                   onclick="window.location='list.jsp'"></td>
             </tr>
          </table>
       </form>
       <%
       } catch (Exception e) {
           e.printStackTrace();
       }
       %>
    </div>
</body>
</html>