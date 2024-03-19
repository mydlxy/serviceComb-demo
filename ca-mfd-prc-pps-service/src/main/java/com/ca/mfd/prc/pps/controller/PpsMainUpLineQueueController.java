package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.dto.LmsWeOnlineQueueDTO;
import com.ca.mfd.prc.pps.entity.PpsMainUpLineQueueEntity;
import com.ca.mfd.prc.pps.service.IPpsMainUpLineQueueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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
@RequestMapping("ppsmainuplinequeue")
@Tag(name = "焊装主体上线队列服务", description = "焊装主体上线队列")
public class PpsMainUpLineQueueController extends BaseController<PpsMainUpLineQueueEntity> {

    private IPpsMainUpLineQueueService ppsMainUpLineQueueService;

    @Autowired
    public PpsMainUpLineQueueController(IPpsMainUpLineQueueService ppsMainUpLineQueueService) {
        this.crudService = ppsMainUpLineQueueService;
        this.ppsMainUpLineQueueService = ppsMainUpLineQueueService;
    }

}