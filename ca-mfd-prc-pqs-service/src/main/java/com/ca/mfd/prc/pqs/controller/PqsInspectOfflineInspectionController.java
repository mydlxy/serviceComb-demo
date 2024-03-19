package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsInspectOfflineInspectionEntity;
import com.ca.mfd.prc.pqs.service.IPqsInspectOfflineInspectionService;
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
 * @Description: 离线巡检记录表Controller
 * @author bo.yang
 * @date 2024年03月14日
 * @变更说明 BY bo.yang At 2024年03月14日
 */
@RestController
@RequestMapping("pqsinspectofflineinspection")
@Tag(name = "离线巡检记录表服务", description = "离线巡检记录表")
public class PqsInspectOfflineInspectionController extends BaseWithDefValController<PqsInspectOfflineInspectionEntity> {

    private IPqsInspectOfflineInspectionService pqsInspectOfflineInspectionService;

    @Autowired
    public PqsInspectOfflineInspectionController(IPqsInspectOfflineInspectionService pqsInspectOfflineInspectionService) {
        this.crudService = pqsInspectOfflineInspectionService;
        this.pqsInspectOfflineInspectionService = pqsInspectOfflineInspectionService;
    }

}