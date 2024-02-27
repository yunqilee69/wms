package com.yunqi.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yunqi.backend.mapper.DictCodeMapper;
import com.yunqi.backend.model.entity.DictCode;
import com.yunqi.backend.service.DictCodeService;
import org.springframework.stereotype.Service;

/**
 * @author liyunqi
 */
@Service
public class DictCodeServiceImpl extends ServiceImpl<DictCodeMapper, DictCode> implements DictCodeService {
}
