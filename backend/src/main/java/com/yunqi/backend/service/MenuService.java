package com.yunqi.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yunqi.backend.model.dto.RouterDTO;
import com.yunqi.backend.model.entity.Menu;

import java.util.List;
import java.util.Set;

/**
 * @author liyunqi
 */
public interface MenuService extends IService<Menu> {

    Set<String> selectMenuPermsByUserId(Long userId);

    List<Menu> selectMenuTreeByUserId(Long userId);

    List<RouterDTO> buildMenus(List<Menu> menus);


}
