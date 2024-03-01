package com.yunqi.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunqi.backend.model.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

/**
 * @author liyunqi
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 通过userid获取该用户所有的roleKey
     * @param userId
     * @return
     */
    Set<String> getRolePermissionByUserId(Long userId);
}
