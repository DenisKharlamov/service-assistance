<%-- 
    Document   : register
    Created on : 
    Author     : Ribbonse
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registration</title>
        <link rel="stylesheet" href="styles_reg.css">
    </head>
    <body>
        <div class="login">
            <form action="RegServlet" method="post">
                <fieldset>
                    <legend>Регистрация</legend>
                    <p>
                        <label for="name">Введите ваше имя:</label>
                        <input type="text" name="name" placeholder="Иван" 
                               required value=""><br><br>
                        <label for="surname">Введите вашу фамилию:</label>
                        <input type="text" name="surname" placeholder="Иванов"
                               required value=""><br><br>
                        <label for="telephone">Введите ваш номер телефона:</label>
                        <input type="text" name="telephone" placeholder="+7(123)456-78-90"
                               required value=""><br><br>
                        <label for="email">Введите ваш Email:</label>
                        <input type="email" name="email" required value=""><br><br>
                        <label for="pswd">Введите пароль:</label>
                        <input type="password" name="pswd" required value="">
                    </p><br>
                    <input type="submit" name="register" value="Регистрация"><hr>
                    <p align="center">После регистрации аккаунта Вам будет доступна запись на ТО.</p>
                    <a href="login.jsp">Вернуться на страницу входа</a>
                    <a href="index.jsp">Вернуться на главную</a>
                </fieldset>
            </form>
        </div>
        <%
            String message = (String)request.getAttribute("msg");
            if (message != null) {%>
        <h3 align="center" style="background-color: whitesmoke;
            border: 2px solid red; border-radius: 10px;"><%=message%></h3>
        <%}%>
    </body>
</html>
