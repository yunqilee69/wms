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
import com.yunqi.backend.mapper.OrderSaleDetailMapper;
import com.yunqi.backend.mapper.OrderSaleMapper;
import com.yunqi.backend.model.dto.OrderSaleDTO;
import com.yunqi.backend.model.dto.SettlementDTO;
import com.yunqi.backend.model.entity.OrderSale;
import com.yunqi.backend.model.entity.OrderSaleDetail;
import com.yunqi.backend.model.entity.Customer;
import com.yunqi.backend.model.entity.User;
import com.yunqi.backend.service.CustomerService;
import com.yunqi.backend.service.OrderSaleService;
import com.yunqi.backend.service.OrderSettlementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 订单销售服务实现类
 * @author liyunqi
 */
@Service
@Transactional
public class OrderSaleServiceImpl extends ServiceImpl<OrderSaleMapper, OrderSale> implements OrderSaleService {

    @Resource
    OrderSaleMapper orderSaleMapper;

    @Resource
    OrderSaleDetailMapper orderSaleDetailMapper;

    @Resource
    OrderSettlementService orderSettlementService;

    @Resource
    CustomerService customerService;

    @Override
    public Page<OrderSale> getOrderSalePage(OrderSaleDTO orderSaleDTO) {
        Page<OrderSale> page = PageUtils.getPage();
        LambdaQueryWrapper<OrderSale> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(orderSaleDTO.getDocumentCode() != null, OrderSale::getDocumentCode, orderSaleDTO.getDocumentCode());
        wrapper.like(orderSaleDTO.getCustomerName() != null, OrderSale::getCustomerName, orderSaleDTO.getCustomerName());
        wrapper.like(orderSaleDTO.getCustomerPhone() != null, OrderSale::getCustomerPhone, orderSaleDTO.getCustomerPhone());
        wrapper.eq(orderSaleDTO.getStatus() != null, OrderSale::getStatus, orderSaleDTO.getStatus());
        wrapper.orderByDesc(OrderSale::getCreateTime);
        return orderSaleMapper.selectPage(page, wrapper);
    }

    @Override
    public void saveOrderSale(Long customerId) {
        // 客户数据
        Customer customer = customerService.getById(customerId);

        // 设置初始数据
        OrderSale orderSale = new OrderSale();
        orderSale.setDocumentCode(DocumentUtils.generateDocumentNumber(DocumentConstants.PURCHASE));
        orderSale.setStatus("1");
        orderSale.setCustomerId(customerId);
        orderSale.setCustomerName(customer.getNickname());
        orderSale.setCustomerPhone(customer.getPhone());
        orderSale.setCustomerAddress(customer.getAddress());

        // 保存
        orderSaleMapper.insert(orderSale);
    }

    @Override
    public void takeDelivery(Long orderId) {
        if (orderId == null) {
            throw new BizException(OrderError.ORDER_ID_IS_EMPTY);
        }

        // 送货校验，只有订单状态为"已新建"时才能送货
        OrderSale orderSale = orderSaleMapper.selectById(orderId);
        if (!orderSale.getStatus().equals(DictUtils.getValueByLabel("已新建", "sale_order_status"))) {
            throw new BizException(OrderError.ORDER_STATUS_IS_NOT_1);
        }

        // 更新订单状态为"已送货"，并设置送货人数据
        User user = SecurityUtils.getLoginUser().getUser();
        LambdaUpdateWrapper<OrderSale> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(OrderSale::getStatus, DictUtils.getValueByLabel("已送货", "sale_order_status"));
        wrapper.set(OrderSale::getDeliveryTime, LocalDateTime.now());
        wrapper.set(OrderSale::getDeliveryName, user.getNickname());
        wrapper.set(OrderSale::getDeliveryPhone, user.getPhone());
        wrapper.eq(OrderSale::getId, orderId);
        orderSaleMapper.update(wrapper);

    }

    @Override
    public void settlementOrder(SettlementDTO settlementDTO) {
        if (settlementDTO.getOrderId() == null) {
            throw new BizException(OrderError.ORDER_ID_IS_EMPTY);
        }
        if (settlementDTO.getActualAmount() == null) {
            throw new BizException(OrderError.AMOUNT_IS_EMPTY);
        }

        // 送货校验，只有订单状态为"已送货"时才能进行结算
        OrderSale orderSale = orderSaleMapper.selectById(settlementDTO.getOrderId());
        if (!orderSale.getStatus().equals(DictUtils.getValueByLabel("已送货", "sale_order_status"))) {
            throw new BizException(OrderError.SALE_ORDER_STATUS_IS_NOT_2);
        }

        LambdaUpdateWrapper<OrderSale> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(OrderSale::getStatus, DictUtils.getValueByLabel("已付款", "sale_order_status"));
        wrapper.set(OrderSale::getActualAmount, settlementDTO.getActualAmount());
        wrapper.set(OrderSale::getRemark, settlementDTO.getRemark());
        wrapper.eq(OrderSale::getId, settlementDTO.getOrderId());
        orderSaleMapper.update(wrapper);

        // 保存支付截图
        orderSettlementService.savePicture(settlementDTO.getOrderId(), Arrays.asList(settlementDTO.getPictureList()));
    }

    @Override
    public void deleteByIds(List<Long> orderIds) {
        for (Long orderId : orderIds) {
            OrderSale orderSale = orderSaleMapper.selectById(orderId);
            if (!orderSale.getStatus().equals(DictUtils.getValueByLabel("已新建", "sale_order_status"))) {
                throw new BizException(OrderError.ORDER_STATUS_IS_NOT_1);
            }
        }

        removeBatchByIds(orderIds);

        // 删除订单细节表中的数据
        LambdaUpdateWrapper<OrderSaleDetail> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(OrderSaleDetail::getOrderId, orderIds);
        orderSaleDetailMapper.delete(wrapper);
    }

    @Override
    public void updateOrderSaleData(Long orderId) {
        // 查询订单细节
        LambdaQueryWrapper<OrderSaleDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderSaleDetail::getOrderId, orderId);
        List<OrderSaleDetail> orderSaleDetails = orderSaleDetailMapper.selectList(wrapper);

        // 处理数据
        BigDecimal originAmount = new BigDecimal(0);
        BigDecimal returnAmount = new BigDecimal(0);
        int originNumber = 0;
        int returnNumber = 0;

        for (OrderSaleDetail orderSaleDetail : orderSaleDetails) {
            BigDecimal temp = new BigDecimal(0);
            if (orderSaleDetail.getType().equals(DictUtils.getValueByLabel("进货", "order_detail_type"))) {
                temp = temp.add(orderSaleDetail.getWareSalePrice()).multiply(BigDecimal.valueOf(orderSaleDetail.getWareNumber()));
                // 进货
                originAmount = originAmount.add(temp);
                originNumber += orderSaleDetail.getWareNumber();
            } else if (orderSaleDetail.getType().equals(DictUtils.getValueByLabel("退货", "order_detail_type"))) {
                temp = temp.add(orderSaleDetail.getWareSalePrice()).multiply(BigDecimal.valueOf(orderSaleDetail.getWareNumber()));
                // 退货
                returnAmount = returnAmount.add(temp);
                returnNumber += orderSaleDetail.getWareNumber();
            }
        }

        // 保存
        LambdaUpdateWrapper<OrderSale> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(OrderSale::getOriginAmount, originAmount);
        updateWrapper.set(OrderSale::getOriginNumber, originNumber);
        updateWrapper.set(OrderSale::getReturnAmount, returnAmount);
        updateWrapper.set(OrderSale::getReturnNumber, returnNumber);
        updateWrapper.eq(OrderSale::getId, orderId);
        orderSaleMapper.update(updateWrapper);
    }

}
