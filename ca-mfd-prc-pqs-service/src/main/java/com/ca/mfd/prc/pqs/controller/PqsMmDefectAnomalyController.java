package com.ca.mfd.prc.pqs.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.entity.PqsMmDefectAnomalyEntity;
import com.ca.mfd.prc.pqs.service.IPqsMmDefectAnomalyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 零部件缺陷记录Controller
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@RestController
@RequestMapping("pqsmmdefectanomaly")
@Tag(name = "零部件缺陷记录服务", description = "零部件缺陷记录")
public class PqsMmDefectAnomalyController extends BaseController<PqsMmDefectAnomalyEntity> {

    private final IPqsMmDefectAnomalyService pqsMmDefectAnomalyService;

    @Autowired
    public PqsMmDefectAnomalyController(IPqsMmDefectAnomalyService pqsMmDefectAnomalyService) {
        this.crudService = pqsMmDefectAnomalyService;
        this.pqsMmDefectAnomalyService = pqsMmDefectAnomalyService;
    }

    /**
     * 获取车辆缺陷分页
     *
     * @param para
     * @return
     */
    @PostMapping("getpagevehicledatas")
    @Operation(summary = "获取车辆缺陷分页")
    public ResultVO<IPage<PqsMmDefectAnomalyEntity>> getPageVehicleDatas(@RequestBody PageDataDto para) {
        return new ResultVO<IPage<PqsMmDefectAnomalyEntity>>().ok(pqsMmDefectAnomalyService.getDataByPage(para), "获取数据成功");
    }
}