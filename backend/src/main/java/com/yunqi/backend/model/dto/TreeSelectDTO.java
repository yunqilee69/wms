package com.yunqi.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yunqi.backend.model.entity.Menu;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 树形选择器DTO
 *
 * @author liyunqi
 */
@Data
public class TreeSelectDTO {
    /**
     * 节点ID
     */
    private Long id;

    /**
     * 节点名称
     */
    private String label;

    /**
     * 子节点
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelectDTO> children;

    public TreeSelectDTO(Menu menu) {
        this.id = menu.getId();
        this.label = menu.getName();
        this.children = menu.getChildren().stream().map(TreeSelectDTO::new).collect(Collectors.toList());
    }
}
