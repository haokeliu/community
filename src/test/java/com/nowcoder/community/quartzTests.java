package com.nowcoder.community;

import net.minidev.json.JSONUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)  // 帮助测试类读取CommunityApplication配置类
public class quartzTests {

    @Autowired
    private Scheduler scheduler;

    @Test
    public void testDeleteJob() {
        boolean result = false;
        try {
            result = scheduler.deleteJob(new JobKey("alphaJob","alphaJobGroup"));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        System.out.println(result);
    }
}
