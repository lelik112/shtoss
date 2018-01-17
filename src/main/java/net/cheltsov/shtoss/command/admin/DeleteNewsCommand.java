package net.cheltsov.shtoss.command.admin;

import net.cheltsov.shtoss.command.Command;
import net.cheltsov.shtoss.entity.News;
import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.service.NewsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

import static net.cheltsov.shtoss.resource.BundleManager.PATH_JSP;

public class DeleteNewsCommand implements Command, AdminCommand {
    private static final String PARAM_NEWS_ID = "news-id";
    private static final String ATTR_ERROR = "error";
    private static final String ATTR_SUCCESS = "success";
    private static final String ATTR_NEWS = "news";
    @Override
    public String execute(HttpServletRequest request) {
        synchronized (request.getSession()) {
            int newsID = Integer.parseInt(request.getParameter(PARAM_NEWS_ID));
            ServletContext context = request.getServletContext();
            synchronized (context) {
                List<News> newsList = (List<News>) context.getAttribute(ATTR_NEWS);
                if (NewsService.deleteNews(newsID)) {
                    newsList.removeIf(x -> x.getNewsID() == newsID);
                    request.setAttribute(ATTR_SUCCESS, getCurrentBundle(request).getString("mess.success.delete-news"));
                } else {
                    request.setAttribute(ATTR_ERROR, getCurrentBundle(request).getString("mess.error.something-wrong"));
                }
            }
        }
        return PATH_JSP.getString("jsp.news");
    }
}
