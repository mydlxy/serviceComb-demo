package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.eps.entity.EpsBodyshopJobDetailsEntity;
import com.ca.mfd.prc.eps.service.IEpsBodyshopJobDetailsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 焊装车间执行码详情Controller
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@RestController
@RequestMapping("epsbodyshopjobdetails")
@Tag(name = "焊装车间执行码详情服务", description = "焊装车间执行码详情")
public class EpsBodyshopJobDetailsController extends BaseController<EpsBodyshopJobDetailsEntity> {


    @Autowired
    public EpsBodyshopJobDetailsController(IEpsBodyshopJobDetailsService epsBodyshopJobDetailsService) {
        this.crudService = epsBodyshopJobDetailsService;
    }

}