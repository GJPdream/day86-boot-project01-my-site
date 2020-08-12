package cn.itcast.dao;

import cn.itcast.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    /*@Select("select username,password from test")*/
    public User findAll();
}
