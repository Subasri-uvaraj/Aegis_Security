package com.aegis.security;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ViewMessagesServlet")
public class ViewMessagesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Ensure user is logged in before viewing messages
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String currentUser = (String) session.getAttribute("username");

        try {
            // Use MessageService to fetch all messages for the current user
            MessageService messageService = new MessageService();
            List<Message> messages = messageService.fetchMessages(currentUser);

            // Attach the messages to the request for display
            request.setAttribute("messages", messages);
            request.setAttribute("username", currentUser);

            // Forward to JSP for rendering
            request.getRequestDispatcher("viewMessages.jsp").forward(request, response);

        } catch (Exception e) {
            System.out.println("Error loading messages for " + currentUser + ": " + e.getMessage());
            request.setAttribute("errorMessage", "Unable to load messages right now. Please try again.");
            request.getRequestDispatcher("viewMessages.jsp").forward(request, response);
        }
    }
}

