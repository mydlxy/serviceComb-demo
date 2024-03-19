package com.ca.mfd.prc.core.communication.controller;

import com.alibaba.fastjson.JSONArray;
import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.core.communication.dto.ApiResultVo;
import com.ca.mfd.prc.core.communication.service.IApiLogicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @Description: 接口统一逻辑
 * @author inkelink
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@RestController
@RequestMapping("midapi/apilogic")
@Tag(name = "统一接口服务", description = "接口统一逻辑")
public class ApiLogicController extends BaseApiController {

    @Autowired
    private IApiLogicService apiLogicService;

    @Operation(summary = "接收数据接口")
    @PostMapping("receivedata")
    public List<ApiResultVo> receiveData(@RequestBody JSONArray iccDtos, @RequestParam(value = "MDMType") String type) {
        return apiLogicService.receiveData(iccDtos,type);
    }


}