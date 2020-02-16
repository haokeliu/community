package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface DiscussPostMapper {

    //考虑查询 首页时不需要传入UserId 该方法是动态生产SQL语句
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    //查询一共多少个帖子，首页时不需要传入UserId
    //如果动态拼SQL 并且只有一个参数， 使用@Param 给参数取别名 <if>使用，必须加别名
    int selectDiscussPostRows(@Param("userId") int userId);
}
