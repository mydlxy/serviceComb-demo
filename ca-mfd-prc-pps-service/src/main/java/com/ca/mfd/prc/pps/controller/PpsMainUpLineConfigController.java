package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.dto.LmsWeOnlineQueueDTO;
import com.ca.mfd.prc.pps.entity.PpsMainUpLineConfigEntity;
import com.ca.mfd.prc.pps.service.IPpsMainUpLineConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @Description: 焊装主体上线队列Controller
 * @author inkelink
 * @date 2024年01月18日
 * @变更说明 BY inkelink At 2024年01月18日
 */
@RestController
@RequestMapping("ppsmainuplineconfig")
@Tag(name = "焊装主体上线队列服务", description = "焊装主体上线队列")
public class PpsMainUpLineConfigController extends BaseController<PpsMainUpLineConfigEntity> {

    private IPpsMainUpLineConfigService ppsMainUpLineConfigService;

    @Autowired
    public PpsMainUpLineConfigController(IPpsMainUpLineConfigService ppsMainUpLineConfigService) {
        this.crudService = ppsMainUpLineConfigService;
        this.ppsMainUpLineConfigService = ppsMainUpLineConfigService;
    }

    @Operation(summary = "获取lms焊装工单上线队列")
    @GetMapping("getweonlinequeue")
    public ResultVO getWeOnlineQueue(String key) {
        List<String> subCodes = new ArrayList<>();
        subCodes.add(key);
        List<LmsWeOnlineQueueDTO> datas = ppsMainUpLineConfigService.getWeOnlineQueue(subCodes, false, false);
        return new ResultVO<List<LmsWeOnlineQueueDTO>>()
                .ok(datas);
    }

}