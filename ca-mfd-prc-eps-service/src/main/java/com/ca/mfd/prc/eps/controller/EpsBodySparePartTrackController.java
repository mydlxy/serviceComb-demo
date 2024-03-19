package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.dto.CallingCarrierPara;
import com.ca.mfd.prc.eps.dto.ReleasePassSuccessPara;
import com.ca.mfd.prc.eps.entity.EpsBodySparePartTrackEntity;
import com.ca.mfd.prc.eps.service.IEpsBodySparePartTrackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 焊装车间备件运输跟踪Controller
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@RestController
@RequestMapping("epsbodyspareparttrack")
@Tag(name = "焊装车间备件运输跟踪服务", description = "焊装车间备件运输跟踪")
public class EpsBodySparePartTrackController extends BaseController<EpsBodySparePartTrackEntity> {

    private final IEpsBodySparePartTrackService epsBodySparePartTrackService;

    @Autowired
    public EpsBodySparePartTrackController(IEpsBodySparePartTrackService epsBodySparePartTrackService) {
        this.crudService = epsBodySparePartTrackService;
        this.epsBodySparePartTrackService = epsBodySparePartTrackService;
    }

    /**
     * 根据 虚拟VIN号 查询备件运输跟踪
     *
     * @param vehicleSn 虚拟VIN号
     * @return 返回 焊装车间备件运输跟踪
     */
    @Operation(summary = "根据 虚拟VIN号 查询备件运输跟踪")
    @GetMapping("/provider/getentitybyvirtualvin")
    public ResultVO<EpsBodySparePartTrackEntity> getEntityByVirtualVin(String vehicleSn) {
        EpsBodySparePartTrackEntity entity = epsBodySparePartTrackService.getEntityByVirtualVin(vehicleSn);
        return new ResultVO<EpsBodySparePartTrackEntity>().ok(entity, "查询成功");
    }

    @Operation(summary = "呼叫空撬")
    @PostMapping("callingcarrier")
    public ResultVO<String> callingCarrier(@RequestBody CallingCarrierPara para) {
        epsBodySparePartTrackService.callingCarrier(para.getWorkstationCode());
        epsBodySparePartTrackService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @Operation(summary = "空撬放行成功")
    @PostMapping("releasepasssuccess")
    public ResultVO<String> releasePassSuccess(@RequestBody ReleasePassSuccessPara para) {
        epsBodySparePartTrackService.releasePassSuccess(para.getVirtualVin());
        epsBodySparePartTrackService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

}