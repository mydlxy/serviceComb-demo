package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsModuleStationMaterialEntity;
import com.ca.mfd.prc.eps.service.IEpsModuleStationMaterialService;
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
 * @Description: 预成组工位物料Controller
 * @author inkelink
 * @date 2024年03月14日
 * @变更说明 BY inkelink At 2024年03月14日
 */
@RestController
@RequestMapping("epsmodulestationmaterial")
@Tag(name = "预成组工位物料服务", description = "预成组工位物料")
public class EpsModuleStationMaterialController extends BaseController<EpsModuleStationMaterialEntity> {

    private IEpsModuleStationMaterialService epsModuleStationMaterialService;

    @Autowired
    public EpsModuleStationMaterialController(IEpsModuleStationMaterialService epsModuleStationMaterialService) {
        this.crudService = epsModuleStationMaterialService;
        this.epsModuleStationMaterialService = epsModuleStationMaterialService;
    }

}