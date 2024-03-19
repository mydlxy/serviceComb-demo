package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ComboDataDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.entity.EpsFurnaceConfigEntity;
import com.ca.mfd.prc.eps.service.IEpsFurnaceConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @Description: 熔化炉配置Controller
 * @author inkelink
 * @date 2023年10月25日
 * @变更说明 BY inkelink At 2023年10月25日
 */
@RestController
@RequestMapping("epsfurnaceconfig")
@Tag(name = "熔化炉配置服务", description = "熔化炉配置")
public class EpsFurnaceConfigController extends BaseController<EpsFurnaceConfigEntity> {

    private IEpsFurnaceConfigService epsFurnaceConfigService;

    @Autowired
    public EpsFurnaceConfigController(IEpsFurnaceConfigService epsFurnaceConfigService) {
        this.crudService = epsFurnaceConfigService;
        this.epsFurnaceConfigService = epsFurnaceConfigService;
    }

    @Operation(summary = "获取熔炉配置")
    @PostMapping("getfurnacecombo")
    public ResultVO<List<ComboDataDTO>> getFurnaceCombo() {
        List<ComboDataDTO> datas = epsFurnaceConfigService.getData(null).stream().map(c -> {
            ComboDataDTO et = new ComboDataDTO();
            et.setValue(c.getFurnaceNo());
            et.setLabel(c.getFurnaceNo());
            return et;
        }).collect(Collectors.toList());
        return new ResultVO<List<ComboDataDTO>>().ok(datas, "操作成功");
    }

}