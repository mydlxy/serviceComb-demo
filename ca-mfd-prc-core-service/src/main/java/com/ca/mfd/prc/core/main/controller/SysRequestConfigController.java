package com.ca.mfd.prc.core.main.controller;

import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.entity.MiddleResponse;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.communication.dto.DefalutRequestModel;
import com.ca.mfd.prc.core.main.entity.SysRequestConfigEntity;
import com.ca.mfd.prc.core.main.service.ISysRequestConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 系统内部站点访问
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("main/sysrequestconfig")
@Tag(name = "系统内部站点访问")
public class SysRequestConfigController extends BaseController<SysRequestConfigEntity> {

    private final ISysRequestConfigService sysRequestConfigService;


    @Autowired
    private LocalCache cache;

    @Autowired
    public SysRequestConfigController(ISysRequestConfigService sysRequestConfigService) {
        this.crudService = sysRequestConfigService;
        this.sysRequestConfigService = sysRequestConfigService;
    }

    @Operation(summary = "查看请求日志100条")
    @GetMapping("getloginfobyrequestkey")
    public ResultVO getLogInfoByKey(String requestKey) {
        List<DefalutRequestModel> data = cache.getObject("defaultRequest:" + requestKey);
        return new ResultVO().ok(data, "刷新成功");
    }

    @Operation(summary = "测试访问")
    @GetMapping("debugbykey")
    public ResultVO getLogInfoByKey(String requestKey, Object content) {
        MiddleResponse data = sysRequestConfigService.defaultRequestClinet(requestKey, content);

        return new ResultVO<>().ok(data, "刷新成功");
    }

}