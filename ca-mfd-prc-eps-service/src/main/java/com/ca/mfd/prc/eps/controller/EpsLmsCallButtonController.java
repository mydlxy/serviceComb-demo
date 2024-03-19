package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsLmsCallButtonEntity;
import com.ca.mfd.prc.eps.service.IEpsLmsCallButtonService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 物流拉动按钮配置Controller
 * @author inkelink
 * @date 2023年10月25日
 * @变更说明 BY inkelink At 2023年10月25日
 */
@RestController
@RequestMapping("epslmscallbutton")
@Tag(name = "物流拉动按钮配置服务", description = "物流拉动按钮配置")
public class EpsLmsCallButtonController extends BaseController<EpsLmsCallButtonEntity> {

    private IEpsLmsCallButtonService epsLmsCallButtonService;

    @Autowired
    public EpsLmsCallButtonController(IEpsLmsCallButtonService epsLmsCallButtonService) {
        this.crudService = epsLmsCallButtonService;
        this.epsLmsCallButtonService = epsLmsCallButtonService;
    }

}