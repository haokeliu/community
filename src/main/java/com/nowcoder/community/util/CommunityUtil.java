package com.nowcoder.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class CommunityUtil {

    // 生成随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    //MD5加密
    // hello -> abc12345 不能解密 需要加盐
    // hello + 3e4ab -> abc12345eadkf
    public static String md5(String key) {
        if (StringUtils.isBlank(key)){
            return null;
        }
        //转换为16进制的 MD5码
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }



}
