package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.mapper.DictItemMapper;
import com.yunqi.backend.model.entity.DictItem;
import com.yunqi.backend.service.DictItemService;
import org.springframework.stereotype.Service;

/**
 * @author liyunqi
 */
@Service
public class DictItemServiceImpl extends ServiceImpl<DictItemMapper, DictItem> implements DictItemService {
}
