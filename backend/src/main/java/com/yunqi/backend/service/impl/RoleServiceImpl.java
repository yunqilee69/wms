package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.mapper.RoleMapper;
import com.yunqi.backend.model.entity.Role;
import com.yunqi.backend.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * @author liyunqi
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
}
