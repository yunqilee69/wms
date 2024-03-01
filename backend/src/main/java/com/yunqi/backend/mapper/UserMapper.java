package com.yunqi.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yunqi.backend.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author liyunqi
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {


}
