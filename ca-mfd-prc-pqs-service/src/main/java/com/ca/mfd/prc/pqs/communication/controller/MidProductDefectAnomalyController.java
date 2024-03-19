package com.ca.mfd.prc.pqs.communication.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IApiPtformService;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.pqs.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pqs.communication.dto.ProductDefectAnomalyDto;
import com.ca.mfd.prc.pqs.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pqs.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pqs.entity.PqsProductDefectAnomalyEntity;
import com.ca.mfd.prc.pqs.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pqs.remote.app.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.pqs.remote.app.pps.provider.PpsOrderProvider;
import com.ca.mfd.prc.pqs.service.IPqsDefectAnomalyService;
import com.ca.mfd.prc.pqs.service.IPqsProductDefectAnomalyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("communication/productDefectAnomaly")
@Tag(name = "QMS_ProductDefectAnomaly接口", description = "QMS_ProductDefectAnomaly接口")
public class MidProductDefectAnomalyController extends BaseApiController {
    private static final Logger logger = LoggerFactory.getLogger(MidProductDefectAnomalyController.class);

    @Autowired
    private IPqsProductDefectAnomalyService pqsProductDefectAnomalyService;
    @Autowired
    private IMidApiLogService midApiLogService;
    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;
    @Autowired
    private PpsOrderProvider ppsOrderProvider;
    @Autowired
    private IPqsDefectAnomalyService pqsDefectAnomalyService;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("apiPtService")
    private IApiPtformService apiPtService;

    /**
     * MOM产品缺陷信息(全量数据)
     *
     * @return
     */
    @GetMapping(value = "productdefectanomalysend")
    @Operation(summary = "MOM产品缺陷信息(全量数据)")
    public ResultVO<String> productDefectAnomalySend() {

        String reqNo = UUIDUtils.getGuid();
        logger.info("======MOM产品缺陷信息全量数据[" + reqNo + "]开始:=============");

        List<ProductDefectAnomalyDto> dataList = pqsProductDefectAnomalyService.getProductDefectAnomalyList();
        logger.info("============MOM产品缺陷信息全量数据[" + reqNo + "]开始查询数据:" + (dataList == null ? 0 : dataList.size()));
        if (CollectionUtil.isEmpty(dataList)) {
            throw new InkelinkException("没有需要推送的数据");
        }

        // sn取出来，去重
        List<String> snList = dataList.stream().filter(s->StringUtils.isNotBlank(s.getSn())).collect(Collectors.toList()).stream().map(ProductDefectAnomalyDto::getSn).distinct().collect(Collectors.toList());
        List<PpsOrderEntity> ppsOrderList = ppsOrderProvider.getPpsOrderBySnsOrBarcodes(snList);
        if (CollectionUtil.isNotEmpty(ppsOrderList)) {
            logger.info("==========车型数据组装开始=========[{}]", ppsOrderList.size());
            dataList.forEach(d -> {
                ppsOrderList.forEach(p -> {
                    if (StringUtils.equals(d.getSn(), p.getSn()) || StringUtils.equals(d.getSn(), p.getBarcode())) {
                        d.setModel(p.getModel());
                    }
                });
            });
            logger.info("==========车型数据组装结束=========");
        }

        // QMS接口调用数据推送
        String apiPath = sysConfigurationProvider.getConfiguration("pqs-defect-info", "qmsapi");
        if (StringUtils.isBlank(apiPath)) {
            throw new InkelinkException("没有配置上报的地址[]");
        }

        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.PMS_PRODUCT_DEFECT_ANOMALY);
        loginfo.setDataLineNo(dataList.size());
        loginfo.setRequestStartTime(new Date());
        int status = 1;
        String errMsg = "";
        try {
            loginfo.setStatus(0);
            midApiLogService.insert(loginfo);
            midApiLogService.saveChange();
            loginfo.setDataLineNo(dataList.size());
            try {
                String sendData = JsonUtils.toJsonString(dataList);
                loginfo.setReqData(sendData);

                String responseData = apiPtService.postapi(apiPath, dataList, null);
                loginfo.setResponseData(responseData);
                logger.warn("===========API平台测试url调用：" + responseData);
                ResultVO<?> resultVo = JsonUtils.parseObject(responseData, ResultVO.class);
                if (!resultVo.getSuccess()) {
                    status = 5;
                    errMsg = resultVo.getMessage();
                    logger.info(errMsg);
                }
            } catch (Exception exception) {
                logger.error("", dataList);
                status = 5;
                errMsg = "MOM产品缺陷信息[" + reqNo + "]处理失败:" + exception.getMessage();
                logger.info(errMsg);
            }
        } catch (Exception ex) {
            status = 5;
            errMsg = "MOM产品缺陷信息[" + reqNo + "]处理失败:" + ex.getMessage();
            logger.info(errMsg);
            logger.error(errMsg, ex);
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(errMsg);
        midApiLogService.update(loginfo);
        midApiLogService.saveChange();
        logger.info("=============MOM产品缺陷信息[" + reqNo + "]执行完成=============");
        return new ResultVO<String>().ok("", "推送成功");
    }

    /**
     * MOM产品缺陷信息(定时调度)--按天
     *
     * @return
     */
    @GetMapping(value = "productdefectanomalysendbyday")
    @Operation(summary = "MOM产品缺陷信息(定时调度)")
    public ResultVO<String> productDefectAnomalySendByDay() {

        String reqNo = UUIDUtils.getGuid();
        logger.info("======MOM产品缺陷信息[" + reqNo + "]开始:=============");

        LocalDate today = LocalDate.now();
        // 创建当天的开始时间和结束时间
        LocalDateTime startOfDay = LocalDateTime.of(today, LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(today, LocalTime.MAX);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Date startDate = DateUtils.parse(startOfDay.format(formatter), DateUtils.DATE_TIME_PATTERN);
        Date endDate = DateUtils.parse(endOfDay.format(formatter), DateUtils.DATE_TIME_PATTERN);

        QueryWrapper<PqsProductDefectAnomalyEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().ne(PqsProductDefectAnomalyEntity::getStatus, 0)
                .ne(PqsProductDefectAnomalyEntity::getStatus, 3)
                .between(PqsProductDefectAnomalyEntity::getActivateTime, startDate, endDate);
        List<PqsProductDefectAnomalyEntity> defectAnomalyList = pqsProductDefectAnomalyService.getData(queryWrapper, false);
        logger.info("============MOM产品缺陷信息[" + reqNo + "]开始查询数据:" + (defectAnomalyList == null ? 0 : defectAnomalyList.size()));
        if (CollectionUtil.isEmpty(defectAnomalyList)) {
            throw new InkelinkException("没有需要推送的数据");
        }

        List<ProductDefectAnomalyDto> dataList = Lists.newArrayList();
        defectAnomalyList.forEach(d -> {
            ProductDefectAnomalyDto dto = new ProductDefectAnomalyDto();
            BeanUtils.copyProperties(d, dto);
            dto.setPrcPqsProductDefectAnomalyId(d.getId());
            dataList.add(dto);
        });

        // sn取出来，去重
        List<String> snList = dataList.stream().filter(s->StringUtils.isNotBlank(s.getSn()))
                .collect(Collectors.toList()).stream().map(ProductDefectAnomalyDto::getSn).distinct().collect(Collectors.toList());
        List<PpsOrderEntity> ppsOrderList = ppsOrderProvider.getPpsOrderBySnsOrBarcodes(snList);
        if (CollectionUtil.isNotEmpty(ppsOrderList)) {
            logger.info("==========车型数据组装开始=========[{}]", ppsOrderList.size());
            dataList.forEach(d -> {
                ppsOrderList.forEach(p -> {
                    if (StringUtils.equals(d.getSn(), p.getSn()) || StringUtils.equals(d.getSn(), p.getBarcode())) {
                        d.setModel(p.getModel());
                    }
                });
            });
            logger.info("==========车型数据组装结束=========");
        }

        // QMS接口调用数据推送
        String apiPath = sysConfigurationProvider.getConfiguration("pqs-defect-info", "qmsapi");
        if (StringUtils.isBlank(apiPath)) {
            throw new InkelinkException("没有配置上报的地址[]");
        }

        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.PMS_PRODUCT_DEFECT_ANOMALY);
        loginfo.setDataLineNo(dataList.size());
        loginfo.setRequestStartTime(new Date());
        int status = 1;
        String errMsg = "";
        try {
            loginfo.setStatus(0);
            midApiLogService.insert(loginfo);
            midApiLogService.saveChange();
            loginfo.setDataLineNo(dataList.size());
            try {
                String sendData = JsonUtils.toJsonString(dataList);
                loginfo.setReqData(sendData);

                String responseData = apiPtService.postapi(apiPath, dataList, null);
                loginfo.setResponseData(responseData);
                logger.warn("===========API平台测试url调用：" + responseData);
                ResultVO<?> resultVo = JsonUtils.parseObject(responseData, ResultVO.class);
                if (!resultVo.getSuccess()) {
                    status = 5;
                    errMsg = resultVo.getMessage();
                    logger.info(errMsg);
                }
            } catch (Exception exception) {
                logger.error("", dataList);
                status = 5;
                errMsg = "MOM产品缺陷信息[" + reqNo + "]处理失败:" + exception.getMessage();
                logger.info(errMsg);
            }
        } catch (Exception ex) {
            status = 5;
            errMsg = "MOM产品缺陷信息[" + reqNo + "]处理失败:" + ex.getMessage();
            logger.info(errMsg);
            logger.error(errMsg, ex);
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(errMsg);
        midApiLogService.update(loginfo);
        midApiLogService.saveChange();
        logger.info("=============MOM产品缺陷信息[" + reqNo + "]执行完成=============");
        return new ResultVO<String>().ok("", "推送成功");
    }

}
