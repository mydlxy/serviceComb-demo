package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.dto.DefectFilterlParaInfo;
import com.ca.mfd.prc.pqs.dto.ResultDto;
import com.ca.mfd.prc.pqs.entity.PqsDefectComponentEntity;
import com.ca.mfd.prc.pqs.service.IPqsDefectComponentService;
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
 * @Description: 组件代码Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsdefectcomponent")
@Tag(name = "组件代码服务", description = "组件代码")
public class PqsDefectComponentController extends BaseController<PqsDefectComponentEntity> {

    private final IPqsDefectComponentService pqsDefectComponentService;

    @Autowired
    public PqsDefectComponentController(IPqsDefectComponentService pqsDefectComponentService) {
        this.crudService = pqsDefectComponentService;
        this.pqsDefectComponentService = pqsDefectComponentService;
    }

    /**
     * 获取所有组件
     *
     * @return
     */
    @GetMapping("getalldatas")
    @Operation(summary = "获取所有组件")
    public ResultVO<List<PqsDefectComponentEntity>> getAllDatas() {
        return new ResultVO<List<PqsDefectComponentEntity>>().ok(pqsDefectComponentService.getAllDatas(), "获取数据成功");
    }

    /**
     * 获取组件代码配置
     *
     * @param info
     * @return
     */
    @PostMapping("getcomponentshowlist")
    @Operation(summary = "获取组件代码配置")
    public ResultVO<List<ResultDto>> getComponentShowList(@RequestBody DefectFilterlParaInfo info) {

        return new ResultVO<List<ResultDto>>().ok(pqsDefectComponentService.getComponentShowList(info)
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
     * 获取所有的组件信息
     *
     * @return 组件列表
     */
    @GetMapping("/provider/getalldatas")
    @Operation(summary = "获取所有组件")
    public ResultVO<List<PqsDefectComponentEntity>> getProviderAllDatas() {
        ResultVO<List<PqsDefectComponentEntity>> result = new ResultVO<>();
        List<PqsDefectComponentEntity> data = pqsDefectComponentService.getAllDatas();
        return result.ok(data);
    }
}