package com.ca.mfd.prc.pps.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;

import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.JsonUtils;

import com.ca.mfd.prc.pps.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pps.communication.dto.BomResultVo;
import com.ca.mfd.prc.pps.communication.dto.VehicleCodeDto;

import com.ca.mfd.prc.pps.communication.mapper.IMidVehicleCodeMapper;
import com.ca.mfd.prc.pps.communication.entity.MidVehicleCodeEntity;

import com.ca.mfd.prc.pps.communication.service.IMidVehicleCodeService;

import org.apache.commons.codec.digest.DigestUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 *
 * @Description: 车型代码中间表服务实现
 * @author inkelink
 * @date 2023年12月08日
 * @变更说明 BY inkelink At 2023年12月08日
 */
@Service
public class MidVehicleCodeServiceImpl extends MidBomBaseServiceImpl<IMidVehicleCodeMapper, MidVehicleCodeEntity, VehicleCodeDto> implements IMidVehicleCodeService {

    private static final Logger logger = LoggerFactory.getLogger(MidVehicleCodeServiceImpl.class);


    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_MID_VEHICLE_CODE";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<MidVehicleCodeEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(MidVehicleCodeEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(MidVehicleCodeEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<MidVehicleCodeEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<MidVehicleCodeEntity> getAllDatas() {
        Function<Object, ? extends List<MidVehicleCodeEntity>> getDataFunc = (obj) -> {
            List<MidVehicleCodeEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<MidVehicleCodeEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    @Override
    public void receive() {
        super.fetchDataFromApi("vehiclecode_receive","车型代码信息",ApiTypeConst.BOM_VEHICLE_CODE,true,true);
//        String apiUrl = sysConfigurationProvider.getConfiguration("vehiclecode_receive", "midapi");
//        if (StringUtils.isBlank(apiUrl)) {
//            throw new InkelinkException("没有配置上报的地址[vehiclecode_receive]");
//        }
//
//        String reqNo = UUIDUtils.getGuid();;
//        logger.info("车型代码数据信息[" + reqNo + "]开始接收数据");
//        MidApiLogEntity loginfo = new MidApiLogEntity();
//        loginfo.setApiType(ApiTypeConst.BOM_VEHICLE_CODE);
//        int status = 1;
//        String errMsg = "";
//        loginfo.setDataLineNo(0);
//        loginfo.setRequestStartTime(new Date());
//        List<VehicleCodeDto> infos = new ArrayList<>();
//        try {
//            loginfo.setStatus(0);
//            midApiLogBaseService.insert(loginfo);
//            midApiLogBaseService.saveChange();
//
//            // 构建API请求参数
//            Map param = new HashMap<>();
//            param.put("serviceId",reqNo);
//            // 发起HTTP请求
//            String responseData = apiPtService.postapi(apiUrl, param, null);
//
//            logger.warn("API平台测试url调用：" + responseData);
//            BomResultVo resultVO = JsonUtils.parseObject(responseData, BomResultVo.class);
//            infos = JsonUtils.parseArray(JsonUtils.toJsonString(resultVO.getData()), VehicleCodeDto.class);
//            String verifyData = md5(JsonUtils.toJsonString(infos));
//            QueryWrapper<MidApiLogEntity> queryWrapper=new QueryWrapper<>();
//            queryWrapper.lambda().eq(BaseEntity::getAttribute1,verifyData);
//            List<MidApiLogEntity> data = midApiLogBaseService.getData(queryWrapper, false);
//            if(CollectionUtils.isEmpty(data)){
//                this.insertBatch(infos.stream().map(c -> {
//                    MidVehicleCodeEntity entity = new MidVehicleCodeEntity();
//                    BeanUtils.copyProperties(c,entity);
//                    entity.setPrcMidApiLogId(loginfo.getId());
//                    entity.setId(IdGenerator.getId());
//                    entity.setExeStatus(0);
//                    entity.setExeTime(new Date());
//                    entity.setExeMsg(StringUtils.EMPTY);
//                    return entity;
//                }).collect(Collectors.toList()),200,false,1);
//                this.saveChange();
//                errMsg = "车型代码信息[" + reqNo + "]接收保存成功";
//                logger.info(errMsg);
//            }else {
//                errMsg = "车型代码信息[" + reqNo + "]处理成功，数据已存在";
//                logger.info(errMsg);
//            }
//            loginfo.setAttribute1(verifyData);
//
//        } catch (Exception ex) {
//            status = 5;
//            errMsg = "车型代码信息[" + reqNo + "]处理失败:";
//            logger.info(errMsg);
//            logger.error(errMsg, ex);
//        }
//        loginfo.setRequestStopTime(new Date());
//        loginfo.setStatus(status);
//        loginfo.setRemark(errMsg);
//        midApiLogBaseService.update(loginfo);
//        midApiLogBaseService.saveChange();
//        logger.info("车型代码信息[" + reqNo + "]执行完成:");
    }

    private String md5(String str) {
        //.replace("-", "")
        return DigestUtils.md5Hex(str.getBytes(StandardCharsets.UTF_8)).toLowerCase();
    }

    @Override
    protected MidVehicleCodeEntity getEntity(VehicleCodeDto vehicleCodeDto, Long loginfoId) {
        MidVehicleCodeEntity entity = new MidVehicleCodeEntity();
        BeanUtils.copyProperties(vehicleCodeDto,entity);
        entity.setPrcMidApiLogId(loginfoId);
        entity.setId(IdGenerator.getId());
        entity.setExeStatus(0);
        entity.setExeTime(new Date());
        entity.setExeMsg(StringUtils.EMPTY);
        return entity;
    }

    @Override
    protected List<VehicleCodeDto> fetchEntity(BomResultVo resultVO) {
        return JsonUtils.parseArray(JsonUtils.toJsonString(resultVO.getData()), VehicleCodeDto.class);
    }

    @Override
    protected Map<String, String> getParams(String... params) {
        return new HashMap<>(1);
    }
}