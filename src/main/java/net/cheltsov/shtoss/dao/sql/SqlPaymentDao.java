package net.cheltsov.shtoss.dao.sql;

import net.cheltsov.shtoss.dao.PaymentDao;
import net.cheltsov.shtoss.entity.BalanceOperation;
import net.cheltsov.shtoss.entity.Payment;
import net.cheltsov.shtoss.exception.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlPaymentDao extends SqlAbstractDao implements PaymentDao {
    private static final String DAO_AMOUNT = "amount";
    private static final String DAO_TYPE_OF_OPERATION = "type";
    private static final String DAO_DATE = "date";
    private static final String DAO_BALANCE = "balance";
    private static final String SQL_ADD_PAYMENT = "INSERT INTO payment (user_id, amount) VALUES (?, ?)";
    private static final String SQL_FIND_USER_OPERATIONS =
            "SELECT amount, 'payment' AS type, payment_date AS date, " +
            "       (SELECT sum(p.amount) FROM " +
            "           (SELECT amount, payment_date, user_id FROM payment UNION SELECT bid, game_time, user_id FROM game) p " +
            "       WHERE payment_date <= date AND user_id = ?) AS balance " +
            "  FROM payment WHERE user_id = ? " +
            "UNION " +
            "SELECT bid, 'game', game_time date, " +
            "       (SELECT sum(p.amount) FROM " +
            "           (SELECT amount, payment_date, user_id FROM payment UNION SELECT bid, game_time, user_id FROM game) p " +
            "       WHERE payment_date <= date AND user_id = ?) " +
            "  FROM game WHERE user_id = ?";

    private static SqlPaymentDao methodLevelPaymentDao = new SqlPaymentDao();
    private SqlPaymentDao() {
    }

    SqlPaymentDao(SqlInitializer initializer) {
        super(initializer.getConnection());
        initializer.addDao(this);
    }

    public static SqlPaymentDao getMethodLevelPaymentDao() {
        return methodLevelPaymentDao;
    }
    public boolean  create (Payment payment) throws DaoException {
        Connection cn = getConnection();
        try (PreparedStatement ps = cn.prepareStatement(SQL_ADD_PAYMENT)){
            ps.setInt(1, payment.getUserId());
            ps.setBigDecimal(2, payment.getAmount());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    @Override
    public List<BalanceOperation> findUserOperations(int userID) throws DaoException {
        Connection cn = getConnection();
        try (PreparedStatement ps = cn.prepareStatement(SQL_FIND_USER_OPERATIONS)){
            for (int i = 1; i <= 4; i++) {
                ps.setInt(i, userID);
            }
            return buildOperations(ps.executeQuery());
        } catch (SQLException e) {
            throw new DaoException("Problem with preparing statement", e);
        } finally {
            releaseConnectionIfLocal(cn);
        }
    }

    private List<BalanceOperation> buildOperations (ResultSet rs) throws SQLException {
        List<BalanceOperation> operations = new ArrayList<>();
        while (rs.next()) {
            BalanceOperation operation = new BalanceOperation();
            operation.setAmount(rs.getBigDecimal(DAO_AMOUNT));
            operation.setBalance(rs.getBigDecimal(DAO_BALANCE));
            operation.setType(rs.getString(DAO_TYPE_OF_OPERATION));
            operation.setDate(rs.getTimestamp(DAO_DATE));
            operations.add(operation);
        }
        return operations;
    }
}
