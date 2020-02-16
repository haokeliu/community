package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
@Scope("prototype")
public class AlphaService {

    @Autowired
    private AlphaDao alphaDao;

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
}
