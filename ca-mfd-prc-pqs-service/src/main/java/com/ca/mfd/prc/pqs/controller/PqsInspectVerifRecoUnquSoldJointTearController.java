package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.controller.BaseWithDefValController;
import com.ca.mfd.prc.pqs.entity.PqsInspectVerifRecoUnquSoldJointTearEntity;
import com.ca.mfd.prc.pqs.service.IPqsInspectVerifRecoUnquSoldJointTearService;
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
 * @Description: 撕裂不合格焊点验证记录表Controller
 * @author inkelink
 * @date 2024年02月15日
 * @变更说明 BY inkelink At 2024年02月15日
 */
@RestController
@RequestMapping("pqsinspectverifrecounqusoldjointtear")
@Tag(name = "撕裂不合格焊点验证记录表服务", description = "撕裂不合格焊点验证记录表")
public class PqsInspectVerifRecoUnquSoldJointTearController extends BaseWithDefValController<PqsInspectVerifRecoUnquSoldJointTearEntity> {

    private IPqsInspectVerifRecoUnquSoldJointTearService pqsInspectVerifRecoUnquSoldJointTearService;

    @Autowired
    public PqsInspectVerifRecoUnquSoldJointTearController(IPqsInspectVerifRecoUnquSoldJointTearService pqsInspectVerifRecoUnquSoldJointTearService) {
        this.crudService = pqsInspectVerifRecoUnquSoldJointTearService;
        this.pqsInspectVerifRecoUnquSoldJointTearService = pqsInspectVerifRecoUnquSoldJointTearService;
    }

}