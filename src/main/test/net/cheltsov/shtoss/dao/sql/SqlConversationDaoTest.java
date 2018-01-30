package net.cheltsov.shtoss.dao.sql;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SqlConversationDaoTest {

    @Test
    public void testGetMethodLevelConversationDao() {
        SqlConversationDao conversationDao = SqlConversationDao.getMethodLevelConversationDao();
        Assert.assertTrue(!conversationDao.isClassLevel() && !conversationDao.isClosed());
    }
}