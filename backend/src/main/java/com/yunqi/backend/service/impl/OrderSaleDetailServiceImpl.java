package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.common.util.DictUtils;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.exception.BizException;
import com.yunqi.backend.exception.message.OrderError;
import com.yunqi.backend.mapper.OrderSaleDetailMapper;
import com.yunqi.backend.mapper.OrderSaleMapper;
import com.yunqi.backend.model.dto.OrderSaleDetailDTO;
import com.yunqi.backend.model.entity.OrderSale;
import com.yunqi.backend.model.entity.OrderSaleDetail;
import com.yunqi.backend.model.entity.Record;
import com.yunqi.backend.model.entity.Ware;
import com.yunqi.backend.service.OrderSaleDetailService;
import com.yunqi.backend.service.OrderSaleService;
import com.yunqi.backend.service.RecordService;
import com.yunqi.backend.service.WareService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * 订单销售细节服务实现类
 * @author liyunqi
 */
@Service
@Transactional
public class OrderSaleDetailServiceImpl extends ServiceImpl<OrderSaleDetailMapper, OrderSaleDetail> implements OrderSaleDetailService {

    @Resource
    OrderSaleService orderSaleService;

    @Resource
    OrderSaleDetailMapper orderSaleDetailMapper;

    @Resource
    OrderSaleMapper orderSaleMapper;

    @Resource
    RecordService recordService;

    @Resource
    WareService wareService;

    @Override
    public Page<OrderSaleDetail> getOrderSaleDetailPage(OrderSaleDetailDTO orderSaleDetailDTO) {
        if (orderSaleDetailDTO.getOrderId() == null) {
            throw new BizException(OrderError.ORDER_ID_IS_EMPTY);
        }
        Page<OrderSaleDetail> page = PageUtils.getPage();
        LambdaQueryWrapper<OrderSaleDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderSaleDetail::getOrderId, orderSaleDetailDTO.getOrderId());
        wrapper.like(orderSaleDetailDTO.getWareBarCode() != null, OrderSaleDetail::getWareBarCode, orderSaleDetailDTO.getWareBarCode());
        wrapper.like(orderSaleDetailDTO.getWareName() != null, OrderSaleDetail::getWareName, orderSaleDetailDTO.getWareName());
        wrapper.like(orderSaleDetailDTO.getLocationName() != null, OrderSaleDetail::getLocationName, orderSaleDetailDTO.getLocationName());
        return orderSaleDetailMapper.selectPage(page, wrapper);
    }

    @Override
    public void addUnSaleRecord(Long orderId, Long[] recordIds, String detailType) {
        OrderSale orderSale = orderSaleMapper.selectById(orderId);
        if (!orderSale.getStatus().equals(DictUtils.getValueByLabel("已新建", "sale_order_status"))) {
            throw new BizException(OrderError.ORDER_STATUS_IS_NOT_1);
        }

        // 获取所有的库存记录
        List<Record> records = recordService.getBaseMapper().selectBatchIds(Arrays.asList(recordIds));
        if (records == null || records.size() == 0) {
            throw new BizException(OrderError.FIND_RECORD_ERROR);
        }

        // 设置订单明细并保存
        for (Record record : records) {
            OrderSaleDetail t = new OrderSaleDetail();
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
            t.setWareSalePrice(ware.getSalePrice());
            t.setWarePurchasePrice(ware.getPurchasePrice());
            t.setWareNumber(0);
            t.setType(DictUtils.getValueByLabel(detailType, "order_detail_type"));
            orderSaleDetailMapper.insert(t);
        }

        // 更新订单数据
        orderSaleService.updateOrderSaleData(orderId);
    }

    @Override
    public void setNumber(Long[] detailIds, int number) {
        if (detailIds.length == 0) {
            throw new BizException(OrderError.ORDER_DETAIL_ID_EMPTY);
        }

        OrderSaleDetail orderSaleDetail = orderSaleDetailMapper.selectById(detailIds[0]);
        Long orderId = orderSaleDetail.getOrderId();
        OrderSale orderSale = orderSaleMapper.selectById(orderId);
        if (!orderSale.getStatus().equals(DictUtils.getValueByLabel("已新建", "sale_order_status"))) {
            throw new BizException(OrderError.ORDER_STATUS_IS_NOT_1);
        }

        LambdaUpdateWrapper<OrderSaleDetail> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(OrderSaleDetail::getWareNumber, number);
        updateWrapper.in(OrderSaleDetail::getId, detailIds);
        orderSaleDetailMapper.update(updateWrapper);

        // 更新订单数据
        orderSaleService.updateOrderSaleData(orderId);
    }

    @Override
    public void deleteByIds(List<Long> detailIds) {
        if (detailIds.size() == 0) {
            throw new BizException(OrderError.ORDER_DETAIL_ID_EMPTY);
        }
        
        OrderSaleDetail orderSaleDetail = orderSaleDetailMapper.selectById(detailIds.get(0));
        Long orderId = orderSaleDetail.getOrderId();
        OrderSale orderSale = orderSaleMapper.selectById(orderId);
        if (!orderSale.getStatus().equals(DictUtils.getValueByLabel("已新建", "sale_order_status"))) {
            throw new BizException(OrderError.ORDER_STATUS_IS_NOT_1);
        }
        orderSaleDetailMapper.deleteBatchIds(detailIds);

        // 更新订单数据
        orderSaleService.updateOrderSaleData(orderId);
    }

    @Override
    public void setAmount(Long[] detailIds, BigDecimal amount) {
        if (detailIds.length == 0) {
            throw new BizException(OrderError.ORDER_DETAIL_ID_EMPTY);
        }

        OrderSaleDetail orderSaleDetail = orderSaleDetailMapper.selectById(detailIds[0]);
        Long orderId = orderSaleDetail.getOrderId();
        OrderSale orderSale = orderSaleMapper.selectById(orderId);
        if (!orderSale.getStatus().equals(DictUtils.getValueByLabel("已新建", "sale_order_status"))) {
            throw new BizException(OrderError.ORDER_STATUS_IS_NOT_1);
        }

        LambdaUpdateWrapper<OrderSaleDetail> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(OrderSaleDetail::getWareSalePrice, amount);
        updateWrapper.in(OrderSaleDetail::getId, detailIds);
        orderSaleDetailMapper.update(updateWrapper);

        // 更新订单数据
        orderSaleService.updateOrderSaleData(orderId);
    }


}
