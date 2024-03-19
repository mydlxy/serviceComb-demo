package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.utils.ResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * @author inkelink
 * @Description: 首页提示
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @GetMapping("/")
    public ResultVO<String> index() {
        String tips = "你好，inkelink-pm已启动，请启动inkelink-ui，才能访问页面！";
        return new ResultVO<String>().ok(tips);
    }

    @PostMapping("/receivefromcmc")
    public ResultVO<String> receiveFromCmc(@NotNull(message="参数不能为空") @RequestBody String param) {
        logger.info("receiveFromCmc:{}", param);
        return new ResultVO<String>().ok(null,"接收成功");
    }
}
