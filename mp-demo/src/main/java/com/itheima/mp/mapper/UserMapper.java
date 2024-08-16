package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.mp.domain.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("UPDATE user SET balance = balance - #{money} ${ew.customSqlSegment}")
    void deductBalanceByIds(@Param("money") int money, @Param("ew") LambdaQueryWrapper<User> wrapper);

    @Select("UPDATE user SET balance = balance - #{money} WHERE id = #{id}")
    void deductMoneyById(Long id, Integer money);

    // void saveUser(User user);
    //
    // void deleteUser(Long id);
    //
    // void updateUser(User user);
    //
    // User queryUserById(@Param("id") Long id);
    //
    // List<User> queryUserByIds(@Param("ids") List<Long> ids);
}
