package net.cheltsov.shtoss.controller;

import net.cheltsov.shtoss.command.Command;
import net.cheltsov.shtoss.command.CommandType;
import net.cheltsov.shtoss.command.Responsenable;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

import static net.cheltsov.shtoss.resource.BundleManager.PATH_JSP;

public class Controller extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ATTR_COMMAND_TYPE = "commandType";
    private static final String ATTR_RESPONSE = "response";
    private static final String ATTR_NULL_PAGE = "nullPage";
    private static final String ATTR_REDIRECT = "redirect";
    private static final String REDIRECT_URL = "/controller?command=redirect&nextPage=";



    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Command command = ((CommandType) request.getAttribute(ATTR_COMMAND_TYPE)).getCommand();
        if (command instanceof Responsenable) {
            request.setAttribute(ATTR_RESPONSE, response);
        }
        String page = command.execute(request);
        boolean redirect = Optional.ofNullable((Boolean) request.getAttribute(ATTR_REDIRECT)).orElse(false);
        if (page != null && !redirect) {
            getServletContext().getRequestDispatcher(page).forward(request, response);
        } else if (page != null) {
            response.sendRedirect(request.getContextPath() + response.encodeRedirectURL(REDIRECT_URL + page));
        } else {
            ResourceBundle rb = command.getCurrentBundle(request);
            LOGGER.log(Level.ERROR, "Page is null");
            request.getSession().setAttribute(ATTR_NULL_PAGE, rb.getString("mess.null"));
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + PATH_JSP.getString("jsp.index")));
        }
    }


}
