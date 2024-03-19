package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.dto.DefectAnomalyParaInfo;
import com.ca.mfd.prc.pqs.dto.MmDefectRepairRecordDto;
import com.ca.mfd.prc.pqs.dto.MmDefectRepairRecordInfo;
import com.ca.mfd.prc.pqs.dto.PpsPlanPartsDto;
import com.ca.mfd.prc.pqs.entity.PqsMmDefectRepairRecordEntity;
import com.ca.mfd.prc.pqs.service.IPqsMmDefectRepairRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 缺陷返修记录表Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsmmdefectrepairrecord")
@Tag(name = "缺陷返修记录表服务", description = "缺陷返修记录表")
public class PqsMmDefectRepairRecordController extends BaseController<PqsMmDefectRepairRecordEntity> {

    private final IPqsMmDefectRepairRecordService pqsMmDefectRepairRecordService;

    @Autowired
    public PqsMmDefectRepairRecordController(IPqsMmDefectRepairRecordService pqsMmDefectRepairRecordService) {
        this.crudService = pqsMmDefectRepairRecordService;
        this.pqsMmDefectRepairRecordService = pqsMmDefectRepairRecordService;
    }

    /**
     * 根据计划编号，查询生产计划-零部件
     *
     * @param planNo
     * @return
     */
    @GetMapping("getppsplanparts")
    @Operation(summary = "根据计划编号，查询生产计划-零部件")
    public ResultVO<PpsPlanPartsDto> getPpsPlanParts(String planNo) {
        return new ResultVO<PpsPlanPartsDto>().ok(pqsMmDefectRepairRecordService.getPpsPlanParts(planNo), "获取数据成功");
    }

    /**
     * 保存批次件返修记录
     *
     * @param info 问题信息
     * @return
     */
    @PostMapping("savepqsmmdefectrepairrecord")
    @Operation(summary = "保存批次件返修记录")
    public ResultVO savePqsMmDefectRepairRecord(@RequestBody MmDefectRepairRecordInfo info) {
        pqsMmDefectRepairRecordService.savePqsMmDefectRepairRecord(info);
        pqsMmDefectRepairRecordService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    /**
     * 批次件返修记录列表
     *
     * @param info
     * @return
     */
    @PostMapping("getmmdefectrepairrecordlist")
    @Operation(summary = "批次件返修记录列表")
    public ResultVO<PageData<MmDefectRepairRecordDto>> getMmDefectRepairRecordList(@RequestBody DefectAnomalyParaInfo info) {
        return new ResultVO<PageData<MmDefectRepairRecordDto>>().ok(pqsMmDefectRepairRecordService.getMmDefectRepairRecordList(info), "获取数据成功");
    }
}