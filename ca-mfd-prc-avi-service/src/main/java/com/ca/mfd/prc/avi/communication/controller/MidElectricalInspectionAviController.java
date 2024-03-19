package com.ca.mfd.prc.avi.communication.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.avi.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.avi.communication.dto.CarInfoDto;
import com.ca.mfd.prc.avi.communication.dto.DJResultVo;
import com.ca.mfd.prc.avi.communication.dto.EcuCarInfoDto;
import com.ca.mfd.prc.avi.communication.dto.SiteInfoDto;
import com.ca.mfd.prc.avi.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.avi.communication.entity.MidDjCarSendEntity;
import com.ca.mfd.prc.avi.communication.entity.MidDjEcuSendEntity;
import com.ca.mfd.prc.avi.communication.entity.MidDjEpSendEntity;
import com.ca.mfd.prc.avi.communication.entity.MidDjSiteSendEntity;
import com.ca.mfd.prc.avi.communication.remote.app.eps.IEpsCommunicationService;
import com.ca.mfd.prc.avi.communication.remote.app.eps.entity.EpInfoDto;
import com.ca.mfd.prc.avi.communication.remote.app.pps.IPpsCommunicationService;
import com.ca.mfd.prc.avi.communication.service.IMidApiLogService;
import com.ca.mfd.prc.avi.communication.service.IMidDjCarSendService;
import com.ca.mfd.prc.avi.communication.service.IMidDjEcuSendService;
import com.ca.mfd.prc.avi.communication.service.IMidDjEpSendService;
import com.ca.mfd.prc.avi.communication.service.IMidDjSiteSendService;
import com.ca.mfd.prc.avi.entity.AviQueueReleaseEntity;
import com.ca.mfd.prc.avi.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.avi.remote.app.core.sys.entity.SysConfigurationEntity;
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

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("communication/electricalinspectionavi")
@Tag(name = "电检下发接口", description = "电检下发接口")
public class MidElectricalInspectionAviController {
    private static final Logger logger = LoggerFactory.getLogger(MidElectricalInspectionAviController.class);
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
    @Qualifier("apiPtService")
    private IApiPtformService apiPtService;

    @GetMapping(value = "carinfosend")
    @Operation(summary = "整车信息下发")
    public ResultVO carInfoSend() {
        String queueStr = sysConfigurationProvider.getConfiguration("DJCarinfoQueueReleaseSet", "DJCarinfoAviQueue");
        if (StringUtils.isBlank(queueStr)) {
            throw new InkelinkException("没有配置队列代码[DJCarinfoQueueReleaseSet]");
        }
        try {
            //查询队列数据
            QueryWrapper<AviQueueReleaseEntity> queryWrapper = new QueryWrapper<>();
            LambdaQueryWrapper<AviQueueReleaseEntity> lambdaQueryWrapper = queryWrapper.lambda();
            lambdaQueryWrapper.eq(AviQueueReleaseEntity::getIsSend, false);
            lambdaQueryWrapper.eq(AviQueueReleaseEntity::getQueueCode, queueStr);
            List<AviQueueReleaseEntity> datas = aviQueueReleaseService.getData(queryWrapper, false).stream().sorted(Comparator.comparing(AviQueueReleaseEntity::getCreationDate)).collect(Collectors.toList());;
            //处理过点数据触发下发
            if (CollectionUtils.isNotEmpty(datas)) {
                for (AviQueueReleaseEntity entity : datas) {
                    ResultVO<CarInfoDto> carInfoByVin = ppsCommunicationService.getCarInfoByVin(entity.getSn());
                    if (carInfoByVin == null || !carInfoByVin.getSuccess()) {
                        throw new InkelinkException("电检下发整车信息失败。" + (carInfoByVin == null ? "" : carInfoByVin.getMessage()));
                    }
                    carInfoByVin.getData().setFaDate(entity.getInsertDt());
                    DJResultVo resultVO = fetchDataFromApi(carInfoByVin.getData());
                    if (resultVO.getSuccess()) {
                        entity.setIsSend(true);
                        aviQueueReleaseService.update(entity);
                        aviQueueReleaseService.saveChange();
                    } else {
                        logger.error("电检下发整车信息失败:" + resultVO.getMessage());
                    }
                }
            }
        } catch (Exception ex) {
            return new ResultVO().error(-1, "电检下发整车信息异常：" + ex.getMessage());
        }
        return new ResultVO().ok("", "电检下发整车信息成功");
    }

    @GetMapping(value = "ecucarinfosend")
    @Operation(summary = "软件信息下发")
    public ResultVO ecucarInfoSend() {
        String queueStr = sysConfigurationProvider.getConfiguration("DJEcuCarinfoQueueReleaseSet", "DJEcuCarinfoAviQueue");
        if (StringUtils.isBlank(queueStr)) {
            throw new InkelinkException("没有配置队列代码[DJEcuCarinfoQueueReleaseSet]");
        }
        try {
            QueryWrapper<AviQueueReleaseEntity> queryWrapper = new QueryWrapper<>();
            LambdaQueryWrapper<AviQueueReleaseEntity> lambdaQueryWrapper = queryWrapper.lambda();
            lambdaQueryWrapper.eq(AviQueueReleaseEntity::getIsSend, false);
            lambdaQueryWrapper.eq(AviQueueReleaseEntity::getQueueCode, queueStr);
            List<AviQueueReleaseEntity> datas = aviQueueReleaseService.getData(queryWrapper, false).stream().sorted(Comparator.comparing(AviQueueReleaseEntity::getCreationDate)).collect(Collectors.toList());;
            //处理过点数据触发下发
            if (CollectionUtils.isNotEmpty(datas)) {
                for (AviQueueReleaseEntity entity : datas) {
                    ResultVO<EcuCarInfoDto> carInfoByVin = ppsCommunicationService.getEcuCarInfoByVin(entity.getSn());
                    if (carInfoByVin == null || !carInfoByVin.getSuccess()) {
                        throw new InkelinkException("电检下发软件信息失败。" + (carInfoByVin == null ? "" : carInfoByVin.getMessage()));
                    }
                    DJResultVo resultVO = fetchDataFromApi(carInfoByVin.getData());
                    if (resultVO.getSuccess()) {
                        entity.setIsSend(true);
                        aviQueueReleaseService.update(entity);
                        aviQueueReleaseService.saveChange();
                    } else {
                        logger.error("电检下发软件信息失败:" + resultVO.getMessage());
                    }
                }
            }
        } catch (Exception ex) {
            return new ResultVO().error(-1, "电检下发软件信息异常：" + ex.getMessage());
        }
        return new ResultVO().ok("", "电检下发软件信息成功");
    }

    @GetMapping(value = "siteinfosend")
    @Operation(summary = "过点信息下发")
    public ResultVO siteInfoSend() {
        List<SysConfigurationEntity> sysConfigurations = sysConfigurationProvider.getSysConfigurations("DJSiteinfoAviQueue");
        if (sysConfigurations == null || sysConfigurations.isEmpty()) {
            throw new InkelinkException("没有配置队列代码DJSiteinfoAviQueue");
        }
        try {
            List<String> str = sysConfigurations.stream().map(SysConfigurationEntity::getText).collect(Collectors.toList());
            QueryWrapper<AviQueueReleaseEntity> queryWrapper = new QueryWrapper<>();
            LambdaQueryWrapper<AviQueueReleaseEntity> lambdaQueryWrapper = queryWrapper.lambda();
            lambdaQueryWrapper.eq(AviQueueReleaseEntity::getIsSend, false);
            lambdaQueryWrapper.in(AviQueueReleaseEntity::getQueueCode, str);
            List<AviQueueReleaseEntity> datas = aviQueueReleaseService.getData(queryWrapper, false).stream().sorted(Comparator.comparing(AviQueueReleaseEntity::getCreationDate)).collect(Collectors.toList());;
            //处理过点数据触发下发
            if (CollectionUtils.isNotEmpty(datas)) {
                for (AviQueueReleaseEntity entity : datas) {
                    ResultVO<SiteInfoDto> carInfoByVin = ppsCommunicationService.getSiteInfoByVin(entity.getSn());
                    if (carInfoByVin == null || !carInfoByVin.getSuccess()) {
                        throw new InkelinkException("电检下发过点信息失败。" + (carInfoByVin == null ? "" : carInfoByVin.getMessage()));
                    }
                    //赋值过点时间
                    if(carInfoByVin.getData()!=null){
                        carInfoByVin.getData().setFaDate(entity.getCreationDate());
                    }
                    DJResultVo resultVO = fetchDataFromApi(carInfoByVin.getData());
                    if (resultVO.getSuccess()) {
                        entity.setIsSend(true);
                        aviQueueReleaseService.update(entity);
                        aviQueueReleaseService.saveChange();
                    } else {
                        logger.error("电检下发过点信息失败:" + resultVO.getMessage());
                    }
                }
            }
        } catch (Exception ex) {
            return new ResultVO().error(-1, "电检下发过点信息异常：" + ex.getMessage());
        }
        return new ResultVO().ok("", "电检下发过点信息成功");
    }

    @GetMapping(value = "epinfosend")
    @Operation(summary = "EP信息接口下发")
    public ResultVO epInfoSend() {

        String queueStr = sysConfigurationProvider.getConfiguration("DJEpinfoQueueReleaseSet", "DJEpinfoAviQueue");
        if (StringUtils.isBlank(queueStr)) {
            throw new InkelinkException("没有配置队列代码[DJEpinfoQueueReleaseSet]");
        }
        try {
            QueryWrapper<AviQueueReleaseEntity> queryWrapper = new QueryWrapper<>();
            LambdaQueryWrapper<AviQueueReleaseEntity> lambdaQueryWrapper = queryWrapper.lambda();
            lambdaQueryWrapper.eq(AviQueueReleaseEntity::getIsSend, false);
            lambdaQueryWrapper.eq(AviQueueReleaseEntity::getQueueCode, queueStr);
            List<AviQueueReleaseEntity> datas = aviQueueReleaseService.getData(queryWrapper, false).stream().sorted(Comparator.comparing(AviQueueReleaseEntity::getCreationDate)).collect(Collectors.toList());;
            //处理过点数据触发下发
            if (CollectionUtils.isNotEmpty(datas)) {
                for (AviQueueReleaseEntity entity : datas) {
                    ResultVO<List<EpInfoDto>> epInfoByVin = epsCommunicationService.getEpInfoByVin(entity.getSn());
                    if (epInfoByVin == null || !epInfoByVin.getSuccess()) {
                        throw new InkelinkException("电检下发EP信息失败。" + (epInfoByVin == null ? "" : epInfoByVin.getMessage()));
                    }
                    DJResultVo resultVO = fetchDataFromApi(epInfoByVin.getData());
                    if (resultVO.getSuccess()) {
                        entity.setIsSend(true);
                        aviQueueReleaseService.update(entity);
                        aviQueueReleaseService.saveChange();
                    } else {
                        logger.error("电检下发EP信息失败:" + resultVO.getMessage());
                    }
                }
            }

        } catch (Exception ex) {
            return new ResultVO().error(-1, "电检下发EP信息异常：" + ex.getMessage());
        }
        return new ResultVO().ok("", "电检下发EP信息成功");
    }


    //调用carinfo接口
    private DJResultVo fetchDataFromApi(CarInfoDto infoDto) {
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
            BeanUtils.copyProperties(infoDto, carSendEntity);
            carSendEntity.setPrcMidApiLogId(loginfo.getId());
            carSendService.insert(carSendEntity);
            carSendService.saveChange();
            // 发起HTTP请求
            String responseData = apiPtService.postapi(apiUrl, infoDto, null);

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

    //调用ECU_CAR_INFO接口
    private DJResultVo fetchDataFromApi(EcuCarInfoDto infoDto) {
        String apiUrl = sysConfigurationProvider.getConfiguration("djecucarinfo_send", "midapi");
        if (StringUtils.isBlank(apiUrl)) {
            throw new InkelinkException("没有配置上报的地址[djecucarinfo_send]");
        }

        String reqNo = UUIDUtils.getGuid();
        logger.info("电检下发软件信息[" + reqNo + "]开始发送数据");
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.DJ_ECU_CARINFO);
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
            MidDjEcuSendEntity ecuSendEntity = new MidDjEcuSendEntity();
            BeanUtils.copyProperties(infoDto, ecuSendEntity);
            ecuSendEntity.setPrcMidApiLogId(loginfo.getId());
            String jsonString = JsonUtils.toJsonString(infoDto.getEcuList());
            ecuSendEntity.setEcuList(jsonString);
            ecuSendService.insert(ecuSendEntity);
            ecuSendService.saveChange();
            // 发起HTTP请求
            String responseData = apiPtService.postapi(apiUrl, infoDto, null);

            logger.warn("API平台测试url调用：" + responseData);
            resultVo = JsonUtils.parseObject(responseData, DJResultVo.class);
            if (!resultVo.getSuccess()) {
                status = 5;
                errMsg = resultVo.getMessage();
                logger.info(errMsg);
            }
        } catch (Exception ex) {
            status = 5;
            errMsg = "电检下发软件信息[" + reqNo + "]处理失败:";
            logger.info(errMsg);
            logger.error(errMsg, ex);
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(errMsg);
        midApiLogService.update(loginfo);
        midApiLogService.saveChange();
        logger.info("电检下发软件信息[" + reqNo + "]执行完成:");
        return resultVo;
    }

    //调用SITE_INFO接口
    private DJResultVo fetchDataFromApi(SiteInfoDto infoDto) {
        String apiUrl = sysConfigurationProvider.getConfiguration("djsiteinfo_send", "midapi");
        if (StringUtils.isBlank(apiUrl)) {
            throw new InkelinkException("没有配置上报的地址[djsiteinfo_send]");
        }

        String reqNo = UUIDUtils.getGuid();
        logger.info("电检下发过点信息[" + reqNo + "]开始发送数据");
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.DJ_SITEINFO);
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
            MidDjSiteSendEntity siteSendEntity = new MidDjSiteSendEntity();
            BeanUtils.copyProperties(infoDto, siteSendEntity);
            siteSendEntity.setPrcMidApiLogId(loginfo.getId());
            siteSendService.insert(siteSendEntity);
            siteSendService.saveChange();

            // 发起HTTP请求
            String responseData = apiPtService.postapi(apiUrl, infoDto, null);

            logger.warn("API平台测试url调用：" + responseData);
            resultVo = JsonUtils.parseObject(responseData, DJResultVo.class);
            if (!resultVo.getSuccess()) {
                status = 5;
                errMsg = resultVo.getMessage();
                logger.info(errMsg);
            }
        } catch (Exception ex) {
            status = 5;
            errMsg = "电检下发过点信息[" + reqNo + "]处理失败:";
            logger.info(errMsg);
            logger.error(errMsg, ex);
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(errMsg);
        midApiLogService.update(loginfo);
        midApiLogService.saveChange();
        logger.info("电检下发过点信息[" + reqNo + "]执行完成:");
        return resultVo;
    }

    private DJResultVo fetchDataFromApi(List<EpInfoDto> epInfoDtoList) {
        String apiUrl = sysConfigurationProvider.getConfiguration("djepinfo_send", "midapi");
        if (StringUtils.isBlank(apiUrl)) {
            throw new InkelinkException("没有配置上报的地址[djepinfo_send]");
        }

        String reqNo = UUIDUtils.getGuid();
        logger.info("电检下发EP信息[" + reqNo + "]开始发送数据");
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.DJ_EPINFO_RESULT);
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
            epInfoDtoList.stream().forEach(epInfoDto -> {
                MidDjEpSendEntity epSendEntity = new MidDjEpSendEntity();
                BeanUtils.copyProperties(epInfoDto, epSendEntity);
                epSendEntity.setPrcMidApiLogId(loginfo.getId());
                epSendService.insert(epSendEntity);
                epSendService.saveChange();
            });

            // 发起HTTP请求
            String responseData = apiPtService.postapi(apiUrl, epInfoDtoList, null);

            logger.warn("API平台测试url调用：" + responseData);
            resultVo = JsonUtils.parseObject(responseData, DJResultVo.class);
            if (!resultVo.getSuccess()) {
                status = 5;
                errMsg = resultVo.getMessage();
                logger.info(errMsg);
            }
        } catch (Exception ex) {
            status = 5;
            errMsg = "电检下发EP信息[" + reqNo + "]处理失败:";
            logger.info(errMsg);
            logger.error(errMsg, ex);
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(errMsg);
        midApiLogService.update(loginfo);
        midApiLogService.saveChange();
        logger.info("电检下发EP信息[" + reqNo + "]执行完成:");
        return resultVo;
    }
}
