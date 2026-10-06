<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %> 
 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<!-- 데이터소스 설정 -->
<sql:setDataSource dataSource="jdbc/myOracle" var="ds" scope="application"/>

<!-- SQL 쿼리 실행 (끝에 세미콜론 ; 제거 완료) -->
<sql:query var="rs" dataSource="${ds}">
select * from tempmember
</sql:query>

<table border="1">
    <!-- 필드명 출력 -->
    <tr>
       <c:forEach var="columnName" items="${rs.columnNames}">
          <th>
              <c:out value="${columnName}"/>
          </th>
       </c:forEach>
    </tr>
       
    <!-- 레코드 수만큼 반복 수행 -->
    <c:forEach var="row" items="${rs.rowsByIndex}">
        <tr>
        <!-- 레코드의 필드 수 만큼 반복 수행 -->
        <c:forEach var="column" items="${row}" varStatus="i">
         <td>
              <!-- 해당 필드값이 null이 아닌 경우 -->
              <c:if test="${column ne null}">
                  <c:out value="${column}"></c:out>
              </c:if>
              
              <!-- 해당 필드값이 null인 경우 -->
              <c:if test="${column eq null}">
                  &nbsp;
              </c:if>
         </td>
        </c:forEach>
        </tr>
    </c:forEach>
</table>

</body>
</html>