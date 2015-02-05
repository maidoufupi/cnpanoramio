package com.cnpanoramio.webapp.controller;

import com.cnpanoramio.domain.SocialUser;

/**
 * 第三方登录控制器
 *
 *
 * @Author:  anypossible.w@gmail.com
 * @Create_Date: 2/4/2015
 */
public abstract class AbstractSocialLoginController {



    protected SocialUser getOrCreateUser(SocialUser socialUser) {
        return null;
    }
}
