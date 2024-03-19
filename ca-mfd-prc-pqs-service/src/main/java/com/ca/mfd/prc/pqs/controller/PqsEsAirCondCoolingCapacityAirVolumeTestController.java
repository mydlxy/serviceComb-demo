package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsEsAirCondCoolingCapacityAirVolumeTestEntity;
import com.ca.mfd.prc.pqs.service.IPqsEsAirCondCoolingCapacityAirVolumeTestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *
 * @Description: 空调出风口风量及温度测试记录Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsesaircondcoolingcapacityairvolumetest")
@Tag(name = "空调出风口风量及温度测试记录服务", description = "空调出风口风量及温度测试记录")
public class PqsEsAirCondCoolingCapacityAirVolumeTestController extends BaseWithDefValController<PqsEsAirCondCoolingCapacityAirVolumeTestEntity> {

    private IPqsEsAirCondCoolingCapacityAirVolumeTestService pqsEsAirCondCoolingCapacityAirVolumeTestService;

    @Autowired
    public PqsEsAirCondCoolingCapacityAirVolumeTestController(IPqsEsAirCondCoolingCapacityAirVolumeTestService pqsEsAirCondCoolingCapacityAirVolumeTestService) {
        this.crudService = pqsEsAirCondCoolingCapacityAirVolumeTestService;
        this.pqsEsAirCondCoolingCapacityAirVolumeTestService = pqsEsAirCondCoolingCapacityAirVolumeTestService;
    }

}