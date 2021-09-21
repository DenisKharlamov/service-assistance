package app.servlet;

import app.model.Order;
import app.model.Staff;
import app.model.TimeMashine;
import app.model.User;
import app.model.UserGarage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Класс создаёт объект класса Order, заполняет в нём параметры относящиеся к
 * дате записи и добавляет его в сессию пользователя
 */
@WebServlet(name = "EventServlet", urlPatterns = {"/EventServlet"})
public class EventServlet extends HttpServlet {

    // набор шаблонов даты для последующего преобразования
    DateTimeFormatter today = DateTimeFormatter.ofPattern("dd MMMM yyyy EEEE");
    DateTimeFormatter year = DateTimeFormatter.ofPattern("yyyy");
    DateTimeFormatter day = DateTimeFormatter.ofPattern("dd");

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
                UserGarage garage = (UserGarage) session.getAttribute("userGarage");
                if (garage == null) {
                    request.setAttribute("errorMsg", "Дла записи требуется регистрация автомобиля в личном кабинете.");
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                } else {

                    Staff staff = (Staff) request.getServletContext().getAttribute("staff");

                    // выбранное пользователем время
                    TimeMashine time = (TimeMashine) session.getAttribute("time");
                    // Проверить запись в прошлое
                    if (time.getTime().isBefore(LocalDate.now())) {
                        request.setAttribute("errorMsg", "Запись в прошедшее время невозможна.");
                        request.getRequestDispatcher("index.jsp").forward(request, response);
                    } else {
                        // получаем параметры запроса: id механика и время записи
                        Map<String, String[]> requestParameters = request.getParameterMap();

                        Optional<Map.Entry<String, String[]>> button
                                = requestParameters.entrySet().stream().findFirst();

                        int mechanicId = Integer.parseInt(button.get().getKey());
                        String clockEvent = button.get().getValue()[0];

                        // создаем новый заказ
                        Order order = new Order(time.getTime(), clockEvent, user,
                                staff.getMechanicForId(mechanicId));

                        session.setAttribute("newOrder", order);
                        // переходим на страницу выбора автомобиля
                        response.sendRedirect("selectCarForEvent.jsp");
                    }
                }
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}