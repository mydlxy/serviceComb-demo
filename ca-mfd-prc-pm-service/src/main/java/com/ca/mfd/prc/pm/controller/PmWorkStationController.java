package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.dto.MoveStationDto;
import com.ca.mfd.prc.pm.entity.PmBopEntity;
import com.ca.mfd.prc.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.pm.service.IPmWorkStationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author inkelink
 * @Description: 岗位
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("pmworkstation")
@Tag(name = "岗位")
public class PmWorkStationController extends PmBaseController<PmWorkStationEntity> {

    private final IPmWorkStationService pmWorkplaceService;

    @Autowired
    public PmWorkStationController(IPmWorkStationService pmWorkplaceService) {
        this.crudService = pmWorkplaceService;
        this.pmWorkplaceService = pmWorkplaceService;
    }

    /**
     * getCurrentWorkplaceList
     *
     * @param pageIndex
     * @param pageSize
     * @param conditions
     * @return
     */
    @GetMapping("/getcurrentworkplacelist")
    @Operation(summary = "获取当前版本的工位列表")
    public ResultVO getCurrentWorkplaceList(@RequestParam int pageIndex, @RequestParam int pageSize, @RequestBody List<ConditionDto> conditions) {
        return new ResultVO().ok(pmWorkplaceService.getCurrentWorkplaceList(pageIndex, pageSize, conditions));
    }

    /**
     * getWorkStationCodeByLineId
     *
     * @param lineId
     * @return
     */
    @GetMapping("/getworkstationcodebylineid")
    @Operation(summary = "根据线体获取工位列表")
    public ResultVO getWorkStationCodeByLineId(@RequestParam Long lineId) {
        return new ResultVO().ok(pmWorkplaceService.getWorkStationCodeByLineId(lineId));
    }


    /**
     * getCurrentWorkplaceList
     *
     * @param pageIndex
     * @param pageSize
     * @param conditions
     * @return
     */
    @GetMapping("/provider/getcurrentworkplacelist")
    @Operation(summary = "根据条件查询工位列表")
    public ResultVO getCurrentWorkplaceLists(@RequestParam int pageIndex, @RequestParam int pageSize, @RequestBody List<ConditionDto> conditions) {
        return new ResultVO().ok(pmWorkplaceService.getCurrentWorkplaceList(pageIndex, pageSize, conditions));
    }

    @GetMapping("checkcandelete")
    @Operation(summary = "删除前检测条件")
    public ResultVO<Boolean> checkCanDelete(@RequestParam Long prcWorkstationId) {
        return pmWorkplaceService.checkCanDelete(prcWorkstationId);
    }

    /**
     * 获取当前线体下的所有工位
     */
    @GetMapping("/getpmworkplace")
    @Operation(summary = "获取当前线体下的所有工位")
    public ResultVO<List<ComboInfoDTO>> getPmWorkplace(@NotNull(message = "guid不能为空") String guid) {
        return pmWorkplaceService.getPmWorkplace(guid);
    }

    /**
     * 获取所有车间的质量岗位
     */
    @GetMapping("getqualitypmworkplacelist")
    @Operation(summary = "获取所有车间的质量岗位")
    public ResultVO<List<ComboInfoDTO>> getQualityPmWorkplaceList() {
        return pmWorkplaceService.getQualityPmWorkplaceList();
    }

    @Schema(title = "移动工位")
    @Operation(summary = "移动工位")
    @PostMapping("moveworkstation")
    public ResultVO moveWorkStation(@RequestBody MoveStationDto dto) {
        ResultVO resultVO = pmWorkplaceService.moveWorkStation(dto);
        pmWorkplaceService.saveChange();
        return resultVO;
    }

    @GetMapping("getpmworkstationlist")
    @Operation(summary = "根据车间获取工位列表")
    public ResultVO<List<PmWorkStationEntity>> getPmWorkStationList(Long shopId,String shopCode) {
        return new ResultVO<List<PmWorkStationEntity>>().ok(pmWorkplaceService.getPmWorkStationList(shopId,shopCode), "获取数据成功");
    }
}