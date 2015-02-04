package com.cnpanoramio.dao.impl.hibernate;

import com.cnpanoramio.dao.SocialUserDao;
import com.cnpanoramio.domain.SocialUser;
import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.springframework.stereotype.Repository;

/**
 * 社会化登录用户
 *
 * @Author anypossible.w@gmail.com
 * @Create_Date 2/4/2015.
 */
@Repository
public class SocialUserDaoImpl extends GenericDaoHibernate<SocialUser, Long> implements SocialUserDao {

    public SocialUserDaoImpl() {
        super(SocialUser.class);
    }

    @Override
    public SocialUser persist(SocialUser socialUser) {

        getSession().persist(socialUser);

        return socialUser;
    }
}
