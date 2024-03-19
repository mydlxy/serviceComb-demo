package com.ca.mfd.prc.pps.communication.controller;

import com.alibaba.fastjson.JSONObject;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.dto.as.AsResultDto;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IApiPtformService;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.pps.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pps.communication.dto.AsBatchPiecesDto;
import com.ca.mfd.prc.pps.communication.dto.AsResultVo;
import com.ca.mfd.prc.pps.communication.dto.MidAsShopPlanFeedbackDto;
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
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: AS批次件进度反馈 Controller
 * @date 2023年10月11日
 * @变更说明 BY inkelink At 2023年10月11日
 */
@RestController
@RequestMapping("communication/entryreportparts")
@Tag(name = "AS批次件进度反馈", description = "AS批次件进度反馈")
public class MidAsBatchPiecesController extends BaseApiController {
    private static final Logger logger = LoggerFactory.getLogger(MidAsBatchPiecesController.class);
    @Autowired
    private IMidApiLogService midApiLogService;

    @Autowired
    private IPpsAsAviPointService ppsAsAviPointService;
    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;
    @Autowired
    @Qualifier("apiPtService")
    private IApiPtformService apiPtService;

    @GetMapping(value = "send")
    @Operation(summary = "AS批次件进度反馈(定时调度)")
    public ResultVO<String> send() {
        String reqNo = UUIDUtils.getGuid();
        logger.info("AS批次件进度反馈[" + reqNo + "]开始:");
        List<PpsAsAviPointEntity> list = ppsAsAviPointService.getNoAsBatchPieces(500);
        List<AsBatchPiecesDto> baths = midApiLogService.getAsBatchPieces(list);
        logger.info("AS批次件进度反馈[" + reqNo + "]查询数据:" + (baths == null ? 0 : baths.size()));
        if (baths == null || baths.isEmpty()) {
            if(!list.isEmpty()) {
                updateAsAviAsSendFlag(list, Constant.DEFAULT_ID, 3);
                midApiLogService.saveChange();
            }
            throw new InkelinkException("没有需要上报的数据");
        }
        String apiPath = sysConfigurationProvider.getConfiguration("asbatchpieces_send", "midapi");
        if (StringUtils.isBlank(apiPath)) {
            throw new InkelinkException("没有配置上报的地址[asbatchpieces_send]");
        }
        Long logId = IdGenerator.getId();
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setId(logId);
        loginfo.setApiType(ApiTypeConst.AS_BATCH_PIECES);
        loginfo.setDataLineNo(baths.size());
        loginfo.setRequestStartTime(new Date());
        int status = 1;
        String errMsg = "";
        try {
            loginfo.setStatus(0);
            midApiLogService.insert(loginfo);
            midApiLogService.saveChange();

            baths = baths.stream().distinct().collect(Collectors.toList());
            loginfo.setDataLineNo(baths.size());

            try {
                AsResultDto<AsBatchPiecesDto> apiData = new AsResultDto(baths, "MOM", "batchprogressfeedback");
                String sendData = JsonUtils.toJsonString(baths);
                loginfo.setReqData(sendData);
                String res = apiPtService.postapi(apiPath, apiData, null);
                loginfo.setResponseData(res);
                logger.warn("API平台测试url调用：" + res);
                AsResultVo asResultVo = JSONObject.parseObject(res, AsResultVo.class);
                if (asResultVo != null) {
                    if (StringUtils.equals("1", asResultVo.getCode())) {
                        status = 1;
                    } else {
                        status = 5;
                        errMsg = "AS批次件进度反馈[" + reqNo + "]处理失败:" + asResultVo.getMsg();
                        logger.info(errMsg);
                    }
                }
            } catch (Exception exception) {
                logger.error("", exception);
                status = 1;
                errMsg = "AS批次件进度反馈[" + reqNo + "]处理失败:";
                logger.info(errMsg);
                Thread.currentThread().interrupt();
            }

        } catch (Exception ex) {
            status = 5;
            errMsg = "AS批次件进度反馈[" + reqNo + "]处理失败:";
            logger.info(errMsg);
            logger.error(errMsg, ex);
            midApiLogService.clearChange();
            Thread.currentThread().interrupt();
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark("AS批次件进度反馈[" + reqNo + "]" + com.ca.mfd.prc.common.utils.StringUtils.getSubStr(errMsg,1000));

        midApiLogService.update(loginfo);
        midApiLogService.saveChange();
        if (status == 1) {
            updateAsAviAsSendFlag(list, logId, 1);
            midApiLogService.saveChange();
        } else {
            updateAsAviAsSendFlag(list, logId, 2);
            midApiLogService.saveChange();
        }

        logger.info("AS批次件进度反馈[" + reqNo + "]执行完成:");
        if (status == 1) {
            return new ResultVO<String>().ok("", "处理成功");
        } else {
            return new ResultVO<String>().error(-1, errMsg);
        }
    }

    private void updateAsAviAsSendFlag(List<PpsAsAviPointEntity> baths, Long logId, Integer asSendFlag) {
        if (baths.isEmpty()) {
            return;
        }
        for (PpsAsAviPointEntity item : baths) {
            item.setPrcMidApiLogId(logId);
            item.setAsSendFlag(asSendFlag);
        }
        ppsAsAviPointService.updateBatchById(baths);
    }

    @GetMapping(value = "getsenddata")
    @Operation(summary = "进度反馈test")
    public ResultVO<List<AsBatchPiecesDto>> getSendData() {
        List<PpsAsAviPointEntity> list = ppsAsAviPointService.getNoAsBatchPieces(500);
        List<AsBatchPiecesDto> baths = midApiLogService.getAsBatchPieces(list);
        return new ResultVO<List<AsBatchPiecesDto>>().ok(baths);
    }

    @GetMapping(value = "test")
    @Operation(summary = "进度反馈test")
    public ResultVO<AsResultDto<MidAsShopPlanFeedbackDto>> test() {
        List<MidAsShopPlanFeedbackDto> dtos = new ArrayList<>();
        MidAsShopPlanFeedbackDto et = new MidAsShopPlanFeedbackDto();
        et.setActualStartTime(DateUtils.addDateMinutes(new Date(), 30));
        et.setActualEndTime(DateUtils.addDateMinutes(new Date(), 60));
        et.setYieldQuantity(100);
        et.setDefectQuantity(5);
        et.setLotStatus(2);
        et.setOrgCode("CA");
        et.setShopCode("WE");
        //et.setLineCode(btplan.getLineCode());
        et.setTaskCode("PCGD-20231110-001311");
        et.setWsCode("WE1-1");
        et.setWsFlag("0");
        dtos.add(et);

        AsResultDto<MidAsShopPlanFeedbackDto> apiData = new AsResultDto(dtos, "MOM", "vehicleprogressfeedback");

        return new ResultVO<AsResultDto<MidAsShopPlanFeedbackDto>>().ok(apiData);

    }
}
