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
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("communication/partqueue")
@Tag(name = "LMS_PARTQUEUE接口", description = "LMS_PARTQUEUE接口")
public class MidLmsPartQueueController extends BaseApiController {
    private static final Logger logger = LoggerFactory.getLogger(MidLmsPartQueueController.class);

    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;

    @Autowired
    private IPpsAsAviPointService ppsAsAviPointService;

    @Autowired
    private IMidApiLogService midApiLogService;

    @Autowired
    private IPpsPlanService ppsPlanService;

    @Autowired
    private IPpsPlanPartsService ppsPlanPartsService;

    @Autowired
    @Qualifier("apiPtService")
    private IApiPtformService apiPtService;

    @GetMapping(value = "send")
    @Operation(summary = "Lms零部件过点信息(定时调度)")
    public ResultVO<String> send() {
        String reqNo = UUIDUtils.getGuid();
        logger.info("Lms零部件过点信息[" + reqNo + "]开始:");
        List<PpsAsAviPointEntity> partList = ppsAsAviPointService.getPartSendList();
        if (partList.isEmpty()) {
            return new ResultVO<String>().ok("暂无发送数据");
        }
        List<MidLmsPartQueueDto> fbacks = new ArrayList<>();
        for (PpsAsAviPointEntity item : partList) {
            PpsPlanEntity ppsPlanInfo = new PpsPlanEntity();
            PpsPlanPartsEntity ppsPlanPartsInfo = new PpsPlanPartsEntity();
            if (StringUtils.equals(item.getOrderCategory(), "1") || StringUtils.equals(item.getOrderCategory(), "2")
                    || StringUtils.equals(item.getOrderCategory(), "7")) {
                ppsPlanInfo = ppsPlanService.getFirstByPlanNo(item.getPlanNo());
            } else {
                ppsPlanPartsInfo = ppsPlanPartsService.getFirstByPlanNo(item.getPlanNo());
            }
            MidLmsPartQueueDto dto = new MidLmsPartQueueDto();
            dto.setOrgCode(item.getOrgCode());
            dto.setWorkshopCode(item.getWorkshopCode());
            dto.setLineCode(item.getLineCode());
            dto.setVid("");
            dto.setVin(item.getVin());
            dto.setProductType("2");
            String orderCategory = item.getOrderCategory();
            String productCode = StringUtils.EMPTY;
            if (StringUtils.equals(item.getOrderCategory(), "1") || StringUtils.equals(item.getOrderCategory(), "2")
                    || StringUtils.equals(item.getOrderCategory(), "7")) {
                if (ppsPlanInfo != null) {
                    //白车身,备件
                    if (orderCategory.equals("7") || StringUtils.equals(ppsPlanInfo.getAttribute2(), "1")) {
                        productCode = ppsPlanInfo.getCharacteristic5();
                    } else {
                        productCode = ppsPlanInfo.getProductCode();
                    }
                }
            } else {
                if (ppsPlanPartsInfo != null) {
                    productCode = ppsPlanPartsInfo.getProductCode();
                }
            }
            dto.setProductCode(productCode);
            dto.setOneId("");
            dto.setManager("");
            dto.setPassTime(item.getScanTime());
            dto.setAviCode(item.getAviCode());
            dto.setOrderSign("");
            dto.setUniqueCode(item.getId());
            fbacks.add(dto);
        }
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
                logger.warn("API平台测试url调用：" + ars);
                AsResultVo asResultVo = JSONObject.parseObject(ars, AsResultVo.class);
                if (asResultVo != null) {
                    if (StringUtils.equals("1", asResultVo.getCode())) {
                        for (MidLmsPartQueueDto item : fbacks) {
                            ids.add(item.getUniqueCode());
                        }
                        if (ids.size() > 0) {
                            ppsAsAviPointService.updatePartSendStatus(ids);
                        }
                        status = 1;
                    } else {
                        status = 5;
                        errMsg = "LMS零部件过点记录[" + reqNo + "]处理失败:" + asResultVo.getMsg();
                        logger.info(errMsg);
                    }
                }
            } catch (Exception ex) {
                logger.error("", fbacks);
                status = 5;
                errMsg = "LMS零部件过点记录[" + reqNo + "]处理失败:" + ex.getMessage();
                logger.info(errMsg);
            }
        } catch (Exception ex) {
            status = 5;
            errMsg = "LMS零部件过点记录[" + reqNo + "]处理失败:" + ex.getMessage();
            logger.info(errMsg);
            logger.error(errMsg, ex);
            ppsAsAviPointService.clearChange();
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(com.ca.mfd.prc.common.utils.StringUtils.getSubStr(errMsg,1000));
        midApiLogService.update(loginfo);
        ppsAsAviPointService.saveChange();
        logger.info("LMS零部件过点记录[" + reqNo + "]执行完成:");
        if (status == 1) {
            return new ResultVO<String>().ok("", "处理成功");
        } else {
            return new ResultVO<String>().error(-1, errMsg);
        }
    }

    @GetMapping(value = "getppsasavipoint")
    @Operation(summary = "获取零部件过点数据")
    public ResultVO<List<PpsAsAviPointEntity>> getPpsAsAviPoint() {
        List<PpsAsAviPointEntity> partList = ppsAsAviPointService.getPartSendList();
        if (partList.isEmpty()) {
            throw new InkelinkException("暂无零部件过点数据");
        }
        return new ResultVO<List<PpsAsAviPointEntity>>().ok(partList, "获取成功");
    }

}
