package net.cheltsov.shtoss.service;

import net.cheltsov.shtoss.dao.AbstractDaoFactory;
import net.cheltsov.shtoss.dao.DaoManager;
import net.cheltsov.shtoss.dao.Initializer;
import net.cheltsov.shtoss.dao.UserDao;
import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.exception.DaoException;
import net.cheltsov.shtoss.exception.ServiceException;
import net.cheltsov.shtoss.validator.ValidationResult;
import net.cheltsov.shtoss.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static net.cheltsov.shtoss.validator.ValidationResult.*;

/**
 * A service layer class implementing all the logic concerning users
 */
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

    /**
     * Edits user's name
     *
     * @param firstName new first name
     * @param lastName  new last name
     * @param user      user to edit
     * @return true if update was successful, false otherwise
     */
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

    /**
     * Authorizes user by login or email
     *
     * @param loginOrEmail login or email of user
     * @param password     password
     * @return the user, wrapped in Optional if the conversation was created, Optional.empty() otherwise
     * @throws ServiceException if any exceptions occurred on the DAO layer
     */
    public static Optional<User> authorize(String loginOrEmail, String password) throws ServiceException {
        try {
            return factory.getUserDao().authorizeUserByLoginOrEmail(loginOrEmail, password);
        } catch (DaoException e) {
            throw new ServiceException("Database problem", e);
        }
    }

    /**
     * Changes the user's password
     * @param user user to edit
     * @param oldPassword old password
     * @param newPassword new password
     * @return result of user validation and editing
     */
    public static ValidationResult changePassword(User user, String oldPassword, String newPassword) {
        try {
            Optional<User> sameUser = authorize(user.getLogin(), oldPassword);
            if (!sameUser.isPresent()) {
                return PASSWORD_NOT_CORRECT;
            }
            if (factory.getUserDao().updatePassword(newPassword, user.getUserId())) {
                return ALL_RIGHT;
            }
        } catch (ServiceException | DaoException e) {
            LOGGER.catching(e);
        }
        return SERVICE_ERROR;
    }

    /**
     * Registers a new user
     * @param user a user for setting fields
     * @param login login of new user
     * @param password password of new user
     * @param email email of new user
     * @param firstName first name of new user
     * @param lastName last name of new user
     * @return the result of user creating
     * @throws ServiceException if any exceptions occurred on the DAO layer
     */
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
            int userId = userDao.createUser(user);
            if (userId > 0) {
                user.setUserId(userId);
                return ALL_RIGHT;
            } else {
                throw new ServiceException("Database problem. Can't register user for unknown reason");
            }
        } catch (DaoException e) {
            throw new ServiceException("Database problem", e);
        }
    }

    /**
     * Checks if the given login is already registered
     * @param userLogin login to check
     * @return <tt>true</tt> if the given login is already registered, <tt>false</tt> otherwise
     * @throws ServiceException if any exceptions occurred on the DAO layer
     */
    public static boolean isLoginRegistered(String userLogin) throws ServiceException {
        try {
            return !factory.getUserDao().isLoginFree(userLogin);
        } catch (DaoException e) {
            throw new ServiceException("Database problem", e);
        }
    }

    /**
     * Finds all users ordered by given parameter
     * @param orderBy the String representation of field to order by
     * @return the list of found users
     * @throws ServiceException if any exceptions occurred on the DAO layer
     */
    public static List<User> findAllUsers(String orderBy) throws ServiceException {
        try {
            if (orderBy == null) {
                orderBy = ORDER_BY_ID;
            }
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

    /**
     * Finds a user by specific id
     *
     * @param userId specific user's id
     * @return the user, wrapped in Optional if the user was found, Optional.empty() otherwise
     * @throws ServiceException if any exceptions occurred on the DAO layer
     */
    public static Optional<User> findUserById(int userId) throws ServiceException {
        try {
            return Optional.ofNullable(factory.getUserDao().findUserById(userId));
        } catch (DaoException e) {
            throw new ServiceException("Database problem", e);
        }
    }

    /**
     * Changes user's role
     *
     * @param userToChange user to edit
     * @param newRole      new Role
     * @return <tt>true</tt> if changing was successful, <tt>false</tt> otherwise
     * @throws ServiceException if any exceptions occurred on the DAO layer
     */
    public static boolean changeRole(User admin, User userToChange, User.Role newRole) throws ServiceException {
        Initializer initializer = null;
        try {
            initializer = factory.getInitializer();
            initializer.setAutoCommit(false);
            UserDao userDao = factory.getUserDao(initializer);
            userDao.updateRole(userToChange.getUserId(), newRole.ordinal());
            if (User.Role.ADMIN.equals(userToChange.getRole()) && userDao.countAdmin() < 1) {
                initializer.rollback();
                return false;
            }
            initializer.commit();
            userToChange.setRole(newRole);
            if (admin.getUserId() == userToChange.getUserId()) {
                admin.setRole(userToChange.getRole());
            }
            return true;
        } catch (DaoException e) {
            initializer.rollback();
            throw new ServiceException("Database problem", e);
        } finally {
            initializer.close();
        }
    }

    /**
     * Changes user's email
     * @param user user to edit
     * @param password password for authorization
     * @param email new email
     * @return <tt>true</tt> if changing was successful, <tt>false</tt> otherwise
     */
    public static ValidationResult changeEmail(User user, String password, String email) {
        UserDao userDao = factory.getUserDao();
        try {
            Optional<User> sameUser = authorize(user.getLogin(), password);
            if (!sameUser.isPresent()) {
                return PASSWORD_NOT_CORRECT;
            }
            if (!userDao.isEmailFree(email)) {
                return EMAIL_NOT_UNIQUE;
            }
            if (!Validator.validateEmail(email)) {
                return EMAIL_NOT_MATCH;
            }
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









