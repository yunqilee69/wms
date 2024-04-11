package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.common.constant.DocumentConstants;
import com.yunqi.backend.common.util.DictUtils;
import com.yunqi.backend.common.util.DocumentUtils;
import com.yunqi.backend.common.util.PageUtils;
import com.yunqi.backend.common.util.SecurityUtils;
import com.yunqi.backend.exception.BizException;
import com.yunqi.backend.exception.message.OrderError;
import com.yunqi.backend.mapper.OrderPurchaseDetailMapper;
import com.yunqi.backend.mapper.OrderPurchaseMapper;
import com.yunqi.backend.model.dto.LoginUserDTO;
import com.yunqi.backend.model.dto.OrderPurchaseDTO;
import com.yunqi.backend.model.dto.SettlementDTO;
import com.yunqi.backend.model.entity.*;
import com.yunqi.backend.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单采购服务实现类
 * @author liyunqi
 */
@Service
@Transactional
public class OrderPurchaseServiceImpl extends ServiceImpl<OrderPurchaseMapper, OrderPurchase> implements OrderPurchaseService {

    @Resource
    OrderPurchaseMapper orderPurchaseMapper;

    @Resource
    OrderPurchaseDetailMapper orderPurchaseDetailMapper;

    @Resource
    OrderSettlementService orderSettlementService;

    @Resource
    SupplierService supplierService;

    @Resource
    RecordService recordService;

    @Resource
    WareService wareService;

    @Override
    public Page<OrderPurchase> getOrderPurchasePage(OrderPurchaseDTO orderPurchaseDTO) {
        Page<OrderPurchase> page = PageUtils.getPage();
        LambdaQueryWrapper<OrderPurchase> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(orderPurchaseDTO.getDocumentCode() != null, OrderPurchase::getDocumentCode, orderPurchaseDTO.getDocumentCode());
        wrapper.like(orderPurchaseDTO.getSupplierName() != null, OrderPurchase::getSupplierName, orderPurchaseDTO.getSupplierName());
        wrapper.like(orderPurchaseDTO.getSupplierPhone() != null, OrderPurchase::getSupplierPhone, orderPurchaseDTO.getSupplierPhone());
        wrapper.eq(orderPurchaseDTO.getStatus() != null, OrderPurchase::getStatus, orderPurchaseDTO.getStatus());
        wrapper.orderByDesc(OrderPurchase::getCreateTime);
        return orderPurchaseMapper.selectPage(page, wrapper);
    }

    @Override
    public void saveOrderPurchase(Long supplierId) {
        // 货物供应商数据
        Supplier supplier = supplierService.getById(supplierId);

        // 设置初始数据
        OrderPurchase orderPurchase = new OrderPurchase();
        orderPurchase.setDocumentCode(DocumentUtils.generateDocumentNumber(DocumentConstants.PURCHASE));
        orderPurchase.setStatus("1");
        orderPurchase.setSupplierId(supplierId);
        orderPurchase.setSupplierName(supplier.getName());
        orderPurchase.setSupplierPhone(supplier.getPhone());

        // 保存
        orderPurchaseMapper.insert(orderPurchase);
    }

    @Override
    public void takeDelivery(Long orderId) {
        if (orderId == null) {
            throw new BizException(OrderError.ORDER_ID_IS_EMPTY);
        }

        // 收货校验，只有订单状态为"已新建"时才能收货
        OrderPurchase orderPurchase = orderPurchaseMapper.selectById(orderId);
        if (!orderPurchase.getStatus().equals(DictUtils.getValueByLabel("已新建", "purchase_order_status"))) {
            throw new BizException(OrderError.ORDER_STATUS_IS_NOT_1);
        }

        // 更新订单状态为"已收货"，并设置收货人数据
        User user = SecurityUtils.getLoginUser().getUser();
        LambdaUpdateWrapper<OrderPurchase> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(OrderPurchase::getStatus, DictUtils.getValueByLabel("已收货", "purchase_order_status"));
        wrapper.set(OrderPurchase::getReceiptTime, LocalDateTime.now());
        wrapper.set(OrderPurchase::getReceiverName, user.getNickname());
        wrapper.set(OrderPurchase::getReceiverPhone, user.getPhone());
        wrapper.eq(OrderPurchase::getId, orderId);
        orderPurchaseMapper.update(wrapper);

        // 更新库存数据
        List<OrderPurchaseDetail> purchaseDetailList = orderPurchaseDetailMapper.selectList(
                new LambdaQueryWrapper<OrderPurchaseDetail>().eq(OrderPurchaseDetail::getOrderId, orderId));
        List<Long> recordIdList = purchaseDetailList.stream().map(OrderPurchaseDetail::getRecordId).collect(Collectors.toList());
        for (Long recordId : recordIdList) {
            Record record = recordService.getById(recordId);
            OrderPurchaseDetail purchaseDetail = purchaseDetailList.stream().filter(item -> item.getRecordId().equals(recordId)).collect(Collectors.toList()).get(0);
            if (purchaseDetail.getType().equals(DictUtils.getValueByLabel("进货", "order_detail_type"))) {
                // 进货类型，库存记录要增加
                record.setNumber(record.getNumber() + purchaseDetail.getWareNumber());
            } else  {
                // 退货类型，库存记录要减少
                record.setNumber(record.getNumber() - purchaseDetail.getWareNumber());
            }
            // 更新价格
            Ware ware = wareService.getById(record.getWareId());
            record.setTotalAmount(ware.getSalePrice().multiply(BigDecimal.valueOf(record.getNumber())));
            recordService.updateById(record);
        }
    }

    @Override
    public void settlementOrder(SettlementDTO settlementDTO) {
        if (settlementDTO.getOrderId() == null) {
            throw new BizException(OrderError.ORDER_ID_IS_EMPTY);
        }
        if (settlementDTO.getActualAmount() == null) {
            throw new BizException(OrderError.AMOUNT_IS_EMPTY);
        }

        // 收货校验，只有订单状态为"已收货"时才能进行结算
        OrderPurchase orderPurchase = orderPurchaseMapper.selectById(settlementDTO.getOrderId());
        if (!orderPurchase.getStatus().equals(DictUtils.getValueByLabel("已收货", "purchase_order_status"))) {
            throw new BizException(OrderError.ORDER_STATUS_IS_NOT_2);
        }

        LambdaUpdateWrapper<OrderPurchase> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(OrderPurchase::getStatus, DictUtils.getValueByLabel("已付款", "purchase_order_status"));
        wrapper.set(OrderPurchase::getActualAmount, settlementDTO.getActualAmount());
        wrapper.set(OrderPurchase::getRemark, settlementDTO.getRemark());
        wrapper.eq(OrderPurchase::getId, settlementDTO.getOrderId());
        orderPurchaseMapper.update(wrapper);

        // 保存支付截图
        orderSettlementService.savePicture(settlementDTO.getOrderId(), Arrays.asList(settlementDTO.getPictureList()));
    }

    @Override
    public void deleteByIds(List<Long> orderIds) {
        for (Long orderId : orderIds) {
            OrderPurchase orderPurchase = orderPurchaseMapper.selectById(orderId);
            if (!orderPurchase.getStatus().equals(DictUtils.getValueByLabel("已新建", "purchase_order_status"))) {
                throw new BizException(OrderError.ORDER_STATUS_IS_NOT_1);
            }
        }

        removeBatchByIds(orderIds);

        // 删除订单细节表中的数据
        LambdaUpdateWrapper<OrderPurchaseDetail> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(OrderPurchaseDetail::getOrderId, orderIds);
        orderPurchaseDetailMapper.delete(wrapper);
    }

    @Override
    public void updateOrderPurchaseData(Long orderId) {
        // 查询订单细节
        LambdaQueryWrapper<OrderPurchaseDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderPurchaseDetail::getOrderId, orderId);
        List<OrderPurchaseDetail> orderPurchaseDetails = orderPurchaseDetailMapper.selectList(wrapper);

        // 处理数据
        BigDecimal originAmount = new BigDecimal(0);
        BigDecimal returnAmount = new BigDecimal(0);
        int originNumber = 0;
        int returnNumber = 0;

        for (OrderPurchaseDetail orderPurchaseDetail : orderPurchaseDetails) {
            BigDecimal temp = new BigDecimal(0);
            if (orderPurchaseDetail.getType().equals(DictUtils.getValueByLabel("进货", "order_detail_type"))) {
                temp = temp.add(orderPurchaseDetail.getWarePurchasePrice()).multiply(BigDecimal.valueOf(orderPurchaseDetail.getWareNumber()));
                // 进货
                originAmount = originAmount.add(temp);
                originNumber += orderPurchaseDetail.getWareNumber();
            } else if (orderPurchaseDetail.getType().equals(DictUtils.getValueByLabel("退货", "order_detail_type"))) {
                temp = temp.add(orderPurchaseDetail.getWarePurchasePrice()).multiply(BigDecimal.valueOf(orderPurchaseDetail.getWareNumber()));
                // 退货
                returnAmount = returnAmount.add(temp);
                returnNumber += orderPurchaseDetail.getWareNumber();
            }
        }

        // 保存
        LambdaUpdateWrapper<OrderPurchase> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(OrderPurchase::getOriginAmount, originAmount);
        updateWrapper.set(OrderPurchase::getOriginNumber, originNumber);
        updateWrapper.set(OrderPurchase::getReturnAmount, returnAmount);
        updateWrapper.set(OrderPurchase::getReturnNumber, returnNumber);
        updateWrapper.eq(OrderPurchase::getId, orderId);
        orderPurchaseMapper.update(updateWrapper);
    }

    @Override
    public BigDecimal getAmountByDate(LocalDateTime begin, LocalDateTime end) {
        LambdaQueryWrapper<OrderPurchase> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(OrderPurchase::getReceiptTime, begin);
        wrapper.le(OrderPurchase::getReceiptTime, end);
        wrapper.eq(OrderPurchase::getStatus, DictUtils.getValueByLabel("已付款", "purchase_order_status"));
        List<OrderPurchase> purchaseList = orderPurchaseMapper.selectList(wrapper);

        BigDecimal amount = BigDecimal.ZERO;
        for (OrderPurchase orderPurchase : purchaseList) {
            amount = amount.add(orderPurchase.getActualAmount());
        }
        return amount;
    }

}
