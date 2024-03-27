package com.yunqi.backend;

import com.yunqi.backend.common.constant.CacheConstants;
import com.yunqi.backend.common.util.RedisCache;
import com.yunqi.backend.common.util.SecurityUtils;
import com.yunqi.backend.config.properties.AliSmsProperties;
import com.yunqi.backend.core.service.LoginService;
import com.yunqi.backend.mapper.MenuMapper;
import com.yunqi.backend.mapper.RoleMapper;
import com.yunqi.backend.model.entity.DictType;
import com.yunqi.backend.model.entity.Menu;
import com.yunqi.backend.model.request.RegisterRequest;
import com.yunqi.backend.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class BackendApplicationTests {


}
