package net.cheltsov.shtoss.dao;

import net.cheltsov.shtoss.dao.sql.SqlDaoFactory;

public class DaoManager {
    /**
     * @return implementation of AbstractDaoFactory
     */
    public static AbstractDaoFactory getDaoFactory() {
        return new SqlDaoFactory();
    }
}
