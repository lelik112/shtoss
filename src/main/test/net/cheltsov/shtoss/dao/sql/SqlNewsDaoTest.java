package net.cheltsov.shtoss.dao.sql;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class SqlNewsDaoTest {

    @Test
    public void testGetMethodLevelNewsDao() throws Exception {
        SqlNewsDao newsDao = SqlNewsDao.getMethodLevelNewsDao();
        assertTrue(!newsDao.isClassLevel() && !newsDao.isClosed());
    }
}