package app.servlet;

import app.service.DBMaster;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 * Подтверждение почтового адреса
 *
 */
@WebServlet(name = "EmailConfirm", urlPatterns = {"/EmailConfirm"})
public class EmailConfirm extends HttpServlet {

    @EJB
    DBMaster dbMaster;

    private static final Logger logger = Logger.getLogger(EmailConfirm.class);

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
        response.setContentType("text/html;charset=UTF-8");
        // активация учетной записи пользователя - клиент попадает сюда после
        // нажатия на ссылку для активации в письме
        int success = -1;
        String sId = request.getParameter("id");
        String hash = request.getParameter("confirm");
        try {
            int id = Integer.parseInt(sId);
            if (dbMaster.confirmEmail(id, hash)) {
                success = 1;
            }
        } catch (NumberFormatException ex) {
            logger.error("NumberFormatException = {}", ex);
        }

        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>EmailConfirm</title>");
            out.println("</head>");
            out.println("<body>");
            if (success > 0) {
                out.println("<h2 align=\"center\">Ваш учетная запись успешно активирована.</h2>");
            } else {
                out.println("<h2 align=\"center\">Ошибка активации</h2>");
            }
            out.println("</body>");
            out.println("</html>");
        }
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
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Method GET not supported yet...");
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