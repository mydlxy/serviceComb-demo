package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pm.entity.PmOtEntity;
import com.ca.mfd.prc.pm.service.IPmOtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 操作终端
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("pmot")
@Tag(name = "操作终端")
public class PmOtController extends PmBaseController<PmOtEntity> {

    private final IPmOtService pmOtService;

    @Autowired
    public PmOtController(IPmOtService pmOtService) {
        this.crudService = pmOtService;
        this.pmOtService = pmOtService;
    }

}