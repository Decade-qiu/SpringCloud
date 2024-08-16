package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.enums.UserStatus;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.service.UserService;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService  {
    @Override
    @Transactional
    public void deductBalance(Long id, Integer money) {
        // 1.查询用户
        User user = getById(id);
        // 2.判断用户状态
        if (user == null || user.getStatus() == UserStatus.FREEZE) {
            throw new RuntimeException("用户状态异常");
        }
        // 3.判断用户余额
        if (user.getBalance() < money) {
            throw new RuntimeException("用户余额不足");
        }
        // 4.扣减余额
        int remainBalance = user.getBalance() - money;
        lambdaUpdate()
                .set(User::getBalance, remainBalance) // 更新余额
                .set(remainBalance == 0, User::getStatus, 2) // 动态判断，是否更新status
                .eq(User::getId, id)
                .eq(User::getBalance, user.getBalance()) // 乐观锁
                .update();
    }

    @Override
    public PageDTO<UserVO> queryUsersByPage(UserQuery query){
        // 1.构建条件
        Page<User> page = query.toMpPageDefaultSortByCreateTimeDesc();
        String username = query.getName();
        UserStatus status = query.getStatus();
        Page<User> users = lambdaQuery()
                .like(username != null, User::getUsername, username)
                .eq(status != null, User::getStatus, status)
                .page(page);
        return PageDTO.of(page, user -> {
            // 拷贝属性到VO
            UserVO vo = BeanUtil.copyProperties(user, UserVO.class);
            // 用户名脱敏
            String name = vo.getUsername();
            vo.setUsername(name.substring(0, name.length() - 2) + "**");
            return vo;
        });
    }

}
