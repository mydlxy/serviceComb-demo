package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ComboDataDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.entity.EpsSpareConfigEntity;
import com.ca.mfd.prc.eps.service.IEpsSpareConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author inkelink
 * @Description: 区域对应备件配置Controller
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@RestController
@RequestMapping("epsspareconfig")
@Tag(name = "区域对应备件配置服务", description = "区域对应备件配置")
public class EpsSpareConfigController extends BaseController<EpsSpareConfigEntity> {

    private final IEpsSpareConfigService epsSpareConfigService;

    @Autowired
    public EpsSpareConfigController(IEpsSpareConfigService epsSpareConfigService) {
        this.crudService = epsSpareConfigService;
        this.epsSpareConfigService = epsSpareConfigService;
    }


    @Operation(summary = "获取线体物料清单")
    @GetMapping("getsparecombo")
    public ResultVO<List<ComboDataDTO>> getSpareCombo(String lineCode) {
        List<ComboDataDTO> datas = epsSpareConfigService.getByLineCodes(lineCode);
        return new ResultVO<List<ComboDataDTO>>().ok(datas, "操作成功");
    }

}