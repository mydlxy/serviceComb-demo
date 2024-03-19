package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.entity.PpsVtVinRuleEntity;
import com.ca.mfd.prc.pps.entity.PpsVtVinYearEntity;
import com.ca.mfd.prc.pps.service.IPpsVtVinYearService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * VIN号年份配置
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@RestController
@RequestMapping("ppsvtvinyear")
@Tag(name = "VIN号年份配置")
public class PpsVtVinYearController extends BaseController<PpsVtVinYearEntity> {

    private final IPpsVtVinYearService ppsVtVinYearService;

    @Autowired
    public PpsVtVinYearController(IPpsVtVinYearService ppsVtVinYearService) {
        this.crudService = ppsVtVinYearService;
        this.ppsVtVinYearService = ppsVtVinYearService;
    }

    @Operation(summary = "获取所有")
    @GetMapping("getyears")
    public ResultVO getyears() {
        List<PpsVtVinYearEntity> list = ppsVtVinYearService.getData(null);
        List<ComboInfoDTO> dtos = new ArrayList<>();
        list.stream().forEach(c -> {
            ComboInfoDTO et = new ComboInfoDTO();
            et.setText(c.getYear().toString());
            et.setValue(c.getVinYearCode());
            dtos.add(et);
        });
        return new ResultVO<List<ComboInfoDTO>>().ok(dtos, "获取列表成功！");
    }

}