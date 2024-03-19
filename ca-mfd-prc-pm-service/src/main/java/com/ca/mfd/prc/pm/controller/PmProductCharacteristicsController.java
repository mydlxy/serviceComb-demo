package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.dto.ProductCharacteristicsDTO;
import com.ca.mfd.prc.pm.entity.PmProductCharacteristicsEntity;
import com.ca.mfd.prc.pm.service.IPmProductCharacteristicsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author inkelink
 * @Description: 特征主数据
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("pmproductcharacteristics")
@Tag(name = "特征主数据")
public class PmProductCharacteristicsController extends BaseController<PmProductCharacteristicsEntity> {

    private final IPmProductCharacteristicsService pmProductCharacteristicsService;

    @Autowired
    public PmProductCharacteristicsController(IPmProductCharacteristicsService pmProductCharacteristicsService) {
        this.crudService = pmProductCharacteristicsService;
        this.pmProductCharacteristicsService = pmProductCharacteristicsService;
    }


    /**
     * 特征主数据
     */
    @GetMapping(value = "/provider/getbycharacteristicsversionsid")
    @Operation(summary = "特征主数据")
    public ResultVO<List<PmProductCharacteristicsEntity>> getByCharacteristicsVersionsId(Long characteristicsVersionsId) {
        ResultVO<List<PmProductCharacteristicsEntity>> result = new ResultVO<>();
        List<PmProductCharacteristicsEntity> data = pmProductCharacteristicsService.getByCharacteristicsVersionsId(characteristicsVersionsId);
        result.setData(data);
        return result;
    }

    /**
     * 特征主数据
     *
     * @param versionsid 特征版本外键
     * @param name       特征项
     * @param materialNo 产品编码
     * @param isdelete   是否删除
     * @return 特征主数据
     */
    @GetMapping(value = "/provider/getbyversionsandname")
    @Operation(summary = "特征主数据")
    public ResultVO<PmProductCharacteristicsEntity> getByVersionsAndName(String versionsid, String name, String materialNo, String isdelete) {
        ResultVO<PmProductCharacteristicsEntity> result = new ResultVO<>();
        PmProductCharacteristicsEntity data = pmProductCharacteristicsService.getByVersionsAndName(versionsid, name, materialNo, isdelete);
        result.setData(data);
        return result;
    }

    /**
     * 获取特征主数据
     *
     * @return
     */
    @GetMapping(value = "/getcharacteristicsmaster")
    @Operation(summary = "获取特征主数据")
    public ResultVO<List<ProductCharacteristicsDTO>> getCharacteristicsMaster() {
        return new ResultVO<List<ProductCharacteristicsDTO>>().ok(pmProductCharacteristicsService.getCharacteristicsMaster());
    }

}