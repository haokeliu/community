package com.nowcoder.community.util;

public interface CommunityConstant {

    /**
     * 激活成功
     */
    int ACTIVATION_SUCESS = 0;

    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT = 1;


    /**
     * 激活失败
     */
    int ACTIVATION_FAILLURE = 2;

    /**
     * 默认状态的登录凭证超时时间
     */
    int DEFALUT_EXPIRED_SECONDS = 3600 * 12;

    /**
     * 记住状态下的登录凭证超时时间
     */
    int REMEMBERME_EXPIRED_SECONDS = 3600 * 24 *100;

}
