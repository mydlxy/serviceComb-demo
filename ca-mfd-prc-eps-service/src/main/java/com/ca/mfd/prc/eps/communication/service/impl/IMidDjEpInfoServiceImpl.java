package com.ca.mfd.prc.eps.communication.service.impl;

import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.eps.communication.remote.app.pps.dto.SoftwareBomListDto;
import com.ca.mfd.prc.eps.communication.remote.app.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.eps.communication.remote.app.pps.provider.PpsCommunicationProvider;
import com.ca.mfd.prc.eps.communication.remote.app.pps.provider.PpsOrderProvider;
import com.ca.mfd.prc.eps.communication.remote.app.pps.provider.PpsPlanProvider;
import com.ca.mfd.prc.eps.communication.service.IMidDjEpInfoService;
import com.ca.mfd.prc.eps.communication.dto.EpInfoDto;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoDataEntity;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoDataTrcEntity;
import com.ca.mfd.prc.eps.mapper.IEpsVehicleWoDataMapper;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoDataService;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoDataTrcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 电检EP信息组装实现
 * @date 2023年11月29日
 * @变更说明 BY inkelink At 2023年11月29日
 */
@Service
public class IMidDjEpInfoServiceImpl implements IMidDjEpInfoService {

    @Autowired
    private PpsCommunicationProvider ppsCommunicationProvider;
    @Autowired
    private PpsOrderProvider ppsOrderProvider;
    @Autowired
    private PpsPlanProvider ppsPlanProvider;


    @Autowired
    private IEpsVehicleWoDataService epsVehicleWoDataService;
    @Autowired
    private IEpsVehicleWoDataTrcService epsVehicleWoDataTrcService;
    @Autowired
    private IEpsVehicleWoDataMapper epsVehicleWoDataMapper;


    @Override
    public List<EpInfoDto> getEpInfoByVin(String vin) {
        List<EpInfoDto> epInfoDtoList = new ArrayList<>();

       PpsOrderEntity ppsOrderEntity = ppsOrderProvider.getPpsOrderBySnOrBarcode(vin);
        if (ppsOrderEntity == null) {
            throw new InkelinkException("未查到车辆订单数据");
        }
        /*
        PpsPlanEntity firstByPlanNo = ppsPlanProvider.getFirstByPlanNo(ppsOrderEntity.getPlanNo());
        if (firstByPlanNo == null) {
            throw new InkelinkException("未查到计划数据");
        }

        //获取软件清单
        //List<SoftwareBomListDto> softwareBomListDto = ppsCommunicationProvider.getSoftBom(firstByPlanNo.getPlanNo(), DateUtils.format(firstByPlanNo.getEstimatedStartDt(), DateUtils.DATE_PATTERN));
        List<SoftwareBomListDto> softwareBomListDto = ppsCommunicationProvider.getSoftBom(firstByPlanNo.getPlanNo(), DateUtils.format(new Date(), DateUtils.DATE_PATTERN));
        if (CollectionUtils.isEmpty(softwareBomListDto)) {
            throw new InkelinkException("未获取到软件清单");
        }*/

        //取工艺数据
        List<EpsVehicleWoDataEntity> woDataEntityList = epsVehicleWoDataMapper.getVehicleDataByVin(vin).stream().filter(wo-> wo.getRemark().contains("电检用")).collect(Collectors.toList());
        if (woDataEntityList == null || woDataEntityList.isEmpty()) {
            throw new InkelinkException("未取到EP的工艺数据");
        }

        //循环取工艺数据的追朔记录
        woDataEntityList.stream().forEach(wo -> {
            List<ConditionDto> conditions = new ArrayList<>();
            conditions.add(new ConditionDto("prcEpsVehicleWoDataId", wo.getId().toString(), ConditionOper.Equal));
            EpsVehicleWoDataTrcEntity woDataTrcEntity = epsVehicleWoDataTrcService.getData(conditions).stream().findFirst().orElse(null);
            if (woDataTrcEntity == null) {
                throw new InkelinkException("未取到追朔操作记录");
            }
            EpInfoDto epInfoDto = new EpInfoDto();
            epInfoDto.setVinCode(vin);
            //epInfoDto.setEcuTypeCode(softwareBomListDto.get(0).getEcuTypeCode());
            epInfoDto.setEcuTypeCode("EDC");
            epInfoDto.setBarcode(woDataTrcEntity.getBarcode());
            epInfoDto.setMaterialCode(woDataTrcEntity.getPartsNumber());
            epInfoDto.setLastUpdateDate(woDataTrcEntity.getLastUpdateDate());
            epInfoDtoList.add(epInfoDto);
        });
        return epInfoDtoList;
    }
}
