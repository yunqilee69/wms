package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.common.util.DictUtils;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.exception.BizException;
import com.yunqi.backend.exception.message.OrderError;
import com.yunqi.backend.mapper.OrderPurchaseDetailMapper;
import com.yunqi.backend.mapper.OrderPurchaseMapper;
import com.yunqi.backend.model.dto.OrderPurchaseDetailDTO;
import com.yunqi.backend.model.entity.OrderPurchase;
import com.yunqi.backend.model.entity.OrderPurchaseDetail;
import com.yunqi.backend.model.entity.Record;
import com.yunqi.backend.model.entity.Ware;
import com.yunqi.backend.service.OrderPurchaseDetailService;
import com.yunqi.backend.service.OrderPurchaseService;
import com.yunqi.backend.service.RecordService;
import com.yunqi.backend.service.WareService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 订单采购细节服务实现类
 * @author liyunqi
 */
@Service
@Transactional
public class OrderPurchaseDetailServiceImpl extends ServiceImpl<OrderPurchaseDetailMapper, OrderPurchaseDetail> implements OrderPurchaseDetailService {

    @Resource
    OrderPurchaseService orderPurchaseService;

    @Resource
    OrderPurchaseDetailMapper orderPurchaseDetailMapper;

    @Resource
    OrderPurchaseMapper orderPurchaseMapper;

    @Resource
    RecordService recordService;

    @Resource
    WareService wareService;

    @Override
    public Page<OrderPurchaseDetail> getOrderPurchaseDetailPage(OrderPurchaseDetailDTO orderPurchaseDetailDTO) {
        if (orderPurchaseDetailDTO.getOrderId() == null) {
            throw new BizException(OrderError.ORDER_ID_IS_EMPTY);
        }
        Page<OrderPurchaseDetail> page = PageUtils.getPage();
        LambdaQueryWrapper<OrderPurchaseDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderPurchaseDetail::getOrderId, orderPurchaseDetailDTO.getOrderId());
        wrapper.like(orderPurchaseDetailDTO.getWareBarCode() != null, OrderPurchaseDetail::getWareBarCode, orderPurchaseDetailDTO.getWareBarCode());
        wrapper.like(orderPurchaseDetailDTO.getWareName() != null, OrderPurchaseDetail::getWareName, orderPurchaseDetailDTO.getWareName());
        wrapper.like(orderPurchaseDetailDTO.getLocationName() != null, OrderPurchaseDetail::getLocationName, orderPurchaseDetailDTO.getLocationName());
        return orderPurchaseDetailMapper.selectPage(page, wrapper);
    }

    @Override
    public void addUnPurchaseRecord(Long orderId, Long[] recordIds, String detailType) {
        OrderPurchase orderPurchase = orderPurchaseMapper.selectById(orderId);
        if (!orderPurchase.getStatus().equals(DictUtils.getValueByLabel("已新建", "purchase_order_status"))) {
            throw new BizException(OrderError.ORDER_STATUS_IS_NOT_1);
        }

        // 获取所有的库存记录
        List<Record> records = recordService.getBaseMapper().selectBatchIds(Arrays.asList(recordIds));
        if (records == null || records.size() == 0) {
            throw new BizException(OrderError.FIND_RECORD_ERROR);
        }

        // 设置订单明细并保存
        for (Record record : records) {
            OrderPurchaseDetail t = new OrderPurchaseDetail();
            t.setOrderId(orderId);
            t.setWareId(record.getWareId());
            t.setRecordId(record.getId());
            t.setLocationName(record.getLocationName());
            Ware ware = wareService.getById(record.getWareId());
            t.setPicture(ware.getPicture());
            t.setWareName(ware.getName());
            t.setWareBrand(DictUtils.getLabelByValue(ware.getBrand(), "sys_inventory_brand"));
            t.setWareSpec(DictUtils.getLabelByValue(ware.getSpec(), "sys_inventory_spec"));
            t.setWareUnit(DictUtils.getLabelByValue(ware.getUnit(), "sys_inventory_unit"));
            t.setWareBarCode(ware.getBarCode());
            t.setWarePurchasePrice(ware.getPurchasePrice());
            t.setWareNumber(0);
            t.setType(DictUtils.getValueByLabel(detailType, "order_detail_type"));
            orderPurchaseDetailMapper.insert(t);
        }

        // 更新订单数据
        orderPurchaseService.updateOrderPurchaseData(orderId);
    }

    @Override
    public void setNumber(Long[] detailIds, int number) {
        if (detailIds.length == 0) {
            throw new BizException(OrderError.ORDER_DETAIL_ID_EMPTY);
        }

        OrderPurchaseDetail orderPurchaseDetail = orderPurchaseDetailMapper.selectById(detailIds[0]);
        Long orderId = orderPurchaseDetail.getOrderId();
        OrderPurchase orderPurchase = orderPurchaseMapper.selectById(orderId);
        if (!orderPurchase.getStatus().equals(DictUtils.getValueByLabel("已新建", "purchase_order_status"))) {
            throw new BizException(OrderError.ORDER_STATUS_IS_NOT_1);
        }

        LambdaUpdateWrapper<OrderPurchaseDetail> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(OrderPurchaseDetail::getWareNumber, number);
        updateWrapper.in(OrderPurchaseDetail::getId, detailIds);
        orderPurchaseDetailMapper.update(updateWrapper);

        // 更新订单数据
        orderPurchaseService.updateOrderPurchaseData(orderId);
    }

    @Override
    public void deleteByIds(List<Long> detailIds) {
        if (detailIds.size() == 0) {
            throw new BizException(OrderError.ORDER_DETAIL_ID_EMPTY);
        }
        
        OrderPurchaseDetail orderPurchaseDetail = orderPurchaseDetailMapper.selectById(detailIds.get(0));
        Long orderId = orderPurchaseDetail.getOrderId();
        OrderPurchase orderPurchase = orderPurchaseMapper.selectById(orderId);
        if (!orderPurchase.getStatus().equals(DictUtils.getValueByLabel("已新建", "purchase_order_status"))) {
            throw new BizException(OrderError.ORDER_STATUS_IS_NOT_1);
        }
        orderPurchaseDetailMapper.deleteBatchIds(detailIds);

        // 更新订单数据
        orderPurchaseService.updateOrderPurchaseData(orderId);
    }
    

}
