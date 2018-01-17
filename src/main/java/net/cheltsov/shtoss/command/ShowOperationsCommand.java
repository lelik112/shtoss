package net.cheltsov.shtoss.command;

import net.cheltsov.shtoss.entity.BalanceOperation;
import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.exception.ServiceException;
import net.cheltsov.shtoss.resource.BundleManager;
import net.cheltsov.shtoss.service.PaymentService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class ShowOperationsCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ATTR_USER = "user";
    private static final String ATTR_ERROR = "error";
    private static final String ATTR_OPERATIONS = "operations";
    @Override
    public String execute(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(ATTR_USER);
        try {
            List<BalanceOperation> operations = PaymentService.findUserOperations(user);
            operations.sort(Comparator.comparing(BalanceOperation::getDate).reversed());
            request.setAttribute(ATTR_OPERATIONS, operations);
        } catch (ServiceException e) {
            LOGGER.log(Level.ERROR, "Can't get balance operations", e);
            ResourceBundle rb = getCurrentBundle(request);
            request.setAttribute(ATTR_ERROR, rb.getString("mess.error.something-wrong"));
        }
        return BundleManager.PATH_JSP.getString("jsp.operation-history");
    }
}
