package com.yunqi.backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yunqi.backend.model.dto.SupplierDTO;
import com.yunqi.backend.model.entity.Supplier;

/**
 * 供应商服务类
 * @author liyunqi
 */
public interface SupplierService extends IService<Supplier> {

    /**
     * 分页查询
     * @param SupplierDTO
     * @return
     */
    Page<Supplier> getSupplierPage(SupplierDTO SupplierDTO);

    /**
     * 新增
     * @param SupplierDTO
     */
    void saveSupplier(SupplierDTO SupplierDTO);

    /**
     * 更新
     * @param SupplierDTO
     */
    void updateSupplier(SupplierDTO SupplierDTO);
}
