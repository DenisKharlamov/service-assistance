package app.servlet;

import app.model.Role;
import app.model.TimeMashine;
import app.model.User;
import app.service.DBMaster;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 * Класс позволяет клиенту выбрать дату на доске записи
 */
@WebServlet(name = "ControlTime", urlPatterns = {"/ControlTime"})
public class ControlTime extends HttpServlet {

    @EJB
    private DBMaster dbMaster;

    private static final Logger logger = Logger.getLogger(ControlTime.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Method GET not supported yet...");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("utf-8");
        HttpSession session = request.getSession();
        if (session == null) {
            response.sendRedirect("index.jsp");
        } else {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                response.sendRedirect("index.jsp");
            } else {
                TimeMashine time = (TimeMashine) session.getAttribute("time");
                // переместиться на день назад
                if (request.getParameter("previosDay") != null) {
                    time.previosDay();
                }
                // вернуться на сегодняшний день
                if (request.getParameter("today") != null) {
                    time.today();
                }
                // переместится на день вперёд
                if (request.getParameter("nextDay") != null) {
                    time.nextDay();
                }
                // перемещение по календарю
                if (request.getParameter("refresh") != null) {
                    String date = request.getParameter("date");
                    if (date.isEmpty()) {
                    } else {
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate toSet = LocalDate.from(format.parse(date));
                        time.setTime(toSet);
                    }
                }
                // для персонала обновить заказы согласно выбранного дня
                if (user.getRole().equals(Role.SUPERUSER)) {
                    session.setAttribute("orders", dbMaster.getOrdersToDate(time.getTime()));
                }
                response.sendRedirect("index.jsp");
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}