package com.yunqi.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunqi.backend.model.dto.EmpDTO;
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

    /**
     * 查询未分配用户角色列表
     * @param userDTO
     * @return
     */
    Page<User> selectUnallocatedList(UserDTO userDTO, Long roleId);

    /**
     * 更新密码
     * @param userDTO
     */
    void updatePassword(UserDTO userDTO);

    /**
     * 更新用户信息
     * @param userDTO
     */
    void updateProfile(UserDTO userDTO);

    /**
     * 获取员工管理的分页数据
     * @param userDTO
     * @return
     */
    Page<EmpDTO> getEmpPage(EmpDTO empDTO);

    /**
     * 根据id获取用户
     * @param userId
     * @return
     */
    User selectUserById(Long userId);

    /**
     * 新增员工
     * @param empDTO
     */
    void saveEmp(EmpDTO empDTO);

    /**
     * 更新员工
     * @param empDTO
     */
    void updateEmp(EmpDTO empDTO);

    /**
     * 根据用户id删除员工
     * @param userId
     */
    void deleteEmp(List<Long> userId);

    /**
     * 重置密码
     * @param userId
     * @param password
     */
    void resetPwd(Long userId, String password);
}
