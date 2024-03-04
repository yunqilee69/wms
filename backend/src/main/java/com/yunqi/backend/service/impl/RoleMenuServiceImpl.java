package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.mapper.RoleMenuMapper;
import com.yunqi.backend.model.entity.RoleMenu;
import com.yunqi.backend.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * @author liyunqi
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {
}
