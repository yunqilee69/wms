package com.yunqi.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
}
