package com.nowcoder.community;

import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.service.AlphaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)  // 帮助测试类读取CommunityApplication配置类
public class CommunityApplicationTests implements ApplicationContextAware {

	private ApplicationContext applicationContext;

//	@Test
//	public void contextLoads() {
//	}

	//ApplicationContext 就是Spring容器 最终继承与BeanFactory这一接口，BeanFactory是Spring 的顶层接口
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext; //当程序启动时，该方法会自动运行，那么 就会传入到成员变量applicationContext中
	}

	@Test
	public void testApplicationContext() {
		System.out.println(applicationContext);//org.springframework.web.context.support.GenericWebApplicationContext@4fa1c212 实现类的类名

		AlphaDao alphaDao = applicationContext.getBean(AlphaDao.class);
		//从容器中获取Bean 在其中传入Bean的类型 //
		//@Primary注解可以解决重名问题
		System.out.println(alphaDao.select());

		System.out.println(applicationContext.getBean("alphaHibernate",AlphaDao.class));
	}

	@Test
	public void testBeanManagement() {
		AlphaService alphaService = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);
	}

	@Test
	public void testBeanConfig() {
		SimpleDateFormat simpleDateFormat = applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(simpleDateFormat.format(new Date()));
	}

	@Autowired
	private AlphaDao alphaDao;

	@Autowired
	private AlphaService alphaService;

	@Autowired
	private SimpleDateFormat simpleDateFormat;

	@Test
	public void TestDI() {
		System.out.println(applicationContext);
		System.out.println(alphaService);
		System.out.println(simpleDateFormat);
		System.out.println(alphaDao);
	}
}
