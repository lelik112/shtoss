package net.cheltsov.shtoss.service;

import net.cheltsov.shtoss.dao.AbstractDaoFactory;
import net.cheltsov.shtoss.dao.DaoManager;
import net.cheltsov.shtoss.dao.Initializer;
import net.cheltsov.shtoss.dao.UserDao;
import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.exception.DaoException;
import net.cheltsov.shtoss.exception.ServiceException;
import net.cheltsov.shtoss.validator.ValidationResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static net.cheltsov.shtoss.validator.ValidationResult.*;

public class UserService {
    private static final Logger LOGGER = LogManager.getLogger();
    private static AbstractDaoFactory factory = DaoManager.getDaoFactory();
    private static final String ORDER_BY_ID = "userId";
    private static final String ORDER_BY_LOGIN = "login";
    private static final String ORDER_BY_EMAIL = "email";
    private static final String ORDER_BY_BALANCE = "balance";
    private static final String ORDER_BY_ROLE = "role";
    private static final String ORDER_BY_FIRST_NAME = "fname";
    private static final String ORDER_BY_LAST_NAME = "lname";

    public static boolean updateNames(String firstName, String lastName, User user) {
        try {
            if (factory.getUserDao().updateNames(firstName, lastName, user.getUserId())) {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                return true;
            }
        } catch (DaoException e) {
            LOGGER.catching(e);
        }
        return false;
    }

    public static Optional<User> authorize(String loginOrEmail, String password) throws ServiceException {
        try {
            return factory.getUserDao().authorizeUserByLoginOrEmail(loginOrEmail, password);
        } catch (DaoException e) {
            throw new ServiceException("Database problem", e);
        }
    }

    public static ValidationResult changePassword(User user, String oldPassword, String newPassword) {
        try {
            Optional<User> sameUser = authorize(user.getLogin(), oldPassword);
            if (!sameUser.isPresent()) return PASSWORD_NOT_CORRECT;
            if (factory.getUserDao().updatePassword(newPassword, user.getUserId())) return ALL_RIGHT;
        } catch (ServiceException | DaoException e) {
            LOGGER.catching(e);
        }
        return SERVICE_ERROR;
    }

    public static ValidationResult register(User user, String login, String password, String email, String firstName, String lastName) throws ServiceException {

        try  {
            UserDao userDao = factory.getUserDao();
            if (!userDao.isLoginFree(login)) {
                return ValidationResult.LOGIN_NOT_UNIQUE;
            }
            if (!userDao.isEmailFree(email)) {
                return EMAIL_NOT_UNIQUE;
            }
            user.setLogin(login);
            user.setPassword(password);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setRole(User.Role.USER);
            if (userDao.createUser(user)) {
                return ALL_RIGHT;
            } else {
                throw new ServiceException("Database problem. Can't register user for unknown reason");
            }
        } catch (DaoException e) {
            throw new ServiceException("Database problem", e);
        }
    }

    public static boolean isLoginRegistered(String userLogin) throws ServiceException {
        try {
            return !factory.getUserDao().isLoginFree(userLogin);
        } catch (DaoException e) {
            throw new ServiceException("Database problem", e);
        }
    }

    public static List<User> findAllUsers(String orderBy) throws ServiceException {
        try {
            if (orderBy == null) orderBy = ORDER_BY_ID;
            List<User> users = factory.getUserDao().findAllUser();
            Comparator<User> defaultComparator = Comparator.comparing(User::getUserId);
            Comparator<User> comparator;
            switch (orderBy) {
                case ORDER_BY_LOGIN:
                    comparator = Comparator.comparing(User::getLogin);
                    break;
                case ORDER_BY_EMAIL:
                    comparator = Comparator.comparing(User::getEmail);
                    break;
                case ORDER_BY_BALANCE:
                    comparator = Comparator.comparing(User::getBalance).thenComparing(defaultComparator);
                    break;
                case ORDER_BY_ROLE:
                    comparator = Comparator.comparing(User::getRole).thenComparing(defaultComparator);
                    break;
                case ORDER_BY_FIRST_NAME:
                    comparator = Comparator.comparing(User::getFirstName).thenComparing(defaultComparator);
                    break;
                case ORDER_BY_LAST_NAME:
                    comparator = Comparator.comparing(User::getLastName).thenComparing(defaultComparator);
                    break;
                case ORDER_BY_ID:
                default:
                    comparator = defaultComparator;
            }
            users.sort(comparator);
            return users;
        } catch (DaoException e) {
            throw new ServiceException("Database problem", e);
        }
    }

    public static Optional<User> findUserById(int userID) throws ServiceException {
        try {
            return Optional.ofNullable(factory.getUserDao().findUserById(userID));
        } catch (DaoException e) {
            throw new ServiceException("Database problem", e);
        }
    }

    public static boolean changeRole(User userToChange, User.Role newRole) throws ServiceException {
        Initializer initializer = null;
        try {
            initializer = factory.getInitializer();
            initializer.setAutoCommit(false);
            UserDao userDao = factory.getUserDao(initializer);
            userDao.updateRole(userToChange.getUserId(), newRole.ordinal());
            if (User.Role.ADMIN.equals(userToChange.getRole()) && userDao.countAdmin() < 1) {
                initializer.rollback(); // TODO: 06.01.2018 проверить работоспособность
                return false;
            }
            initializer.commit();
            userToChange.setRole(newRole);
            return true;
        } catch (DaoException e) {
            initializer.rollback();
            throw new ServiceException("Database problem", e);
        } finally {
            initializer.close();
        }
    }

    public static ValidationResult changeEmail(User user, String password, String email) {
        UserDao userDao = factory.getUserDao();
        try {
            Optional<User> sameUser = authorize(user.getLogin(), password);
            if (!sameUser.isPresent()) return PASSWORD_NOT_CORRECT;
            if (!userDao.isEmailFree(email)) return EMAIL_NOT_UNIQUE;
            if (userDao.updateEmail(email, user.getUserId())) {
                user.setEmail(email);
                return ALL_RIGHT;
            }
        } catch (ServiceException | DaoException e) {
            LOGGER.catching(e);
        }
        return SERVICE_ERROR;
    }
}









