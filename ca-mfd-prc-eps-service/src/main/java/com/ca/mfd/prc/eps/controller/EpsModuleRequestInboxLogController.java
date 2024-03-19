package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsModuleRequestInboxLogEntity;
import com.ca.mfd.prc.eps.service.IEpsModuleRequestInboxLogService;
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
 * @Description: 预成组请求入箱记录Controller
 * @author inkelink
 * @date 2024年03月14日
 * @变更说明 BY inkelink At 2024年03月14日
 */
@RestController
@RequestMapping("epsmodulerequestinboxlog")
@Tag(name = "预成组请求入箱记录服务", description = "预成组请求入箱记录")
public class EpsModuleRequestInboxLogController extends BaseController<EpsModuleRequestInboxLogEntity> {

    private IEpsModuleRequestInboxLogService epsModuleRequestInboxLogService;

    @Autowired
    public EpsModuleRequestInboxLogController(IEpsModuleRequestInboxLogService epsModuleRequestInboxLogService) {
        this.crudService = epsModuleRequestInboxLogService;
        this.epsModuleRequestInboxLogService = epsModuleRequestInboxLogService;
    }

}