package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pm.entity.PmScrConfigEntity;
import com.ca.mfd.prc.pm.service.IPmScrConfigService;
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
 * @Description: 拧紧指示配置Controller
 * @author inkelink
 * @date 2024年01月24日
 * @变更说明 BY inkelink At 2024年01月24日
 */
@RestController
@RequestMapping("pmscrconfig")
@Tag(name = "拧紧指示配置服务", description = "拧紧指示配置")
public class PmScrConfigController extends BaseController<PmScrConfigEntity> {

    private IPmScrConfigService pmScrConfigService;

    @Autowired
    public PmScrConfigController(IPmScrConfigService pmScrConfigService) {
        this.crudService = pmScrConfigService;
        this.pmScrConfigService = pmScrConfigService;
    }

}