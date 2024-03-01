package com.yunqi.backend.controller;

import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.SecurityUtils;
import com.yunqi.backend.model.dto.MenuDTO;
import com.yunqi.backend.model.entity.Menu;
import com.yunqi.backend.service.MenuService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单控制器
 * @author liyunqi
 */
@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Resource
    MenuService menuService;

    @GetMapping("/list")
    public Result listMenu(MenuDTO menuDTO) {
        Long userId = SecurityUtils.getLoginUser().getUserId();
        List<Menu> menuList = menuService.selectMenuList(menuDTO, userId);
        return Result.success(menuList);
    }

    /**
     * 新增菜单
     * @param menuDTO
     * @return
     */
    @PostMapping
    public Result saveMenu(@Validated @RequestBody MenuDTO menuDTO) {
        menuService.saveMenu(menuDTO);
        return Result.success();
    }

    /**
     * 新增菜单
     * @param menuDTO
     * @return
     */
    @PutMapping
    public Result updateMenu(@Validated @RequestBody MenuDTO menuDTO) {
        menuService.updateMenu(menuDTO);
        return Result.success();
    }

    @DeleteMapping
    public Result deleteMenu(@RequestBody List<Long> menuIds) {
        menuService.deleteMenu(menuIds);
        return Result.success();
    }

    @GetMapping("/{menuId}")
    public Result getMenu(@PathVariable Long menuId) {
        Menu menu = menuService.getById(menuId);
        return Result.success(menu);
    }
}
