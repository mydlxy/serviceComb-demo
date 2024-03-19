package com.ca.mfd.prc.pqs.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.ca.mfd.prc.common.annotation.LogOperation;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.entity.PqsQualityMatrikAnomalyEntity;
import com.ca.mfd.prc.pqs.entity.PqsQualityMatrikEntity;
import com.ca.mfd.prc.pqs.entity.PqsQualityMatrikTcEntity;
import com.ca.mfd.prc.pqs.service.IPqsQualityMatrikAnomalyService;
import com.ca.mfd.prc.pqs.service.IPqsQualityMatrikService;
import com.ca.mfd.prc.pqs.service.IPqsQualityMatrikTcService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 百格图Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsqualitymatrik")
@Tag(name = "百格图服务", description = "百格图")
public class PqsQualityMatrikController extends BaseController<PqsQualityMatrikEntity> {

    private final IPqsQualityMatrikService pqsQualityMatrikService;

    @Autowired
    private IPqsQualityMatrikTcService pqsQualityMatrikTcService;
    @Autowired
    private IPqsQualityMatrikAnomalyService pqsQualityMatrikAnomalyService;

    @Autowired
    public PqsQualityMatrikController(IPqsQualityMatrikService pqsQualityMatrikService) {
        this.crudService = pqsQualityMatrikService;
        this.pqsQualityMatrikService = pqsQualityMatrikService;
    }

    @PostMapping("edit")
    @Operation(summary = "更新")
    @LogOperation("更新")
    @Override
    public ResultVO edit(@RequestBody PqsQualityMatrikEntity data) {
        List<ConditionDto> conditionInfos = new ArrayList<>();
        conditionInfos.add(new ConditionDto("PRC_PQS_QUALITY_MATRIK_ID", data.getId().toString(), ConditionOper.Equal));
        if (CollectionUtil.isEmpty(data.getDefects())) {
            throw new InkelinkException("缺陷分类未配置");
        }
        if (CollectionUtil.isNotEmpty(data.getVehicles())) {
            data.setModels(String.join(",", data.getVehicles().stream().map(t -> t.getModelName()).collect(Collectors.toList())));
        }

        if (data.getId().equals(IdGenerator.getId())) {
            data.setId(IdGenerator.getId());
            pqsQualityMatrikService.insert(data);
        } else {
            pqsQualityMatrikService.update(data);
            // modify by lee.li 正式环境没有物理删除权限，暂时修改为逻辑删除
            // 删除历史数据
            // pqsQualityMatrikTcService.delete(conditionInfos, false);
            // pqsQualityMatrikAnomalyService.delete(conditionInfos, false);
            pqsQualityMatrikTcService.delete(conditionInfos);
            pqsQualityMatrikAnomalyService.delete(conditionInfos);
        }
        for (PqsQualityMatrikTcEntity item : data.getVehicles()) {
            item.setId(IdGenerator.getId());
            item.setPrcPqsQualityMatrikId(data.getId());
        }
        for (PqsQualityMatrikAnomalyEntity item : data.getDefects()) {
            item.setId(IdGenerator.getId());
            item.setPrcPqsQualityMatrikId(data.getId());
        }
        pqsQualityMatrikTcService.insertBatch(data.getVehicles());
        pqsQualityMatrikAnomalyService.insertBatch(data.getDefects());

        pqsQualityMatrikTcService.saveChange();
        pqsQualityMatrikAnomalyService.saveChange();
        pqsQualityMatrikService.saveChange();
        return new ResultVO<String>().ok("", "保存成功");
    }

    /**
     * 获取所有数据
     */
    @GetMapping("/provider/getalldatas")
    @Operation(summary = "获取所有数据")
    public ResultVO<List<PqsQualityMatrikEntity>> getAllDatas() {
        return new ResultVO<List<PqsQualityMatrikEntity>>().ok(pqsQualityMatrikService.getAllDatas());
    }
}