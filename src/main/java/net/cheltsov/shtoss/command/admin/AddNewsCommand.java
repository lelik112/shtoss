package net.cheltsov.shtoss.command.admin;

import net.cheltsov.shtoss.command.Command;
import net.cheltsov.shtoss.entity.News;
import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.exception.ServiceException;
import net.cheltsov.shtoss.resource.BundleManager;
import net.cheltsov.shtoss.service.NewsService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static net.cheltsov.shtoss.resource.BundleManager.PATH_JSP;

public class AddNewsCommand implements Command, AdminCommand {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ATTR_USER = "user";
    private static final String PARAM_TOPIC = "topic";
    private static final String PARAM_TEXT = "text";
    private static final String ATTR_ERROR = "error";
    private static final String ATTR_NEWS = "news";
    @Override
    public String execute(HttpServletRequest request) {
        synchronized (request.getSession()) {
            User user = (User) request.getSession().getAttribute(ATTR_USER);
            String topic = request.getParameter(PARAM_TOPIC);
            String text = request.getParameter(PARAM_TEXT);
            Optional<News> shredingerNews = NewsService.createNews(user, topic, text);
            ServletContext context = request.getServletContext();
            synchronized (context) {
                shredingerNews.ifPresent(((Deque<News>) context.getAttribute(ATTR_NEWS))::push);
            }
            if (!shredingerNews.isPresent()) {
                request.setAttribute(ATTR_ERROR, getCurrentBundle(request).getString("mess.error.something-wrong"));
            }
        }
        return PATH_JSP.getString("jsp.news");
    }
}
