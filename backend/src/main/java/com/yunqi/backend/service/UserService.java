package com.yunqi.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunqi.backend.model.dto.RegisterUserDTO;
import com.yunqi.backend.model.entity.User;
import com.yunqi.backend.model.request.RegisterRequest;

/**
 * @author liyunqi
 */
public interface UserService extends IService<User> {

    User selectUserByUsername(String username);

    Long registerUser(RegisterRequest registerRequest);

    User getUserByUsername(String username);
}
