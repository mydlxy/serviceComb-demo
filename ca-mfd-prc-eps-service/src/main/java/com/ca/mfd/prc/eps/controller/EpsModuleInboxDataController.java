package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsModuleInboxDataEntity;
import com.ca.mfd.prc.eps.service.IEpsModuleInboxDataService;
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
 * @Description: 预成组入箱数据Controller
 * @author inkelink
 * @date 2024年02月23日
 * @变更说明 BY inkelink At 2024年02月23日
 */
@RestController
@RequestMapping("/{version}/epsmoduleinboxdata")
@Tag(name = "预成组入箱数据服务", description = "预成组入箱数据")
public class EpsModuleInboxDataController extends BaseController<EpsModuleInboxDataEntity> {

    private IEpsModuleInboxDataService epsModuleInboxDataService;

    @Autowired
    public EpsModuleInboxDataController(IEpsModuleInboxDataService epsModuleInboxDataService) {
        this.crudService = epsModuleInboxDataService;
        this.epsModuleInboxDataService = epsModuleInboxDataService;
    }

}