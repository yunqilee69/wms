package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.common.constant.MenuConstants;
import com.yunqi.backend.common.constant.UserConstants;
import com.yunqi.backend.common.util.SecurityUtils;
import com.yunqi.backend.mapper.MenuMapper;
import com.yunqi.backend.model.dto.MetaDTO;
import com.yunqi.backend.model.dto.RouterDTO;
import com.yunqi.backend.model.entity.Menu;
import com.yunqi.backend.service.MenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liyunqi
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Resource
    MenuMapper menuMapper;

    @Override
    public Set<String> selectMenuPermsByUserId(Long userId) {
        return null;
    }

    @Override
    public List<Menu> selectMenuTreeByUserId(Long userId) {
        List<Menu> menuList = null;
        // 管理员直接返回全部菜单
        if (SecurityUtils.getLoginUser().getUsername().equals("admin")) {
            menuList = menuMapper.selectMenuTreeAll();
        } else {

        }

        return getChildPerms(menuList, 0L);
    }

    @Override
    public List<RouterDTO> buildMenus(List<Menu> menus) {
        List<RouterDTO> routers = new LinkedList<>();
        for (Menu menu : menus) {
            RouterDTO router = new RouterDTO();
            router.setHidden(false);
            router.setName(getRouterName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQueryParams(menu.getQueryParams());
            router.setMeta(new MetaDTO(menu.getName(), menu.getIcon()));
            List<Menu> menuChildren = menuMapper.getMenuChildren(menu.getPathCode());
            if (menuChildren.size() > 0 && MenuConstants.TYPE_DIR.equals(menu.getMenuType())) {
                // 目录
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(menuChildren));

//            } else if (isMenuFrame(menu)) {
//                router.setMeta(null);
//                List<RouterVo> childrenList = new ArrayList<RouterVo>();
//                RouterVo children = new RouterVo();
//                children.setPath(menu.getPath());
//                children.setComponent(menu.getComponent());
//                children.setName(StringUtils.capitalize(menu.getPath()));
//                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
//                children.setQuery(menu.getQuery());
//                childrenList.add(children);
//                router.setChildren(childrenList);
            } else if (menu.getParentId().intValue() == 0) {
                router.setMeta(new MetaDTO(menu.getName(), menu.getIcon()));
                router.setPath("/");
                List<RouterDTO> childrenList = new ArrayList<>();
                RouterDTO children = new RouterDTO();
                String routerPath = menu.getRouterPath();
                children.setPath(routerPath);
                children.setComponent(UserConstants.INNER_LINK);
                children.setName(StringUtils.capitalize(routerPath));
                children.setMeta(new MetaDTO(menu.getName(), menu.getIcon(), menu.getRouterPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<Menu> getChildPerms(List<Menu> list, Long parentId) {
        return list.stream().filter(menu -> menu.getParentId().equals(parentId)).collect(Collectors.toList());
    }

    /**
     * 根据菜单获取，路由名称
     *
     * @param menu
     * @return
     */
    String getRouterName(Menu menu) {
        String routerName = StringUtils.capitalize(menu.getRouterPath());
        // 非外链并且是一级目录（类型为目录）
//        if (isMenuFrame(menu)) {
//            routerName = StringUtils.EMPTY;
//        }
        return routerName;
    }

    /**
     * 根据菜单获取路由路径
     *
     * @param menu
     * @return
     */
    String getRouterPath(Menu menu) {
        if (menu.getMenuType().equals(MenuConstants.TYPE_DIR)) {
            // 目录需要添加  /
            return "/" + menu.getRouterPath();
        } else if (menu.getMenuType().equals(MenuConstants.TYPE_MENU)) {
            // 菜单不需要进行添加
            return menu.getRouterPath();
        }

        return menu.getRouterPath();
    }

    /**
     * 根据菜单获取路由组件
     *
     * @param menu
     * @return
     */
    String getComponent(Menu menu) {
        if (StringUtils.isEmpty(menu.getComponent()) && menu.getMenuType().equals(MenuConstants.TYPE_DIR)) {
            return MenuConstants.LAYOUT;
        }
        return menu.getComponent();
    }

}
