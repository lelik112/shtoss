package net.cheltsov.shtoss.dao.sql;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class SqlPaymentDaoTest {

    @Test
    public void testGetMethodLevelPaymentDao() throws Exception {
        SqlPaymentDao paymentDao = SqlPaymentDao.getMethodLevelPaymentDao();
        assertTrue(!paymentDao.isClassLevel() && !paymentDao.isClosed());
    }
}