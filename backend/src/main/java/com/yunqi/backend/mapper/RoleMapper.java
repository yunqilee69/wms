package com.yunqi.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunqi.backend.model.dto.RoleOptionDTO;
import com.yunqi.backend.model.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author liyunqi
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 通过userid获取该用户所有的roleName
     * @param id
     * @return
     */
    Set<String> getRoleNameByUserId(Long userId);

    /**
     * 通过userid获取该用户所有的roleKey
     * @param userId
     * @return
     */
    Set<String> getRoleKeyByUserId(Long userId);

    Role selectByName(String name);

    Role selectByRoleKey(String roleKey);

    // 获取角色下拉选项
    List<RoleOptionDTO> getRoleoptions();
}
