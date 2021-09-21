package app.servlet;

import app.model.Order;
import app.model.Orders;
import app.model.TimeMashine;
import app.model.User;
import app.service.DBMaster;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Класс для выполнения удаления заказ-наряда
 */
@WebServlet(name = "OrdersControlServlet", urlPatterns = {"/OrdersControlServlet"})
public class OrdersControlServlet extends HttpServlet {

    @EJB
    private DBMaster dbMaster;

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
        response.sendError(HttpServletResponse.SC_FORBIDDEN,
                "Method GET not supported yet...");
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
                String orderNumber = request.getParameter("delete");
                if (orderNumber == null) {
                    response.sendRedirect("index.jsp");
                } else {
                    TimeMashine time = (TimeMashine) session.getAttribute("time");
                    Orders orders = (Orders) session.getAttribute("orders");
                    Order order = orders.getOrderFromNumber(orderNumber);
                    if (dbMaster.deleteEntry(order)) {
                        // добавляем в запрос сообщение об успешном удалении
                        request.setAttribute("msg", "Запись успешно удалена.");
                        // обновляем записи
                        session.setAttribute("orders", dbMaster.getOrdersToDate(time.getTime()));
                    } else {
                        // сообщение об ошибке
                        request.setAttribute("errorMsg", "Ошибка удаления попробуйте ещё раз.");
                    }
                    // воврат на страницу просмотра записей
                    request.getRequestDispatcher("index.jsp").forward(request, response);
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