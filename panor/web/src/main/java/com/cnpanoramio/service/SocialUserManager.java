package com.cnpanoramio.service;

import com.cnpanoramio.domain.SocialUser;
import org.appfuse.service.GenericManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 第三方用户管理器
 *
 * @Author:  anypossible.w@gmail.com
 * @Create_Date: 2/4/2015
 */
@Service
@Transactional
public interface SocialUserManager extends GenericManager<SocialUser, Long> {

    /**
     * 查询或创建社会化登录用户
     *
     * @param socialUser
     * @return
     */
    public SocialUser getOrCreateUser(SocialUser socialUser);

}
