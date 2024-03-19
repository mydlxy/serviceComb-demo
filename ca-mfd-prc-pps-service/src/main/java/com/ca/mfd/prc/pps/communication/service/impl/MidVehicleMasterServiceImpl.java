package com.ca.mfd.prc.pps.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.enums.ConditionRelation;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.pps.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pps.communication.dto.BomResultVo;
import com.ca.mfd.prc.pps.communication.dto.VehicleModelDto;
import com.ca.mfd.prc.pps.communication.entity.MidVehicleCodeEntity;
import com.ca.mfd.prc.pps.communication.entity.MidVehicleMasterEntity;
import com.ca.mfd.prc.pps.communication.mapper.IMidVehicleMasterMapper;
import com.ca.mfd.prc.pps.communication.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pps.communication.remote.app.pm.provider.PmCommunicationProvider;
import com.ca.mfd.prc.pps.communication.service.IMidVehicleCodeService;
import com.ca.mfd.prc.pps.communication.service.IMidVehicleMasterService;
import com.ca.mfd.prc.pps.communication.remote.app.core.sys.entity.SysConfigurationEntity;
import com.ca.mfd.prc.pps.entity.PpsOrderEntity;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @Description: 车型主数据中间表服务实现
 * @author inkelink
 * @date 2023年11月02日
 * @变更说明 BY inkelink At 2023年11月02日
 */
@Service
public class MidVehicleMasterServiceImpl extends MidBomBaseServiceImpl<IMidVehicleMasterMapper, MidVehicleMasterEntity, VehicleModelDto> implements IMidVehicleMasterService {

    private static final Object lockObj = new Object();
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_MID_VEHICLE_MASTER";
    private static final Logger logger = LoggerFactory.getLogger(MidVehicleMasterServiceImpl.class);


    @Autowired
    private IMidVehicleCodeService vehicleCodeService;

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<MidVehicleMasterEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(MidVehicleMasterEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(MidVehicleMasterEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<MidVehicleMasterEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<MidVehicleMasterEntity> getAllDatas() {
        List<MidVehicleMasterEntity> datas = localCache.getObject(cacheName);
        if (datas == null || datas.isEmpty()) {
            synchronized (lockObj) {
                datas = localCache.getObject(cacheName);
                if (datas == null || datas.isEmpty()) {
                    datas = getData(null);
                    localCache.addObject(cacheName, datas);
                }
            }
        }
        return datas;
    }

    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;

    @Override
    public void receive() {

        List<MidVehicleCodeEntity> nodeCodes = vehicleCodeService.getAllDatas().stream().filter(c -> c.getIsBomNode()==true && 0 == c.getExeStatus()).collect(Collectors.toList());

        for (int i = 0; i <nodeCodes.size() ; i++) {
            //调用外部接口
            try {
                fetchDataFromApi(nodeCodes.get(i));
            }catch (Exception e){
                logger.error("",e);
            }
        }
        nodeCodes.stream().forEach(v->{
            v.setExeStatus(1);
        });
        vehicleCodeService.updateBatchById(nodeCodes,200);
    }

    private void fetchDataFromApi(MidVehicleCodeEntity vehicleCodeEntity) {
        super.fetchDataFromApi("vehiclemodel_receive","整车物料号主数据",ApiTypeConst.BOM_VEHICLE_MODE,true,true,vehicleCodeEntity.getNodeCode());
//        String apiUrl = sysConfigurationProvider.getConfiguration("vehiclemodel_receive", "midapi");
//        if (StringUtils.isBlank(apiUrl)) {
//            throw new InkelinkException("没有配置上报的地址[vehiclemodel_receive]");
//        }
//
//        String reqNo = UUIDUtils.getGuid();;
//        logger.info("整车车型数据信息[" + reqNo + "]开始接收数据");
//        MidApiLogEntity loginfo = new MidApiLogEntity();
//        loginfo.setApiType(ApiTypeConst.BOM_VEHICLE_MODE);
//        int status = 1;
//        String errMsg = "";
//        loginfo.setDataLineNo(0);
//        loginfo.setRequestStartTime(new Date());
//        List<VehicleModelDto> infos = new ArrayList<>();
//        try {
//            loginfo.setStatus(0);
//            midApiLogBaseService.insert(loginfo);
//            midApiLogBaseService.saveChange();
//
//            // 构建API请求参数
//            Map param = new HashMap<>();
//            param.put("serviceId",reqNo);
//            param.put("bomRoom", vehicleCodeEntity.getNodeCode());
//            param.put("startDate", DateUtils.format(new Date(),DateUtils.DATE_PATTERN));
//            param.put("endDate", DateUtils.format(new Date(),DateUtils.DATE_PATTERN));
//            // 发起HTTP请求
//            String responseData = apiPtService.postapi(apiUrl, param, null);
//
//            logger.warn("API平台测试url调用：" + responseData);
//            BomResultVo resultVO = JsonUtils.parseObject(responseData, BomResultVo.class);
//            infos = JsonUtils.parseArray(JsonUtils.toJsonString(resultVO.getData()), VehicleModelDto.class);
//            String verifyData = md5(JsonUtils.toJsonString(infos));
//            QueryWrapper<MidApiLogEntity> queryWrapper=new QueryWrapper<>();
//            queryWrapper.lambda().eq(BaseEntity::getAttribute1,verifyData);
//            List<MidApiLogEntity> data = midApiLogBaseService.getData(queryWrapper, false);
//            if(CollectionUtils.isEmpty(data)){
//                this.insertBatch(infos.stream().map(c -> {
//                    MidVehicleMasterEntity entity = new MidVehicleMasterEntity();
//                    BeanUtils.copyProperties(c,entity);
//                    entity.setPrcMidApiLogId(loginfo.getId());
//                    entity.setId(IdGenerator.getId());
//                    entity.setExeStatus(0);
//                    entity.setExeTime(new Date());
//                    entity.setExeMsg(StringUtils.EMPTY);
//                    return entity;
//                }).collect(Collectors.toList()),200,false,1);
//                this.saveChange();
//                errMsg = "整车车型信息[" + reqNo + "]接收保存成功";
//                logger.info(errMsg);
//            }else {
//                errMsg = "整车车型信息[" + reqNo + "]处理成功，数据已存在";
//                logger.info(errMsg);
//            }
//            loginfo.setAttribute1(verifyData);
//
//        } catch (Exception ex) {
//            status = 5;
//            errMsg = "整车车型信息[" + reqNo + "]处理失败:" + ex.getMessage();
//            logger.info(errMsg);
//            logger.error(errMsg, ex);
//        }
//        loginfo.setRequestStopTime(new Date());
//        loginfo.setStatus(status);
//        loginfo.setRemark(errMsg);
//        midApiLogBaseService.update(loginfo);
//        midApiLogBaseService.saveChange();
//        logger.info("整车车型信息[" + reqNo + "]执行完成:");
    }

    private String md5(String str) {
        //.replace("-", "")
        return DigestUtils.md5Hex(str.getBytes(StandardCharsets.UTF_8)).toLowerCase();
    }

    @Override
    protected MidVehicleMasterEntity getEntity(VehicleModelDto vehicleModelDto, Long loginfoId) {
        MidVehicleMasterEntity entity = new MidVehicleMasterEntity();
        BeanUtils.copyProperties(vehicleModelDto,entity);
        entity.setPrcMidApiLogId(loginfoId);
        entity.setId(IdGenerator.getId());
        entity.setExeStatus(0);
        entity.setExeTime(new Date());
        entity.setExeMsg(StringUtils.EMPTY);
        return entity;
    }

    @Override
    protected List<VehicleModelDto> fetchEntity(BomResultVo resultVO) {
        return JsonUtils.parseArray(JsonUtils.toJsonString(resultVO.getData()), VehicleModelDto.class);
    }

    @Override
    protected Map<String, String> getParams(String... params) {
        Map<String,String> param = new HashMap<>(4);
        param.put("bomRoom", params[0]);
        param.put("startDate", "2023-12-01");
        param.put("endDate", DateUtils.format(new Date(),DateUtils.DATE_PATTERN));
        return param;
    }

    @Override
    public List<VehicleModelDto> getVehicleModelData(String materialNo) {
        List<VehicleModelDto> collect = this.getAllDatas().stream().filter(c -> materialNo.equals(c.getVehicleMaterialNumber())).map(x -> {
            VehicleModelDto dto = new VehicleModelDto();
            BeanUtils.copyProperties(x, dto);
            return dto;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public void excute() {
        List<MidVehicleMasterEntity> allDatas = this.getAllDatas();
        List<String> bomRooms = allDatas.stream().map(MidVehicleMasterEntity::getBomRoom).collect(Collectors.toList());
        List<SysConfigurationEntity> getSysConfigurations = sysConfigurationProvider.getSysConfigurations("vehicleModel");
        List<String> values = getSysConfigurations.stream().map(SysConfigurationEntity::getValue).collect(Collectors.toList());
        Set<String> insertResult = bomRooms.stream().filter(x -> !values.contains(x)).collect(Collectors.toSet());
        if(!CollectionUtils.isEmpty(insertResult)){
            List<SysConfigurationEntity> list=new ArrayList<>();
            insertResult.stream().forEach(c->{
                SysConfigurationEntity sysConfiguration=new SysConfigurationEntity();
                sysConfiguration.setCategory("vehicleModel");
                sysConfiguration.setText(c);
                sysConfiguration.setValue(c);
                list.add(sysConfiguration);
            });
            sysConfigurationProvider._insertBath(list);
        }


    }

    @Override
    public MidVehicleMasterEntity getVehicleMasterByParam(String vehicleMaterialNumber, String bomRoom) {
        List<ConditionDto> dtos = new ArrayList<>();
        dtos.add(new ConditionDto("vehicleMaterialNumber", vehicleMaterialNumber, ConditionOper.Equal));
        dtos.add(new ConditionDto("bomRoom", bomRoom, ConditionOper.Equal));
        return this.getData(dtos).stream().findFirst().orElse(null);
    }
}