package com.ca.mfd.prc.pm.controller;


import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pm.entity.PmDislinePlcConfigEntity;
import com.ca.mfd.prc.pm.service.IPmDislinePlcConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 * @Description: 分布线体PLC配置Controller
 * @author inkelink
 * @date 2023年10月28日
 * @变更说明 BY inkelink At 2023年10月28日
 */
@RestController
@RequestMapping("pmdislineplcconfig")
@Tag(name = "分布线体PLC配置服务", description = "分布线体PLC配置")
public class PmDislinePlcConfigController extends BaseController<PmDislinePlcConfigEntity> {

    private IPmDislinePlcConfigService pmDislinePlcConfigService;

    @Autowired
    public PmDislinePlcConfigController(IPmDislinePlcConfigService pmDislinePlcConfigService) {
        this.crudService = pmDislinePlcConfigService;
        this.pmDislinePlcConfigService = pmDislinePlcConfigService;
    }

}