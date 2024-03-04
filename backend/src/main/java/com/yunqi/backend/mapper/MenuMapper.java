package com.yunqi.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunqi.backend.model.dto.MenuDTO;
import com.yunqi.backend.model.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liyunqi
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    public List<Menu> selectMenuTreeAll();

    /**
     * 获取菜单的子菜单，不包含按钮
     * @param pathCode
     * @return
     */
    public List<Menu> getMenuChildren(String pathCode);

    /**
     * 根据userId查找菜单
     * @param userId
     * @return
     */
    List<Menu> selectMenuListByUserId(MenuDTO menuDTO, Long userId);

    /**
     * 根据用户id获取该用户的树形菜单
     * @param userId
     * @return
     */
    List<Menu> selectMenuTreeByUserId(Long userId);
}
