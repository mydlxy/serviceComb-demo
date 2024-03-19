package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsSpcFileUserLogEntity;
import com.ca.mfd.prc.pqs.service.IPqsSpcFileUserLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 用户文件操作记录Controller
 * @author inkelink
 * @date 2023年11月30日
 * @变更说明 BY inkelink At 2023年11月30日
 */
@RestController
@RequestMapping("pqsspcfileuserlog")
@Tag(name = "用户文件操作记录服务", description = "用户文件操作记录")
public class PqsSpcFileUserLogController extends BaseController<PqsSpcFileUserLogEntity> {

    private IPqsSpcFileUserLogService pqsSpcFileUserLogService;

    @Autowired
    public PqsSpcFileUserLogController(IPqsSpcFileUserLogService pqsSpcFileUserLogService) {
        this.crudService = pqsSpcFileUserLogService;
        this.pqsSpcFileUserLogService = pqsSpcFileUserLogService;
    }

}