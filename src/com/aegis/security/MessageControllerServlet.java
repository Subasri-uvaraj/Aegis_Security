package com.aegis.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/MessageController")
public class MessageControllerServlet extends HttpServlet {

    private final MessageController controller = new MessageController();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String sender = (String) session.getAttribute("username");
        String receiver = request.getParameter("receiver");
        String message = request.getParameter("message");

        if (receiver == null || receiver.isEmpty() || message == null || message.isEmpty()) {
            request.setAttribute("error", "Please select a receiver and enter a message.");
            request.getRequestDispatcher("sendMessage.jsp").forward(request, response);
            return;
        }

        controller.sendMessage(sender, receiver, message);
        response.sendRedirect("viewMessages.jsp");
    }
}
