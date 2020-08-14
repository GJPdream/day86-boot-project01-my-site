package cn.itcast.service.user.impl;

import cn.itcast.constant.ErrorContant;
import cn.itcast.dao.UserDao;
import cn.itcast.dto.cond.UserCond;
import cn.itcast.exception.BusinessException;
import cn.itcast.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*@Service
public class UserServiceImpl implements UserService {
   @Autowired
    UserDao userDao;
   boolean flag= false;
    @Override
    public UserCond findByUser(UserCond userCond) {
       if (userCond==null)
           throw BusinessException.withErrorCode(ErrorContant.Common.PARAM_IS_EMPTY);
        UserCond byUser = userDao.findByUser(userCond);
        if (byUser==null)
        {
            return null;
        }
        return byUser;
    }
}*/
