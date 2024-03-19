package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.entity.PpsProductProcessAviEntity;
import com.ca.mfd.prc.pps.service.IPpsProductProcessAviService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @author inkelink
 * @Description: 工艺路径详细
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("ppsproductprocessavi")
@Tag(name = "工艺路径详细")
public class PpsProductProcessAviController extends BaseController<PpsProductProcessAviEntity> {

    private final IPpsProductProcessAviService ppsProductProcessAviService;

    @Autowired
    public PpsProductProcessAviController(IPpsProductProcessAviService pmProductProcessAviService) {
        this.crudService = pmProductProcessAviService;
        this.ppsProductProcessAviService = pmProductProcessAviService;
    }

    @Operation(summary = "根据车间查询工艺路径信息")
    @Parameters({
            @Parameter(name = "shopCode", description = "车间编号")})
    @GetMapping(value = "getpmproductprocessaviinfo")
    public ResultVO getPmProductProcessAviInfo(String shopCode) {
        if (StringUtils.isBlank(shopCode)) {
            throw new InkelinkException("车间编号不能为空");
        }
        return new ResultVO<>().ok(ppsProductProcessAviService.getData(
                Arrays.asList(new ConditionDto("WORKSHOP_CODE", shopCode, ConditionOper.Equal))));
    }

}