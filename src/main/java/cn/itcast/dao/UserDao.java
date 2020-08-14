package cn.itcast.dao;

import cn.itcast.dto.cond.UserCond;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserDao {

    UserCond findByUser(String username);
}
