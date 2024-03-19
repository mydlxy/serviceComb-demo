package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.basedto.ExportModel;
import com.ca.mfd.prc.eps.entity.EpsVehicleEqumentConfigEntity;
import com.ca.mfd.prc.eps.service.IEpsVehicleEqumentConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author inkelink
 * @Description: 追溯设备工艺配置Controller
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@RestController
@RequestMapping("epsvehicleequmentconfig")
@Tag(name = "追溯设备工艺配置服务", description = "追溯设备工艺配置服务")
public class EpsVehicleEqumentConfigController extends BaseController<EpsVehicleEqumentConfigEntity> {

    private final IEpsVehicleEqumentConfigService vehicleEqumentConfigService;

    @Autowired
    public EpsVehicleEqumentConfigController(IEpsVehicleEqumentConfigService vehicleEqumentConfigService) {
        this.crudService = vehicleEqumentConfigService;
        this.vehicleEqumentConfigService = vehicleEqumentConfigService;
    }

    @Operation(summary = "导出eswich地址")
    @PostMapping("exporteswitch")
    public void exportEswitch(ExportModel model, HttpServletResponse response) throws IOException {
        if (StringUtils.isBlank(model.getFileName())) {
            model.setFileName("Eswitch");
        }
        vehicleEqumentConfigService.exprotEswitch(model.getConditions(), model.getSorts(), model.getFileName(), response);
    }

}