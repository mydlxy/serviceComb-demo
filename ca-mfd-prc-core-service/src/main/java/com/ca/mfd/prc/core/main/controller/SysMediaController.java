package com.ca.mfd.prc.core.main.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.core.main.entity.SysMediaEntity;
import com.ca.mfd.prc.core.main.service.ISysMediaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 媒体库
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("main/sysmedia")
@Tag(name = "媒体库")
public class SysMediaController extends BaseController<SysMediaEntity> {

    private final ISysMediaService sysMediaService;

    @Autowired
    public SysMediaController(ISysMediaService sysMediaService) {
        this.crudService = sysMediaService;
        this.sysMediaService = sysMediaService;
    }

}