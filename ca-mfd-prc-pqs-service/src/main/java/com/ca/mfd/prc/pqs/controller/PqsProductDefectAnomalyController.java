package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.dto.UpdateDutyAreaInfo;
import com.ca.mfd.prc.pqs.dto.UpdateDutyTeamNoInfo;
import com.ca.mfd.prc.pqs.entity.PqsProductDefectAnomalyEntity;
import com.ca.mfd.prc.pqs.service.IPqsProductDefectAnomalyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author inkelink
 * @Description: 产品缺陷记录Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsproductdefectanomaly")
@Tag(name = "产品缺陷记录服务", description = "产品缺陷记录")
public class PqsProductDefectAnomalyController extends BaseController<PqsProductDefectAnomalyEntity> {

    private final IPqsProductDefectAnomalyService pqsProductDefectAnomalyService;

    @Autowired
    public PqsProductDefectAnomalyController(IPqsProductDefectAnomalyService pqsProductDefectAnomalyService) {
        this.crudService = pqsProductDefectAnomalyService;
        this.pqsProductDefectAnomalyService = pqsProductDefectAnomalyService;
    }

    /**
     * 获取车辆缺陷分页
     *
     * @param model
     * @return
     */
    @PostMapping("getpagevehicledatas")
    @Operation(summary = "获取车辆缺陷分页")
    public ResultVO<PageData<PqsProductDefectAnomalyEntity>> getPageVehicleDatas(@RequestBody PageDataDto model) {
        PageData<PqsProductDefectAnomalyEntity> page = pqsProductDefectAnomalyService.getPageVehicleDatas(model.getConditions(), model.getSorts(), model.getPageIndex(), model.getPageSize());

        return new ResultVO<PageData<PqsProductDefectAnomalyEntity>>().ok(page, "获取数据成功");
    }

    /**
     * 后台更新缺陷责任区域
     *
     * @param info
     * @return
     */
    @PostMapping("updatevehicledefectanomalydutyarea")
    @Operation(summary = "后台更新缺陷责任区域")
    public ResultVO<String> updateVehicleDefectAnomalyDutyArea(@RequestBody UpdateDutyAreaInfo info) {
        pqsProductDefectAnomalyService.updateVehicleDefectAnomalyDutyArea(info);
        pqsProductDefectAnomalyService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    /**
     * 后台更新缺陷责任班组
     *
     * @param info
     * @return
     */
    @PostMapping("updatevehicledefectanomalydutyteamno")
    @Operation(summary = "后台更新缺陷责任班组")
    public ResultVO<String> updateVehicleDefectAnomalyDutyTeamNo(@RequestBody UpdateDutyTeamNoInfo info) {
        pqsProductDefectAnomalyService.updateVehicleDefectAnomalyDutyTeamNo(info);
        pqsProductDefectAnomalyService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    /**
     * 获取已激活的缺陷ID列表
     *
     * @param tpsCode tps编码
     * @return 已激活的缺陷ID列表
     */
    @GetMapping("/provider/getpqsproductdefectanomalylist")
    @Operation(summary = "获取已激活的缺陷ID列表")
    public ResultVO<List<String>> getPqsProductDefectAnomalyList(@RequestParam String tpsCode) {
        ResultVO<List<String>> result = new ResultVO<>();
        List<String> data = pqsProductDefectAnomalyService.getPqsProductDefectAnomalyList(tpsCode);
        return result.ok(data);
    }

    /**
     * 获取一条产品缺陷
     *
     * @param id 缺陷ID
     * @return 产品缺陷实体
     */
    @GetMapping("/provider/getbyids")
    @Operation(summary = "根据ID获取一条产品缺陷")
    public ResultVO<PqsProductDefectAnomalyEntity> getByIds(@RequestParam String id) {
        ResultVO<PqsProductDefectAnomalyEntity> result = new ResultVO<>();
        PqsProductDefectAnomalyEntity data = pqsProductDefectAnomalyService.get(id);
        return result.ok(data);
    }

    /**
     * 更新产品缺陷
     *
     * @param info 产品缺陷实体
     * @return 执行结果
     */
    @PostMapping("/provider/updatebyentity")
    @Operation(summary = "更新产品缺陷")
    public ResultVO<String> updateByEntity(@RequestBody PqsProductDefectAnomalyEntity info) {
        ResultVO<String> result = new ResultVO<>();
        pqsProductDefectAnomalyService.update(info);
        pqsProductDefectAnomalyService.saveChange();
        return result.ok("操作成功");
    }

    /**
     * 查询产品缺陷
     *
     * @param conditions 条件
     * @return 查询结果
     */
    @PostMapping("/provider/getdata")
    @Operation(summary = "查询产品缺陷")
    public ResultVO<List<PqsProductDefectAnomalyEntity>> getData(@RequestBody List<ConditionDto> conditions) {
        return new ResultVO().ok(pqsProductDefectAnomalyService.getData(conditions));
    }
}