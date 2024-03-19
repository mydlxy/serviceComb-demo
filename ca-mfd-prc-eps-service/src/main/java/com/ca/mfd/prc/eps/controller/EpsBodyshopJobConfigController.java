package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.dto.SetModelDTO;
import com.ca.mfd.prc.eps.dto.VehicleJobInfo;
import com.ca.mfd.prc.eps.entity.EpsBodyshopJobConfigEntity;
import com.ca.mfd.prc.eps.service.IEpsBodySparePartTrackService;
import com.ca.mfd.prc.eps.service.IEpsBodyshopJobConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author inkelink
 * @Description: 焊装车系码配置Controller
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@RestController
@RequestMapping("epsbodyshopjobconfig")
@Tag(name = "焊装车系码配置服务", description = "焊装车系码配置")
public class EpsBodyshopJobConfigController extends BaseController<EpsBodyshopJobConfigEntity> {
    private final IEpsBodyshopJobConfigService epsBodyshopJobConfigService;

    @Autowired
    public EpsBodyshopJobConfigController(IEpsBodyshopJobConfigService epsBodyshopJobConfigService) {
        this.crudService = epsBodyshopJobConfigService;
        this.epsBodyshopJobConfigService = epsBodyshopJobConfigService;
    }

    @Operation(summary = "获取车辆各个区域的执行码")
    @GetMapping("getjobconfigbysn")
    public ResultVO<VehicleJobInfo> getJobConfigBySn(String sn) {
        return new ResultVO<VehicleJobInfo>().ok(epsBodyshopJobConfigService.getJobConfigBySn(sn));
    }

}