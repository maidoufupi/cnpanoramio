package com.cnpanoramio.dao;

import com.cnpanoramio.domain.SocialUser;
import org.appfuse.dao.GenericDao;

/**
 * Created by tiwen.wang on 2/4/2015.
 */
public interface SocialUserDao extends GenericDao<SocialUser, Long> {

    public SocialUser persist(SocialUser socialUser);

}
