<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- <%@ page buffer="1kb" autoFlush="true" %> --%>

<%-- <%@ page buffer="none" autoFlush="false" %> --%>
<!-- buffer 속성이 none로 설정되면 autoFlush 속성은 false로 설정할 수 없다
이유: buffer가 없는 상태에서는 buffer에 대한 예외가 발생할 수 없기때문 -->

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
</head>
<body>
<% for(int i= 0; i< 1000; i++) { %>
1234
<%};%>
}
</body>
</html>