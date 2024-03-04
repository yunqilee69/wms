package com.yunqi.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunqi.backend.model.dto.UserDTO;
import com.yunqi.backend.model.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

/**
 * @author liyunqi
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 获取所有已分配角色的用户
     * @return
     */
    List<User> selectAllocatedList(@Param("userDTO") UserDTO userDTO, @Param("roleId") Long roleId);

    /**
     * 获取所有未分配角色的用户
     * @param userDTO
     * @return
     */
    List<User> selectUnallocatedList(@Param("userDTO") UserDTO userDTO);


}
