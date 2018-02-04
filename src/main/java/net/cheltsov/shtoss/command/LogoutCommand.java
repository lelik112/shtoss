package net.cheltsov.shtoss.command;

import javax.servlet.http.HttpServletRequest;

public class LogoutCommand implements Command {
    private static final String ATTR_REDIRECT = "redirect";
    private static final String REDIRECT_NEXT_PAGE_INDEX = "jsp.index";
    @Override
    public String execute(HttpServletRequest request) {
        request.getSession().invalidate();
        request.setAttribute(ATTR_REDIRECT, true);
        return REDIRECT_NEXT_PAGE_INDEX;
    }
}
