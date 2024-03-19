package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.dto.VerifyRivetPara;
import com.ca.mfd.prc.eps.entity.EpsRivetFcConfigEntity;
import com.ca.mfd.prc.eps.service.IEpsRivetFcConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 铆钉防错配置Controller
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@RestController
@RequestMapping("epsrivetfcconfig")
@Tag(name = "铆钉防错配置服务", description = "铆钉防错配置")
public class EpsRivetFcConfigController extends BaseController<EpsRivetFcConfigEntity> {

    private final IEpsRivetFcConfigService epsRivetFcConfigService;

    @Autowired
    public EpsRivetFcConfigController(IEpsRivetFcConfigService epsRivetFcConfigService) {
        this.crudService = epsRivetFcConfigService;
        this.epsRivetFcConfigService = epsRivetFcConfigService;
    }

    @Operation(summary = "校验料口上料")
    @PostMapping("verifyrivet")
    public ResultVO<String> verifyrivet(@RequestBody VerifyRivetPara para) {
        Boolean sucess = epsRivetFcConfigService.verifyRivet(para);
        epsRivetFcConfigService.saveChange();
        if (!sucess) {
            throw new InkelinkException("物料与料口不匹配");
        }
        return new ResultVO<String>().ok("", "操作成功");
    }


}