package com.ca.mfd.prc.avi.communication.controller;

import com.ca.mfd.prc.avi.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.avi.communication.dto.IotResponseBase;
import com.ca.mfd.prc.avi.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.avi.communication.service.IMidApiLogService;
import com.ca.mfd.prc.avi.remote.app.pm.dto.AviInfoDTO;
import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("communication/listeniotdata")
@Tag(name = "IOT接口", description = "IOT变化上报接口")
public class MidIotListenDataController extends BaseApiController {
    private static final Logger logger = LoggerFactory.getLogger(MidIotListenDataController.class);

    @Autowired
    private IMidApiLogService midApiLogService;

    @PostMapping(value = "receive")
    @Operation(summary = "监听IOT平台变化上报的接口")
    public ResultVO<String> receive(@RequestBody IotResponseBase data) {
        ResultVO<String> result = new ResultVO<>();
        try {
            logger.info("获取到IOT数据,数据内容:" + JsonUtils.toJsonString(data));
            MidApiLogEntity loginfo = new MidApiLogEntity();
            loginfo.setApiType(ApiTypeConst.IOT_QUEUE_START);
            loginfo.setDataLineNo(1);
            loginfo.setRequestStartTime(new Date());
            loginfo.setStatus(1);
            //loginfo.setRemark(JsonUtils.toJsonString(data));
            midApiLogService.insert(loginfo);
            midApiLogService.saveChange();
        } catch (Exception ex) {
            logger.error("获取IOT数据异常,Ex:" + ex.getMessage());
            return result.ok("获取数据失败");
        }
        return result.ok("获取数据成功");
    }
}
