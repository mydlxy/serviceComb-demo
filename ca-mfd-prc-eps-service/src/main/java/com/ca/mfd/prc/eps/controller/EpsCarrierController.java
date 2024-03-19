package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.dto.IdModel;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.dto.BindCarrierPara;
import com.ca.mfd.prc.eps.dto.CarrierMaterialsResponse;
import com.ca.mfd.prc.eps.dto.EmptyCarrierPara;
import com.ca.mfd.prc.eps.entity.EpsCarrierEntity;
import com.ca.mfd.prc.eps.entity.EpsCarrierLogEntity;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsEntryReportPartsEntity;
import com.ca.mfd.prc.eps.remote.app.pps.provider.PpsEntryReportPartsProvider;
import com.ca.mfd.prc.eps.service.IEpsCarrierLogService;
import com.ca.mfd.prc.eps.service.IEpsCarrierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @Description: 载具信息Controller
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@RestController
@RequestMapping("epscarrier")
@Tag(name = "载具信息服务", description = "载具信息")
public class EpsCarrierController extends BaseController<EpsCarrierEntity> {

    @Autowired
    private PpsEntryReportPartsProvider ppsEntryReportPartsProvider;

    @Autowired
    private IEpsCarrierLogService epsCarrierLogService;

    private IEpsCarrierService epsCarrierService;

    @Autowired
    public EpsCarrierController(IEpsCarrierService epsCarrierService) {
        this.crudService = epsCarrierService;
        this.epsCarrierService = epsCarrierService;
    }

    @Operation(summary = "获取载具上面的货物")
    @GetMapping("getcarriergoods")
    public ResultVO<CarrierMaterialsResponse> getCarrierGoods(String barCode) {
        CarrierMaterialsResponse data = epsCarrierService.getCarrierMaterials(barCode);
        return new ResultVO<CarrierMaterialsResponse>().ok(data, "操作成功");
    }

    @Operation(summary = "移除单个物料")
    @PostMapping("removecarriergoods")
    public ResultVO<String> removeCarrierGoods(@RequestBody IdModel para) {
        EpsCarrierLogEntity logInfo = epsCarrierLogService.get(ConvertUtils.stringToLong(para.getId()));
        if (logInfo == null) {
            throw new InkelinkException("未找到绑定信息！");
        }
        epsCarrierService.useCarrierByEntryReportNo(logInfo.getEntryReportNo());
        epsCarrierService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @Operation(summary = "清空载具")
    @PostMapping("emptycarrier")
    public ResultVO<String> emptyCarrier(@RequestBody EmptyCarrierPara request) {
        epsCarrierService.emptyCarrier(request.getCarrierCode());
        epsCarrierService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @Operation(summary = "单件绑定载具")
    @PostMapping("bindcarrier")
    public ResultVO<String> bindCarrier(@RequestBody BindCarrierPara bindCarrierPara) {
        PpsEntryReportPartsEntity entryReportInfo = ppsEntryReportPartsProvider.getFirstByBarcode(bindCarrierPara.getBarcode());
        if (entryReportInfo == null) {
            throw new InkelinkException("无效的单件条码");
        }
        if (StringUtils.isBlank(bindCarrierPara.getCarrierCode())) {
            throw new InkelinkException("非法的载具条码");
        }
        entryReportInfo.setCarrierCode(bindCarrierPara.getCarrierCode());
        epsCarrierService.bindCarrier(entryReportInfo);
        epsCarrierService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

}