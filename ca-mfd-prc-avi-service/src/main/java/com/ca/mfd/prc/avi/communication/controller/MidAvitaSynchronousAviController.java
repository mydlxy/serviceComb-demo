package com.ca.mfd.prc.avi.communication.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.avi.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.avi.communication.dto.AvitaApplicationDto;
import com.ca.mfd.prc.avi.communication.dto.DJResultVo;
import com.ca.mfd.prc.avi.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.avi.communication.entity.MidDjCarSendEntity;

import com.ca.mfd.prc.avi.communication.remote.app.eps.IEpsCommunicationService;
import com.ca.mfd.prc.avi.communication.remote.app.pps.IPpsCommunicationService;
import com.ca.mfd.prc.avi.communication.remote.app.pps.IPpsOrderService;
import com.ca.mfd.prc.avi.communication.service.IMidApiLogService;
import com.ca.mfd.prc.avi.communication.service.IMidDjCarSendService;
import com.ca.mfd.prc.avi.communication.service.IMidDjEcuSendService;
import com.ca.mfd.prc.avi.communication.service.IMidDjEpSendService;
import com.ca.mfd.prc.avi.communication.service.IMidDjSiteSendService;
import com.ca.mfd.prc.avi.communication.remote.app.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.avi.entity.AviQueueReleaseEntity;
import com.ca.mfd.prc.avi.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.avi.service.IAviQueueReleaseService;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.IApiPtformService;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("communication/avitasynchronousavi")
@Tag(name = "阿维塔同步相关接口", description = "阿维塔同步相关接口")
public class MidAvitaSynchronousAviController {
    private static final Logger logger = LoggerFactory.getLogger(MidAvitaSynchronousAviController.class);
    @Autowired
    private IMidApiLogService midApiLogService;

    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;

    @Autowired
    private IPpsCommunicationService ppsCommunicationService;

    @Autowired
    private IAviQueueReleaseService aviQueueReleaseService;

    @Autowired
    private IMidDjSiteSendService siteSendService;
    @Autowired
    private IMidDjCarSendService carSendService;
    @Autowired
    private IMidDjEcuSendService ecuSendService;
    @Autowired
    private IMidDjEpSendService epSendService;
    @Autowired
    private IEpsCommunicationService epsCommunicationService;
    @Autowired
    private IPpsOrderService ppsOrderService;


    @Autowired
    @Qualifier("apiPtService")
    private IApiPtformService apiPtService;

    @GetMapping(value = "pendingapplication")
    @Operation(summary = "同步待申请秘钥车辆接口")
    public DJResultVo pendingApplication() {

        DJResultVo resultVO = new DJResultVo();
        String queueStr = sysConfigurationProvider.getConfiguration("DJCarinfoQueueReleaseSet", "DJCarinfoAviQueue");
        if (StringUtils.isBlank(queueStr)) {
            throw new InkelinkException("没有配置队列代码[DJCarinfoQueueReleaseSet]");
        }
        //查询队列数据
        QueryWrapper<AviQueueReleaseEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<AviQueueReleaseEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(AviQueueReleaseEntity::getIsSend, false);
        lambdaQueryWrapper.in(AviQueueReleaseEntity::getQueueCode, queueStr);
        List<AviQueueReleaseEntity> datas = aviQueueReleaseService.getData(queryWrapper, false);
        //处理过点数据触发下发
        if (CollectionUtils.isNotEmpty(datas)) {
            //获取ppsOrder表数据
            ResultVO<List<PpsOrderEntity>> orderEntityList = ppsOrderService.getListByBarcodes(datas.stream().map(avi -> avi.getSn()).distinct().collect(Collectors.toList()));
            if (orderEntityList == null || !orderEntityList.getSuccess()) {
                throw new InkelinkException("获取订单表数据的失败。" + (orderEntityList == null ? "" : orderEntityList.getMessage()));
            }

            List<AvitaApplicationDto> dtoList = new ArrayList<>();
            //根据车系进行分组
            Map<String, List<PpsOrderEntity>> orderMap = orderEntityList.getData().stream().collect(Collectors.groupingBy(PpsOrderEntity::getCharacteristic1));
            for (Map.Entry<String, List<PpsOrderEntity>> entry : orderMap.entrySet()) {
                AvitaApplicationDto dto = new AvitaApplicationDto();
                dto.setVclSer(entry.getKey());
                dto.setCarCode(entry.getKey());
                dto.setVinList(entry.getValue().stream().map(order -> order.getSn()).distinct().collect(Collectors.toList()));
                dtoList.add(dto);
            }
            dtoList.stream().forEach(dto -> {
                //调用API生命周期平台-数据发给阿维塔
                fetchDataFromApi(dto);
            });


        }
        return resultVO;
    }
    @GetMapping(value = "applycmplete")
    @Operation(summary = "拉取申请完成的密钥数据接口")
    public DJResultVo ompleteData() {

        DJResultVo resultVO = new DJResultVo();
        String queueStr = sysConfigurationProvider.getConfiguration("DJCarinfoQueueReleaseSet", "DJCarinfoAviQueue");
        if (StringUtils.isBlank(queueStr)) {
            throw new InkelinkException("没有配置队列代码[DJCarinfoQueueReleaseSet]");
        }
        //查询队列数据
        QueryWrapper<AviQueueReleaseEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<AviQueueReleaseEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(AviQueueReleaseEntity::getIsSend, false);
        lambdaQueryWrapper.in(AviQueueReleaseEntity::getQueueCode, queueStr);
        List<AviQueueReleaseEntity> datas = aviQueueReleaseService.getData(queryWrapper, false);
        //处理过点数据触发下发
        if (CollectionUtils.isNotEmpty(datas)) {
            //获取ppsOrder表数据
            ResultVO<List<PpsOrderEntity>> orderEntityList = ppsOrderService.getListByBarcodes(datas.stream().map(avi -> avi.getSn()).distinct().collect(Collectors.toList()));
            if (orderEntityList == null || !orderEntityList.getSuccess()) {
                throw new InkelinkException("获取订单表数据的失败。" + (orderEntityList == null ? "" : orderEntityList.getMessage()));
            }

            List<AvitaApplicationDto> dtoList = new ArrayList<>();
            //根据车系进行分组
            Map<String, List<PpsOrderEntity>> orderMap = orderEntityList.getData().stream().collect(Collectors.groupingBy(PpsOrderEntity::getCharacteristic1));
            for (Map.Entry<String, List<PpsOrderEntity>> entry : orderMap.entrySet()) {
                AvitaApplicationDto dto = new AvitaApplicationDto();
                dto.setVclSer(entry.getKey());
                dto.setCarCode(entry.getKey());
                dto.setVinList(entry.getValue().stream().map(order -> order.getSn()).distinct().collect(Collectors.toList()));
                dtoList.add(dto);
            }
            dtoList.stream().forEach(dto -> {
                //调用API生命周期平台-数据发给阿维塔
                fetchDataFromApi(dto);
            });


        }
        return resultVO;
    }


    //调用carinfo接口
    private DJResultVo fetchDataFromApi(AvitaApplicationDto dto) {
        String apiUrl = sysConfigurationProvider.getConfiguration("djcarinfo_send", "midapi");
        if (StringUtils.isBlank(apiUrl)) {
            throw new InkelinkException("没有配置上报的地址[djcarinfo_send]");
        }

        String reqNo = UUIDUtils.getGuid();
        logger.info("电检下发整车信息[" + reqNo + "]开始发送数据");
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.DJ_CARINFO);
        int status = 1;
        String errMsg = "";
        loginfo.setDataLineNo(0);
        loginfo.setRequestStartTime(new Date());
        DJResultVo resultVo = new DJResultVo();
        try {
            loginfo.setStatus(0);
            midApiLogService.insert(loginfo);
            midApiLogService.saveChange();

            //保存发送记录
            MidDjCarSendEntity carSendEntity = new MidDjCarSendEntity();
            BeanUtils.copyProperties(dto, carSendEntity);
            carSendEntity.setPrcMidApiLogId(loginfo.getId());
            carSendService.insert(carSendEntity);
            carSendService.saveChange();
            // 发起HTTP请求
            String responseData = apiPtService.postapi(apiUrl, dto, null);

            logger.warn("API平台测试url调用：" + responseData);
            resultVo = JsonUtils.parseObject(responseData, DJResultVo.class);
            if (!resultVo.getSuccess()) {
                status = 5;
                errMsg = resultVo.getMessage();
                logger.info(errMsg);
            }
        } catch (Exception ex) {
            status = 5;
            errMsg = "电检下发整车信息[" + reqNo + "]处理失败:";
            logger.info(errMsg);
            logger.error(errMsg, ex);
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(errMsg);
        midApiLogService.update(loginfo);
        midApiLogService.saveChange();
        logger.info("电检下发整车信息[" + reqNo + "]执行完成:");
        return resultVo;
    }
}
