package com.ca.mfd.prc.pps.communication.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.communication.dto.SoftwareBomListDto;
import com.ca.mfd.prc.pps.communication.entity.MidSoftwareBomListEntity;
import com.ca.mfd.prc.pps.communication.service.IMidSoftwareBomListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @Description: 单车软件清单Controller
 * @author inkelink
 * @date 2023年11月23日
 * @变更说明 BY inkelink At 2023年11月23日
 */
@RestController
@RequestMapping("communication/midsoftwarebomlist")
@Tag(name = "单车软件清单服务", description = "单车软件清单")
public class MidSoftwareBomListController  extends BaseController<MidSoftwareBomListEntity> {

    @Autowired
    private IMidSoftwareBomListService softwareBomListService;

    @GetMapping(value = "getsoftbom")
    @Operation(summary = "获取软件清单")
    public List<SoftwareBomListDto> getSoftBom(String materialNo,String effectiveDate) {
        return softwareBomListService.getSoftBom(materialNo,effectiveDate);
    }

    @GetMapping(value = "/provider/getsoftbom")
    @Operation(summary = "获取软件清单-远程调用")
    public ResultVO<List<SoftwareBomListDto>> providerGetSoftBom(String materialNo, String effectiveDate) {
        return new ResultVO<List<SoftwareBomListDto>>().ok(softwareBomListService.getSoftBom(materialNo,effectiveDate));
    }

}