package net.cheltsov.shtoss.command;

import javax.servlet.http.HttpServletRequest;

import static net.cheltsov.shtoss.resource.BundleManager.PATH_JSP;

public class LogoutCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        request.getSession().invalidate();
        return PATH_JSP.getBundle().getString("jsp.login");

    }
}
