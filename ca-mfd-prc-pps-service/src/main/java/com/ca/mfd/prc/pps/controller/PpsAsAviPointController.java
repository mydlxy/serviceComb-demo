package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.dto.AsSendRptDTO;
import com.ca.mfd.prc.pps.dto.InsertAsAviPointInfo;
import com.ca.mfd.prc.pps.dto.PpsEntryReportPartsDto;
import com.ca.mfd.prc.pps.entity.PpsAsAviPointEntity;
import com.ca.mfd.prc.pps.entity.PpsEntryReportPartsEntity;
import com.ca.mfd.prc.pps.service.IPpsAsAviPointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 整车AVI锁定
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-10-20
 */
@RestController
@RequestMapping("aviasavipoint")
@Tag(name = "AS车辆实际过点")
public class PpsAsAviPointController extends BaseController<PpsAsAviPointEntity> {
    private final IPpsAsAviPointService aviAsAviPointService;

    @Autowired
    public PpsAsAviPointController(IPpsAsAviPointService aviAsAviPointService) {
        this.crudService = aviAsAviPointService;
        this.aviAsAviPointService = aviAsAviPointService;
    }

    @Operation(summary = "as报工数据插入")
    @PostMapping("/provider/insertasavipoint")
    public ResultVO<String> insertAsAviPoint(@RequestBody InsertAsAviPointInfo datas) {
        aviAsAviPointService.insertAsAviPoint(datas);
        aviAsAviPointService.saveChange();
        return new ResultVO<String>().ok("操作成功");
    }

    @Operation(summary = "avi过点补录添加As过点记录")
    @GetMapping("/provider/insertdataasavipoint")
    public ResultVO<String> insertDataAsAviPoint(String vehicleSn, String aviCode, int AviType) {
        aviAsAviPointService.insertDataAsAviPoint(vehicleSn,aviCode,AviType);
        aviAsAviPointService.saveChange();
        return new ResultVO<String>().ok("操作成功");
    }


    @Operation(summary = "AS报表数据")
    @PostMapping("getassendrpt")
    public ResultVO getAsSendRpt(@RequestBody Map pms) {
        return new ResultVO<>().ok(aviAsAviPointService.getAsSendRpt(pms), "");
    }

}
