package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.mp.service.UserService;
import com.itheima.mp.domain.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Test
    void testPage() {
        // 1.创建分页对象
        Page<User> page = new Page<>(2, 2);
        page.addOrder(new OrderItem("balance", false));
        // 2.调用mapper的分页查询方法
        userService.page(page);
        // 总条数
        System.out.println("total = " + page.getTotal());
        // 总页数
        System.out.println("pages = " + page.getPages());
        // 数据
        List<User> records = page.getRecords();
        records.forEach(System.out::println);
    }

    @Test
    void testQueryWrapper() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .select("id", "username", "phone")
                .like("username", "o")
                .ge("balance", 1000);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    void testLambdaQueryWrapper() {
        // LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<User>()
        //         .eq(User::getUsername, "Jack")
        //         .select(User::getId, User::getUsername, User::getPhone);
        // List<User> users = userMapper.selectList(lambdaQueryWrapper);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                    .eq(User::getUsername, "Jack")
                    .select(User::getId, User::getUsername, User::getPhone);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    void testUpdateWrapper() {
        // User user = new User();
        // user.setBalance(66666);
        // QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
        //         .eq("username", "Jack");
        // userMapper.update(user, queryWrapper);

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<User>()
                .set("balance", 99999)
                .in("id", 1, 2, 4);
        userMapper.update(null, updateWrapper);
    }

    @Test
    void testCustomWrapper() {
        // 1.准备自定义查询条件
        List<Long> ids = List.of(1L, 2L, 4L);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .in(User::getId, ids);
        // 2.调用mapper的自定义方法，直接传递Wrapper
        userMapper.deductBalanceByIds(200, wrapper);
    }

    @Test
    void testIService() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .like("username", "o");
        List<User> users = userService.list(queryWrapper);
        users.forEach(System.out::println);
    }

    // @Test
    // void testInsert() {
    //     User user = new User();
    //     user.setId(9L);
    //     user.setUsername("Luc12121y");
    //     user.setPassword("13123123");
    //     user.setPhone("18688990011");
    //     user.setBalance(22100);
    //     user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
    //     user.setCreateTime(LocalDateTime.now());
    //     user.setUpdateTime(LocalDateTime.now());
    //     userMapper.saveUser(user);
    // }
    //
    // @Test
    // void testSelectById() {
    //     User user = userMapper.selectById(9L);
    //     System.out.println("user = " + user);
    // }
    //
    //
    // @Test
    // void testQueryByIds() {
    //     List<User> users = userMapper.queryUserByIds(List.of(1L, 2L, 3L, 4L));
    //     users.forEach(System.out::println);
    // }
    //
    // @Test
    // void testUpdateById() {
    //     User user = new User();
    //     user.setId(5L);
    //     user.setBalance(20000);
    //     userMapper.updateUser(user);
    // }
    //
    // @Test
    // void testDeleteUser() {
    //     userMapper.deleteUser(5L);
    // }
}