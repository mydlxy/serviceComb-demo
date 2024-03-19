package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.dto.DefectFilterlParaInfo;
import com.ca.mfd.prc.audit.dto.ResultDto;
import com.ca.mfd.prc.audit.entity.PqsAuditDefectPositionEntity;
import com.ca.mfd.prc.audit.service.IPqsAuditDefectPositionService;
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

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: AUDIT缺陷位置代码Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsauditdefectposition")
@Tag(name = "AUDIT缺陷位置代码服务", description = "AUDIT缺陷位置代码")
public class PqsAuditDefectPositionController extends BaseController<PqsAuditDefectPositionEntity> {

    private final IPqsAuditDefectPositionService pqsAuditDefectPositionService;

    @Autowired
    public PqsAuditDefectPositionController(IPqsAuditDefectPositionService pqsAuditDefectPositionService) {
        this.crudService = pqsAuditDefectPositionService;
        this.pqsAuditDefectPositionService = pqsAuditDefectPositionService;
    }

    /**
     * 获取所有缺陷位置
     *
     * @return
     */
    @GetMapping("getalldatas")
    @Operation(summary = "获取所有组件")
    public ResultVO<List<PqsAuditDefectPositionEntity>> getAllDatas() {
        return new ResultVO<List<PqsAuditDefectPositionEntity>>().ok(pqsAuditDefectPositionService.getAllDatas(), "获取数据成功");
    }

    /**
     * 获取位置配置
     *
     * @param info
     * @return
     */
    @PostMapping("getpositionshowlist")
    @Operation(summary = "获取组件代码配置")
    public ResultVO<List<ResultDto>> getPositionShowList(@RequestBody DefectFilterlParaInfo info) {

        return new ResultVO<List<ResultDto>>().ok(pqsAuditDefectPositionService.getPositionShowList(info)
                .stream().map(t -> {
                    ResultDto resultDTO = new ResultDto();
                    resultDTO.setCodeId(t.getId());
                    resultDTO.setCodeCode(t.getCode());
                    resultDTO.setCodeDescription(t.getDescription());
                    return resultDTO;
                }).collect(Collectors.toList()), "获取数据成功");
    }
}