package com.yunqi.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunqi.backend.model.dto.DictTypeDTO;
import com.yunqi.backend.model.entity.DictType;

import java.util.List;

/**
 * @author liyunqi
 */
public interface DictTypeService extends IService<DictType> {

    Page<DictType> getDictTypePage(DictTypeDTO dictTypeDTO);

    List<DictType> getDictTypeAll();

    void saveDictType(DictTypeDTO dictTypeDTO);

    void updateDictType(DictTypeDTO dictTypeDTO);

    void deleteDictType(List<Long> ids);
}
