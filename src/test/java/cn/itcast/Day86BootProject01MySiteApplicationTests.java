package cn.itcast;

import cn.itcast.dao.MetaDao;
import cn.itcast.dao.UserMapper;
import cn.itcast.dto.cond.MetaCond;
import cn.itcast.model.MetaDomain;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class Day86BootProject01MySiteApplicationTests {
@Autowired
    UserMapper userMapper;
@Autowired
MetaDao metaDao;
    @Test
    void contextLoads() {
        System.out.println(123);
        System.out.println(userMapper.findAll());
    }

    @Test
    public void fun()
    {
        MetaCond metaCond = new MetaCond();
        metaCond.setType("category");
        metaCond.setName("java");
        List<MetaDomain> metasByCond = metaDao.getMetasByCond(metaCond);
        System.out.println(metasByCond);
    }

}
