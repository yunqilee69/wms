package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.exception.BizException;
import com.yunqi.backend.exception.message.OrderError;
import com.yunqi.backend.mapper.OrderSettlementMapper;
import com.yunqi.backend.model.entity.OrderSettlement;
import com.yunqi.backend.service.OrderSettlementService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单支付截图服务实现类
 * @author liyunqi
 */
@Service
@Transactional
public class OrderSettlementServiceImpl extends ServiceImpl<OrderSettlementMapper, OrderSettlement> implements OrderSettlementService {

    @Resource
    OrderSettlementMapper orderSettlementMapper;

    @Override
    public void savePicture(Long orderId, List<String> pictureList) {
        if (orderId == null) {
            throw new BizException(OrderError.ORDER_ID_IS_EMPTY);
        }
        for (String picture : pictureList) {
            OrderSettlement orderSettlement = new OrderSettlement();
            orderSettlement.setOrderId(orderId);
            orderSettlement.setPicture(picture);
            orderSettlementMapper.insert(orderSettlement);
        }

    }

    @Override
    public List<String> getPicturesByOrderId(Long orderId) {
        if (orderId == null) {
            throw new BizException(OrderError.ORDER_ID_IS_EMPTY);
        }
        LambdaQueryWrapper<OrderSettlement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderSettlement::getOrderId, orderId);
        return orderSettlementMapper.selectList(wrapper).stream()
                .map(OrderSettlement::getPicture)
                .collect(Collectors.toList());
    }
}
