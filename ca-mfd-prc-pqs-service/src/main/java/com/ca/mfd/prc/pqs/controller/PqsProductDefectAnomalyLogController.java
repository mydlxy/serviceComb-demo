package com.ca.mfd.prc.pqs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.dto.ProductDefectAnomalyLogInfo;
import com.ca.mfd.prc.pqs.entity.PqsProductDefectAnomalyLogEntity;
import com.ca.mfd.prc.pqs.service.IPqsProductDefectAnomalyLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author inkelink
 * @Description: 产品缺陷日志Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsproductdefectanomalylog")
@Tag(name = "产品缺陷日志服务", description = "产品缺陷日志")
public class PqsProductDefectAnomalyLogController extends BaseController<PqsProductDefectAnomalyLogEntity> {

    private final IPqsProductDefectAnomalyLogService pqsProductDefectAnomalyLogService;

    @Autowired
    public PqsProductDefectAnomalyLogController(IPqsProductDefectAnomalyLogService pqsProductDefectAnomalyLogService) {
        this.crudService = pqsProductDefectAnomalyLogService;
        this.pqsProductDefectAnomalyLogService = pqsProductDefectAnomalyLogService;
    }

    /**
     * 根据数据编号获取日志列表
     *
     * @param dataId
     * @return
     */
    @GetMapping("getvehicledefectanomalylog")
    @Operation(summary = "根据数据编号获取日志列表")
    public ResultVO<List<ProductDefectAnomalyLogInfo>> getVehicleDefectAnomalyLog(String dataId) {
        return new ResultVO<List<ProductDefectAnomalyLogInfo>>().ok(pqsProductDefectAnomalyLogService.getVehicleDefectAnomalyLog(dataId), "获取数据成功");
    }

    /**
     * 获取激活缺陷日志
     *
     * @param dataId ID
     * @return 查询结果
     */
    @GetMapping("/provider/getvehicledefectanomalylog")
    @Operation(summary = "获取激活缺陷日志")
    public ResultVO<List<ProductDefectAnomalyLogInfo>> getProviderVehicleDefectAnomalyLog(@RequestParam String dataId) {
        ResultVO<List<ProductDefectAnomalyLogInfo>> result = new ResultVO<>();
        List<ProductDefectAnomalyLogInfo> data = pqsProductDefectAnomalyLogService.getVehicleDefectAnomalyLog(dataId);
        return result.ok(data);
    }
}