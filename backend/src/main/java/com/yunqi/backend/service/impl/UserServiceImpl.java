package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.common.util.CheckUtils;
import com.yunqi.backend.common.util.RedisCache;
import com.yunqi.backend.common.util.SecurityUtils;
import com.yunqi.backend.core.service.TokenService;
import com.yunqi.backend.exception.BizException;
import com.yunqi.backend.exception.message.UserError;
import com.yunqi.backend.mapper.UserMapper;
import com.yunqi.backend.model.dto.RegisterUserDTO;
import com.yunqi.backend.model.entity.User;
import com.yunqi.backend.model.request.RegisterRequest;
import com.yunqi.backend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liyunqi
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    RedisCache redisCache;



    @Autowired
    private TokenService tokenService;



    /**
     * 根据username进行查找
     * @param username
     * @return
     */
    @Override
    public User selectUserByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return userMapper.selectOne(wrapper);
    }

    /**
     * 注册新用户
     * @param registerRequest
     */
    @Override
    public Long registerUser(RegisterRequest registerRequest) {
        // TODO 完成注册逻辑

        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();
        String confirmPassword = registerRequest.getConfirmPassword();
        String phone = registerRequest.getPhone();

        // 校验密码
        if (!password.equals(confirmPassword)) {
            throw new BizException(UserError.PASSWORD_NOT_MATCH);
        }

        // 校验手机号
        if (!CheckUtils.checkPhone(phone)) {
            throw new BizException(UserError.PHONE_NUMBER_ERROR);
        }

        // 校验数据库中是否存在该用户
        if (getUserByUsername(username) != null) {
            throw new BizException(UserError.USERNAME_ALREADY_EXISTS);
        }

        // 插入数据
        User user = new User();
        BeanUtils.copyProperties(registerRequest, user);
        String encryptPassword = SecurityUtils.encryptPassword(user.getPassword());
        user.setPassword(encryptPassword);
        userMapper.insert(user);

        return user.getId();
    }

    @Override
    public User getUserByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return  userMapper.selectOne(wrapper);
    }
}
