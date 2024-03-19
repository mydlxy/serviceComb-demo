package com.ca.mfd.prc.avi.communication.controller;

import com.alibaba.fastjson.JSONObject;
import com.ca.mfd.prc.avi.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.avi.communication.dto.AsResultVo;
import com.ca.mfd.prc.avi.communication.dto.MidAsWbsPbsDto;
import com.ca.mfd.prc.avi.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.avi.communication.service.IMidApiLogService;
import com.ca.mfd.prc.avi.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.avi.service.IAviQueueReleaseService;
import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.DateUtils;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author inkelink
 * @Description: AS_WBSPBS接口Controller
 * @date 2023年10月11日
 * @变更说明 BY inkelink At 2023年10月11日
 */
@RestController
@RequestMapping("communication/wbspbs")
@Tag(name = "AS_WBSPBS接口", description = "AS_WBSPBS接口")
public class MidAsPbsWbsController extends BaseApiController {
    private static final Logger logger = LoggerFactory.getLogger(MidAsPbsWbsController.class);
    @Autowired
    private IMidApiLogService midApiLogService;
    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;
    @Autowired
    private IAviQueueReleaseService aviQueueReleaseService;

    @Autowired
    @Qualifier("apiPtService")
    private IApiPtformService apiPtService;

    @GetMapping(value = "send")
    @Operation(summary = "AS_WBSPBS记录发送(定时调度)")
    public ResultVO<String> send() {
        List<MidAsWbsPbsDto> fbacks = new ArrayList<>();
        String reqNo = UUIDUtils.getGuid();

        String apiPath = sysConfigurationProvider.getConfiguration("aswbspbs_send", "midapi");
        if (StringUtils.isBlank(apiPath)) {
            throw new InkelinkException("没有配置上报的地址[aswbspbs_send]");
        }
        logger.info("AS_WBSPBS记录[" + reqNo + "]开始:");
        fbacks = getWbsPbsData();
        logger.info("AS_WBSPBS记录[" + reqNo + "]查询数据结束:" + fbacks.size());
        //校验
        if (fbacks.isEmpty()) {
            throw new InkelinkException("没有需要上报的数据");
        }
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.AS_WBS_PBS);
        loginfo.setDataLineNo(fbacks.size());
        loginfo.setRequestStartTime(new Date());
        int status = 1;
        String errMsg = "";
        try {
            loginfo.setStatus(0);
            midApiLogService.insert(loginfo);
            midApiLogService.saveChange();

            loginfo.setDataLineNo(fbacks.size());
            String sendData = JsonUtils.toJsonString(fbacks);
            loginfo.setReqData(sendData);
            String ars = apiPtService.postapi(apiPath, fbacks, null);
            loginfo.setResponseData(ars);
            try {
                ////rowCount
                logger.warn("API平台测试url调用：" + ars);
                AsResultVo asResultVo = JSONObject.parseObject(ars, AsResultVo.class);
                if (asResultVo != null) {
                    if (StringUtils.equals("1", asResultVo.getCode())) {
                        status = 1;
                    } else {
                        status = 5;
                        errMsg = "AS_WBSPBS记录[" + reqNo + "]处理失败:" + asResultVo.getMsg();
                        logger.info(errMsg);
                    }
                }
            } catch (Exception eas) {
                logger.error("", eas);
                status = 1;
                errMsg = "AS_WBSPBS记录[" + reqNo + "]处理失败:";
                logger.info(errMsg);
            }

        } catch (Exception ex) {
            status = 5;
            errMsg = "AS_WBSPBS记录[" + reqNo + "]处理失败:";
            logger.info(errMsg);
            logger.error(errMsg, ex);
            midApiLogService.clearChange();
        }

        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(errMsg);
        midApiLogService.update(loginfo);
        midApiLogService.saveChange();

        logger.info("AS_WBSPBS记录[" + reqNo + "]执行完成:");
        if (status == 1) {
            return new ResultVO<String>().ok("", "处理成功");
        } else {
            return new ResultVO<String>().error(-1, errMsg);
        }
    }

    private List<MidAsWbsPbsDto> getWbsPbsData() {
        List<MidAsWbsPbsDto> fbacks = new ArrayList<>();
        try {
            List<MidAsWbsPbsDto> pbsDatas = midApiLogService.sendPbsWbs("PBS");
            if (!pbsDatas.isEmpty()) {
                fbacks.addAll(pbsDatas);
            }
        } catch (Exception e) {
            logger.error("AS_PBS记录查询失败", e);
        }
        try {
            List<MidAsWbsPbsDto> wbsDatas = midApiLogService.sendPbsWbs("WBS");

            if (!wbsDatas.isEmpty()) {
                fbacks.addAll(wbsDatas);
            }
        } catch (Exception e) {
            logger.error("AS_WBS记录查询失败", e);
        }
        return fbacks;
    }

    @GetMapping(value = "getsenddata")
    @Operation(summary = "AS_WBSPBS记录发送test")
    public ResultVO<List<MidAsWbsPbsDto>> getSendData() {
        List<MidAsWbsPbsDto> fbacks = getWbsPbsData();
        return new ResultVO<List<MidAsWbsPbsDto>>().ok(fbacks);
    }

    @GetMapping(value = "test")
    @Operation(summary = "AS_WBSPBS记录发送test")
    public ResultVO<String> test() {
        String apiPath = sysConfigurationProvider.getConfiguration("aswbspbs_send", "midapi");
        if (StringUtils.isBlank(apiPath)) {
            throw new InkelinkException("没有配置上报的地址[aswbspbs_send]");
        }
        List<MidAsWbsPbsDto> dtos = new ArrayList<>();
        MidAsWbsPbsDto et = new MidAsWbsPbsDto();
        et.setOrgCode("CA");
        et.setWbspbs("wbs");
        et.setEntryTime(new Date());
        et.setExitTime(DateUtils.addDateSeconds(new Date(), 21));
        et.setStayTime(22);
        et.setVin("L1NSPGHB1PB080534");
        et.setVrn("1000009");
        dtos.add(et);
        String ars = apiPtService.postapi(apiPath, dtos, null);
        return new ResultVO<String>().error(-1, ars);
    }
}