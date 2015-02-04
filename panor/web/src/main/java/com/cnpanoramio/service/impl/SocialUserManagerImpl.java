package com.cnpanoramio.service.impl;

import com.cnpanoramio.domain.SocialUser;
import com.cnpanoramio.service.SocialUserManager;
import org.appfuse.service.impl.GenericManagerImpl;

/**
 * 第三方用户管理器实现
 *
 * @Author:  anypossible.w@gmail.com
 * @Create_Date: 2/4/2015
 */
public class SocialUserManagerImpl extends GenericManagerImpl<SocialUser, Long> implements SocialUserManager {


    @Override
    public SocialUser getOrCreateUser(SocialUser socialUser) {
        return null;
    }
}
