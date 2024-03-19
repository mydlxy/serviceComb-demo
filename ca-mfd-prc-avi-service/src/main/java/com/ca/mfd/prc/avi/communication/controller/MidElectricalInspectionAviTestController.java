package com.ca.mfd.prc.avi.communication.controller;

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
import com.ca.mfd.prc.avi.communication.entity.MidDjTestSendEntity;
import com.ca.mfd.prc.avi.communication.remote.app.eps.IEpsCommunicationService;
import com.ca.mfd.prc.avi.communication.remote.app.eps.entity.EpInfoDto;
import com.ca.mfd.prc.avi.communication.remote.app.pps.IPpsCommunicationService;
import com.ca.mfd.prc.avi.communication.service.IMidApiLogService;
import com.ca.mfd.prc.avi.communication.service.IMidDjCarSendService;
import com.ca.mfd.prc.avi.communication.service.IMidDjEcuSendService;
import com.ca.mfd.prc.avi.communication.service.IMidDjEpSendService;
import com.ca.mfd.prc.avi.communication.service.IMidDjSiteSendService;
import com.ca.mfd.prc.avi.communication.service.IMidDjTestSendService;
import com.ca.mfd.prc.avi.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.avi.service.IAviQueueReleaseService;
import com.ca.mfd.prc.common.controller.BaseController;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("communication/dianjiantest")
@Tag(name = "电检下发手动触发", description = "电检下发手动触发")
public class MidElectricalInspectionAviTestController extends BaseController<MidDjTestSendEntity> {
    @Autowired
    private IMidDjTestSendService midDjTestSendService;
    @Autowired
    public MidElectricalInspectionAviTestController(IMidDjTestSendService midDjTestSendService) {
        this.crudService = midDjTestSendService;
        this.midDjTestSendService = midDjTestSendService;
    }
    private static final Logger logger = LoggerFactory.getLogger(MidElectricalInspectionAviTestController.class);
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

    @PostMapping(value = "websend")
    @Operation(summary = "页面操作-发送电检数据")
    public ResultVO webSend(@RequestBody List<String> ids) {
        //获取下发数据
        List<MidDjTestSendEntity> list = midDjTestSendService.getDataByIds(ids);
        for (MidDjTestSendEntity entity : list) {
            //整车信息组装下发
            ResultVO<CarInfoDto> carInfo = ppsCommunicationService.getCarInfoByVinTest(entity);
            if (carInfo == null || !carInfo.getSuccess()) {
                throw new InkelinkException("整车信息组装失败。" + (carInfo == null ? "" : carInfo.getMessage()));
            }
            logger.info("整车信息组装JSON:"+JsonUtils.toJsonString(carInfo.getData()));
            fetchDataFromApi(carInfo.getData());



            //EP信息组装下发
           /* ResultVO<List<EpInfoDto>> epInfoByVin = epsCommunicationService.getEpInfoByVin(entity.getVin());
            if (epInfoByVin == null || !epInfoByVin.getSuccess()) {
                throw new InkelinkException("电检下发EP信息失败。" + (epInfoByVin == null ? "" : epInfoByVin.getMessage()));
            }
            logger.info("EP信息组装JSON:"+JsonUtils.toJsonString(epInfoByVin.getData()));
            fetchDataFromApi(epInfoByVin.getData());*/


            //软件信息组装
            ResultVO<EcuCarInfoDto> carInfoByVin = ppsCommunicationService.getEcuCarInfoByVinTest(entity);
            if (carInfoByVin == null || !carInfoByVin.getSuccess()) {
                throw new InkelinkException("软件信息组装失败。" + (carInfoByVin == null ? "" : carInfoByVin.getMessage()));
            }
            logger.info("软件信息组装JSON:"+JsonUtils.toJsonString(carInfoByVin.getData()));
            fetchDataFromApi(carInfoByVin.getData());

            //更新状态
            entity.setStatus(true);
            midDjTestSendService.updateById(entity);
            midDjTestSendService.saveChange();
        }
        return new ResultVO<>().ok("", "操作成功");
    }



    @GetMapping(value = "send")
    @Operation(summary = "组装数据发送")
    public ResultVO send() {
        //获取下发数据
        List<MidDjTestSendEntity> list = midDjTestSendService.getData(null);
        for (MidDjTestSendEntity entity : list) {
            //整车信息组装下发
            ResultVO<CarInfoDto> carInfo = ppsCommunicationService.getCarInfoByVinTest(entity);
            if (carInfo == null || !carInfo.getSuccess()) {
                throw new InkelinkException("整车信息组装失败。" + (carInfo == null ? "" : carInfo.getMessage()));
            }
            logger.info("整车信息组装JSON:"+JsonUtils.toJsonString(carInfo.getData()));
            fetchDataFromApi(carInfo.getData());



            //EP信息组装下发
           /* ResultVO<List<EpInfoDto>> epInfoByVin = epsCommunicationService.getEpInfoByVin(entity.getVin());
            if (epInfoByVin == null || !epInfoByVin.getSuccess()) {
                throw new InkelinkException("电检下发EP信息失败。" + (epInfoByVin == null ? "" : epInfoByVin.getMessage()));
            }
            logger.info("EP信息组装JSON:"+JsonUtils.toJsonString(epInfoByVin.getData()));
            fetchDataFromApi(epInfoByVin.getData());*/


            //软件信息组装
            ResultVO<EcuCarInfoDto> carInfoByVin = ppsCommunicationService.getEcuCarInfoByVinTest(entity);
            if (carInfoByVin == null || !carInfoByVin.getSuccess()) {
                throw new InkelinkException("软件信息组装失败。" + (carInfoByVin == null ? "" : carInfoByVin.getMessage()));
            }
            logger.info("软件信息组装JSON:"+JsonUtils.toJsonString(carInfoByVin.getData()));
            fetchDataFromApi(carInfoByVin.getData());

            //更新状态
            entity.setStatus(true);
            midDjTestSendService.updateById(entity);
            midDjTestSendService.saveChange();
        }
        return new ResultVO<>().ok("", "操作成功");
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
            epInfoDtoList.stream().forEach(epInfoDto->{
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
