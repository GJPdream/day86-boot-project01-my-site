package cn.itcast.service.user;

import cn.itcast.dto.cond.UserCond;

public interface UserService {
    UserCond findByUser(UserCond userCond);
}
