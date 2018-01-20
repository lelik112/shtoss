package net.cheltsov.shtoss.dao;

import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.exception.DaoException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserDao { 
    User findUserById(int userID) throws DaoException;
    User findUserByLogin(String userlogin) throws DaoException;
    List<User> findAllUser() throws DaoException;
    boolean updateBalance (BigDecimal amount, int userID) throws DaoException;
    boolean updateNames (String firstName, String lastName, int userID) throws DaoException;
    boolean updatePassword (String newPassword, int userID) throws DaoException;
    boolean updateEmail (String email, int userID) throws DaoException;
    boolean isEmailFree (String email) throws DaoException;
    boolean isLoginFree (String login) throws DaoException;
    boolean createUser(User user) throws DaoException;
    Optional<User> authorizeUserByLoginOrEmail(String loginOrEmail, String password) throws DaoException;
    int countAdmin() throws DaoException;
    boolean updateRole(int userID, int newRoleID) throws DaoException;
}
