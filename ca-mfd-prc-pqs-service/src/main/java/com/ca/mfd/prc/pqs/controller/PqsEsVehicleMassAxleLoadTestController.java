package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsEsVehicleMassAxleLoadTestEntity;
import com.ca.mfd.prc.pqs.service.IPqsEsVehicleMassAxleLoadTestService;
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
 * @Description: 整车质量轴荷测试Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsesvehiclemassaxleloadtest")
@Tag(name = "整车质量轴荷测试服务", description = "整车质量轴荷测试")
public class PqsEsVehicleMassAxleLoadTestController extends BaseWithDefValController<PqsEsVehicleMassAxleLoadTestEntity> {

    private IPqsEsVehicleMassAxleLoadTestService pqsEsVehicleMassAxleLoadTestService;

    @Autowired
    public PqsEsVehicleMassAxleLoadTestController(IPqsEsVehicleMassAxleLoadTestService pqsEsVehicleMassAxleLoadTestService) {
        this.crudService = pqsEsVehicleMassAxleLoadTestService;
        this.pqsEsVehicleMassAxleLoadTestService = pqsEsVehicleMassAxleLoadTestService;
    }

}