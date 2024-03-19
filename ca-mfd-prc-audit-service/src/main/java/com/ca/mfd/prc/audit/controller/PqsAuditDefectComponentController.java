package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.dto.DefectFilterlParaInfo;
import com.ca.mfd.prc.audit.entity.PqsAuditDefectComponentEntity;
import com.ca.mfd.prc.audit.service.IPqsAuditDefectComponentService;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: AUDIT组件代码Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsauditdefectcomponent")
@Tag(name = "AUDIT组件代码服务", description = "AUDIT组件代码")
public class PqsAuditDefectComponentController extends BaseController<PqsAuditDefectComponentEntity> {

    private final IPqsAuditDefectComponentService pqsAuditDefectComponentService;

    @Autowired
    public PqsAuditDefectComponentController(IPqsAuditDefectComponentService pqsAuditDefectComponentService) {
        this.crudService = pqsAuditDefectComponentService;
        this.pqsAuditDefectComponentService = pqsAuditDefectComponentService;
    }

    /**
     * 获取所有组件
     *
     * @return
     */
    @GetMapping("getalldatas")
    @Operation(summary = "获取所有组件")
    public ResultVO<List<PqsAuditDefectComponentEntity>> getAllDatas() {
        return new ResultVO<List<PqsAuditDefectComponentEntity>>().ok(pqsAuditDefectComponentService.getAllDatas(), "获取数据成功");
    }

    /**
     * 获取组件代码配置
     *
     * @param info
     * @return
     */
    @PostMapping("getcomponentshowlist")
    @Operation(summary = "获取组件代码配置")
    public ResultVO<List<Map<String, String>>> getComponentShowList(@RequestBody DefectFilterlParaInfo info) {

        return new ResultVO<List<Map<String, String>>>().ok(pqsAuditDefectComponentService.getComponentShowList(info)
                .stream().map(t -> {
                    Map<String, String> resultDTO = new HashMap<>(3);
                    resultDTO.put("componentId", t.getId() + "");
                    resultDTO.put("componentCode", t.getCode());
                    resultDTO.put("componentDescription", t.getDescription());
                    return resultDTO;
                }).collect(Collectors.toList()), "获取数据成功");
    }
}