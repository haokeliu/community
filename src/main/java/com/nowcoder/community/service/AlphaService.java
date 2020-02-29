package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;

@Service
@Scope("prototype")
public class AlphaService {

    private static final Logger logger = LoggerFactory.getLogger(AlphaService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private AlphaDao alphaDao;

    @Autowired
    private TransactionTemplate transactionTemplate;

    public String find() {
        return alphaDao.select();
    }

//    public AlphaService() {
//        System.out.println("实例化 AlphaService");
//    }

//    @PostConstruct
//    //告诉容器 初始化之后调用 post Construct
//    public void init() {
//        System.out.println("初始化 AlphaService");
//    }

//    @PreDestroy
    //告诉容器 销毁对象之前调用 Pre Destroy
//    public void destory() {
//        System.out.println("销毁对象 AlphaService");
//    }

    /***
     * REQUIRED: 支持当前事务（外部事务），如果不存在则创建新事务
     * REQUIRES_NEW： 创建新事务，并且暂停当前事务（外部事务）
     * NESTED：如果当前存在事务（外部事务），嵌套在该事务中执行（独立的提交和回滚），如果不存在就和Required
     * */

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Object save1 () {
        User user = new User();
        user.setUsername("alpha");
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5("123"+user.getSalt()));
        user.setEmail("alpha@qq.com");
        user.setHeaderUrl("https://images.nowcoder.com/head/521m.png");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 新增帖子
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle("hello");
        post.setContent("新人报道");
        post.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(post);

        Integer.valueOf("abc");

        return "ok";
    }

    public Object save2() {
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        return transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                User user = new User();
                user.setUsername("alpha");
                user.setSalt(CommunityUtil.generateUUID().substring(0,5));
                user.setPassword(CommunityUtil.md5("123"+user.getSalt()));
                user.setEmail("alpha@qq.com");
                user.setHeaderUrl("https://images.nowcoder.com/head/521m.png");
                user.setCreateTime(new Date());
                userMapper.insertUser(user);

                // 新增帖子
                DiscussPost post = new DiscussPost();
                post.setUserId(user.getId());
                post.setTitle("hello");
                post.setContent("新人报道");
                post.setCreateTime(new Date());
                discussPostMapper.insertDiscussPost(post);

                Integer.valueOf("abc");
                return "ok";
            }

        });
    }

    // 可以让该方法，在多线程环境下被异步调用.
//    @Async
    public void execite1() {
        logger.debug("execute1");
    }

//    @Scheduled(initialDelay = 10000, fixedRate = 1000)
    public void execute2() {
        logger.debug("execute2");
    }
}
