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
import java.util.ResourceBundle;

import static net.cheltsov.shtoss.resource.BundleManager.PATH_JSP;

public class UpdateNewsCommand implements Command, AdminCommand {
    private static final String PARAM_TOPIC = "topic";
    private static final String PARAM_TEXT = "text";
    private static final String PARAM_NEWS_ID = "news-id";
    private static final String ATTR_ERROR = "error";
    private static final String ATTR_SUCCESS = "success";
    private static final String ATTR_NEWS = "news";
    @Override
    public String execute(HttpServletRequest request) {
        synchronized (request.getSession()) {
            String topic = request.getParameter(PARAM_TOPIC);
            String text = request.getParameter(PARAM_TEXT);
            int newsID = Integer.parseInt(request.getParameter(PARAM_NEWS_ID));
            ServletContext context = request.getServletContext();
            synchronized (context) {
                List<News> newsList = (List<News>) context.getAttribute(ATTR_NEWS);
                Optional<News> shredingeOldNews = newsList.stream().filter(x -> x.getNewsID() == newsID).findAny();
                if (shredingeOldNews.isPresent() && NewsService.updateNews(topic, text, newsID)) {
                    shredingeOldNews.get().setCaption(topic);
                    shredingeOldNews.get().setText(text);
                    request.setAttribute(ATTR_SUCCESS, getCurrentBundle(request).getString("mess.success.update-news"));
                } else {
                    request.setAttribute(ATTR_ERROR, getCurrentBundle(request).getString("mess.error.something-wrong"));
                }
            }
        }
        return PATH_JSP.getString("jsp.news");
    }
}
