package com.ca.mfd.prc.pps.communication.controller;

import com.alibaba.fastjson.JSONObject;
import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.IApiPtformService;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.pps.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pps.communication.dto.AsResultVo;
import com.ca.mfd.prc.pps.communication.dto.MidLmsPartQueueDto;
import com.ca.mfd.prc.pps.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pps.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pps.entity.PpsAsAviPointEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanPartsEntity;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pps.service.IPpsAsAviPointService;
import com.ca.mfd.prc.pps.service.IPpsMainUpLineConfigService;
import com.ca.mfd.prc.pps.service.IPpsMainUpLineQueueService;
import com.ca.mfd.prc.pps.service.IPpsPlanPartsService;
import com.ca.mfd.prc.pps.service.IPpsPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("communication/lmsweonqueue")
@Tag(name = "LMS_WEOnQUEUE接口", description = "焊装上线队列")
public class MidLmsWeOnlineQueueController extends BaseApiController {
    private static final Logger logger = LoggerFactory.getLogger(MidLmsWeOnlineQueueController.class);

    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;

    @Autowired
    private IPpsMainUpLineConfigService ppsMainUpLineConfigService;


    @GetMapping(value = "send")
    @Operation(summary = "Lms焊装上线队列(定时调度)")
    public ResultVO<String> send() {
        logger.info("Lms焊装上线队列开始:");
        String wequeueCodes = sysConfigurationProvider.getConfiguration("lmsweonqueue_code", "midapi");
        if (StringUtils.isBlank(wequeueCodes) || wequeueCodes.trim().split(",").length == 0) {
            return new ResultVO<String>().ok("没有配置订阅码【lmsweonqueue_code】");
        }
        List<String> subCodes = Arrays.asList(wequeueCodes.trim().split(","));
        ppsMainUpLineConfigService.getWeOnlineQueue(subCodes, true, true);
        return new ResultVO<String>().ok("", "处理成功");
    }

}