package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pm.entity.PmShcBreakEntity;
import com.ca.mfd.prc.pm.service.IPmShcBreakService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 休息时间
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@RestController
@RequestMapping("pmshcbreak")
@Tag(name = "休息时间")
public class PmShcBreakController extends BaseController<PmShcBreakEntity> {

    private final IPmShcBreakService pmShcBreakService;

    @Autowired
    public PmShcBreakController(IPmShcBreakService pmShcBreakService) {
        this.crudService = pmShcBreakService;
        this.pmShcBreakService = pmShcBreakService;
    }

}