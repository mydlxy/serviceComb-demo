package com.ca.mfd.prc.core.prm.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 首页
 *
 * @author inkelink ${email}
 * @date 2023-07-24
 */
@RestController
@RequestMapping("member/home")
@Tag(name = "首页")
public class HomeController extends BaseApiController {

    @GetMapping(value = "index")
    @Operation(summary = "Index")
    public String index() {
        return "<html><body>启动成功!<script>location.href='/swagger-ui/index.html';</script></body></html>";
    }

    @GetMapping(value = "error")
    @Operation(summary = "错误消息")
    public String error() {
        return "<html><body>远程接口发生错误!</body></html>";
    }
}