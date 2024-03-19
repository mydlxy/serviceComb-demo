package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsQgCheckListEntity;
import com.ca.mfd.prc.pqs.service.IPqsQgCheckListService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: QG必检项目Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsqgchecklist")
@Tag(name = "QG必检项目服务", description = "QG必检项目")
public class PqsQgCheckListController extends BaseController<PqsQgCheckListEntity> {

    private final IPqsQgCheckListService pqsQgCheckListService;

    @Autowired
    public PqsQgCheckListController(IPqsQgCheckListService pqsQgCheckListService) {
        this.crudService = pqsQgCheckListService;
        this.pqsQgCheckListService = pqsQgCheckListService;
    }

}