package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.dto.DefectFilterlParaInfo;
import com.ca.mfd.prc.pqs.dto.ResultDto;
import com.ca.mfd.prc.pqs.entity.PqsDefectCodeEntity;
import com.ca.mfd.prc.pqs.service.IPqsDefectCodeService;
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
 * @Description: 缺陷代码Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsdefectcode")
@Tag(name = "缺陷代码服务", description = "缺陷代码")
public class PqsDefectCodeController extends BaseController<PqsDefectCodeEntity> {

    private final IPqsDefectCodeService pqsDefectCodeService;

    @Autowired
    public PqsDefectCodeController(IPqsDefectCodeService pqsDefectCodeService) {
        this.crudService = pqsDefectCodeService;
        this.pqsDefectCodeService = pqsDefectCodeService;
    }

    /**
     * 获取所有缺陷
     *
     * @return
     */
    @GetMapping("getalldatas")
    @Operation(summary = "获取所有缺陷")
    public ResultVO<List<PqsDefectCodeEntity>> getAllDatas() {
        return new ResultVO<List<PqsDefectCodeEntity>>().ok(pqsDefectCodeService.getAllDatas(), "获取数据成功");
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

        return new ResultVO<List<ResultDto>>().ok(pqsDefectCodeService.getCodeShowList(info)
                .stream().collect(
                        Collectors.collectingAndThen(Collectors.toCollection(() ->
                                new TreeSet<>(Comparator.comparing(o -> o.getCode() + ";" + o.getDescription()))
                        ), ArrayList::new)).stream().map(t -> {
                    ResultDto resultDTO = new ResultDto();
                    resultDTO.setCodeId(t.getId());
                    resultDTO.setCodeCode(t.getCode());
                    resultDTO.setCodeDescription(t.getDescription());
                    return resultDTO;
                }).collect(Collectors.toList()), "获取数据成功");
    }

    /**
     * 获取所有缺陷
     *
     * @return 获取所有缺陷
     */
    @GetMapping("/provider/getalldatas")
    @Operation(summary = "获取所有缺陷")
    public ResultVO<List<PqsDefectCodeEntity>> getProviderAllDatas() {
        return new ResultVO<List<PqsDefectCodeEntity>>().ok(pqsDefectCodeService.getAllDatas());
    }
}