package com.nowcoder.community;

import com.nowcoder.community.entity.Message;
import com.nowcoder.community.service.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.nowcoder.community.util.CommunityConstant.TOPIC_FOLLOW;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)  // 帮助测试类读取CommunityApplication配置类
public class MessageTest {

    @Autowired
    private MessageService messageService;

    @Test
    public void findLatestNotice () {

        int noticeUnreadCount = messageService.findNoticeUnreadCount(153, null);
        int letterUnreadCount = messageService.findLetterUnreadCount(153, null);
        System.out.println("noticeUnreadCount: "+noticeUnreadCount);
        System.out.println("letterUnreadCount: "+letterUnreadCount);
    }
}
