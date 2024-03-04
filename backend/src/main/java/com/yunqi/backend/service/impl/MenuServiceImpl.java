package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.common.constant.MenuConstants;
import com.yunqi.backend.common.constant.UserConstants;
import com.yunqi.backend.common.util.SecurityUtils;
import com.yunqi.backend.mapper.MenuMapper;
import com.yunqi.backend.mapper.RoleMenuMapper;
import com.yunqi.backend.model.dto.MenuDTO;
import com.yunqi.backend.model.dto.MetaDTO;
import com.yunqi.backend.model.dto.RouterDTO;
import com.yunqi.backend.model.dto.TreeSelectDTO;
import com.yunqi.backend.model.entity.Menu;
import com.yunqi.backend.model.entity.Role;
import com.yunqi.backend.model.entity.RoleMenu;
import com.yunqi.backend.service.MenuService;
import com.yunqi.backend.service.RoleMenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liyunqi
 */
@Service
@Transactional
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Resource
    MenuMapper menuMapper;

    @Resource
    RoleMenuMapper roleMenuMapper;

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
            // TODO 完成普通用户的返回菜单
        }

        return getChildPerms(menuList, 0L);
    }

    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, roleId);
        List<RoleMenu> roleMenuList = roleMenuMapper.selectList(wrapper);
        return roleMenuList.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
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

    @Override
    public List<Menu> selectMenuList(Long userId) {
        return selectMenuList(new MenuDTO(), userId);
    }

    /**
     * 根据用户id获取该用户所能才查看的菜单
     *
     * @param userId
     * @return
     */
    @Override
    public List<Menu> selectMenuList(MenuDTO menuDTO, Long userId) {
        List<Menu> menuList = null;
        // 管理员显示所有菜单信息
        if (SecurityUtils.isAdmin(userId)) {
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.like(menuDTO.getName() != null, Menu::getName, menuDTO.getName());
            wrapper.eq(menuDTO.getStatus() != null, Menu::getStatus, menuDTO.getStatus());
            // 查找全部的菜单
            menuList = menuMapper.selectList(wrapper);
        } else {
            // 根据用户id去查找
            menuList = menuMapper.selectMenuListByUserId(menuDTO, userId);
        }
        return menuList;
    }

    @Override
    public void saveMenu(MenuDTO menuDTO) {
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuDTO, menu);
        // TODO  完成校验，路由名称必须是唯一的

        save(menu);

    }

    @Override
    public void updateMenu(MenuDTO menuDTO) {
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuDTO, menu);
        // TODO  完成校验，路由名称必须是唯一的

        updateById(menu);
    }

    @Override
    public void deleteMenu(List<Long> menuIds) {
        removeBatchByIds(menuIds);
    }

    @Override
    public List<Menu> buildMenuTree(List<Menu> menuList) {
        List<Menu> returnList = new ArrayList<>();
        List<Long> tempList = menuList.stream().map(Menu::getId).collect(Collectors.toList());
        for (Iterator<Menu> iterator = menuList.iterator(); iterator.hasNext(); ) {
            Menu menu = (Menu) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId())) {
                recursionFn(menuList, menu);

                returnList.add(menu);
            }
        }
        if (returnList.isEmpty()) {
            returnList = menuList;
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list 分类表
     * @param t    子节点
     */
    private void recursionFn(List<Menu> list, Menu t) {
        // 得到子节点列表
        List<Menu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (Menu tChild : childList) {
            recursionFn(list, tChild);
        }
    }

    /**
     * 得到子节点列表
     */
    private List<Menu> getChildList(List<Menu> list, Menu t) {
        List<Menu> tlist = new ArrayList<>();
        Iterator<Menu> it = list.iterator();
        while (it.hasNext()) {
            Menu n = (Menu) it.next();
            if (n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    @Override
    public List<TreeSelectDTO> buildMenuTreeSelect(List<Menu> menuList) {
        List<Menu> menuTrees = buildMenuTree(menuList);
        return menuTrees.stream().map(TreeSelectDTO::new).collect(Collectors.toList());
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
        // 使用路由路径当作路由名称
        return StringUtils.capitalize(menu.getRouterPath());
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
