package com.mkstu.score.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mkstu.score.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Mapper接口
 *
 * @author MKStu
 * @date 2024-01-01
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名和用户类型查询用户
     *
     * @param username 用户名
     * @param userType 用户类型
     * @return User
     */
    User selectByUsernameAndUserType(@Param("username") String username, @Param("userType") Integer userType);

    /**
     * 根据用户编号和用户类型查询用户
     *
     * @param userNo   用户编号
     * @param userType 用户类型
     * @return User
     */
    User selectByUserNoAndUserType(@Param("userNo") String userNo, @Param("userType") Integer userType);
} 