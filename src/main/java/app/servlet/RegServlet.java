package app.servlet;

import app.model.Staff;
import app.model.TimeMashine;
import app.model.User;
import app.util.PswdCrypt;
import app.util.Validator;
import app.service.DBMaster;
import app.service.EmailService;
import java.io.IOException;
import java.time.LocalDate;
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
 * Класс для регистрации, аутентификации и авторизации пользователей
 *
 */
@WebServlet(name = "RegServlet", urlPatterns = {"/RegServlet"})
public class RegServlet extends HttpServlet {

    @EJB
    DBMaster dbMaster;

    @EJB
    EmailService emailService;

    @Inject
    Validator validator;

    @Inject
    PswdCrypt pswdCrypt;

    private static final Logger logger = Logger.getLogger(RegServlet.class);

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getSession().invalidate();
        response.sendRedirect("index.jsp");
//        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Method GET not supported yet...");
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        // если была нажата кнопка зарегистрироваться
        if (request.getParameter("register") != null) {
            // получаем параметры запроса
            String name = request.getParameter("name").trim();
            String surname = request.getParameter("surname").trim();
            String telephone = request.getParameter("telephone").trim();
            String email = request.getParameter("email").trim();
            String pswd = request.getParameter("pswd").trim();
            // проверяем что поля заполнены
            if (name.isEmpty() || surname.isEmpty() || telephone.isEmpty()
                    || email.isEmpty() || pswd.isEmpty()) {
                request.setAttribute("msg", "Для успешной регистрации необходимо"
                        + "заполнить все поля данной формы.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            } else {
                String msg = "";
                // проверка написания имени согласно допустимому шаблону
                if (!validator.isValidName(name)) {
                    msg += "Имя должно совпадать с указанным в паспорте и не должно "
                            + "содержать пробелов и спецсимволов<br>";
                }
                // проверка написания фамилии согласно допустимому шаблону
                if (!validator.isValidName(surname)) {
                    msg += "Фамилия должна совпадать с указанной в паспорте и не должна "
                            + "содержать пробелов и спецсимволов<br>";
                }
                // проверка написания номера телефона согласно шаблону
                if (!validator.isValidTelephone(telephone)) {
                    msg += "Написание телефона должно соответствовать шаблону<br>";
                }
                // проверка написания email
                if (!validator.isValidEmail(email)) {
                    msg += "Введен некорректный Email<br>";
                }
                // проверяем, что пароль содержит более 4 символов
                if (pswd.length() < 5) {
                    msg += "Пароль должен содержать более 4 символов";
                }
                if (!msg.isEmpty()) {
                    request.setAttribute("msg", msg);
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                } else {
                    // проверяем наличие уже созданного аккаунта с указанным email
                    int contains = dbMaster.containsEmail(email);
                    if (contains != 1) {
                        if (contains == 2) {
                            msg += "Пользователь с указанным email уже существует.<br>";
                        } else {
                            msg += "Ошибка соединения, попробуйте ещё раз.<br>";
                        }
                    }
                    // проверяем наличие уже созданного аккаунта с указанным телефоном
                    contains = dbMaster.containsTelephone(telephone);
                    if (contains != 1) {
                        if (contains == 2) {
                            msg += "Пользователь с указанным номером телефона уже существует.";
                        } else {
                            if (!msg.contains("Ошибка соединения, попробуйте ещё раз.<br>")) {
                                msg += "Ошибка соединения, попробуйте ещё раз.<br>";
                            }
                        }
                    }
                    if (!msg.isEmpty()) {
                        request.setAttribute("msg", msg);
                        request.getRequestDispatcher("register.jsp").forward(request, response);
                    } else {
                        // после успешного прохождения всех проверок создаем новый аккаунт
                        logger.info("addContragentAccount");
                        if (dbMaster.addContragentAccount(name, surname, telephone, email, pswdCrypt.pswdCrypt(pswd))) {
                            // отправка активационного письма
                            logger.info("sendMail");
                            emailService.sendEmailForConfirm(email, dbMaster.getUserIdFromEmail(email));
                            request.setAttribute("msg", "Учетная запись успешно создана.<br>"
                                    + "Для активации учетной записи перейдите по ссылке в отправленом вам письме.");
                            request.getRequestDispatcher("register.jsp").forward(request, response);
                        } else {
                            request.setAttribute("msg", "Ошибка создания учетной записи.");
                            request.getRequestDispatcher("register.jsp").forward(request, response);
                        }
                    }
                }
            }
        }
        // если нажата кнопка вход
        if (request.getParameter("login") != null) {
            // получаем параметры запроса
            String email = request.getParameter("login").trim();
            String password = request.getParameter("pswd").trim();
            if (email.isEmpty() || password.isEmpty()) {
                request.setAttribute("msg", "Необходимо"
                        + "заполнить все поля данной формы.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                // если указанная учетная запись существует и пароль совпадает
                if (dbMaster.login(email, password)) {
                    // проверяем был ли активирован аккаунт
                    if (dbMaster.accountConfirmed(email)) {
                        // если пароль правильный и учетная запись активирована,
                        // то создаем сессию
                        HttpSession session = request.getSession(true);
                        // добавляем шаблонные авто
                        request.getServletContext().setAttribute("collectionAuto", dbMaster.getCollection());
                        // добавляем сотрудников
                        Staff staff = dbMaster.getStaff();
                        request.getServletContext().setAttribute("staff", staff);
                        // добавляем события
                        request.getServletContext().setAttribute("event", dbMaster.getEvent());
                        // класс для управления выбором времени записи
                        session.setAttribute("time", new TimeMashine());
                        // определяем права пользователя и в зависимости от прав создаем пользователя
                        String role = dbMaster.userRights(email);
                        if (role.equals("USER")) {
                            // добавляем в сессию пользователя
                            User user = dbMaster.getInstanceUser(email);
                            session.setAttribute("user", user);
                            // добавляем в сессию гараж пользователя
                            session.setAttribute("userGarage", dbMaster.addUserAuto(user.getId()));
                        } else if (role.equals("SUPERUSER")) {
                            // добавляем в сессию сотрудника
                            User user = staff.getEmployeeFromEmail(email);
                            session.setAttribute("user", user);
                            // добавляем заказ-наряды на сегодняшний день
                            session.setAttribute("orders", dbMaster.getOrdersToDate(LocalDate.now()));
                        }
                        response.sendRedirect("index.jsp");
                    } else {
                        request.setAttribute("msg", "Аккаунт ещё не активирован. Инструкции по активации аккаунта<br>"
                                + "находятся в письме отправленном на почту указанную при регистрации");
                        request.getRequestDispatcher("login.jsp").forward(request, response);
                    }
                } else {
                    request.setAttribute("msg", "Такого аккаунта нет.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
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
