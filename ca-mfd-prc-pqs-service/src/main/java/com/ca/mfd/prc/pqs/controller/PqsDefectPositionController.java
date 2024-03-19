package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.dto.DefectFilterlParaInfo;
import com.ca.mfd.prc.pqs.dto.PositionResultDto;
import com.ca.mfd.prc.pqs.entity.PqsDefectPositionEntity;
import com.ca.mfd.prc.pqs.service.IPqsDefectPositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 缺陷位置代码Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsdefectposition")
@Tag(name = "缺陷位置代码服务", description = "缺陷位置代码")
public class PqsDefectPositionController extends BaseController<PqsDefectPositionEntity> {

    private final IPqsDefectPositionService pqsDefectPositionService;

    @Autowired
    public PqsDefectPositionController(IPqsDefectPositionService pqsDefectPositionService) {
        this.crudService = pqsDefectPositionService;
        this.pqsDefectPositionService = pqsDefectPositionService;
    }

    /**
     * 获取所有缺陷位置
     *
     * @return
     */
    @GetMapping("getalldatas")
    @Operation(summary = "获取所有缺陷位置")
    public ResultVO<List<PqsDefectPositionEntity>> getAllDatas() {
        return new ResultVO<List<PqsDefectPositionEntity>>().ok(pqsDefectPositionService.getAllDatas(), "获取数据成功");
    }

    /**
     * 获取位置配置
     *
     * @param info
     * @return
     */
    @PostMapping("getpositionshowlist")
    @Operation(summary = "获取位置配置")
    public ResultVO<List<PositionResultDto>> getPositionShowList(@RequestBody DefectFilterlParaInfo info) {

        return new ResultVO<List<PositionResultDto>>().ok(pqsDefectPositionService.getPositionShowList(info)
                .stream().collect(
                        Collectors.collectingAndThen(Collectors.toCollection(() ->
                                new TreeSet<>(Comparator.comparing(o -> o.getCode() + ";" + o.getDescription()))
                        ), ArrayList::new)).stream().map(t -> {
                    PositionResultDto resultDTO = new PositionResultDto();
                    resultDTO.setPositionId(t.getId());
                    resultDTO.setPositionCode(t.getCode());
                    resultDTO.setPositionDescription(t.getDescription());
                    return resultDTO;
                }).collect(Collectors.toList()), "获取数据成功");
    }

    /**
     * 获取所有缺陷位置
     *
     * @return 查询结果
     */
    @GetMapping("/provider/getalldatas")
    @Operation(summary = "获取所有缺陷位置")
    public ResultVO<List<PqsDefectPositionEntity>> getProviderAllDatas() {
        return new ResultVO<List<PqsDefectPositionEntity>>().ok(pqsDefectPositionService.getAllDatas());
    }
}