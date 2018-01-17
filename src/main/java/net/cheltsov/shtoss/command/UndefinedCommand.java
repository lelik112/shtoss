package net.cheltsov.shtoss.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import java.util.ResourceBundle;

import static net.cheltsov.shtoss.resource.BundleManager.PATH_JSP;

public class UndefinedCommand implements Command {
    private static final String ATTR_WRONG_ACTION = "wrongAction";
    @Override
    public String execute(HttpServletRequest request) {
        ResourceBundle rb = getCurrentBundle(request);
        request.setAttribute(ATTR_WRONG_ACTION, rb.getString("mess.error.wrong-action"));
        return PATH_JSP.getString("jsp.login");
    }
}
