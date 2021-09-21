<%@page import="app.model.UserGarage"%>
<%@page import="app.model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Select Car</title>
        <style>
            .event {
                margin: 70px auto 40px auto;
                width: 650px;
                height: 300px;
                border-radius: 10px;
                border: 2px solid #000;
            }
            select {
                font-family: serif;
                font-size: 19px;
                border: 2px solid #000;
                border-radius: 5px;
                margin-left: 75px;
                margin-top: 30px;
                text-align-last: center;
            }
            select:hover {
                border: 2px solid #3366ff;
            }
            .button {
                font-family: serif;
                font-size: 21px;
                border: 2px solid #000;
                border-radius: 5px;
                width: 100px;
                height: 40px;
            }
            .button:hover {
                background-color: chartreuse;
                font-weight: bold;
                cursor: pointer;
            }
            p {
                display: inline-block;
                margin-top: 40px;
            }
            .p1 {
                position: relative;
                bottom: 96px;
                left: 190px;

            }
            .p2 {
                margin-left: 350px;
            }
            h2 {
                background-color: #ccffff;
            }
        </style>
    </head>
    <body>
        <%
            User user = (User) session.getAttribute("user");
            if (user == null) {
                response.sendRedirect("index.jsp");
            } else {
                UserGarage userGarage = (UserGarage) session.getAttribute("userGarage");
                if (userGarage == null) {
                    request.setAttribute("msg", "Для выполнения записи нужно добавить ваш автомобиль в личном кабинете.");
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                } else {
        %>
        <div class="event">
            <form action="OrderServlet" method="POST">
                <h2 align="center">Выберите параметры для записи на ТО</h2>
                <select class="age" name="ageAuto">
                    <option value="zero" selected="selected">
                        Срок эксплуатации(годы)
                    </option>
                    <%
                        for (int i = 1; i < 11; i++) {
                    %>
                    <option value="<%=i%>">
                        <%=i%>
                    </option>
                    <%
                        }
                    %>
                </select>
                <select class="mileage" name="mileageAuto">
                    <option value="zero" selected="selected">
                        Пробег(тыс.км)
                    </option>
                    <%
                        for (int k = 0; k <= 150; k += 15) {
                    %>
                    <option value="<%=k%>">
                        <%=k%>
                    </option>
                    <%
                        }
                    %>
                </select>
                <select class="auto" name="auto">
                    <option value="zero" selected="selected">
                        Ваш автомобиль
                    </option>
                    <%
                        int n = userGarage.amountUserAuto();
                        for (int i = 0; i < n; i++) {
                    %>
                    <option value="<%=userGarage.getAuto(i).getUserAutoId()%>" >
                        <%=userGarage.getAuto(i).getBrand()
                                + " " + userGarage.getAuto(i).getModel()
                                + " " + userGarage.getAuto(i).getPlateNumber()
                                + " " + userGarage.getAuto(i).getVin()%>
                    </option>
                    <%
                        }
                    %>
                </select>
                <p class="p2"><input class="button" type="submit" name="addOrder" value="Запись"></p>
            </form>
            <p class="p1"><a style="text-decoration: none" href="index.jsp"><button class="button">Назад</button></a></p>
        </div>
        <%
                }
            }
        %>
    </body>
</html>
