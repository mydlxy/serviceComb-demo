package com.ca.mfd.prc.avi.controller;

import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页提示
 *
 * @author inkelink
 */
@RestController
public class IndexController {

    @GetMapping("/")
    public ResultVO<String> index() {
        String tips = "你好，inkelink-avi启动，请启动inkelink-ui，才能访问页面！";
        return new ResultVO<String>().ok(tips);
    }
}
