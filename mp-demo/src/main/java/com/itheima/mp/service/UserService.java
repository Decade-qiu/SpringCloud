package com.itheima.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.UserVO;

public interface UserService extends IService<User> {
    void deductBalance(Long id, Integer money);

    PageDTO<UserVO> queryUsersByPage(UserQuery userQuery);
}
