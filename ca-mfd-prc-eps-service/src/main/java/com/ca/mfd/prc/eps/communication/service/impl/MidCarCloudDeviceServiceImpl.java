package com.ca.mfd.prc.eps.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.exception.ErrorCode;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.*;
import com.ca.mfd.prc.eps.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.eps.communication.dto.CarCloudDeviceDatailDto;
import com.ca.mfd.prc.eps.communication.dto.CarCloudDeviceDto;
import com.ca.mfd.prc.eps.communication.dto.CheyunTestDto;
import com.ca.mfd.prc.eps.communication.dto.EcuSoftwareDidDto;
import com.ca.mfd.prc.eps.communication.dto.MidDjEcuCarResultDto;
import com.ca.mfd.prc.eps.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.eps.communication.mapper.IMidCarCloudDeviceMapper;
import com.ca.mfd.prc.eps.communication.entity.MidCarCloudDeviceEntity;
import com.ca.mfd.prc.eps.communication.remote.app.pps.dto.*;
import com.ca.mfd.prc.eps.communication.remote.app.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.eps.communication.remote.app.pps.provider.PpsCommunicationProvider;
import com.ca.mfd.prc.eps.communication.remote.app.pps.provider.PpsOrderProvider;
import com.ca.mfd.prc.eps.communication.remote.app.pps.provider.PpsPlanProvider;
import com.ca.mfd.prc.eps.communication.service.IMidApiLogService;
import com.ca.mfd.prc.eps.communication.service.IMidCarCloudDeviceService;
import com.ca.mfd.prc.eps.communication.service.IMidDjEcuCarResultService;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsOrderEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author inkelink
 * @Description: 车辆设备中间表（车云）服务实现
 * @date 2023年12月12日
 * @变更说明 BY inkelink At 2023年12月12日
 */
@Service
public class MidCarCloudDeviceServiceImpl extends AbstractCrudServiceImpl<IMidCarCloudDeviceMapper, MidCarCloudDeviceEntity> implements IMidCarCloudDeviceService {
    final Logger logger = LoggerFactory.getLogger(MidCarCloudDeviceServiceImpl.class);

    @Autowired
    private PpsOrderProvider ppsOrderProvider;

    @Autowired
    private PpsPlanProvider ppsPlanProvider;


    @Autowired
    private PpsCommunicationProvider communicationProvider;

    @Autowired
    private IMidDjEcuCarResultService ecuCarResultService;

    @Autowired
    private IMidApiLogService midApiLogBaseService;

    @Autowired
    private PpsCommunicationProvider ppsCommunicationProvider;

    @Autowired
    private IMidDjEcuCarResultService midDjEcuCarResultService;

    @Override
    public ResultVO<CarCloudDeviceDto> carCloudDeviceSend(String vinCode) {

        ResultVO resultVO = new ResultVO();
        resultVO.setCode(ErrorCode.SUCCESS);
        String reqNo = UUIDUtils.getGuid();
        ;
        logger.info("车云设备组装数据信息[" + reqNo + "]开始组装数据");
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.CAR_CLOUD_DEVICE);
        int status = 1;
        String errMsg = "";
        loginfo.setDataLineNo(0);
        loginfo.setRequestStartTime(new Date());

        CarCloudDeviceDto dto = new CarCloudDeviceDto();
        dto.setVinCode(vinCode);
        try {
            loginfo.setStatus(0);
            midApiLogBaseService.insert(loginfo);
            midApiLogBaseService.saveChange();

            MidCarCloudDeviceEntity entity = new MidCarCloudDeviceEntity();
            entity.setVinCode(vinCode);
            entity.setPrcMidApiLogId(loginfo.getId());

            PpsOrderEntity ppsOrderEntity = ppsOrderProvider.getPpsOrderBySnOrBarcode(dto.getVinCode());
            if (ppsOrderEntity == null) {
                throw new InkelinkException("未查到车辆订单数据");
            }
            PpsPlanEntity firstByPlanNo = ppsPlanProvider.getFirstByPlanNo(ppsOrderEntity.getPlanNo());
            if (firstByPlanNo == null) {
                throw new InkelinkException("未查到计划数据");
            }

            //获取软件配置字
            List<BomConfigDto> bomConfig = ppsCommunicationProvider.getBomConfig(firstByPlanNo.getProductCode(), DateUtils.format(firstByPlanNo.getEstimatedStartDt(), DateUtils.DATE_PATTERN));
            if (org.springframework.util.CollectionUtils.isEmpty(bomConfig)) {
                throw new InkelinkException("未取软到件配置字");
            }
            //获取软件清单
            List<SoftwareBomListDto> softwareBomListDto = ppsCommunicationProvider.getSoftBom(firstByPlanNo.getProductCode(), DateUtils.format(firstByPlanNo.getEstimatedStartDt(), DateUtils.DATE_PATTERN));
            if (org.springframework.util.CollectionUtils.isEmpty(softwareBomListDto)) {
                throw new InkelinkException("未获取到软件清单");
            }

            List<CarCloudDeviceDatailDto> ecuList = new ArrayList<>();
            //根据vin号获取电检回传结果数据
            MidDjEcuCarResultDto carResultDto = midDjEcuCarResultService.queryEcuCarByVinCode(vinCode);
            if (carResultDto != null && !carResultDto.getEcuList().isEmpty()) {
                carResultDto.getEcuList().forEach(x -> {
                    CarCloudDeviceDatailDto listDto = new CarCloudDeviceDatailDto();

                    //根据电检回传的ecuType匹配bom的ecuTypeCode取得bom数据
                    SoftwareBomListDto bomListDto = softwareBomListDto.stream().filter(c -> c.getEcuTypeCode().equals(x.getEcuType())).findFirst().orElse(null);

                    listDto.setEcuTuid(x.getEcuTuid());
                    listDto.setTraceBarcode(x.getBarcode());
                    listDto.setEcuTypeCode(x.getEcuType());
                    listDto.setSoftwareCode(x.getSoftwareCode());
                    listDto.setHardwarePartNo(x.getHardwarePartNo());
                    listDto.setSoftwareVersion(x.getSoftwareVersion());
                    listDto.setDeploymentLocation(x.getDeploymentLocation());

                    //ecuConf赋值
                    List<EcuSoftwareDidDto> ecuConfList = new ArrayList<>();
                    x.getEcuConf().forEach(conf -> {
                        EcuSoftwareDidDto ecuConf = new EcuSoftwareDidDto();
                        BeanUtils.copyProperties(conf, ecuConf);
                        ecuConfList.add(ecuConf);
                    });
                    listDto.setEcuConf(ecuConfList);

                    //bom补充其他字段赋值
                    if (bomListDto != null) {
                        listDto.setEcuTypeNameCh(bomListDto.getEcuTypeName());
                        listDto.setDevEngineerIdUserLoginName(bomListDto.getDevEngineerIdUserLoginName());
                        listDto.setDevEngineerIdDisplayName(bomListDto.getDevEngineerIdDisplayName());
                        listDto.setEmployeeIdUserLoginName(bomListDto.getEmployeeIdUserLoginName());
                        listDto.setEmployeeIdDisplayName(bomListDto.getEmployeeIdDisplayName());
                        listDto.setIsSupportDiaWritten(bomListDto.getIsSupportDiaWritten() ? "1" : "0");
                        listDto.setIsSupportOta(bomListDto.getIsSupportOta() ? "1" : "0");
                        listDto.setIsCheckSwVersion(bomListDto.getIsCheckSwVersion() ? "1" : "0");
                        listDto.setIsOnlineWritten(bomListDto.getIsEleInspection() ? "1" : "0");
                        listDto.setExtensionField(bomListDto.getExtensionField());
                    } else {
                        listDto.setIsSupportDiaWritten("0");
                        listDto.setIsSupportOta("0");
                        listDto.setIsCheckSwVersion("0");
                        listDto.setIsOnlineWritten("0");
                    }
                    ecuList.add(listDto);
                });
            }


            /*softwareBomListDto.forEach(x -> {
                CarCloudDeviceDatailDto listDto = new CarCloudDeviceDatailDto();
                // TODO: 2024/1/4  设备TUID,设备追溯码：值暂无获取
                //listDto.setEcuTsuid();
                //listDto.setTraceBarcode();
                listDto.setEcuTypeCode(x.getEcuTypeCode());
                listDto.setEcuTypeNameCh(x.getEcuTypeName());
                listDto.setSoftwareCode(x.getSoftwareCode());
                listDto.setHardwarePartNo(x.getHardwarePartNo());
                listDto.setSoftwareVersion(x.getSoftwareVersion());
                listDto.setDevEngineerIdUserLoginName(x.getDevEngineerIdUserLoginName());
                listDto.setDevEngineerIdDisplayName(x.getDevEngineerIdDisplayName());
                listDto.setEmployeeIdUserLoginName(x.getEmployeeIdUserLoginName());
                listDto.setEmployeeIdDisplayName(x.getEmployeeIdDisplayName());
                listDto.setIsSupportDiaWritten(x.getIsSupportDiaWritten() ? "1" : "0");
                listDto.setIsSupportOta(x.getIsSupportOta() ? "1" : "0");
                listDto.setDeploymentLocation(x.getDeploymentLocation());
                listDto.setIsCheckSwVersion(x.getIsCheckSwVersion() ? "1" : "0");
                listDto.setIsOnlineWritten(x.getIsEleInspection() ? "1" : "0");
                listDto.setExtensionField(x.getExtensionField());
                List<SoftwareConfigDto> collect = bomConfig.get(0).getSoftwareConfigList().stream().filter(w -> w.getEcuCode().equals(x.getEcuTypeCode())).collect(Collectors.toList());
                List<EcuSoftwareDidDto> ecuConf = new ArrayList<>();
                collect.stream().forEach(v -> {
                    ecuConf.addAll(v.getEcuConfigList());
                });
                listDto.setEcuConf(ecuConf);
                ecuList.add(listDto);
            });*/


            entity.setEcuList(JsonUtils.toJsonString(ecuList));
            this.save(entity);
            this.saveChange();
            //返回数据赋值
            dto.setEcuList(ecuList);
        } catch (Exception ex) {
            status = 5;
            errMsg = "车云设备组装数据信息[" + reqNo + "]处理失败:";
            resultVO.setCode(ErrorCode.INTERNAL_SERVER_ERROR);
            resultVO.setMessage(ex.getMessage());
            resultVO.setData(null);
            logger.info(errMsg);
            logger.error(errMsg, ex);
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(errMsg);
        midApiLogBaseService.update(loginfo);
        midApiLogBaseService.saveChange();
        logger.info("车云设备组装数据信息[" + reqNo + "]执行完成:");
        resultVO.setData(dto);
        return resultVO;

    }

    @Override
    public ResultVO<List<CarCloudDeviceDto>> queryByVins(List<String> vins) {
        if (CollectionUtils.isEmpty(vins)) {
            throw new InkelinkException("vin码不能为空");
        }
        List<CarCloudDeviceDto> vo = new ArrayList<>();
        QueryWrapper<MidCarCloudDeviceEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(MidCarCloudDeviceEntity::getVinCode, vins);
        List<MidCarCloudDeviceEntity> data = this.getData(wrapper, false);
        if (CollectionUtils.isNotEmpty(data)) {
            CarCloudDeviceDto deviceDto = new CarCloudDeviceDto();
            data.forEach(x -> {
                deviceDto.setVinCode(x.getVinCode());
                deviceDto.setCreationDate(x.getCreationDate());
                deviceDto.setLastUpdateDate(x.getLastUpdateDate());
                List<CarCloudDeviceDatailDto> carCloudDeviceDatailDtos = JsonUtils.parseArray(x.getEcuList(), CarCloudDeviceDatailDto.class);
                deviceDto.setEcuList(carCloudDeviceDatailDtos);
                vo.add(deviceDto);
            });
            return new ResultVO<List<CarCloudDeviceDto>>().ok(vo);
        } else {
            return new ResultVO<List<CarCloudDeviceDto>>().ok(null);
        }
    }

    @Override
    public ResultVO<List<CarCloudDeviceDto>> queryByDate(String startTime, String endTime) {
        if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
            throw new InkelinkException("时间不能为空");
        }
        List<CarCloudDeviceDto> vo = new ArrayList<>();
        QueryWrapper<MidCarCloudDeviceEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().ge(MidCarCloudDeviceEntity::getCreationDate, startTime);
        wrapper.lambda().le(MidCarCloudDeviceEntity::getCreationDate, endTime);
        List<MidCarCloudDeviceEntity> data = this.getData(wrapper, false);
        if (CollectionUtils.isNotEmpty(data)) {
            CarCloudDeviceDto deviceDto = new CarCloudDeviceDto();
            data.forEach(x -> {
                deviceDto.setVinCode(x.getVinCode());
                deviceDto.setCreationDate(x.getCreationDate());
                deviceDto.setLastUpdateDate(x.getLastUpdateDate());
                List<CarCloudDeviceDatailDto> carCloudDeviceDatailDtos = JsonUtils.parseArray(x.getEcuList(), CarCloudDeviceDatailDto.class);
                deviceDto.setEcuList(carCloudDeviceDatailDtos);
                vo.add(deviceDto);
            });
            return new ResultVO<List<CarCloudDeviceDto>>().ok(vo);
        } else {
            return new ResultVO<List<CarCloudDeviceDto>>().ok(null);
        }
    }

    @Override
    public ResultVO carCloudDeviceSendTest(CheyunTestDto dto) {


        ResultVO resultVO = new ResultVO();
        resultVO.setCode(ErrorCode.SUCCESS);
        String reqNo = UUIDUtils.getGuid();
        ;
        logger.info("车云设备组装数据信息[" + reqNo + "]开始组装数据");
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.CAR_CLOUD_DEVICE);
        int status = 1;
        String errMsg = "";
        loginfo.setDataLineNo(0);
        loginfo.setRequestStartTime(new Date());

        CarCloudDeviceDto deviceDto = new CarCloudDeviceDto();
        deviceDto.setVinCode(dto.getVin());
        try {
            loginfo.setStatus(0);
            midApiLogBaseService.insert(loginfo);
            midApiLogBaseService.saveChange();

            MidCarCloudDeviceEntity entity = new MidCarCloudDeviceEntity();
            entity.setVinCode(dto.getVin());
            entity.setPrcMidApiLogId(loginfo.getId());


            //获取软件配置字
            List<BomConfigDto> bomConfig = ppsCommunicationProvider.getBomConfig(dto.getVehicleMaterialNumbers(), DateUtils.format(dto.getSpecifyDate(), DateUtils.DATE_PATTERN));
            if (org.springframework.util.CollectionUtils.isEmpty(bomConfig)) {
                throw new InkelinkException("未取软到件配置字");
            }
            //获取软件清单
            List<SoftwareBomListDto> softwareBomListDto = ppsCommunicationProvider.getSoftBom(dto.getVehicleMaterialNumbers(), DateUtils.format(dto.getSpecifyDate(), DateUtils.DATE_PATTERN));
            if (org.springframework.util.CollectionUtils.isEmpty(softwareBomListDto)) {
                throw new InkelinkException("未获取到软件清单");
            }

            List<CarCloudDeviceDatailDto> ecuList = new ArrayList<>();
            //根据vin号获取电检回传结果数据
            MidDjEcuCarResultDto carResultDto = midDjEcuCarResultService.queryEcuCarByVinCode(dto.getVin());
            if (carResultDto != null && !carResultDto.getEcuList().isEmpty()) {
                carResultDto.getEcuList().forEach(x -> {
                    CarCloudDeviceDatailDto listDto = new CarCloudDeviceDatailDto();

                    //根据电检回传的ecuType匹配bom的ecuTypeCode取得bom数据
                    SoftwareBomListDto bomListDto = softwareBomListDto.stream().filter(c -> c.getEcuTypeCode().equals(x.getEcuType())).findFirst().orElse(null);

                    listDto.setEcuTuid(x.getEcuTuid());
                    listDto.setTraceBarcode(x.getBarcode());
                    listDto.setEcuTypeCode(x.getEcuType());
                    listDto.setSoftwareCode(x.getSoftwareCode());
                    listDto.setHardwarePartNo(x.getHardwarePartNo());
                    listDto.setSoftwareVersion(x.getSoftwareVersion());
                    listDto.setDeploymentLocation(x.getDeploymentLocation());

                    //ecuConf赋值
                    List<EcuSoftwareDidDto> ecuConfList = new ArrayList<>();
                    x.getEcuConf().forEach(conf -> {
                        EcuSoftwareDidDto ecuConf = new EcuSoftwareDidDto();
                        BeanUtils.copyProperties(conf, ecuConf);
                        ecuConfList.add(ecuConf);
                    });
                    listDto.setEcuConf(ecuConfList);

                    //bom补充其他字段赋值
                    if (bomListDto != null) {
                        listDto.setEcuTypeNameCh(bomListDto.getEcuTypeName());
                        listDto.setDevEngineerIdUserLoginName(bomListDto.getDevEngineerIdUserLoginName());
                        listDto.setDevEngineerIdDisplayName(bomListDto.getDevEngineerIdDisplayName());
                        listDto.setEmployeeIdUserLoginName(bomListDto.getEmployeeIdUserLoginName());
                        listDto.setEmployeeIdDisplayName(bomListDto.getEmployeeIdDisplayName());
                        listDto.setIsSupportDiaWritten(bomListDto.getIsSupportDiaWritten() ? "1" : "0");
                        listDto.setIsSupportOta(bomListDto.getIsSupportOta() ? "1" : "0");
                        listDto.setIsCheckSwVersion(bomListDto.getIsCheckSwVersion() ? "1" : "0");
                        listDto.setIsOnlineWritten(bomListDto.getIsEleInspection() ? "1" : "0");
                        listDto.setExtensionField(bomListDto.getExtensionField());
                    } else {
                        listDto.setIsSupportDiaWritten("0");
                        listDto.setIsSupportOta("0");
                        listDto.setIsCheckSwVersion("0");
                        listDto.setIsOnlineWritten("0");
                    }
                    ecuList.add(listDto);
                });
            }
            entity.setEcuList(JsonUtils.toJsonString(ecuList));
            this.save(entity);
            this.saveChange();
            //返回数据赋值
            deviceDto.setEcuList(ecuList);
        } catch (Exception ex) {
            status = 5;
            errMsg = "车云设备组装数据信息[" + reqNo + "]处理失败:";
            resultVO.setCode(ErrorCode.INTERNAL_SERVER_ERROR);
            resultVO.setMessage(ex.getMessage());
            resultVO.setData(null);
            logger.info(errMsg);
            logger.error(errMsg, ex);
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(errMsg);
        midApiLogBaseService.update(loginfo);
        midApiLogBaseService.saveChange();
        logger.info("车云设备组装数据信息[" + reqNo + "]执行完成:");
        resultVO.setData(deviceDto);
        return resultVO;
    }
}