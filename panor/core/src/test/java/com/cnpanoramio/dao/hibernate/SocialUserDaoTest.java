package com.cnpanoramio.dao.hibernate;

import com.cnpanoramio.dao.SocialUserDao;
import com.cnpanoramio.domain.SocialUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.dao.UserDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by tiwen.wang on 2/4/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath*:/applicationContext.xml" //"classpath*:/applicationContext-resources.xml",
})
@Transactional
public class SocialUserDaoTest {

    protected transient final Log log = LogFactory.getLog(getClass());

    @Autowired
    private SocialUserDao socialUserDao;

    @Autowired
    private UserDao userDao;

    @Before
    public void preMethodSetup() {

    }

    @After
    public void postMethodTearDown() {

    }

    @Test
    public void testCreateSocialUser() {
        SocialUser socialUser = new SocialUser();
        socialUser.setUser(userDao.get(1L));
        socialUser.setAccessToken("12334aoshdfoaij2");
        socialUser.setProviderId("qq2342342");
        socialUserDao.persist(socialUser);
    }
}
