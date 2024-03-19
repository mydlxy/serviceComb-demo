package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.dto.LabelAndValueMappingDTO;
import com.ca.mfd.prc.common.enums.ConditionDirection;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.entity.PpsProductProcessAviEntity;
import com.ca.mfd.prc.pps.entity.PpsProductProcessEntity;
import com.ca.mfd.prc.pps.service.IPpsProductProcessAviService;
import com.ca.mfd.prc.pps.service.IPpsProductProcessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author inkelink
 * @Description: 工艺路径设置
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("ppsproductprocess")
@Tag(name = " 工艺路径设置")
public class PpsProductProcessController extends BaseController<PpsProductProcessEntity> {

    private final IPpsProductProcessService ppsProductProcessService;
    @Autowired
    private IPpsProductProcessAviService ppsProductProcessAviService;

    @Autowired
    public PpsProductProcessController(IPpsProductProcessService ppsProductProcessService) {
        this.crudService = ppsProductProcessService;
        this.ppsProductProcessService = ppsProductProcessService;
    }

    /**
     * 查询一个实体
     *
     * @return 返回一个实体
     */
    @GetMapping(value = "/provider/getprocess")
    @Operation(summary = "根据订单类型查询工艺路径设置")
    public ResultVO getProcess(String orderCategory) {
        return new ResultVO().ok(ppsProductProcessService.getProcess(orderCategory));
    }

    /**
     * 获取所有的数据
     *
     * @return List<PpsProductProcessEntity>
     */
    @GetMapping(value = "/provider/getalldatas")
    @Operation(summary = "获取所有的数据")
    public ResultVO getAllDatas() {
        return new ResultVO().ok(ppsProductProcessService.getAllDatas());
    }

    @Operation(summary = "获取已设置AVI点")
    @Parameters({
            @Parameter(name = "productProcessId", description = "工艺路径ID")})
    @GetMapping(value = "getsetaviinfos")
    public ResultVO getSetAviInfos(String productProcessId) {
        return new ResultVO().ok(ppsProductProcessAviService.getSetAviInfos(ConvertUtils.stringToLong(productProcessId)), "获取已设置AVI点成功");
    }

    @Operation(summary = "获取未设置AVI点")
    @Parameters({
            @Parameter(name = "productProcessId", description = "工艺路径ID")})
    @GetMapping(value = "getnosetaviinfo")
    public ResultVO getNoSetAviInfo(String productProcessId) {
        return new ResultVO().ok(ppsProductProcessAviService.getNoSetAviInfo(ConvertUtils.stringToLong(productProcessId)), "获取未设置AVI点");
    }

    @Operation(summary = "保存设置AVI点明细")
    @Parameters({
            @Parameter(name = "models", description = "设置对象")})
    @PostMapping(value = "savesetavi")
    public ResultVO saveSetAvi(@RequestBody List<PpsProductProcessAviEntity> models) {
        if (models == null || models.isEmpty()) {
            throw new InkelinkException("AVI列表不能为空");
        }
        for (PpsProductProcessAviEntity ppsProductProcessAviEntity : models) {
            if (ppsProductProcessAviEntity.getId() == null || ppsProductProcessAviEntity.getId() <= 0) {
                ppsProductProcessAviService.save(ppsProductProcessAviEntity);
            } else {
                ppsProductProcessAviService.update(ppsProductProcessAviEntity);
            }
        }
        ppsProductProcessAviService.saveChange();
        return new ResultVO().ok(null, "设置AVI点成功");
    }

    @Operation(summary = "获取工艺路径下拉数据源")
    @Parameters({
            @Parameter(name = "entrySource", description = "产品类型")})
    @GetMapping(value = "getprocesscombodata")
    public ResultVO getProcessComboData(String entrySource) {
        List<ConditionDto> conditions = new ArrayList<>(2);
        /** 备份逻辑conditions.add(new ConditionDto("IS_ENBLE", "1", ConditionOper.Equal));*/
        if (StringUtils.isNotBlank(entrySource)) {
            conditions.add(new ConditionDto("ORDER_CATEGORY", entrySource, ConditionOper.In));
        }
        List<PpsProductProcessEntity> pmProductProcessList = ppsProductProcessService.getData(conditions,
                Arrays.asList(new SortDto("PROCESS_NO", ConditionDirection.ASC)), false);
        /** 备份逻辑PpsProductProcessEntity defaultProductProcess = new PpsProductProcessEntity();
         备份逻辑defaultProductProcess.setProcessNo("默认");
         备份逻辑defaultProductProcess.setVersion(0);
         备份逻辑pmProductProcessList.add(0, defaultProductProcess); */
        List<LabelAndValueMappingDTO> targetList = new ArrayList<>(pmProductProcessList.size());
        for (PpsProductProcessEntity pmProductProcess : pmProductProcessList) {
            LabelAndValueMappingDTO item = new LabelAndValueMappingDTO();
            item.setLabel(pmProductProcess.getProcessNo() + "[" + pmProductProcess.getVersion() + "]");
            item.setValue(pmProductProcess.getId().toString());

            targetList.add(item);
        }
        return new ResultVO().ok(targetList, "获取数据成功");
    }

    @Operation(summary = "获取工艺路径")
    @Parameters({
            @Parameter(name = "orderCategory", description = "orderCategory"),
            @Parameter(name = "orderType", description = "orderType")})
    @GetMapping(value = "getprocesslist")
    public ResultVO getProcessList(String orderCategory, String orderType) {
        return new ResultVO().ok(ppsProductProcessService.getProcessList(orderCategory, orderType), "获取数据成功");
    }


}