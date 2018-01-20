package net.cheltsov.shtoss.dao.sql;

import net.cheltsov.shtoss.dao.UserDao;
import net.cheltsov.shtoss.entity.User;
import net.cheltsov.shtoss.exception.DaoException;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqlUserDao extends SqlAbstractDao implements UserDao {
    private static final String DAO_USER_ID = "user_id";
    private static final String DAO_USER_LOGIN = "login";
    private static final String DAO_USER_BALANCE = "balance";
    private static final String DAO_USER_ROLE = "role";
    private static final String DAO_USER_EMAIL = "email";
    private static final String DAO_USER_FIRST_NAME = "fname";
    private static final String DAO_USER_LAST_NAME = "lname";
    private static final String SQL_AUTHORIZE_USER = "SELECT user_id, login, email, balance, role, fname, lname FROM user " +
            "WHERE (login = ? OR email = ?) AND password = md5(?);";
    private static final String SQL_ADD_USER = "INSERT INTO user (login, password, email, role, fname, lname) " +
            "VALUES (?, MD5(?), ?, ?, ?, ?)";
    private static final String SQL_COUNT_ADMIN = "SELECT count(*) FROM (SELECT role FROM user WHERE role = 2) t";
    private static final String SQL_FIND_USER_ID_BY_LOGIN = "SELECT user_id FROM user WHERE login = ?";
    private static final String SQL_FIND_USER_ID_BY_EMAIL = "SELECT user_id FROM user WHERE email = ?";
    private static final String SQL_UPDATE_BALANCE = "UPDATE user SET balance = balance + ? WHERE user_id = ?";
    private static final String SQL_UPDATE_NAMES = "UPDATE user SET fname = ?, lname = ? WHERE user_id = ?";
    private static final String SQL_UPDATE_PASSWORD = "UPDATE user SET password = md5(?) WHERE user_id = ?";
    private static final String SQL_UPDATE_EMAIL = "UPDATE user SET email = ? WHERE user_id = ?";
    private static final String SQL_UPDATE_ROLE = "UPDATE user SET role = ? WHERE user_id = ?";
    private static final String SQL_FIND_ALL_USERS = "SELECT user_id, login, email, balance, role, fname, lname FROM user ORDER BY login";
    private static final String SQL_FIND_USER_BY_ID = "SELECT user_id, login, email, balance, role, fname, lname FROM user" +
            " WHERE user_id = ?";
    private static final String SQL_FIND_USER_BY_LOGIN = "SELECT user_id, login, email, balance, role, fname, lname FROM user" +
            " WHERE login = ?";

    private static SqlUserDao methodLevelUserDao = new SqlUserDao();

    private SqlUserDao() {
    }

    public SqlUserDao(SqlInitializer initializer) {
        super(initializer.getConnection());
        initializer.addDao(this);
    }

    public static SqlUserDao getMethodLevelUserDao() {
        return methodLevelUserDao;
    }

    public User findUserById(int userID) throws DaoException {
        Connection cn = getConnection();
        try (PreparedStatement ps = cn.prepareStatement(SQL_FIND_USER_BY_ID)) {
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return buildUser(rs);
        } catch (SQLException e) {
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    @Override
    public User findUserByLogin(String userlogin) throws DaoException {
        Connection cn = getConnection();
        try (PreparedStatement ps = cn.prepareStatement(SQL_FIND_USER_BY_LOGIN)) {
            ps.setString(1, userlogin);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return buildUser(rs);
        } catch (SQLException e) {
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    @Override
    public List<User> findAllUser() throws DaoException {
        Connection cn = getConnection();
        try (Statement s = cn.createStatement()) {
            ResultSet rs = s.executeQuery(SQL_FIND_ALL_USERS);
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(buildUser(rs));
            }
            return users;
        } catch (SQLException e) {
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    @Override
    public boolean updateBalance(BigDecimal amount, int userID) throws DaoException {
        Connection cn = getConnection();
        try (PreparedStatement ps = cn.prepareStatement(SQL_UPDATE_BALANCE)) {
            ps.setBigDecimal(1, amount);
            ps.setInt(2, userID);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    @Override
    public boolean updateNames(String firstName, String lastName, int userID) throws DaoException {
        Connection cn = getConnection();
        try (PreparedStatement ps = cn.prepareStatement(SQL_UPDATE_NAMES)) {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setInt(3, userID);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    @Override
    public boolean updatePassword(String newPassword, int userID) throws DaoException {
        Connection cn = getConnection();
        try (PreparedStatement ps = cn.prepareStatement(SQL_UPDATE_PASSWORD)) {
            ps.setString(1, newPassword);
            ps.setInt(2, userID);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    @Override
    public boolean updateEmail(String email, int userID) throws DaoException {
        Connection cn = getConnection();
        try (PreparedStatement ps = cn.prepareStatement(SQL_UPDATE_EMAIL)) {
            ps.setInt(2, userID);
            ps.setString(1, email);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    public boolean isEmailFree(String email) throws DaoException {
        Connection cn = getConnection();
        try (PreparedStatement ps = cn.prepareStatement(SQL_FIND_USER_ID_BY_EMAIL)) {
            ps.setString(1, email);
            return !ps.executeQuery().next();
        } catch (SQLException e) {
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    public boolean isLoginFree(String login) throws DaoException {
        Connection cn = getConnection();
        try (PreparedStatement ps = cn.prepareStatement(SQL_FIND_USER_ID_BY_LOGIN)) {
            ps.setString(1, login);
            boolean b = !ps.executeQuery().next();
            return b;
        } catch (SQLException e) {
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    public boolean createUser(User user) throws DaoException {
        Connection cn = getConnection();
        try (PreparedStatement ps = cn.prepareStatement(SQL_ADD_USER)) {
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setInt(4, user.getRole().ordinal());
            ps.setString(5, user.getFirstName());
            ps.setString(6, user.getLastName());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    public Optional<User> authorizeUserByLoginOrEmail(String loginOrEmail, String password) throws DaoException {
        Connection cn = getConnection();
        ResultSet rs;
        try (PreparedStatement ps = cn.prepareStatement(SQL_AUTHORIZE_USER)) {
            ps.setString(1, loginOrEmail);
            ps.setString(2, loginOrEmail);
            ps.setString(3, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.ofNullable(buildUser(rs));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    @Override
    public int countAdmin() throws DaoException {
        Connection cn = getConnection();
        try (Statement s = cn.createStatement()) {
            ResultSet rs = s.executeQuery(SQL_COUNT_ADMIN);
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    @Override
    public boolean updateRole(int userID, int newRoleID) throws DaoException {
        Connection cn = getConnection();
        try (PreparedStatement ps = cn.prepareStatement(SQL_UPDATE_ROLE)) {
            ps.setInt(1, newRoleID);
            ps.setInt(2, userID);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    private User buildUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setID(rs.getInt(DAO_USER_ID));
        user.setLogin(rs.getString(DAO_USER_LOGIN));
        user.setEmail(rs.getString(DAO_USER_EMAIL));
        user.setBalance(rs.getBigDecimal(DAO_USER_BALANCE));
        user.setRole(User.Role.values()[rs.getInt(DAO_USER_ROLE)]);
        user.setFirstName(rs.getString(DAO_USER_FIRST_NAME));
        user.setLastName(rs.getString(DAO_USER_LAST_NAME));
        return user;
    }

}























