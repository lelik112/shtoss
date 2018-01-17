package net.cheltsov.shtoss.command;

import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.resource.BundleManager;
import net.cheltsov.shtoss.service.PaymentService;
import net.cheltsov.shtoss.service.UserService;
import net.cheltsov.shtoss.validator.ValidationResult;
import net.cheltsov.shtoss.validator.Validator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ResourceBundle;

import static net.cheltsov.shtoss.resource.BundleManager.PATH_JSP;

// TODO: 14.12.2017 как разделять проблемы с данными и проблемы с сервисом?

public class AddFundsCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ATTR_USER = "user";
    private static final String PARAM_AMOUNT = "amount";
    private static final String CONGRATULATION = "congratulation";
    private static final String SUCCESS = "success";
    private static final String NOT_SUCCESS = "notSuccess";


    @Override
    public String execute(HttpServletRequest request) {
        synchronized (request.getSession()) {
            if (Command.isRepeat(request)) {
                return PATH_JSP.getString("jsp.main");
            }
            User user = (User) request.getSession().getAttribute(ATTR_USER);
            ResourceBundle rb = getCurrentBundle(request);
            String stringAmount = request.getParameter(PARAM_AMOUNT).replace(',', '.');
            BigDecimal amount = Validator.validatePayment(stringAmount)? new BigDecimal(stringAmount): BigDecimal.ZERO;
            if (!BigDecimal.ZERO.equals(amount) && PaymentService.addFunds(user, amount)) {
                request.getSession().setAttribute(ATTR_COMMAND_TYPE, CommandType.PAYMENT);
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
