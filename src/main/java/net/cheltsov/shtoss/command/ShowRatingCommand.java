package net.cheltsov.shtoss.command;

import net.cheltsov.shtoss.entity.UserRating;
import net.cheltsov.shtoss.exception.ServiceException;
import net.cheltsov.shtoss.resource.BundleManager;
import net.cheltsov.shtoss.service.GameService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class ShowRatingCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ATTR_ERROR = "error";
    private static final String ATTR_RATINGS = "ratings";
    @Override
    public String execute(HttpServletRequest request) {
        try {
            List<UserRating> ratings = GameService.findRating();
            ratings.sort(Comparator.comparingInt(UserRating::getWinGames)
                    .thenComparingInt(UserRating::getAllGames).reversed());
            request.setAttribute(ATTR_RATINGS, ratings);
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, "Can't find rating", e);
            ResourceBundle rb = getCurrentBundle(request);
            request.setAttribute(ATTR_ERROR, rb.getString("mess.error.something-wrong"));
        }
        return BundleManager.PATH_JSP.getString("jsp.rating");
    }
}
