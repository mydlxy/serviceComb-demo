package com.ca.mfd.prc.avi.controller;

import com.ca.mfd.prc.avi.entity.AviPointVailSetEntity;
import com.ca.mfd.prc.avi.service.IAviPointVailSetService;
import com.ca.mfd.prc.common.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: AVI过点方法验证配置Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("avipointvailset")
@Tag(name = "AVI过点方法验证配置服务", description = "AVI过点方法验证配置")
public class AviPointVailSetController extends BaseController<AviPointVailSetEntity> {

    private final IAviPointVailSetService aviPointVailSetService;

    @Autowired
    public AviPointVailSetController(IAviPointVailSetService aviPointVailSetService) {
        this.crudService = aviPointVailSetService;
        this.aviPointVailSetService = aviPointVailSetService;
    }

}