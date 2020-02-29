# community
A small community prototype

#### 介绍
基于Spring Boot2.X的论坛系统，适合于学习Spring Boot开发、项目毕业设计以及研发团队快速搭建项目，可作为开发项目的脚手架，作为基础项目。

#### 软件技术
1. Spring Boot 2.1.4
2. Spring Security 5.1.5
3. Mybatis 3.5.1
4. kafka 2.12
5. Redis 3.2
6. elasticsearch 6.4.3
7. fastjson 1.2.58
8. caffeine 2.7.0
9. Thymeleaf 3.0.11
10. Layui 2.5.3
11. MySQL 8.0.16

#### 内置功能
1. 用户管理：用户查询、添加用户、修改用户、用户角色设置、删除用户；
2. 发帖、点赞、评论、回复、私信等功能；
3. 搜索帖子、热门帖子；
4. 登录、登出：基于Spring Security的认证和授权；
5. 使用Redis完成高频操作、使用Caffeine缓存热门贴；
6. 使用actuator监控项目运行；
7. 代码自动生成：根据数据表以及自定义模板自动生成html、controller、service、serviceImpl、dao、mapper.xml文件；
