package app.servlet;

import app.model.Order;
import app.model.User;
import app.model.UserGarage;
import app.service.DBMaster;
import app.service.EmailService;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 * Класс для аботы с заказ-нарядом (заполнение, сохранение)
 */
@WebServlet(name = "OrderServlet", urlPatterns = {"/OrderServlet"})
public class OrderServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(OrderServlet.class);
    
    @EJB
    DBMaster dbMaster;
    
    @EJB
    EmailService emailService;
    
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
        // производим все необходимые проверки
        HttpSession session = request.getSession();
        if (session == null) {
            response.sendRedirect("index.jsp");
        } else {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                response.sendRedirect("index.jsp");
            } else {
                Order order = (Order) session.getAttribute("newOrder");
                if (order == null) {
                    response.sendRedirect("index.jsp");
                } else {
                    UserGarage userGarage = (UserGarage) session.getAttribute("userGarage");
                    if (userGarage == null) {
                        response.sendRedirect("index.jsp");
                    } else {
                        String ageString = request.getParameter("ageAuto");
                        String mileageString = request.getParameter("mileageAuto");
                        String userAutoIdString = request.getParameter("auto");

                        // строка содержит сообщения об ошибках
                        String errorMsg = "";
                        
                        if (ageString.equalsIgnoreCase("zero")) {
                            errorMsg += "Введите срок эксплуатации автомобиля.<br>";
                        }
                        if (mileageString.equalsIgnoreCase("zero")) {
                            errorMsg += "Введите пробег.<br>";
                        }
                        if (userAutoIdString.equalsIgnoreCase("zero")) {
                            errorMsg += "Не выбран автомобиль.<br>";
                        }
                        if (!errorMsg.isEmpty()) {
                            request.setAttribute("errorMsg", errorMsg);
                            request.getRequestDispatcher("index.jsp").forward(request, response);
                        } else {
                            int age = 0;
                            try {
                                age = Integer.parseInt(ageString);
                            } catch (NumberFormatException ex) {
                                logger.info("Auto age ---> NumberFormatException");
                                errorMsg += "Что-то пошло не так, попробуйте ещё раз.<br>";
                            }
                            int mileage = 0;
                            try {
                                mileage = Integer.parseInt(mileageString);
                            } catch (NumberFormatException ex) {
                                logger.info("Auto mileage ---> NumberFormatException");
                                errorMsg += "Что-то пошло не так, попробуйте ещё раз.<br>";
                            }
                            int userAutoId = 0;
                            try {
                                userAutoId = Integer.parseInt(userAutoIdString);
                            } catch (NumberFormatException ex) {
                                logger.info("AutoId ---> NumberFormatException");
                                errorMsg += "Что-то пошло не так, попробуйте ещё раз.<br>";
                            }
                            if (!errorMsg.isEmpty()) {
                                request.setAttribute("errorMsg", errorMsg);
                                request.getRequestDispatcher("index.jsp").forward(request, response);
                            } else {
                                // после прохождения всех проверок
                                // добавляем в запись полученные данные возраста, пробега и автомобиля
                                order.setAge(age);
                                order.setMileage(mileage);
                                order.setUserAuto(userGarage.getUserAutoForId(userAutoId));
                                // пробуем создать запись на ТО (клиенту доступна только
                                // стандартная запись на 3 часа)
                                if (dbMaster.createEvent(order.getMechanic().getId(), order.getYearMonthDayHour()[0],
                                        order.getYearMonthDayHour()[1], order.getYearMonthDayHour()[2],
                                        order.getYearMonthDayHour()[3], 3)) {
                                    // если запись создалась, создаем заказ-наряд
                                    dbMaster.orderCreator(order);
                                    // присваиваем ему номер
                                    order.createOrderNumber();
                                    // добавляем заказ-наряд в базу данных
                                    dbMaster.saveOrder(order);
                                    // сериализуем заказ-наряд в файл
                                    dbMaster.saveOrderToFile(order);
                                    // формируем pdf-документ заказ-наряда и отправляем клиенту на почту
                                    emailService.sendEmailWithAttachment(order);
                                    // обновляем события
                                    request.getServletContext().setAttribute("event", dbMaster.getEvent());
                                    // возвращаемся на страницу записи
                                    request.setAttribute("msg", "Запись успешно создана.<br>"
                                            + "Предварительный заказ-наряд выслан вам на email "
                                            + "указанный при регистрации.");
                                    request.getRequestDispatcher("index.jsp").forward(request, response);
                                } else {
                                    // обновляем события
                                    request.getServletContext().setAttribute("event", dbMaster.getEvent());
                                    // переходим обратно на страницу записи
                                    request.setAttribute("errorMsg", "Ошибка создания записи, текущее время уже занято");
                                    request.getRequestDispatcher("index.jsp").forward(request, response);
                                }
                            }
                        }
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
