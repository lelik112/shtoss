package net.cheltsov.shtoss.command;

import net.cheltsov.shtoss.entity.Deck;
import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.exception.ServiceException;
import net.cheltsov.shtoss.service.GameService;
import net.cheltsov.shtoss.validator.Validator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.ResourceBundle;

import static net.cheltsov.shtoss.resource.BundleManager.PATH_JSP;


public class GameCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ATTR_USER = "user";
    private static final String ATTR_WRONG_ACTION = "wrongAction";
    private static final String ATTR_RESULT = "result";
    private static final String PARAM_ATTR_BID = "bid";
    private static final String PARAM_ATTR_CARD = "card";
    private static final String ATTR_DECK = "deck";
    private static final String ATTR_ERROR = "error";
    private static final String ATTR_REDIRECT = "redirect";
    private static final String REDIRECT_NEXT_PAGE_MAIN = "jsp.main";


    @Override
    public String execute(HttpServletRequest request) {

        synchronized (request.getSession()) {

            if (isRepeat(request)) {
                request.setAttribute(ATTR_REDIRECT, true);
                return REDIRECT_NEXT_PAGE_MAIN;
            }
            User user = (User) request.getSession().getAttribute(ATTR_USER);
            int card = Integer.parseInt(request.getParameter(PARAM_ATTR_CARD));
            Deck deck = (Deck) request.getSession().getAttribute(ATTR_DECK);
            deck = deck != null ? deck : (Deck) request.getServletContext().getAttribute(ATTR_DECK);
            String stringBid = request.getParameter(PARAM_ATTR_BID).replace(',', '.');
            BigDecimal bid = Validator.validatePayment(stringBid) ? new BigDecimal(stringBid) : BigDecimal.ZERO;

            ResourceBundle rb = getCurrentBundle(request);

            if (bid.equals(BigDecimal.ZERO)) {
                request.setAttribute(ATTR_ERROR, rb.getString("mess.error.something-wrong"));
                LOGGER.log(Level.WARN, "Bid's pattern problem");
                allowRepeating(request);
                return PATH_JSP.getString("jsp.start-game");
            }

            if (bid.compareTo(user.getBalance()) > 0) {
                request.setAttribute(ATTR_WRONG_ACTION, rb.getString("mess.error.wrong-action.bid"));
                allowRepeating(request);
                return PATH_JSP.getString("jsp.start-game");
            }

            List<String> moves;
            try {
                moves = GameService.doGame(user, bid, card, deck);
            } catch (ServiceException e) {
                LOGGER.catching(e);
                request.setAttribute(ATTR_ERROR, rb.getString("mess.error.something-wrong"));
                return PATH_JSP.getString("jsp.start-game");
            }
            request.setAttribute(ATTR_RESULT, moves);
            request.setAttribute(PARAM_ATTR_CARD, 'x' + Integer.toHexString(card));
            request.setAttribute(PARAM_ATTR_BID, bid);
        }
        return PATH_JSP.getString("jsp.result-game");
    }
}
