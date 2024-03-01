package com.yunqi.backend.controller;

import com.yunqi.backend.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜单控制器
 * @author liyunqi
 */
@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @GetMapping("/list")
    public Result listMenu() {
        return Result.success();
    }


}
