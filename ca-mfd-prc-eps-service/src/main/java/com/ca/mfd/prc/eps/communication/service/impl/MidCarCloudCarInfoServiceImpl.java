package com.ca.mfd.prc.eps.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.exception.ErrorCode;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.eps.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.eps.communication.dto.CarCloudCarInfoDto;
import com.ca.mfd.prc.eps.communication.dto.CarCloudCarInfoQddjDto;
import com.ca.mfd.prc.eps.communication.dto.CheyunTestDto;
import com.ca.mfd.prc.eps.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.eps.communication.mapper.IMidCarCloudCarInfoMapper;
import com.ca.mfd.prc.eps.communication.entity.MidCarCloudCarInfoEntity;
import com.ca.mfd.prc.eps.communication.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.eps.communication.remote.app.pps.dto.BomConfigDto;
import com.ca.mfd.prc.eps.communication.remote.app.pps.dto.MidAsVehicleDto;
import com.ca.mfd.prc.eps.communication.remote.app.pps.dto.SoftwareBomListDto;
import com.ca.mfd.prc.eps.communication.remote.app.pps.entity.MidVehicleMasterEntity;
import com.ca.mfd.prc.eps.communication.remote.app.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.eps.communication.remote.app.pps.provider.MidAsVehicleProvider;
import com.ca.mfd.prc.eps.communication.remote.app.pps.provider.PpsCommunicationProvider;
import com.ca.mfd.prc.eps.communication.remote.app.pps.provider.PpsOrderProvider;
import com.ca.mfd.prc.eps.communication.remote.app.pps.provider.PpsPlanProvider;
import com.ca.mfd.prc.eps.communication.remote.app.pps.provider.PpsVehicleMasterProcessProvider;
import com.ca.mfd.prc.eps.communication.remote.app.qps.provider.QpsCommunicationProvider;
import com.ca.mfd.prc.eps.communication.service.IMidApiLogService;
import com.ca.mfd.prc.eps.communication.service.IMidCarCloudCarInfoService;
import com.ca.mfd.prc.eps.communication.service.IMidCarCloudDeviceService;
import com.ca.mfd.prc.eps.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsOrderEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @author inkelink
 * @Description: 车辆基础数据中间表（车云）服务实现
 * @date 2023年12月12日
 * @变更说明 BY inkelink At 2023年12月12日
 */
@Service
public class MidCarCloudCarInfoServiceImpl extends AbstractCrudServiceImpl<IMidCarCloudCarInfoMapper, MidCarCloudCarInfoEntity> implements IMidCarCloudCarInfoService {
    final Logger logger = LoggerFactory.getLogger(MidCarCloudCarInfoServiceImpl.class);
    @Autowired
    private PmVersionProvider pmVersionProvider;

    @Autowired
    private PpsOrderProvider ppsOrderProvider;

    @Autowired
    private PpsCommunicationProvider communicationProvider;

    @Autowired
    private IMidApiLogService midApiLogBaseService;

    @Autowired
    private IMidCarCloudDeviceService carCloudDeviceService;

    @Autowired
    private IMidCarCloudCarInfoMapper carCloudCarInfoMapper;

    @Autowired
    private PpsPlanProvider ppsPlanProvider;

    @Autowired
    private PpsVehicleMasterProcessProvider ppsVehicleMasterProcessProvider;

    @Autowired
    private QpsCommunicationProvider qpsCommunicationProvider;
    @Autowired
    private MidAsVehicleProvider midAsVehicleProvider;

    @Override
    public ResultVO<CarCloudCarInfoDto> carCloudCarInfoSend(String vinCode) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(ErrorCode.SUCCESS);
        String reqNo = UUIDUtils.getGuid();
        logger.info("车云整车数据信息[" + reqNo + "]开始组装数据");
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.CAR_CLOUD_INFO);
        int status = 1;
        String errMsg = "";
        loginfo.setDataLineNo(0);
        loginfo.setRequestStartTime(new Date());

        CarCloudCarInfoDto dto = new CarCloudCarInfoDto();
        dto.setVinCode(vinCode);
        try {
            loginfo.setStatus(0);
            midApiLogBaseService.insert(loginfo);
            midApiLogBaseService.saveChange();


            PmAllDTO objectedPm = pmVersionProvider.getObjectedPm();
            PpsOrderEntity order = ppsOrderProvider.getPpsOrderBySnOrBarcode(dto.getVinCode());
            if (order != null) {
                //取得车型
                String[] str = order.getProductCode().split("\\.");
                dto.setBomRoom(order.getCharacteristic1());
                if (str != null) {
                    dto.setVehicleModelNumber(str[0]);
                }
                dto.setVehicleMaterialNumber(order.getProductCode());
                //根据整车物料号和型号查询车型
                MidVehicleMasterEntity masterEntity = ppsVehicleMasterProcessProvider.getVehicleMasterByParam(order.getProductCode(), order.getModel());
                if (masterEntity != null) {
                    dto.setBasicVehicleModel(masterEntity.getBasicVehicleModel());
                    dto.setBodyColorCode(masterEntity.getBodyColorCode());
                }
                PpsPlanEntity firstByPlanNo = ppsPlanProvider.getFirstByPlanNo(order.getPlanNo());
                if (firstByPlanNo == null) {
                    throw new InkelinkException("未查到计划数据");
                }
                //赋值销售渠道
                MidAsVehicleDto vehicleDto= midAsVehicleProvider.getVehicleByPlanNo(firstByPlanNo.getPlanNo());
                if(vehicleDto!=null){
                    dto.setDemandOrigin(vehicleDto.getDemandOrigin());
                }

            }
            // TODO: 2024/1/5 车身颜色、车身颜色名称 配置编码名称
             /* dto.setVehicleDesc();//配置编码名称*/
            if (StringUtils.isNotEmpty(dto.getVehicleMaterialNumber())) {
                //获取单车配置字
                List<BomConfigDto> bomConfig = communicationProvider.getBomConfig(dto.getVehicleMaterialNumber(), DateUtils.format(order.getEstimatedStartDt(), DateUtils.DATE_PATTERN));
                BomConfigDto bomConfigDto = bomConfig.stream().findFirst().orElse(null);
                dto.setChangeCode(bomConfigDto == null ? "" : bomConfigDto.getChangeCode());
                //获取软件清单数据
                List<SoftwareBomListDto> softBom = communicationProvider.getSoftBom(dto.getVehicleMaterialNumber(), DateUtils.format(order.getEstimatedStartDt(), DateUtils.DATE_PATTERN));
                SoftwareBomListDto softwareBomListDto = softBom.stream().findFirst().orElse(null);
                dto.setPublishChangeCode(softwareBomListDto == null ? "" : softwareBomListDto.getPublishChangeCode());
                dto.setLastVehicleVersionCode(softwareBomListDto == null ? "" : softwareBomListDto.getLastVehicleVersionCode());
            }
            dto.setOrganizationCode(objectedPm.getOrganization().getOrganizationCode());
            dto.setOrganizationName(objectedPm.getOrganization().getOrganizationName());

            //根据vin号查询法规数据
            dto = qpsCommunicationProvider.carInfoSupplement(dto);

            MidCarCloudCarInfoEntity entity = new MidCarCloudCarInfoEntity();
            BeanUtils.copyProperties(dto, entity);
            entity.setQddj(JsonUtils.toJsonString(dto.getQddj()));
            entity.setPrcMidApiLogId(loginfo.getId());
            this.insert(entity);
            this.saveChange();

        } catch (Exception ex) {
            status = 5;
            errMsg = "车云整车数据信息[" + reqNo + "]处理失败:";
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
        logger.info("车云整车数据信息[" + reqNo + "]执行完成:");
        resultVO.setData(dto);
        return resultVO;
    }

    @Override
    public ResultVO<List<MidCarCloudCarInfoEntity>> queryByDate(String startTime, String endTime) {
        if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
            throw new InkelinkException("时间不能为空");
        }

        QueryWrapper<MidCarCloudCarInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().ge(MidCarCloudCarInfoEntity::getCreationDate, startTime);
        wrapper.lambda().le(MidCarCloudCarInfoEntity::getCreationDate, endTime);
        List<MidCarCloudCarInfoEntity> data = this.getData(wrapper, false);
        if (CollectionUtils.isNotEmpty(data)) {
            return new ResultVO<List<MidCarCloudCarInfoEntity>>().ok(data);
        } else {
            return new ResultVO<List<MidCarCloudCarInfoEntity>>().ok(null);
        }
    }

    @Override
    public ResultVO<CarCloudCarInfoDto> carCloudCarInfoSendTest(CheyunTestDto dto) {

        ResultVO resultVO = new ResultVO();
        resultVO.setCode(ErrorCode.SUCCESS);
        String reqNo = UUIDUtils.getGuid();
        logger.info("车云整车数据信息[" + reqNo + "]开始组装数据");
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.CAR_CLOUD_INFO);
        int status = 1;
        String errMsg = "";
        loginfo.setDataLineNo(0);
        loginfo.setRequestStartTime(new Date());

        CarCloudCarInfoDto carInfoDto = new CarCloudCarInfoDto();
        carInfoDto.setVinCode(dto.getVin());
        try {
            loginfo.setStatus(0);
            midApiLogBaseService.insert(loginfo);
            midApiLogBaseService.saveChange();

            PmAllDTO objectedPm = pmVersionProvider.getObjectedPm();

            //取得车型
            String[] str = dto.getVehicleMaterialNumbers().split("\\.");
            carInfoDto.setBomRoom("CD701");
            if (str != null) {
                carInfoDto.setVehicleModelNumber(str[0]);
            }
            carInfoDto.setBodyColorCode("SL6");
            carInfoDto.setVehicleDesc("中国长安");
            carInfoDto.setRinCode("21001001231030420000000136308076");
            carInfoDto.setVehicleMaterialNumber(dto.getVehicleMaterialNumbers());
            if (StringUtils.isNotEmpty(dto.getVehicleMaterialNumbers())) {
                //获取单车配置字
                List<BomConfigDto> bomConfig = communicationProvider.getBomConfig(dto.getVehicleMaterialNumbers(), DateUtils.format(dto.getSpecifyDate(), DateUtils.DATE_PATTERN));
                BomConfigDto bomConfigDto = bomConfig.stream().findFirst().orElse(null);
                carInfoDto.setChangeCode(bomConfigDto == null ? "" : bomConfigDto.getChangeCode());
                //获取软件清单数据
                List<SoftwareBomListDto> softBom = communicationProvider.getSoftBom(dto.getVehicleMaterialNumbers(), DateUtils.format(dto.getSpecifyDate(), DateUtils.DATE_PATTERN));
                SoftwareBomListDto softwareBomListDto = softBom.stream().findFirst().orElse(null);
                carInfoDto.setPublishChangeCode(softwareBomListDto == null ? "" : softwareBomListDto.getPublishChangeCode());
                carInfoDto.setLastVehicleVersionCode(softwareBomListDto == null ? "" : softwareBomListDto.getLastVehicleVersionCode());
            }
            carInfoDto.setOrganizationCode(objectedPm.getOrganization().getOrganizationCode());
            carInfoDto.setOrganizationName(objectedPm.getOrganization().getOrganizationName());

            //根据vin号查询法规数据
            carInfoDto = qpsCommunicationProvider.carInfoSupplement(carInfoDto);

/*            //法规数据赋值-假数据
            carInfoDto.setClpp("长安牌");
            carInfoDto.setEdzk("5");
            carInfoDto.setJsszcrs("5");
            carInfoDto.setEngineNo("XSCJV2024010101542689234");
            carInfoDto.setRlzl("汽油/电混合动力");
            carInfoDto.setFzrq(new Date());
            carInfoDto.setCertificateNo("2024010101542689234");
            carInfoDto.setClmc("插电式混合动力轿车");
            carInfoDto.setCllx("乘用车及客车");
            carInfoDto.setClscqymc("长安汽车");
            carInfoDto.setZzsmc("长安汽车渝北工厂");
            carInfoDto.setZzjdclzzg("中国");
            CarCloudCarInfoQddjDto qddjDto = new CarCloudCarInfoQddjDto();
            qddjDto.setEngineModel("2024010101542689234");
            qddjDto.setEngineNo("2024010101542689234");
            qddjDto.setMotorFModel("2024010101542689234");
            qddjDto.setMotorFNo("2024010101542689234");
            qddjDto.setMotorBModel("2024010101542689234");
            qddjDto.setMotorBNo("2024010101542689234");
            carInfoDto.setQddj(qddjDto);
            carInfoDto.setCnzzXhScc("长安汽车渝北工厂");*/

            MidCarCloudCarInfoEntity entity = new MidCarCloudCarInfoEntity();
            BeanUtils.copyProperties(carInfoDto, entity);
            entity.setQddj(JsonUtils.toJsonString(carInfoDto.getQddj()));
            entity.setPrcMidApiLogId(loginfo.getId());
            this.insert(entity);
            this.saveChange();

        } catch (Exception ex) {
            status = 5;
            errMsg = "车云整车数据信息[" + reqNo + "]处理失败:";
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
        logger.info("车云整车数据信息[" + reqNo + "]执行完成:");
        resultVO.setData(carInfoDto);
        return resultVO;
    }

    @Override
    public ResultVO<CarCloudCarInfoDto> providerQueryByVin(String vin) {
        if (StringUtils.isEmpty(vin)) {
            throw new InkelinkException("vin码不能为空");
        }
        QueryWrapper<MidCarCloudCarInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(MidCarCloudCarInfoEntity::getVinCode, vin);
        MidCarCloudCarInfoEntity entity = this.getData(wrapper, false).stream().sorted(Comparator.comparing(MidCarCloudCarInfoEntity::getCreationDate).reversed()).findFirst().orElse(null);
        ;
        if (entity != null) {
            CarCloudCarInfoDto infoDto = new CarCloudCarInfoDto();
            BeanUtils.copyProperties(entity, infoDto);
            infoDto.setQddj(JsonUtils.parseObject(entity.getQddj(), CarCloudCarInfoQddjDto.class));
            return new ResultVO<CarCloudCarInfoDto>().ok(infoDto);
        }
        return new ResultVO<CarCloudCarInfoDto>().ok(null);
    }

    @Override
    public ResultVO<List<MidCarCloudCarInfoEntity>> queryByVins(List<String> vins) {
        if (CollectionUtils.isEmpty(vins)) {
            throw new InkelinkException("vin码不能为空");
        }

        QueryWrapper<MidCarCloudCarInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(MidCarCloudCarInfoEntity::getVinCode, vins);
        List<MidCarCloudCarInfoEntity> data = this.getData(wrapper, false);
        if (CollectionUtils.isNotEmpty(data)) {
            return new ResultVO<List<MidCarCloudCarInfoEntity>>().ok(data);
        } else {
            return new ResultVO<List<MidCarCloudCarInfoEntity>>().ok(null);
        }


    }
}