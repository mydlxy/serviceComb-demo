package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.dto.DefectFilterlParaInfo;
import com.ca.mfd.prc.pqs.dto.DefectShowInfo;
import com.ca.mfd.prc.pqs.entity.PqsDefectAnomalyEntity;
import com.ca.mfd.prc.pqs.service.IPqsDefectAnomalyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 组合缺陷库Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsdefectanomaly")
@Tag(name = "组合缺陷库服务", description = "组合缺陷库")
public class PqsDefectAnomalyController extends BaseController<PqsDefectAnomalyEntity> {

    private final IPqsDefectAnomalyService pqsDefectAnomalyService;

    @Autowired
    public PqsDefectAnomalyController(IPqsDefectAnomalyService pqsDefectAnomalyService) {
        this.crudService = pqsDefectAnomalyService;
        this.pqsDefectAnomalyService = pqsDefectAnomalyService;
    }

    /**
     * 获取缺陷数据
     *
     * @param info
     * @return
     */
    @PostMapping("getanomalyshowlist")
    @Operation(summary = "获取缺陷数据")
    public ResultVO<List<DefectShowInfo>> getAnomalyShowList(@RequestBody DefectFilterlParaInfo info) {
        return new ResultVO<List<DefectShowInfo>>().ok(pqsDefectAnomalyService.getAnomalyShowList(info), "获取数据成功");
    }

    /**
     * 获取所有缺陷下拉
     *
     * @param allitem
     * @return
     */
    @GetMapping("getanomalydown")
    @Operation(summary = "获取所有缺陷下拉")
    public ResultVO<List<DefectShowInfo>> getAnomalyShowList(Boolean allitem) {
        if (allitem == null) {
            allitem = false;
        }
        List<DefectShowInfo> data = pqsDefectAnomalyService.getAllDatas().stream()
                .sorted(Comparator.comparing(PqsDefectAnomalyEntity::getDefectAnomalyCode))
                .map(c -> {
                    DefectShowInfo defectShowInfo = new DefectShowInfo();
                    defectShowInfo.setId(c.getId());
                    defectShowInfo.setCode(c.getDefectAnomalyCode());
                    defectShowInfo.setDescription(c.getDefectAnomalyDescription());
                    return defectShowInfo;
                }).collect(Collectors.toList());
        if (allitem) {
            DefectShowInfo defectShowInfo = new DefectShowInfo();
            defectShowInfo.setId(Constant.DEFAULT_ID);
            defectShowInfo.setCode("000000");
            defectShowInfo.setDescription("所有缺陷");
            data.add(0, defectShowInfo);
        }
        return new ResultVO<List<DefectShowInfo>>().ok(data, "获取数据成功");
    }

    /**
     * 获取AUDIT质量缺陷
     *
     * @return
     */
    @GetMapping("getauditanomalylist")
    @Operation(summary = "获取AUDIT质量缺陷")
    public ResultVO<List<PqsDefectAnomalyEntity>> getAuditAnomalyList() {
        return new ResultVO<List<PqsDefectAnomalyEntity>>().ok(pqsDefectAnomalyService.getAuditAnomalyShowList(), "获取数据成功");
    }

    /**
     * 根据缺陷名称或者缺陷编号获取缺陷列表
     *
     * @param count          查询limit 数量
     * @param conditionInfos 条件
     * @return 缺陷数据列表
     */
    @PostMapping("/provider/gettopdatasbycondtion")
    @Operation(summary = "根据缺陷名称或者缺陷编号获取缺陷列表")
    public ResultVO<List<PqsDefectAnomalyEntity>> getTopDatasByCondtion(@RequestParam Integer count, @RequestBody List<ConditionDto> conditionInfos) {
        ResultVO<List<PqsDefectAnomalyEntity>> result = new ResultVO<>();
        List<PqsDefectAnomalyEntity> data = pqsDefectAnomalyService.getTopDatasByCondtion(count, conditionInfos);
        return result.ok(data, "获取数据成功");
    }

    /**
     * 获取缺陷展示数据
     *
     * @param info 参数
     * @return 查询结果
     */
    @PostMapping("/provider/getanomalyshowlist")
    @Operation(summary = "获取缺陷展示数据")
    ResultVO<List<DefectShowInfo>> getProviderAnomalyShowList(@RequestBody DefectFilterlParaInfo info) {
        ResultVO<List<DefectShowInfo>> result = new ResultVO<>();
        List<DefectShowInfo> data = pqsDefectAnomalyService.getAnomalyShowList(info);
        return result.ok(data, "获取数据成功");
    }

    /**
     * 根据code查询缺陷
     *
     * @param code 缺陷Code
     * @return 返回实体
     */
    @GetMapping("/provider/getentitybycode")
    @Operation(summary = "根据code查询缺陷")
    public ResultVO<PqsDefectAnomalyEntity> getEntityByCode(String code) {
        ResultVO<PqsDefectAnomalyEntity> result = new ResultVO<>();
        PqsDefectAnomalyEntity data = pqsDefectAnomalyService.getEntityByCode(code);
        return result.ok(data, "获取数据成功");
    }
}