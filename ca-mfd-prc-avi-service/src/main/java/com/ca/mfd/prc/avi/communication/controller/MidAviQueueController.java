package com.ca.mfd.prc.avi.communication.controller;

import com.ca.mfd.prc.avi.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.avi.communication.dto.AviQueueReadRequestDto;
import com.ca.mfd.prc.avi.communication.dto.AviQueueRequestDto;
import com.ca.mfd.prc.avi.communication.dto.AviQueueResponseDto;
import com.ca.mfd.prc.avi.communication.dto.AviQueueResultDto;
import com.ca.mfd.prc.avi.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.avi.communication.remote.app.pps.provider.PpsPlanProvider;
import com.ca.mfd.prc.avi.communication.service.IMidApiLogService;
import com.ca.mfd.prc.avi.entity.AviQueueReleaseEntity;
import com.ca.mfd.prc.avi.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.avi.remote.app.pps.Provider.PpsOrderProvider;
import com.ca.mfd.prc.avi.service.IAviQueueReleaseService;
import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("communication/aviqueue")
@Tag(name = "AVI队列接口", description = "AVIQUEUE接口")
public class MidAviQueueController extends BaseApiController {
    private static final Logger logger = LoggerFactory.getLogger(MidAviQueueController.class);
    @Autowired
    private IMidApiLogService midApiLogService;

    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;

    @Autowired
    private IAviQueueReleaseService aviQueueReleaseService;

    @Autowired
    private PpsOrderProvider ppsOrderProvider;

    @Autowired
    private PpsPlanProvider ppsPlanProvider;

    @PostMapping(value = "readordersequence")
    @Operation(summary = "下发生产队列")
    public AviQueueResultDto<AviQueueResponseDto> readOrderSequence(@RequestBody AviQueueRequestDto dto) {
        String reqNo = UUIDUtils.getGuid();
        logger.info("下发生产队列[" + reqNo + "]开始:");
        List<AviQueueReleaseEntity> fbacks = aviQueueReleaseService.getNoSendByQuee(dto.getCode(), dto.getCount());
        logger.info("下发生产队列[" + reqNo + "]开始查询数据:" + (fbacks == null ? 0 : fbacks.size()));
        AviQueueResultDto<AviQueueResponseDto> result = new AviQueueResultDto<>();
        if (fbacks == null) {
            fbacks = new ArrayList<>();
        }

        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.AVI_QUEUE_START);
        loginfo.setDataLineNo(fbacks.size());
        loginfo.setRequestStartTime(new Date());
        int status = 1;
        String errMsg = "";
        try {
            loginfo.setStatus(0);
            midApiLogService.insert(loginfo);
            midApiLogService.saveChange();

            try {
                List<AviQueueResponseDto> datas = new ArrayList<>();
                List<Long> ids = new ArrayList<>();
                for (AviQueueReleaseEntity item : fbacks) {
                    /*PpsOrderEntity order = ppsOrderProvider.getPpsOrderBySnOrBarcode(item.getSn());
                    if (order == null) {
                        continue;
                    }*/
                    AviQueueResponseDto et = new AviQueueResponseDto();
                    et.setId(item.getId().toString());
                    et.setCode(item.getQueueCode());
                    et.setSequenceNo(item.getDisplayNo().toString());

                    et.setVin(item.getSn());

                    et.setSpace01("");
                    et.setSpace02("");
                    et.setSpace03("");
                    et.setSpace04("");

                    datas.add(et);
                    ids.add(item.getId());
                }
                result.setData(datas);
                loginfo.setDataLineNo(datas.size());
                errMsg = "";
                status = 1;
            } catch (Exception exception) {
                logger.error("", fbacks);
                status = 5;
                errMsg = "下发生产队列[" + reqNo + "]处理失败:" + exception.getMessage();
                logger.info(errMsg);
            }
        } catch (Exception ex) {
            status = 5;
            errMsg = "下发生产队列[" + reqNo + "]处理失败:" + ex.getMessage();
            logger.info(errMsg);
            logger.error(errMsg, ex);
            aviQueueReleaseService.clearChange();
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(errMsg);
        midApiLogService.update(loginfo);
        aviQueueReleaseService.saveChange();
        logger.info("下发生产队列[" + reqNo + "]执行完成:");
        if (status == 1) {
            result.setCode(200);
            result.setMessage("");
            return result;
        } else {
            result.setCode(-1);
            result.setMessage(errMsg);
            return result;
        }
    }

    @PostMapping(value = "writereadsuccess")
    @Operation(summary = "生产队列接收确认")
    public AviQueueResultDto<String> writeReadSuccess(@RequestBody AviQueueReadRequestDto dto) {
        AviQueueResultDto<String> result = new AviQueueResultDto<>();
        String reqNo = UUIDUtils.getGuid();
        List<Long> ids = ConvertUtils.stringToLongs(dto.getId());
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.AVI_QUEUE_CONFIRM);
        loginfo.setDataLineNo(ids.size());
        loginfo.setRequestStartTime(new Date());
        int status = 1;
        String errMsg = "";
        if (!ids.isEmpty()) {
            try {
                loginfo.setStatus(0);
                midApiLogService.insert(loginfo);
                midApiLogService.saveChange();
                for (Long id : ids) {
                    aviQueueReleaseService.updateIsSendStatus(true, id);
                }
                errMsg = "";
                status = 1;
            } catch (Exception ex) {
                status = 5;
                errMsg = "下发生产队列[" + reqNo + "]处理失败:" + ex.getMessage();
                logger.info(errMsg);
                logger.error(errMsg, ex);
                aviQueueReleaseService.clearChange();
            }
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(errMsg);
        midApiLogService.update(loginfo);
        aviQueueReleaseService.saveChange();
        logger.info("下发生产队列[" + reqNo + "]执行完成:");
        if (status == 1) {
            result.setCode(200);
            result.setMessage("");
            return result;
        } else {
            result.setCode(-1);
            result.setMessage(errMsg);
            return result;
        }
    }
}
