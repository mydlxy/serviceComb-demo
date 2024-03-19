package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsLmsCallRecordEntity;
import com.ca.mfd.prc.eps.service.IEpsLmsCallRecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 物流拉动呼叫记录Controller
 * @author inkelink
 * @date 2023年10月25日
 * @变更说明 BY inkelink At 2023年10月25日
 */
@RestController
@RequestMapping("epslmscallrecord")
@Tag(name = "物流拉动呼叫记录服务", description = "物流拉动呼叫记录")
public class EpsLmsCallRecordController extends BaseController<EpsLmsCallRecordEntity> {

    private IEpsLmsCallRecordService epsLmsCallRecordService;

    @Autowired
    public EpsLmsCallRecordController(IEpsLmsCallRecordService epsLmsCallRecordService) {
        this.crudService = epsLmsCallRecordService;
        this.epsLmsCallRecordService = epsLmsCallRecordService;
    }

}