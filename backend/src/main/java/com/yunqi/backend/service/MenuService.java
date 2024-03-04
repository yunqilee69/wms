package com.yunqi.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunqi.backend.model.dto.MenuDTO;
import com.yunqi.backend.model.dto.RouterDTO;
import com.yunqi.backend.model.dto.TreeSelectDTO;
import com.yunqi.backend.model.entity.Menu;

import java.util.List;
import java.util.Set;

/**
 * @author liyunqi
 */
public interface MenuService extends IService<Menu> {

    Set<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据用户id获取菜单
     * @param userId
     * @return
     */
    List<Menu> selectMenuTreeByUserId(Long userId);

    /**
     * 根据角色id获取菜单
     * @param roleId
     * @return
     */
    List<Long> selectMenuListByRoleId(Long roleId);

    /**
     * 构建前端路由所需要的菜单
     * @param menus
     * @return
     */
    List<RouterDTO> buildMenus(List<Menu> menus);

    /**
     * 根据用户id获取该用户所能才查看的菜单
     * @param userId
     * @return
     */
    List<Menu> selectMenuList(Long userId);

    /**
     * 根据用户id获取该用户所能才查看的菜单
     * @param userId
     * @return
     */
    List<Menu> selectMenuList(MenuDTO menuDTO, Long userId);

    /**
     * 新增菜单
     * @param menuDTO
     */
    void saveMenu(MenuDTO menuDTO);

    /**
     * 更新菜单
     * @param menuDTO
     */
    void updateMenu(MenuDTO menuDTO);

    void deleteMenu(Long menuId);

    /**
     * 将菜单转化为DTO中的上下级关系，
     * @param menuList
     * @return
     */
    List<Menu> buildMenuTree(List<Menu> menuList);

    /**
     * 构造树形选择器
     * @param menuList
     * @return
     */
    List<TreeSelectDTO> buildMenuTreeSelect(List<Menu> menuList);
}
