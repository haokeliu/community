package com.nowcoder.community.service;

import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import com.nowcoder.community.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class UserService implements CommunityConstant {

//    @Autowired
//    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public User findUserById(int id) {
        //return userMapper.selectById(id);
        User user = getCache(id);
        if (user == null) {
            user = initCache(id);
        }
        return user;
    }

    public Map<String, Object> register (User user) {
        Map<String, Object> map = new HashMap<>();

        //对空值处理
        if (user == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        if (StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())){
            map.put("emailMsg", "邮箱不能为空");
            return map;
        }
        // 验证账号是否存在
        User u = userMapper.selectByName(user.getUsername());
        if (u != null){
            map.put("usernameMsg", "该账户已存在");
            return map;
        }
        // 验证邮箱是否存在
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null){
            map.put("emailMsg", "该邮箱已存在");
            return map;
        }

        // 注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5(user.getPassword()+user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        //发送激活邮件
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        // url http://localhost:8080/community/activation/101/code
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url",url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活胡同账号", content);

        return map;
    }

    public int activation(int userId, String code) {
        User user = userMapper.selectById(userId);
        if (user == null){
            return ACTIVATION_FAILLURE;
        }
        if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        }else if (user.getActivationCode().equals(code)){
            userMapper.updateStatus(userId,1);
            clearCache(userId);
            return ACTIVATION_SUCESS;
        }else {
            return ACTIVATION_FAILLURE;
        }

    }

    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();

        //空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg","账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg","密码不能为空");
            return map;
        }

        //合法性
        //验证账号
        User user = userMapper.selectByName(username);
        if (user == null){
            map.put("usernameMsg","账号不存在");
            return map;
        }
        //验证状态
        if (user.getStatus() == 0){
            map.put("usernameMsg","账号未激活，请前往邮箱激活");
            return map;
        }

        //验证密码
        password = CommunityUtil.md5(password+user.getSalt());
        if ( !user.getPassword().equals(password)){
            map.put("passwordMsg","密码不正确");
            return map;
        }

        // 登录成功， 生成登陆凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));
        loginTicket.setStatus(0);

//        loginTicketMapper.insertLoginTicket(loginTicket);
        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        redisTemplate.opsForValue().set(redisKey, loginTicket);

        map.put("ticket",loginTicket.getTicket());
        return map;
    }

    public void logout(String ticket) {
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket)redisTemplate.opsForValue().get(redisKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(redisKey, loginTicket);
//        loginTicketMapper.updateStatus(ticket,1);
    }

    public Map<String, Object> sendPasswordEmail(String email, String confirmCode) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(email)){
            map.put("emailMsg", "邮箱不能为空");
            return map;
        }
        User user = userMapper.selectByEmail(email);
        if (user == null){
            map.put("emailMsg", "邮箱不存在");
            return map;
        }
        //发送激活邮件
        Context context = new Context();
        context.setVariable("email",user.getEmail());

        context.setVariable("confirmCode",confirmCode);
        String content = templateEngine.process("/mail/forget", context);
        mailClient.sendMail(user.getEmail(), "修改胡同密码", content);
        return map;
    }
    public Map<String, Object> updatePassword(String email, String password) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(email)){
            map.put("emailMsg", "邮箱不能为空");
            return map;
        }
        User user = userMapper.selectByEmail(email);
        if (user == null){
            map.put("emailMsg", "邮箱不存在");
            return map;
        }
        userMapper.updatePassword(user.getId(), CommunityUtil.md5(password + user.getSalt()));
        return map;
    }

    public LoginTicket findLoginTicket(String ticket) {
//        return loginTicketMapper.selectByTicket(ticket);
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket)redisTemplate.opsForValue().get(redisKey);
        return loginTicket;
    }

    public int updateHeader(int userId, String HeaderUrl) {
        int rows = userMapper.updateHeader(userId, HeaderUrl);
        clearCache(userId);
        return rows;
    }

    public int changePassword(int userId, String password) {
        User user = userMapper.selectById(userId);
        password = password + user.getSalt();
        password = CommunityUtil.md5(password);
        clearCache(userId);
        return userMapper.updatePassword(userId, password);
    }

    public Boolean vertifyPassword(int userId, String password) {
        User user = userMapper.selectById(userId);
        password = CommunityUtil.md5(password + user.getSalt());
        String oldPassword = user.getPassword();
        if (oldPassword.equals(password)){
            return true;
        }
        return false;
    }

    public User findUserByName(String username) {
        return userMapper.selectByName(username);
    }

    /**
     * 缓存
     * 1.优先从缓存中取值
     * 2.取不到，就初始化缓存数据
     * 3.数据变化时，清除缓存数据
     * * */
    //1.优先从缓存中取值
    private User getCache(int userId){
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User)redisTemplate.opsForValue().get(redisKey);
    }

    //2.取不到，就初始化缓存数据
    private User initCache(int userId) {
        User user = userMapper.selectById(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.opsForValue().set(redisKey, user, 36000, TimeUnit.SECONDS);
        return user;
    }

    //3.数据变化时，清除缓存数据
    private void clearCache(int userId) {
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }
}
