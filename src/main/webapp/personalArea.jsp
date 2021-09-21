<%-- 
    Document   : PersonalArea
    Created on : 05.06.2021, 11:04:42
    Author     : Ribbonse
--%>

<%@page import="app.model.UserGarage"%>
<%@page import="app.model.CollectionTemplateAuto"%>
<%@page import="app.model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Personal Area</title>
        <style>
            .page {
                width: 1350px;
                height: 800px;
                margin: 50px auto 10px auto;
                border-radius: 7px;
                border: 4px double #000;
                background-color: #ffffff;
            }
            .name {
                display: inline-block;
                border-radius: 7px;
                border: 2px solid #000;
                width: 100%;
                height: 300px;
                padding: 0px;
            }
            .myauto {
                display: inline-block;
                float: next;
                border-radius: 7px;
                border: 2px solid #000;
                width: 100%;
                height: 300px;
                overflow: auto;
            }
            .addauto {
                border-radius: 7px;
                border: 2px solid #000;
                width: 100%;
                height: 320px;
            }
            .menu {
                width: 250px;
                overflow: auto;
                margin: 10px auto 0px auto;
                border-radius: 2px;
                border: 2px solid #000;
                text-align: center;
                white-space: nowrap;
            }
            .menu a {
                display: inline-block;
                margin-right: 15px;
                padding: 5px;
                float: start;
                text-align: center;
                color: #000000;
                text-decoration: none;
            }
            .menu a:hover {
                background-color: chartreuse;
            }
            #user_data {
                font-family: serif;
                font-size: 21px;
                list-style-type: none;
            }
            #table {
                margin-left: 20px;

            }
            #tablemyauto {
                font-size: 21px;
            }
            #tablemyauto td {
                font-size: 24px;
                background-color: #ccffcc;
                text-align: center;
            }
            #tablemyauto th {
                background-color: #99ffcc;
            }
            select {
                display: block;
                font-size: 21px;
                text-align: center;
                margin-top: 20px;
                margin-bottom: 20px;
                margin-left: 60px;
            }
            input[type="submit"] {
                background-color: #99ffcc;
                margin-left: 250px;
                display: block;
                font-size: 21px;
                margin-top: 20px;
                border-radius: 7px;
            }
            input[type="submit"]:hover {
                cursor: pointer;
                border: 2px solid red;
            }
            input[type="text"] {
                font-size: 21px;
            }
            input[type="date"] {
                font-size: 21px;
                font-weight: bold;
            }
            form {
                width: 640px;
                display: inline-block;
                border-radius: 7px;
                border: 2px solid #000;
                padding: 10px;
                margin-left: 300px;
                margin-top: 10px;
                /*margin: auto 20px auto 10px;*/
            }
            label {
                font-size: 21px;
                font-weight: bold;

            }
            #formdel {
                width: auto;
                margin: 0px;
                padding: 0px;
                border: none;
            }
        </style>
    </head>
    <body>
        <%
            User user = (User) session.getAttribute("user");
            if (user == null) {
                response.sendRedirect("index.jsp");
            } else {
                CollectionTemplateAuto collectionAuto = (CollectionTemplateAuto) application.getAttribute("collectionAuto");
                UserGarage userGarage = (UserGarage) session.getAttribute("userGarage");
        %>
        <div class="page">
            <div class="menu">
                <a href="index.jsp"><b>ГЛАВНАЯ</b></a>
                <a href="RegServlet"><b>ВЫХОД</b></a>
            </div>
            <h1 style="margin-left: 20px; background-color: gainsboro; width: 70%;">Личный кабинет</h1>
            <table id="table" border="0" cellspacing="5" width="1300px">
                <tbody>
                    <tr>
                        <td width="500px">
                            <div class="name">
                                <h2 align="center" style="margin-top: 3px; background-color: #cccccc">
                                    Данные пользователя
                                </h2>
                                <ul id="user_data">
                                    <li>Фамилия:&nbsp;&nbsp;&nbsp;<%= user.getSurname()%></li>
                                    <li>Имя:&nbsp;&nbsp;&nbsp;<%= user.getName()%></li>
                                    <li>Телефон:&nbsp;&nbsp;&nbsp;<%= user.getTelephone()%></li>
                                    <li>Email:&nbsp;&nbsp;&nbsp;<%= user.getEmail()%></li>
                                </ul>
                            </div>
                        </td>
                        <td width="600px">
                            <div class="myauto">
                                <h2 align="center" style="margin-top: 3px; background-color: #cccccc">
                                    Мои автомобили
                                </h2>
                                <table id="tablemyauto" align="center" border="0" 
                                       width="450" cellspacing="3" cellpadding="7">
                                    <thead>
                                        <tr>
                                            <th>№</th>
                                            <th>Марка</th>
                                            <th>Модель</th>
                                            <th>Гос.номер</th>
                                            <th>VIN номер</th>
                                            <th>Del</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            if (userGarage != null) {
                                                int n = userGarage.amountUserAuto();
                                                for (int i = 0; i < n; i++) {
                                        %>
                                        <tr>
                                            <td><%=i + 1%></td>
                                            <td><%=userGarage.getAuto(i).getBrand()%></td>
                                            <td><%=userGarage.getAuto(i).getModel()%></td>
                                            <td><%=userGarage.getAuto(i).getPlateNumber()%></td>
                                            <td><%=userGarage.getAuto(i).getVin()%></td>
                                            <td>
                                                <form id="formdel" action="AddAutoServlet" method="POST">
                                                    <button type="submit" form="formdel" name="delAuto" value="<%=userGarage.getAuto(i).getUserAutoId()%>">Del</button>
                                                </form>
                                            </td>
                                        </tr>
                                        <%
                                            }
                                        } else {
                                        %>
                                        <tr>
                                            <td colspan="6">У вас пока нет зарегистрированных авто</td>
                                        </tr>
                                        <%
                                            }
                                        %>
                                    </tbody>
                                </table>

                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <div class="addauto">
                                <h2 align="center">Добавление автомобилей</h2>
                                <hr>
                                <%
                                    if (collectionAuto != null) {
                                        collectionAuto.sortAuto();
                                %>
                                <form action="AddAutoServlet" method="post">
                                    <label>VIN:&nbsp;<input style="text-transform:uppercase;" type="text" size="20" name="vin" placeholder="WVWZZZ7PZCG021128" required value="" /></label>&nbsp;&nbsp;
                                    <label>Гос.номер:&nbsp;<input type="text" name="plateNumber" placeholder="о001оо98" required value="" /></label><br>
                                    <select name="addContragentAuto">
                                        <option value="0" selected="selected">
                                            Выберете ваш автомобиль
                                        </option>
                                        <%
                                            int k = collectionAuto.amountTemplateAuto();
                                            for (int i = 0; i < k; i++) {
                                        %>
                                        <option value="<%=collectionAuto.getAuto(i).getId()%>">
                                            <%=collectionAuto.getAuto(i).toString()%>
                                        </option>
                                        <%
                                            }
                                        %>
                                    </select>
                                    <input type="submit" name="addAuto" value="Добавить" >
                                </form>
                                <%
                                    String msg = (String) request.getAttribute("msg");
                                    if (msg == null) {
                                %>
                                <!--Надпись показываемая по умолчанию при входе в личный кабинет-->
                                <h2 align="center" style="background-color: chartreuse">
                                    Заполните все поля формы и нажмите кнопку добавить.
                                </h2>
                                <%
                                } else {%>
                                <h2 align="center" style="background-color: #ffff99"><%=msg%></h2> 
                                <%
                                    }
                                %>
                                <%
                                } else {
                                %>
                                <h2 align="center" style="background-color: lightcoral">
                                    Добавление автомобиля временно не доступно
                                </h2>
                                <%
                                    }
                                %>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <%
            }
        %>
    </body>
</html>
