package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsLogOtOperationEntity;
import com.ca.mfd.prc.eps.service.IEpsLogOtOperationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 智能操作终端操作日志Controller
 * @date 2023年09月12日
 * @变更说明 BY inkelink At 2023年09月12日
 */
@RestController
@RequestMapping("epslogotoperation")
@Tag(name = "智能操作终端操作日志服务", description = "智能操作终端操作日志")
public class EpsLogOtOperationController extends BaseController<EpsLogOtOperationEntity> {


    @Autowired
    public EpsLogOtOperationController(IEpsLogOtOperationService epsLogOtOperationService) {
        this.crudService = epsLogOtOperationService;
    }

}