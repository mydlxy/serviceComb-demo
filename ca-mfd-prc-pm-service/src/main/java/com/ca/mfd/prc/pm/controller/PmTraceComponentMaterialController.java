package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.entity.PmTraceComponentMaterialEntity;
import com.ca.mfd.prc.pm.service.IPmTraceComponentMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author inkelink
 * @Description: 追溯组件物料绑定
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("pmtracecomponentmaterial")
@Tag(name = "追溯组件物料绑定")
public class PmTraceComponentMaterialController extends BaseController<PmTraceComponentMaterialEntity> {

    private final IPmTraceComponentMaterialService pmTraceComponentMaterialService;

    @Autowired
    public PmTraceComponentMaterialController(IPmTraceComponentMaterialService pmTraceComponentMaterialService) {
        this.crudService = pmTraceComponentMaterialService;
        this.pmTraceComponentMaterialService = pmTraceComponentMaterialService;
    }

    /**
     * 获取所有数据
     *
     * @return 追溯组件物料列表
     */
    @GetMapping("/provider/getdatacache")
    @Operation(summary = "获取所有数据")
    public ResultVO<List<PmTraceComponentMaterialEntity>> getDataCache() {
        ResultVO<List<PmTraceComponentMaterialEntity>> result = new ResultVO<>();
        List<PmTraceComponentMaterialEntity> data = pmTraceComponentMaterialService.getAllDatas();
        return result.ok(data);
    }

    @Operation(summary = "保存追溯组件物料绑定")
    @PostMapping("savedata")
    public ResultVO saveSata(List<PmTraceComponentMaterialEntity> editData) {
        ResultVO result = new ResultVO<>();
        result.setMessage("获取数据成功");
        try {
            if (editData == null || editData.size() == 0) {
                throw new InkelinkException("编辑数据为空");
            }
            List<ConditionDto> dtos = new ArrayList<>();
            dtos.add(new ConditionDto("PRC_PM_TRACE_COMPONENT_ID", String.valueOf(editData.get(0).getPrcPmTraceComponentId()), ConditionOper.Equal));
            pmTraceComponentMaterialService.delete(dtos, false);
            pmTraceComponentMaterialService.insertBatch(editData);
            pmTraceComponentMaterialService.saveChange();
        } catch (Exception e) {
            throw new InkelinkException(e.getMessage());
        }
        return result;
    }

}