package com.yunqi.backend.model.dto;

import lombok.Data;

/**
 * 路由显示信息
 *
 * @author liyunqi
 */
@Data
public class MetaDTO {

    /**
     * 设置该路由在侧边栏和面包屑中展示的名字
     */
    private String title;

    /**
     * 设置该路由的图标，对应路径src/assets/icons/svg
     */
    private String icon;

    /**
     * 设置为true，则不会被 <keep-alive>缓存
     */
    private boolean noCache;

    /**
     * 内链地址（http(s)://开头）
     */
    private String link;

    public MetaDTO(String title, String icon) {
        this.title = title;
        this.icon = icon;
    }

    public MetaDTO(String title, String icon, String link) {
        this.title = title;
        this.icon = icon;
        this.link = link;
    }

}
