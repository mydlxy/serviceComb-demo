package com.ca.mfd.prc.avi.communication.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ca.mfd.prc.avi.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.avi.communication.dto.AsResultVo;
import com.ca.mfd.prc.avi.communication.dto.MidLmsAviQueueDto;
import com.ca.mfd.prc.avi.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.avi.communication.service.IMidApiLogService;
import com.ca.mfd.prc.avi.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.avi.service.IAviQueueReleaseService;
import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.IApiPtformService;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("communication/lmsaviqueue")
@Tag(name = "LMS_AVIQUEUE接口", description = "LMS_AVIQUEUE接口")
public class MidLmsAviQueueController extends BaseApiController {
    private static final Logger logger = LoggerFactory.getLogger(MidLmsAviQueueController.class);
    @Autowired
    private IMidApiLogService midApiLogService;

    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;

    @Autowired
    private IAviQueueReleaseService aviQueueReleaseService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("apiPtService")
    private IApiPtformService apiPtService;

    @GetMapping(value = "send")
    @Operation(summary = "Lms车辆过点信息(定时调度)")
    public ResultVO<String> send() {
        String reqNo = UUIDUtils.getGuid();
        logger.info("Lms车辆过点信息[" + reqNo + "]开始:");
        List<MidLmsAviQueueDto> fbacks = aviQueueReleaseService.getLmsAviQueueList();
        logger.info("Lms车辆过点信息[" + reqNo + "]开始查询数据:" + (fbacks == null ? 0 : fbacks.size()));
        //校验
        if (fbacks == null || fbacks.size() == 0) {
            throw new InkelinkException("没有需要上报的数据");
        }
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
                //String ars = "";
                logger.warn("API平台测试url调用：" + ars);
                AsResultVo asResultVo = JSONObject.parseObject(ars, AsResultVo.class);
                if (asResultVo != null) {
                    if (StringUtils.equals("1", asResultVo.getCode())) {
                        for (MidLmsAviQueueDto item : fbacks) {
                            ids.add(item.getUniqueCode());
                        }
                        if (ids.size() > 0) {
                            aviQueueReleaseService.updateLmsAviQueueStatus(ids);
                        }
                        status = 1;
                    } else {
                        status = 5;
                        errMsg = "LMS车辆过点记录[" + reqNo + "]处理失败:" + asResultVo.getMsg();
                        logger.info(errMsg);
                    }
                }
            } catch (Exception exception) {
                logger.error("", fbacks);
                status = 5;
                errMsg = "LMS车辆过点记录[" + reqNo + "]处理失败:"+exception.getMessage();
                logger.info(errMsg);
            }
        } catch (Exception ex) {
            status = 5;
            errMsg = "LMS车辆过点记录[" + reqNo + "]处理失败:"+ex.getMessage();
            logger.info(errMsg);
            logger.error(errMsg, ex);
            aviQueueReleaseService.clearChange();
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(errMsg);
        midApiLogService.update(loginfo);
        aviQueueReleaseService.saveChange();
        logger.info("LMS车辆过点记录[" + reqNo + "]执行完成:");
        if (status == 1) {
            return new ResultVO<String>().ok("", "处理成功");
        } else {
            return new ResultVO<String>().error(-1, errMsg);
        }
    }

    @GetMapping(value = "sendlmsqueue")
    @Operation(summary = "LMS车辆过点记录发送(定时调度)")
    public ResultVO<List<MidLmsAviQueueDto>> sendLmsQueue() {
        String reqNo = UUIDUtils.getGuid();
        //List<MidLmsAviQueueDto> fbacks = aviQueueReleaseService.getLmsAviQueueListBak();
        List<MidLmsAviQueueDto> fbacks = new ArrayList<>();
        MidLmsAviQueueDto test = new MidLmsAviQueueDto();
        test.setVin("");
        test.setLineCode("");
        fbacks.add(test);
        String apiPath = "";
        String ars = apiPtService.postapi(apiPath, fbacks, null);
//            ResponseEntity<ResultVO> responseEntity = null;
//            responseEntity =  restTemplate.postForEntity(apiPath, JSON.toJSONString(fbacks), ResultVO.class);
        //AsResultVo asResultVo = JSONObject.parseObject(responseEntity.getBody().toString(), AsResultVo.class);
        AsResultVo asResultVo = JSONObject.parseObject(ars, AsResultVo.class);
        int status = 1;
        String errMsg = "";
        if (asResultVo != null) {
            if (StringUtils.equals("1", asResultVo.getCode())) {
                status = 1;
            } else {
                status = 5;
                errMsg = "LMS车辆过点记录[" + reqNo + "]处理失败:" + asResultVo.getMsg();
                logger.info(errMsg);
            }
        }
        if (status == 1) {
            return new ResultVO<List<MidLmsAviQueueDto>>().ok(fbacks, "处理成功");
        } else {
            return new ResultVO<List<MidLmsAviQueueDto>>().error(-1, errMsg);
        }
    }

}
