package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.pqs.entity.PqsTechnologyAlarmPolicyEntity;
import com.ca.mfd.prc.pqs.service.IPqsTechnologyAlarmPolicyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 参数预警配置Controller
 * @author inkelink
 * @date 2023年10月17日
 * @变更说明 BY inkelink At 2023年10月17日
 */
@RestController
@RequestMapping("pqstechnologyalarmpolicy")
@Tag(name = "参数预警配置服务", description = "参数预警配置")
public class PqsTechnologyAlarmPolicyController extends BaseController<PqsTechnologyAlarmPolicyEntity> {

    private IPqsTechnologyAlarmPolicyService pqsTechnologyAlarmPolicyService;

    @Autowired
    public PqsTechnologyAlarmPolicyController(IPqsTechnologyAlarmPolicyService pqsTechnologyAlarmPolicyService) {
        this.crudService = pqsTechnologyAlarmPolicyService;
        this.pqsTechnologyAlarmPolicyService = pqsTechnologyAlarmPolicyService;
    }

}