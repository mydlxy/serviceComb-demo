package com.ca.mfd.prc.pmc.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.DataDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pmc.dto.PmcAlarmAreaStopRecordDto;
import com.ca.mfd.prc.pmc.entity.PmcAlarmAreaStopRecordEntity;
import com.ca.mfd.prc.pmc.service.IPmcAlarmAreaStopRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 停线记录(安灯使用)
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("pmcalarmareastoprecord")
@Tag(name = "停线记录(安灯使用)")
public class PmcAlarmAreaStopRecordController extends BaseController<PmcAlarmAreaStopRecordEntity> {

    private final IPmcAlarmAreaStopRecordService pmcAlarmAreaStopRecordService;

    @Autowired
    public PmcAlarmAreaStopRecordController(IPmcAlarmAreaStopRecordService pmcAlarmAreaStopRecordService) {
        this.crudService = pmcAlarmAreaStopRecordService;
        this.pmcAlarmAreaStopRecordService = pmcAlarmAreaStopRecordService;
    }

    @GetMapping(value = "getall")
    @Operation(summary = "获取所有数据")
    public ResultVO<List<PmcAlarmAreaStopRecordEntity>> getall() {
        return new ResultVO<List<PmcAlarmAreaStopRecordEntity>>().ok(pmcAlarmAreaStopRecordService.getData(null), "获取数据成功");
    }

    @GetMapping("/provider/getrecords")
    @Operation(summary = "获取停线记录")
    public ResultVO<List<PmcAlarmAreaStopRecordDto>> providerGetRecords() {
        QueryWrapper<PmcAlarmAreaStopRecordEntity> qry = new QueryWrapper<>();
        List<PmcAlarmAreaStopRecordEntity> dataList = pmcAlarmAreaStopRecordService.getData(qry, true);
        List<PmcAlarmAreaStopRecordDto> dtoList;
        if (CollectionUtils.isNotEmpty(dataList)) {
            dtoList = dataList.stream().map(data -> {
                PmcAlarmAreaStopRecordDto dto = new PmcAlarmAreaStopRecordDto();
                BeanUtil.copyProperties(data, dto);
                return dto;
            }).collect(Collectors.toList());
        } else {
            dtoList = new ArrayList<>();
        }
        ResultVO<List<PmcAlarmAreaStopRecordDto>> res = new ResultVO<>();
        return res.ok(dtoList);
    }

}