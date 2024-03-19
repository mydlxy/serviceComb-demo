package com.ca.mfd.prc.core.main.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.core.main.entity.SysBizTblEntity;
import com.ca.mfd.prc.core.main.service.ISysBizTblService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 系统业务库同步表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("main/sysbiztbl")
@Tag(name = "系统业务库同步表")
public class SysBizTblController extends BaseController<SysBizTblEntity> {

    private final ISysBizTblService sysBizTblService;

    @Autowired
    public SysBizTblController(ISysBizTblService sysBizTblService) {
        this.crudService = sysBizTblService;
        this.sysBizTblService = sysBizTblService;
    }

}