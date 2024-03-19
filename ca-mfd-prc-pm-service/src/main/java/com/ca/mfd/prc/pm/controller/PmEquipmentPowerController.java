package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pm.entity.PmEquipmentPowerEntity;
import com.ca.mfd.prc.pm.service.IPmEquipmentPowerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 设备能力Controller
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@RestController
@RequestMapping("pmequipmentpower")
@Tag(name = "设备能力服务", description = "设备能力")
public class PmEquipmentPowerController extends PmBaseController<PmEquipmentPowerEntity> {

    private IPmEquipmentPowerService pmEquipmentPowerService;

    @Autowired
    public PmEquipmentPowerController(IPmEquipmentPowerService pmEquipmentPowerService) {
        this.crudService = pmEquipmentPowerService;
        this.pmEquipmentPowerService = pmEquipmentPowerService;
    }

}