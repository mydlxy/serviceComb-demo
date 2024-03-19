package com.ca.mfd.prc.audit.controller;

import com.ca.mfd.prc.audit.dto.DefectFilterlParaInfo;
import com.ca.mfd.prc.audit.dto.ResultDto;
import com.ca.mfd.prc.audit.entity.PqsAuditDefectCodeEntity;
import com.ca.mfd.prc.audit.service.IPqsAuditDefectCodeService;
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
 * @Description: AUDIT缺陷代码Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsauditdefectcode")
@Tag(name = "AUDIT缺陷代码服务", description = "AUDIT缺陷代码")
public class PqsAuditDefectCodeController extends BaseController<PqsAuditDefectCodeEntity> {

    private final IPqsAuditDefectCodeService pqsAuditDefectCodeService;

    @Autowired
    public PqsAuditDefectCodeController(IPqsAuditDefectCodeService pqsAuditDefectCodeService) {
        this.crudService = pqsAuditDefectCodeService;
        this.pqsAuditDefectCodeService = pqsAuditDefectCodeService;
    }

    /**
     * 获取所有缺陷
     *
     * @return
     */
    @GetMapping("getalldatas")
    @Operation(summary = "获取所有缺陷")
    public ResultVO<List<PqsAuditDefectCodeEntity>> getAllDatas() {
        return new ResultVO<List<PqsAuditDefectCodeEntity>>().ok(pqsAuditDefectCodeService.getAllDatas(), "获取数据成功");
    }

    /**
     * 获取所有缺陷
     *
     * @param info
     * @return
     */
    @PostMapping("getcodeshowlist")
    @Operation(summary = "获取缺陷分类数据展示")
    public ResultVO<List<ResultDto>> getCodeShowList(@RequestBody DefectFilterlParaInfo info) {

        return new ResultVO<List<ResultDto>>().ok(pqsAuditDefectCodeService.getCodeShowList(info)
                .stream().map(t -> {
                    ResultDto resultDTO = new ResultDto();
                    resultDTO.setCodeId(t.getId());
                    resultDTO.setCodeCode(t.getCode());
                    resultDTO.setCodeDescription(t.getDescription());
                    return resultDTO;
                }).collect(Collectors.toList()), "获取数据成功");
    }
}