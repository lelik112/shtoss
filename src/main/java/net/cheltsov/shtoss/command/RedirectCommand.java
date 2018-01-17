package net.cheltsov.shtoss.command;

import javax.servlet.http.HttpServletRequest;

import static net.cheltsov.shtoss.resource.BundleManager.PATH_JSP;

public class RedirectCommand implements Command{
    private static final String PARAM_NEXT_PAGE = "nextPage";

    @Override
    public String execute(HttpServletRequest request) {
        return PATH_JSP.getString(request.getParameter(PARAM_NEXT_PAGE));
    }
}
