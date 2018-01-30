package net.cheltsov.shtoss.dao.sql;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SqlUserDaoTest {

    @Test
    public void testGetMethodLevelUserDao() {
        SqlUserDao userDao = SqlUserDao.getMethodLevelUserDao();
        Assert.assertTrue(!userDao.isClassLevel() && !userDao.isClosed());
    }
}