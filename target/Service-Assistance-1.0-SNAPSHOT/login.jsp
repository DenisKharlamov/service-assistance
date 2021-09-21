<%-- 
    Document   : login
    Created on : 10.05.2021, 18:29:38
    Author     : Ribbonse
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>LogIn</title>
        <link rel="stylesheet" href="styles_log.css">
    </head>
    <body>
        <div class="login">
            <form action="RegServlet" method="post">
                <fieldset>
                    <legend>Вход в аккаунт</legend>
                    <p><label for="login">Введите Email:</label>
                        <input type="text" name="login" required value=""><br><br>
                        <label for="pswd">Введите пароль:</label>
                        <input type="password" name="pswd" required value=""></p><br>
                    <input type="submit" name="login" value="Вход"><hr>
                    <p align="center">Если у вас нет аккаунта, зарегистрируйтесь.</p>
                    <a href="register.jsp">Регистрация</a>
                    <a href="index.jsp">Вернуться на главную</a>
                </fieldset>
            </form>
        </div>
    </body>
</html>
