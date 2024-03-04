package com.yunqi.backend.controller;

import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.SecurityUtils;
import com.yunqi.backend.model.dto.MenuDTO;
import com.yunqi.backend.model.entity.Menu;
import com.yunqi.backend.service.MenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单控制器
 *
 * @author liyunqi
 */
@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Resource
    MenuService menuService;

    /**
     * 获取所有的菜单
     *
     * @param menuDTO
     * @return
     */
    @GetMapping("/list")
    public Result listMenu(MenuDTO menuDTO) {
        Long userId = SecurityUtils.getLoginUser().getUserId();
        List<Menu> menuList = menuService.selectMenuList(menuDTO, userId);
        return Result.success(menuList);
    }

    /**
     * 新增菜单
     *
     * @param menuDTO
     * @return
     */
    @PostMapping
    public Result saveMenu(@Validated @RequestBody MenuDTO menuDTO) {
        menuService.saveMenu(menuDTO);
        return Result.success();
    }

    /**
     * 更新菜单
     *
     * @param menuDTO
     * @return
     */
    @PutMapping
    public Result updateMenu(@Validated @RequestBody MenuDTO menuDTO) {
        menuService.updateMenu(menuDTO);
        return Result.success();
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{menuId}")
    public Result remove(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return Result.success();
    }

    /**
     * 根据菜单id获取菜单信息
     *
     * @param menuId
     * @return
     */
    @GetMapping("/{menuId}")
    public Result getMenu(@PathVariable Long menuId) {
        Menu menu = menuService.getById(menuId);
        return Result.success(menu);
    }

    /**
     * 生成菜单的树选择器
     *
     * @param menuDTO
     * @return
     */
    @GetMapping("/treeselect")
    public Result treeselect(MenuDTO menuDTO) {
        List<Menu> menuList = menuService.selectMenuList(menuDTO, SecurityUtils.getLoginUser().getUserId());
        return Result.success(menuService.buildMenuTreeSelect(menuList));
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public Result roleMenuTreeselect(@PathVariable Long roleId) {
        List<Menu> menus = menuService.selectMenuList(SecurityUtils.getLoginUser().getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        map.put("menus", menuService.buildMenuTreeSelect(menus));
        return Result.success(map);
    }
}
