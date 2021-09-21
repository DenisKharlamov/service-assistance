<%-- 
    Document   : index
    Created on : 06.05.2021, 16:18:43
    Author     : Ribbonse
--%>

<%@page import="app.service.DBMaster"%>
<%@page import="app.model.CollectionTemplateAuto"%>
<%@page import="app.model.Role"%>
<%@page import="app.model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ServiceAssistance</title>
        <link rel="stylesheet" href="styles.css">
    </head>
    <body>
        <!--<div class="top">-->
        <div class="scrollmenu">
            <%
                User user = (User) session.getAttribute("user");
                if (user == null) {
            %>
            <a href="login.jsp"><b>Вход / Регистрация</b></a>
            <%
            } else {
                if (user.getRole().equals(Role.USER)) {
            %>
            <a href="RegServlet"><b>Выход</b></a>
            <a href="personalArea.jsp"><b>Личный кабинет</b></a>
            <a href="#"><b><%=user.getName() + " " + user.getSurname()%></b></a>
                    <%
                    } else if (user.getRole().equals(Role.SUPERUSER)) {
                    %>
            <a href="RegServlet"><b>Выход</b></a>
            <a href="#"><b>Мастер: <%=user.getName() + " " + user.getSurname()%></b></a>
                    <%
                            }
                        }
                    %>
        </div>
        <!--<h1 id="logo">&COPY;Автосервис</h1>-->
        <!--            <p id="reclama">
                        Мы является профессиональным автотехцентром и предоставляем максимально широкий спектр услуг
                        по обслуживанию, диагностике и слесарному ремонту автомобилей зарубежного и отечественного
                        производства. Ремонт автомобилей осуществляется как по предварительной записи, так и по факту
                        освободившихся автомехаников и подъёмников - в порядке “живой очереди”.</p>-->
        <!--            <div class="time-table">
                        <table border="0">
                            <tbody>
                                <tr>
                                    <td colspan="2"><h2 align="center">Время работы автосервиса</h2></td>
                                </tr>
                                <tr>
                                    <td>Понедельник</td>
                                    <td align="right">9:00 &bullet; 20:00</td>
                                </tr>
                                <tr>
                                    <td>Вторник</td>
                                    <td align="right">9:00 &bullet; 20:00</td>
                                </tr>
                                <tr>
                                    <td>Среда</td>
                                    <td align="right">9:00 &bullet; 20:00</td>
                                </tr>
                                <tr>
                                    <td>Четверг</td>
                                    <td align="right">9:00 &bullet; 20:00</td>
                                </tr>
                                <tr>
                                    <td>Пятница</td>
                                    <td align="right">9:00 &bullet; 20:00</td>
                                </tr>
                                <tr>
                                    <td>Суббота</td>
                                    <td align="right">10:00 &bullet; 18:00</td>
                                </tr>
                                <tr>
                                    <td>Воскресенье</td>
                                    <td align="right">10:00 &bullet; 18:00</td>
                                </tr>
                                <tr>
                                    <td colspan="2" ><p align="center">Автомойка: Пн-Пт 9:00-21:00<br>Сб-Вс 10:00-20:00.</p></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>-->
        <!--</div>-->
        <div class="border">
            <p id="rec" align="center">Онлайн запись на ТО</p>
        </div>
        <div class="main">
            <%
                if (user == null) {%>
            <%@include file="WEB-INF/jspf/default_board.jspf" %>
            <%
            } else {
                 if (user.getRole().equals(Role.SUPERUSER)) {%>
            <%@include file="WEB-INF/jspf/staff_board.jspf" %>
            <%
            } else if (user.getRole().equals(Role.USER)) {%>
            <%@include file="WEB-INF/jspf/contragent_board.jspf" %>
            <%
                    }
                }
            %>
        </div>
        <%
            String errorMsg = (String) request.getAttribute("errorMsg");
            String msg = (String) request.getAttribute("msg");
            if (msg != null) {
        %>
        <p style="font-size:18pt; color: black; text-align:center; margin-top: 7px;
           margin-bottom: 0px; background-color: #ffff99">
            <%=msg%>
        </p>
        <%
            }
            if (errorMsg != null) {
        %>
        <p style="font-size:18pt; color: black; text-align:center; margin-top: 7px;
           margin-bottom: 0px; background-color: #ff6666">
            <%=errorMsg%>
        </p>
        <%
            }
        %>
        <p style="font-size:18pt; color: black; text-align:center; margin-top: 0px">
            &copy;Service-Assistance
        </p>
    </body>
</html>
