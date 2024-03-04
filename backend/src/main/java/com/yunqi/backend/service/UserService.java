package com.yunqi.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunqi.backend.model.dto.RegisterUserDTO;
import com.yunqi.backend.model.dto.UserDTO;
import com.yunqi.backend.model.entity.User;
import com.yunqi.backend.model.request.RegisterRequest;

import java.util.List;

/**
 * @author liyunqi
 */
public interface UserService extends IService<User> {

    User selectUserByUsername(String username);

    Long registerUser(RegisterRequest registerRequest);

    Page<User> selectAllocatedList(UserDTO userDTO, Long roleId);

    Page<User> selectUnallocatedList(UserDTO userDTO);
}
