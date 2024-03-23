package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.common.util.CheckUtils;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.exception.BizException;
import com.yunqi.backend.exception.message.CommonError;
import com.yunqi.backend.mapper.SupplierMapper;
import com.yunqi.backend.model.dto.SupplierDTO;
import com.yunqi.backend.model.entity.Supplier;
import com.yunqi.backend.service.SupplierService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 供应商服务类
 * @author liyunqi
 */
@Service
@Transactional
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements SupplierService {

    @Resource
    SupplierMapper supplierMapper;

    @Override
    public Page<Supplier> getSupplierPage(SupplierDTO supplierDTO) {
        Page<Supplier> page = PageUtils.getPage();
        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(supplierDTO.getName() != null, Supplier::getName, supplierDTO.getName());
        wrapper.like(supplierDTO.getNickname() != null, Supplier::getNickname, supplierDTO.getNickname());
        wrapper.like(supplierDTO.getPhone() != null, Supplier::getPhone, supplierDTO.getPhone());
        return supplierMapper.selectPage(page, wrapper);
    }

    @Override
    public void saveSupplier(SupplierDTO supplierDTO) {
        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(supplierDTO, supplier);

        // 校验手机号
        if (CheckUtils.checkPhone(supplier.getPhone())) {
            throw new BizException(CommonError.PHONE_ERROR);
        }

        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Supplier::getPhone, supplier.getPhone());
        if (supplierMapper.selectCount(wrapper) > 0) {
            throw new BizException(CommonError.PHONE_REPEAT);
        }

        supplierMapper.insert(supplier);
    }

    @Override
    public void updateSupplier(SupplierDTO supplierDTO) {
        Supplier supplier = new Supplier();
        BeanUtils.copyProperties(supplierDTO, supplier);
        // 校验手机号
        if (CheckUtils.checkPhone(supplier.getPhone())) {
            throw new BizException(CommonError.PHONE_ERROR);
        }

        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Supplier::getPhone, supplier.getPhone());
        Supplier temp = supplierMapper.selectOne(wrapper);
        if (temp != null && !temp.getPhone().equals(supplier.getPhone())) {
            throw new BizException(CommonError.PHONE_REPEAT);
        }
        supplierMapper.updateById(supplier);
    }
}
