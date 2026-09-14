<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
</head>
<body>
<div align="center">
             <h1>로그인</h1>
             <form action="Login" method="post">
             <fieldset>
             <legend>로그인</legend>
             <ul>
                    <li>
                           <label>아이디</label>
                           <input type = "text" name="userid">
                    </li>
                     <li>
                           <label>비밀번호</label>
                           <input type = "password" name="passwd">                    
                    </li>
                    <li>
                           <input type ="submit" value ="로그인">
                    </li>                                
             </ul>             
             </fieldset>
             </form>
</div>
</body>
</html>