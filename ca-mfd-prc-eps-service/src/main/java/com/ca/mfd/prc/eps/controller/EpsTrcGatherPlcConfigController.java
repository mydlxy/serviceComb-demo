package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsTrcGatherPlcConfigEntity;
import com.ca.mfd.prc.eps.service.IEpsTrcGatherPlcConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: PLC收集追溯条码配置Controller
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@RestController
@RequestMapping("epstrcgatherplcconfig")
@Tag(name = "PLC收集追溯条码配置服务", description = "PLC收集追溯条码配置")
public class EpsTrcGatherPlcConfigController extends BaseController<EpsTrcGatherPlcConfigEntity> {

    private final IEpsTrcGatherPlcConfigService epsTrcGatherPlcConfigService;

    @Autowired
    public EpsTrcGatherPlcConfigController(IEpsTrcGatherPlcConfigService epsTrcGatherPlcConfigService) {
        this.crudService = epsTrcGatherPlcConfigService;
        this.epsTrcGatherPlcConfigService = epsTrcGatherPlcConfigService;
    }

}