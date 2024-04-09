package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.common.constant.SystemConstants;
import com.yunqi.backend.common.util.CheckUtils;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.common.util.RedisCache;
import com.yunqi.backend.common.util.SecurityUtils;
import com.yunqi.backend.exception.BizException;
import com.yunqi.backend.exception.message.UserError;
import com.yunqi.backend.mapper.UserMapper;
import com.yunqi.backend.model.dto.EmpDTO;
import com.yunqi.backend.model.dto.UserDTO;
import com.yunqi.backend.model.entity.User;
import com.yunqi.backend.model.entity.UserRole;
import com.yunqi.backend.model.request.RegisterRequest;
import com.yunqi.backend.service.UserRoleService;
import com.yunqi.backend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liyunqi
 */
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    RedisCache redisCache;

    @Autowired
    UserRoleService userRoleService;

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
        if (selectUserByUsername(username) != null) {
            throw new BizException(UserError.USERNAME_ALREADY_EXISTS);
        }

        // 插入数据
        User user = new User();
        BeanUtils.copyProperties(registerRequest, user);
        String encryptPassword = SecurityUtils.encryptPassword(user.getPassword());
        user.setPassword(encryptPassword);
        user.setStatus(SystemConstants.STATUS_VALID);
        userMapper.insert(user);

        return user.getId();
    }

    /**
     * 获取所有已分配角色的用户
     * @return
     */
    @Override
    public Page<User> selectAllocatedList(UserDTO userDTO, Long roleId) {
        Page<User> page = PageUtils.getPage();
        List<User> userList = userMapper.selectAllocatedList(userDTO, roleId);
        if (userList == null) {
            userList = new ArrayList<>();
        }
        PageUtils.handlePageList(userList, page);
        return page;
    }

    @Override
    public Page<User> selectUnallocatedList(UserDTO userDTO, Long roleId) {
        Page<User> page = PageUtils.getPage();
        List<User> userList = userMapper.selectUnallocatedList(userDTO, roleId);
        if (userList == null) {
            userList = new ArrayList<>();
        }
        PageUtils.handlePageList(userList, page);
        return page;
    }

    @Override
    public void updatePassword(UserDTO userDTO) {
        String oldPassword = userDTO.getOldPassword();
        String newPassword = userDTO.getNewPassword();
        String confirmPassword = userDTO.getConfirmPassword();

        if (StringUtils.isAnyEmpty(oldPassword, newPassword, confirmPassword)) {
            throw new BizException(UserError.USERNAME_OR_PASSWORD_EMPTY);
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new BizException(UserError.PASSWORD_NOT_MATCH);
        }

        Long userId = SecurityUtils.getLoginUser().getUserId();
        String password = userMapper.selectById(userId).getPassword();
        if (!SecurityUtils.matchesPassword(oldPassword, password)) {
            throw new BizException(UserError.OLD_PASSWORD_ERROR);
        }

        String encryptPassword = SecurityUtils.encryptPassword(newPassword);
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, userId);
        wrapper.set(User::getPassword, encryptPassword);
        userMapper.update(wrapper);
    }

    @Override
    public void updateProfile(UserDTO userDTO) {
        Long userId = SecurityUtils.getLoginUser().getUserId();

        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, userId);
        wrapper.set(userDTO.getEmail() != null,  User::getEmail, userDTO.getEmail());
        wrapper.set(userDTO.getNickname() != null,  User::getNickname, userDTO.getNickname());
        wrapper.set(userDTO.getPhone() != null,  User::getPhone, userDTO.getPhone());
        wrapper.set(userDTO.getGender() != null,  User::getGender, userDTO.getGender());

        userMapper.update(wrapper);
    }

    @Override
    public Page<EmpDTO> getEmpPage(EmpDTO empDTO) {
        Page<EmpDTO> page = PageUtils.getPage();
        List<EmpDTO> userList = userMapper.getEmpPage(empDTO);
        PageUtils.handlePageList(userList, page);
        return page;
    }

    @Override
    public User selectUserById(Long userId) {
        return userMapper.selectById(userId);
    }


    @Override
    public void saveEmp(EmpDTO empDTO) {
        if (StringUtils.isAnyEmpty(empDTO.getUsername(), empDTO.getPassword())) {
            throw new BizException(UserError.USERNAME_OR_PASSWORD_EMPTY);
        }

        User user = new User();
        BeanUtils.copyProperties(empDTO, user);

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, empDTO.getUsername());
        if (userMapper.selectCount(wrapper) > 0) {
            throw new BizException(UserError.USERNAME_EXISTS);
        }

        // 对密码进行加密
        String encryptPassword = SecurityUtils.encryptPassword(user.getPassword());
        user.setPassword(encryptPassword);
        save(user);

        // 保存员工对应的角色
        List<Long> roleIds = empDTO.getRoleIds();
        List<UserRole> userRoleList = new ArrayList<>();
        for (Long roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setRoleId(roleId);
            userRole.setUserId(user.getId());
            userRoleList.add(userRole);
        }
        userRoleService.saveBatch(userRoleList);
    }

    @Override
    public void updateEmp(EmpDTO empDTO) {
        if (StringUtils.isAnyEmpty(empDTO.getUsername(), empDTO.getPassword())) {
            throw new BizException(UserError.USERNAME_OR_PASSWORD_EMPTY);
        }

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, empDTO.getUsername());
        User temp = userMapper.selectOne(wrapper);
        if (temp != null && temp.getId().longValue() != empDTO.getUserId().longValue()) {
            throw new BizException(UserError.USERNAME_EXISTS);
        }

        User user = new User();
        BeanUtils.copyProperties(empDTO, user);

        user.setId(empDTO.getUserId());
        updateById(user);

        // 更新员工对应的角色
        userRoleService.deleteUserRoleByUserId(user.getId());
        List<Long> roleIds = empDTO.getRoleIds();
        List<UserRole> userRoleList = new ArrayList<>();
        for (Long roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setRoleId(roleId);
            userRole.setUserId(user.getId());
            userRoleList.add(userRole);
        }
        userRoleService.saveBatch(userRoleList);
    }

    @Override
    public void deleteEmp(List<Long> userIds) {
        removeByIds(userIds);
        // 删除用户对应的角色
        userIds.forEach(item -> userRoleService.deleteUserRoleByUserId(item));
    }

    @Override
    public void resetPwd(Long userId, String password) {
        if (userId == null) {
            throw new BizException(UserError.RESET_PASSWORD_ERROR);
        }
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, userId);
        wrapper.set(User::getPassword, SecurityUtils.encryptPassword(password));
        userMapper.update(wrapper);
    }

}
