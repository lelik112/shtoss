package net.cheltsov.shtoss.command;

import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.service.PaymentService;
import net.cheltsov.shtoss.validator.Validator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ResourceBundle;

import static net.cheltsov.shtoss.resource.BundleManager.PATH_JSP;

public class AddFundsCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ATTR_USER = "user";
    private static final String PARAM_AMOUNT = "amount";
    private static final String CONGRATULATION = "congratulation";
    private static final String SUCCESS = "success";
    private static final String NOT_SUCCESS = "notSuccess";
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
            ResourceBundle rb = getCurrentBundle(request);
            String stringAmount = request.getParameter(PARAM_AMOUNT).replace(',', '.');
            BigDecimal amount = Validator.validatePayment(stringAmount)? new BigDecimal(stringAmount): BigDecimal.ZERO;
            if (!BigDecimal.ZERO.equals(amount) && PaymentService.addFunds(user, amount)) {
                request.setAttribute(CONGRATULATION, rb.getString("mess.info.congratulation"));
                request.setAttribute(SUCCESS, rb.getString("mess.info.successful"));
                LOGGER.log(Level.INFO, "User: " + user.getLogin() + " payed " + amount);
            } else {
                LOGGER.log(Level.ERROR, "Payment problem. User: " + user.getLogin());
                request.setAttribute(NOT_SUCCESS, rb.getString("mess.info.not-successful"));
            }
        }
        return PATH_JSP.getString("jsp.congratulation");
    }
}
