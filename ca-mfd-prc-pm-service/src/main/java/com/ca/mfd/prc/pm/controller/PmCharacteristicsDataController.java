package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.entity.PmCharacteristicsDataEntity;
import com.ca.mfd.prc.pm.service.IPmCharacteristicsDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author inkelink
 * @Description: 特征主数据Controller
 * @date 2023年08月23日
 * @变更说明 BY inkelink At 2023年08月23日
 */
@RestController
@RequestMapping("pmcharacteristicsdata")
@Tag(name = "特征主数据服务", description = "特征主数据")
public class PmCharacteristicsDataController extends BaseController<PmCharacteristicsDataEntity> {

    private final IPmCharacteristicsDataService pmCharacteristicsDataService;

    @Autowired
    public PmCharacteristicsDataController(IPmCharacteristicsDataService pmCharacteristicsDataService) {
        this.crudService = pmCharacteristicsDataService;
        this.pmCharacteristicsDataService = pmCharacteristicsDataService;
    }

    @GetMapping(value = "getcharacteristicnames", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "获取所有数据")
    public ResultVO<List<PmCharacteristicsDataEntity>> getCharacteristicNames() {
        List<PmCharacteristicsDataEntity> list = pmCharacteristicsDataService.getCharacteristicNames();
        return new ResultVO<List<PmCharacteristicsDataEntity>>().ok(list, "获取数据成功");
    }


}