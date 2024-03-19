package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.entity.EpsAgvMonitoringEntity;
import com.ca.mfd.prc.eps.service.IEpsAgvMonitoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * AGV监听
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("epsagvmonitoring")
@Tag(name = "AGV监听")
public class EpsAgvMonitoringController extends BaseController<EpsAgvMonitoringEntity> {

    private final IEpsAgvMonitoringService epsAgvMonitoringService;

    @Autowired
    public EpsAgvMonitoringController(IEpsAgvMonitoringService epsAgvMonitoringService) {
        this.crudService = epsAgvMonitoringService;
        this.epsAgvMonitoringService = epsAgvMonitoringService;
    }

    @Operation(summary = "获取未逻辑删除的列表数据")
    @PostMapping("/provider/getdata")
    public ResultVO<List<EpsAgvMonitoringEntity>> getData(@RequestBody List<ConditionDto> conditions) {
        return new ResultVO<List<EpsAgvMonitoringEntity>>().ok(epsAgvMonitoringService.getData(conditions));
    }

}