package com.yunqi.backend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yunqi.backend.common.result.PageResult;
import com.yunqi.backend.common.result.Result;
import com.yunqi.backend.common.util.DictUtils;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.common.util.RedisCache;
import com.yunqi.backend.model.dto.DictTypeDTO;
import com.yunqi.backend.model.entity.DictItem;
import com.yunqi.backend.model.entity.DictType;
import com.yunqi.backend.service.DictItemService;
import com.yunqi.backend.service.DictTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author liyunqi
 */
@RestController
@RequestMapping("/system/dict/type")
public class DictTypeController {

    @Resource
    DictTypeService dictTypeService;


    /**
     * 获取字典分页列表
     * @param dictType
     * @return
     */

    @PreAuthorize("@sps.hasPermi('system:dict:list')")
    @GetMapping("/list")
    public Result<PageResult> getDictTypePage(DictTypeDTO dictTypeDTO) {
        Page<DictType> dictTypePage = dictTypeService.getDictTypePage(dictTypeDTO);
        return Result.success(PageUtils.convertPageResult(dictTypePage));
    }

    /**
     * 根据id查询字典
     * @param dictTypeId
     * @return
     */

    @PreAuthorize("@sps.hasPermi('system:dict:query')")
    @GetMapping("/{dictTypeId}")
    public Result<DictType> getByDictTypeId(@PathVariable Long dictTypeId) {
        DictType dictType = dictTypeService.getById(dictTypeId);
        return Result.success(dictType);
    }

    /**
     * 新增字典类型
     * @param dictTypeDTO
     * @return
     */

    @PreAuthorize("@sps.hasPermi('system:dict:add')")
    @PostMapping
    public Result save(@Validated @RequestBody DictTypeDTO dictTypeDTO) {
        dictTypeService.saveDictType(dictTypeDTO);
        return Result.success();
    }

    /**
     * 更新
     * @param dictTypeDTO
     * @return
     */

    @PreAuthorize("@sps.hasPermi('system:dict:edit')")
    @PutMapping
    public Result update(@Validated @RequestBody DictTypeDTO dictTypeDTO) {
        dictTypeService.updateDictType(dictTypeDTO);
        return Result.success();
    }


    @PreAuthorize("@sps.hasPermi('system:dict:delete')")
    @DeleteMapping("/{dictTypeIds}")
    public Result delete(@PathVariable List<Long> dictTypeIds) {
        dictTypeService.deleteDictType(dictTypeIds);
        return Result.success();
    }

    /**
     * 刷新字典的redis缓存
     */

    @PreAuthorize("@sps.hasPermi('system:dict:refresh')")
    @DeleteMapping("/refreshCache")
    public Result refreshCache() {
        DictUtils.refreshCache();
        return Result.success();
    }

    /**
     * 获取字典选择框列表
     */
    @GetMapping("/optionselect")
    public Result optionselect() {
        List<DictType> dictTypeList = dictTypeService.getDictTypeAll();
        return Result.success(dictTypeList);
    }


}
