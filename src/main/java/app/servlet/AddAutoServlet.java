package app.servlet;

import app.model.User;
import app.service.DBMaster;
import app.util.Validator;
import java.io.IOException;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 * Сервлет обрабатывает форму добавления автомобиля клиента
 *
 */
@WebServlet(name = "AddAutoServlet", urlPatterns = {"/AddAutoServlet"})
public class AddAutoServlet extends HttpServlet {

    @EJB
    DBMaster dbMaster;

    @Inject
    Validator validator;

    private static final Logger logger = Logger.getLogger(AddAutoServlet.class);

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
                // добавление автомобиля
                if (request.getParameter("addAuto") != null) {
                    // получаем параметры авто клиента и проверяем 
                    // корректность данных
                    String vin = request.getParameter("vin").trim();
                    String plateNumber = request.getParameter("plateNumber").trim();
                    String autoIdS = request.getParameter("addContragentAuto").trim();
                    int userId = user.getId();
                    if (vin.isEmpty() || plateNumber.isEmpty() || autoIdS.isEmpty()) {
                        request.setAttribute("msg", "Для успешной регистрации необходимо"
                                + "заполнить все поля данной формы.");
                        request.getRequestDispatcher("personalArea.jsp").forward(request, response);
                    } else {
                        String msg = "";
                        // проверка vin номера на корректность написания
                        if (!(vin.length() == 17)) {
                            msg += "Неккоректный ввод VIN номера.<br>";
                        }
                        // проверяем введеный гос номер на соответствие шаблону
                        if (!validator.isValidPlateNumber(plateNumber)) {
                            msg += "Некорректный ввод гос номера.<br>";
                        }
                        // проверяем, что выбран один из шаблонов автомобилей
                        if (autoIdS.equals("0")) {
                            msg += "Не выбран автомобиль.<br>";
                        }
                        int autoId = Integer.parseInt(autoIdS);
                        if (!msg.isEmpty()) {
                            request.setAttribute("msg", msg);
                            request.getRequestDispatcher("personalArea.jsp").forward(request, response);
                        } else {
                            // если все проверки пройдены - создаем клиентское авто
                            if (dbMaster.addClientAuto(userId, autoId, plateNumber, vin.toUpperCase())) {
                                session.setAttribute("userGarage", dbMaster.addUserAuto(userId));
                                msg += "Автомобиль был успешно добавлен.";
                            } else {
                                msg += "Ошибка добавления автомобиля. Попробуйте ещё раз.";
                            }
                            request.setAttribute("msg", msg);
                            request.getRequestDispatcher("personalArea.jsp").forward(request, response);
                        }
                    }
                }
                // удаление автомобиля
                if (request.getParameter("delAuto") != null) {
                    String msg = "";
                    int userAutoId = -1;
                    try {
                        userAutoId = Integer.parseInt(request.getParameter("delAuto"));
                    } catch (NumberFormatException ex) {
                        logger.error("NumberFormatException = {}", ex);
                        msg += "При удалении произошла ошибка. Попробуйте ещё раз.";
                    }
                    if (!msg.isEmpty()) {
                        request.setAttribute("msg", msg);
                        request.getRequestDispatcher("personalArea.jsp").forward(request, response);
                    } else {
                        if (dbMaster.deleteUserAuto(userAutoId)) {
                            msg += "Автомобиль успешно удалён.";
                            session.setAttribute("userGarage", dbMaster.addUserAuto(user.getId()));
                        } else {
                            msg += "При удалении произошла ошибка.";
                        }
                        request.setAttribute("msg", msg);
                        request.getRequestDispatcher("personalArea.jsp").forward(request, response);
                    }
                }
            }
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}