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
import com.ca.mfd.prc.pps.communication.dto.MidAsAviPointDto;
import com.ca.mfd.prc.pps.communication.dto.MidLmsAviQueueDto;
import com.ca.mfd.prc.pps.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pps.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pps.entity.PpsAsAviPointEntity;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pps.service.IPpsAsAviPointService;
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
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("communication/avilmspoint")
@Tag(name = "lms车辆过点记录服务", description = "lms车辆过点记录")
public class MidLmsAviPointController extends BaseApiController {
    private static final Logger logger = LoggerFactory.getLogger(MidLmsAviPointController.class);

    @Autowired
    private IMidApiLogService midApiLogService;
    @Autowired
    private IPpsAsAviPointService midAsAviPointService;
    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;

    @Autowired
    @Qualifier("apiPtService")
    private IApiPtformService apiPtService;

    @GetMapping(value = "send")
    @Operation(summary = "LMS车辆过点记录发送(定时调度)")
    public ResultVO<String> send() {
        List<MidLmsAviQueueDto> fbacks;
        String reqNo = UUIDUtils.getGuid();
        logger.info("lms车辆过点记录[" + reqNo + "]开始:");
        fbacks = midAsAviPointService.getNoLmsSendList(500);
        logger.info("lms车辆过点记录[" + reqNo + "]开始查询数据:" + (fbacks == null ? 0 : fbacks.size()));
        //校验
        if (fbacks == null || fbacks.size() == 0) {
            throw new InkelinkException("没有需要上报的数据");
        }
        fbacks = createMidLmsAviQueueDto(fbacks);
        String json = JsonUtils.toJsonString(fbacks);
        String apiPath = sysConfigurationProvider.getConfiguration("lmsaviqueue_send", "midapi");
        if (StringUtils.isBlank(apiPath)) {
            throw new InkelinkException("没有配置上报的地址[lmsaviqueue_send]");
        }
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.LMS_QUEUE_START);
        loginfo.setDataLineNo(fbacks.size());
        loginfo.setRequestStartTime(new Date());
        int status = 1;
        String errMsg = "";
        try {
            loginfo.setStatus(0);
            midApiLogService.insert(loginfo);
            midApiLogService.saveChange();
            loginfo.setDataLineNo(fbacks.size());
            try {
                String sendData = JsonUtils.toJsonString(fbacks);
                loginfo.setReqData(sendData);

                List<Long> ids = new ArrayList<>();
                String ars = apiPtService.postapi(apiPath, fbacks, null);
                loginfo.setResponseData(ars);
                AsResultVo asResultVo = JSONObject.parseObject(ars, AsResultVo.class);
                if (asResultVo != null) {
                    if (StringUtils.equals("1", asResultVo.getCode())) {
                        for (MidLmsAviQueueDto item : fbacks) {
                            ids.add(item.getUniqueCode());
                        }
                        if (ids.size() > 0) {
                            midAsAviPointService.updatePartSendStatus(ids);
                        }
                        status = 1;
                    } else {
                        status = 5;
                        errMsg = "LMS车辆过点记录[" + reqNo + "]处理失败:" + asResultVo.getMsg();
                        logger.info(errMsg);
                    }
                }
            } catch (Exception ex) {
                logger.error("", fbacks);
                status = 5;
                errMsg = "LMS车辆过点记录[" + reqNo + "]处理失败:" + ex.getMessage();
                logger.info(errMsg);
            }
        } catch (Exception ex) {
            status = 5;
            errMsg = "LMS车辆过点记录[" + reqNo + "]处理失败:" + ex.getMessage();
            logger.info(errMsg);
            logger.error(errMsg, ex);
            midApiLogService.clearChange();
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(com.ca.mfd.prc.common.utils.StringUtils.getSubStr(errMsg,1000));
        midApiLogService.update(loginfo);
        midAsAviPointService.saveChange();
        logger.info("LMS车辆过点记录[" + reqNo + "]执行完成:");
        if (status == 1) {
            return new ResultVO<String>().ok("", "处理成功");
        } else {
            return new ResultVO<String>().error(-1, errMsg);
        }
    }

    private List<MidLmsAviQueueDto> createMidLmsAviQueueDto(List<MidLmsAviQueueDto> list) {
        if (list == null) {
            return null;
        }
        List<MidLmsAviQueueDto> result = new ArrayList<>();
        for (MidLmsAviQueueDto items : list) {
            //items.setVid("");
            items.setProductType("1");
            items.setOneId("");
            items.setManager("");
            result.add(items);
        }
        return result;
    }
}
