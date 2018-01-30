package net.cheltsov.shtoss.dao.sql;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SqlGameDaoTest {

    @Test
    public void testGetMethodLevelGameDao() {
        SqlGameDao gameDao = SqlGameDao.getMethodLevelGameDao();
        Assert.assertTrue(!gameDao.isClassLevel() && !gameDao.isClosed());
    }
}