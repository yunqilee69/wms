package com.yunqi.backend.core.service;

import java.util.HashSet;
import java.util.Set;

import com.yunqi.backend.model.entity.User;
import com.yunqi.backend.service.MenuService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * 用户权限处理
 *
 * @author ruoyi
 */
@Component
public class PermissionService {

    @Resource
    MenuService menuService;

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(User user) {
        Set<String> perms = new HashSet<String>();
        // 管理员拥有所有权限
        if (user.getUsername().equals("admin")) {
            perms.add("*:*:*");
        } else {
            Set<String> set = menuService.selectMenuPermsByUserId(user.getId());
            if (set == null) {
                set = new HashSet<>();
            }
            perms.addAll(set);
        }
        return perms;
    }
}
