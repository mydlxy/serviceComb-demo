package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pm.entity.PmPullCordEntity;
import com.ca.mfd.prc.pm.service.IPmPullCordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 拉绳配置
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("pmpullcord")
@Tag(name = "拉绳配置")
public class PmPullCordController extends PmBaseController<PmPullCordEntity> {

    private final IPmPullCordService pmPullCordService;

    @Autowired
    public PmPullCordController(IPmPullCordService pmPullCordService) {
        this.crudService = pmPullCordService;
        this.pmPullCordService = pmPullCordService;
    }

}