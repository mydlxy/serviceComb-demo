package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsMaterialCutLogEntity;
import com.ca.mfd.prc.eps.service.IEpsMaterialCutLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 生产物料切换记录Controller
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@RestController
@RequestMapping("epsmaterialcutlog")
@Tag(name = "生产物料切换记录服务", description = "生产物料切换记录")
public class EpsMaterialCutLogController extends BaseController<EpsMaterialCutLogEntity> {


    @Autowired
    public EpsMaterialCutLogController(IEpsMaterialCutLogService epsMaterialCutLogService) {
        this.crudService = epsMaterialCutLogService;
    }

}