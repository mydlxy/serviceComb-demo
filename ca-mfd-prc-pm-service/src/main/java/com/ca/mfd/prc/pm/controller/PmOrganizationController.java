package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.entity.PmOrganizationEntity;
import com.ca.mfd.prc.pm.service.IPmOrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author inkelink
 * @Description: 工厂Controller
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@RestController
@RequestMapping("pmplant")
@Tag(name = "工厂服务", description = "工厂")
public class PmOrganizationController extends PmBaseController<PmOrganizationEntity> {

    private final IPmOrganizationService pmOrganizationService;

    @Autowired
    public PmOrganizationController(IPmOrganizationService pmOrganizationService) {
        this.crudService = pmOrganizationService;
        this.pmOrganizationService = pmOrganizationService;
    }

    @Operation(summary = "获取工厂编码")
    @GetMapping("/provider/getcurrentorgcode")
    public ResultVO getCurrentOrgCode() {
        ResultVO<String> result = new ResultVO<>();
        String data = pmOrganizationService.getCurrentOrgCode();
        return result.ok(data);
    }

}