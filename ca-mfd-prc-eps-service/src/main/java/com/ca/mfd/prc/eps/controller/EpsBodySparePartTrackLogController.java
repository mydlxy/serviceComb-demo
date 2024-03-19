package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsBodySparePartTrackLogEntity;
import com.ca.mfd.prc.eps.service.IEpsBodySparePartTrackLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 焊装车间备件运输跟踪日志Controller
 * @date 2023年09月12日
 * @变更说明 BY inkelink At 2023年09月12日
 */
@RestController
@RequestMapping("epsbodyspareparttracklog")
@Tag(name = "焊装车间备件运输跟踪日志服务", description = "焊装车间备件运输跟踪日志")
public class EpsBodySparePartTrackLogController extends BaseController<EpsBodySparePartTrackLogEntity> {


    @Autowired
    public EpsBodySparePartTrackLogController(IEpsBodySparePartTrackLogService epsBodySparePartTrackLogService) {
        this.crudService = epsBodySparePartTrackLogService;
    }

}