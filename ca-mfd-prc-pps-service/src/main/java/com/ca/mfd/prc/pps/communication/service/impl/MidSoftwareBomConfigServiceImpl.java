package com.ca.mfd.prc.pps.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.pps.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pps.communication.dto.BomConfigDto;
import com.ca.mfd.prc.pps.communication.dto.BomResultVo;
import com.ca.mfd.prc.pps.communication.entity.MidSoftwareBomConfigEntity;
import com.ca.mfd.prc.pps.communication.entity.MidSoftwareConfigDetailEntity;
import com.ca.mfd.prc.pps.communication.entity.MidSoftwareConfigVersionEntity;
import com.ca.mfd.prc.pps.communication.mapper.IMidSoftwareBomConfigMapper;
import com.ca.mfd.prc.pps.communication.service.IMidSoftwareBomConfigService;
import com.ca.mfd.prc.pps.communication.service.IMidSoftwareConfigDetailService;
import com.ca.mfd.prc.pps.communication.service.IMidSoftwareConfigVersionService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @Description: 单车配置字服务实现
 * @author inkelink
 * @date 2023年11月24日
 * @变更说明 BY inkelink At 2023年11月24日
 */
@Service
public class MidSoftwareBomConfigServiceImpl extends MidBomBaseServiceImpl<IMidSoftwareBomConfigMapper, MidSoftwareBomConfigEntity, BomConfigDto> implements IMidSoftwareBomConfigService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_MID_SOFTWARE_BOM_CONFIG";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<MidSoftwareBomConfigEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(MidSoftwareBomConfigEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(MidSoftwareBomConfigEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<MidSoftwareBomConfigEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<MidSoftwareBomConfigEntity> getAllDatas() {
        Function<Object, ? extends List<MidSoftwareBomConfigEntity>> getDataFunc = (obj) -> {
            List<MidSoftwareBomConfigEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<MidSoftwareBomConfigEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    @Autowired
    private IMidSoftwareConfigDetailService softwareConfigDetailService;
    @Autowired
    private IMidSoftwareConfigVersionService softwareConfigVersionService;

    @Override
    public List<BomConfigDto> getBomConfig(String materialNo,String effectiveDate) {
        //调用外部接口
        Map<Long,List<BomConfigDto>> tempMap = fetchDataFromApi(materialNo,effectiveDate);
        if(tempMap==null){
            throw new InkelinkException("接口调用失败");
        }
        Long logId = tempMap.keySet().stream().findFirst().orElse(null);
        List<BomConfigDto> infos = tempMap.values().stream().findFirst().orElse(null);
        //获取校验数据
        String verifyData = md5(JsonUtils.toJsonString(infos));
        //版本号
        String versions = materialNo + "_" + DateUtils.format(new Date(), "yyyyMMddHHmmssSSS");
        MidSoftwareConfigVersionEntity versionEntity = getByMaterialNo(materialNo, verifyData);
        if (versionEntity == null) {
            versionEntity = new MidSoftwareConfigVersionEntity();
            versionEntity.setPrcMidApiLogId(logId);
            versionEntity.setId(IdGenerator.getId());
            versionEntity.setVehicleMaterialNumber(materialNo);
            versionEntity.setChangeCode(infos.get(0).getChangeCode());
            versionEntity.setCheckCode(verifyData);
            versionEntity.setIsEnable(true);
            softwareConfigVersionService.insert(versionEntity);
            softwareConfigVersionService.saveChange();
            Long bomVersionId = versionEntity.getId();
            this.insertBatch(infos.get(0).getSoftwareConfigList().stream().map(c -> {
                MidSoftwareBomConfigEntity entity = new MidSoftwareBomConfigEntity();
                BeanUtils.copyProperties(c,entity);
                entity.setPrcMidSoftwareConfigVersionId(bomVersionId);
                entity.setIsEnable(true);
                Long id=IdGenerator.getId();
                entity.setId(id);
                softwareConfigDetailService.insertBatch(c.getEcuConfigList().stream().map(x->{
                    MidSoftwareConfigDetailEntity configEntity=new MidSoftwareConfigDetailEntity();
                    configEntity.setDid(x.getDid());
                    configEntity.setDescription(x.getDescription());
                    configEntity.setAnalysisValue(x.getAnalysisValue());
                    configEntity.setPrcMidSoftwareBomConfigId(id);
                    return configEntity;
                }).collect(Collectors.toList()));
                return entity;
            }).collect(Collectors.toList()));
            this.saveChange();
            softwareConfigDetailService.saveChange();

            LambdaUpdateWrapper<MidSoftwareConfigVersionEntity> upset = new LambdaUpdateWrapper<>();
            upset.set(MidSoftwareConfigVersionEntity::getIsEnable, false);
            upset.eq(MidSoftwareConfigVersionEntity::getVehicleMaterialNumber, materialNo);
            upset.ne(MidSoftwareConfigVersionEntity::getId,bomVersionId);
            softwareConfigVersionService.update(upset);
            softwareConfigVersionService.saveChange();
        }
        return infos;
    }


    private Map<Long,List<BomConfigDto>>fetchDataFromApi(String materialNo,String effectiveDate) {
        return super.fetchDataFromSoftApi("softwarebomconfig_receive","配置字信息",ApiTypeConst.BOM_SOFTWARE_CONFIG,false,false,materialNo, effectiveDate);

//        String apiUrl = sysConfigurationProvider.getConfiguration("softwarebomconfig_receive", "midapi");
//        if (StringUtils.isBlank(apiUrl)) {
//            throw new InkelinkException("没有配置上报的地址[softwarebomconfig_receive]");
//        }
//
//        String reqNo = UUIDUtils.getGuid();
//        logger.info("配置字信息[" + reqNo + "]开始接收数据");
//        MidApiLogEntity loginfo = new MidApiLogEntity();
//        loginfo.setApiType(ApiTypeConst.BOM_SOFTWARE_CONFIG);
//        int status = 1;
//        String errMsg = "";
//        loginfo.setDataLineNo(0);
//        loginfo.setRequestStartTime(new Date());
//        List<BomConfigDto> infos = new ArrayList<>();
//        try {
//            loginfo.setStatus(0);
//            midApiLogService.insert(loginfo);
//            midApiLogService.saveChange();
//
//            // 构建API请求参数
//            Map param =new HashMap<>();
//            param.put("vehicleMaterialNumber",materialNo);
//            param.put("serviceId",reqNo);
//            param.put("effectiveDate",effectiveDate);
//            // 发起HTTP请求
//            String responseData = apiPtService.postapi(apiUrl, param, null);
//
//            logger.warn("API平台测试url调用：" + responseData);
//            ResultVO resultVO = JsonUtils.parseObject(responseData, ResultVO.class);
//            infos = JsonUtils.parseArray(JsonUtils.toJsonString(resultVO.getData()), BomConfigDto.class);
//            status = 1;
//            errMsg = "配置字信息[" + reqNo + "]处理成功:";
//            logger.info(errMsg);
//        } catch (Exception ex) {
//            status = 5;
//            errMsg = "配置字信息[" + reqNo + "]处理失败:" + ex.getMessage();
//            logger.info(errMsg);
//            logger.error(errMsg, ex);
//        }
//        loginfo.setRequestStopTime(new Date());
//        loginfo.setStatus(status);
//        loginfo.setRemark(errMsg);
//        midApiLogService.update(loginfo);
//        midApiLogService.saveChange();
//        logger.info("配置字信息[" + reqNo + "]执行完成:");
//        return infos;
    }

    private String md5(String str) {
        //.replace("-", "")
        return DigestUtils.md5Hex(str.getBytes(StandardCharsets.UTF_8)).toLowerCase();
    }

    @Override
    protected MidSoftwareBomConfigEntity getEntity(BomConfigDto bomConfigDto, Long loginfoId) {
        //本方法不会被执行无需处理
        return null;
    }

    @Override
    protected List<BomConfigDto> fetchEntity(BomResultVo resultVO) {
        return Arrays.asList(JsonUtils.parseObject(JsonUtils.toJsonString(resultVO.getData()), BomConfigDto.class));
    }

    @Override
    protected Map<String, String> getParams(String... params) {
        Map<String,String> param =new HashMap<>(3);
        param.put("vehicleMaterialNumber",params[0]);
        param.put("effectiveDate",params[1]);
        return param;
    }

    private MidSoftwareConfigVersionEntity getByMaterialNo(String productMaterialNo, String checkCode) {
        QueryWrapper<MidSoftwareConfigVersionEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(MidSoftwareConfigVersionEntity::getVehicleMaterialNumber, productMaterialNo).eq(MidSoftwareConfigVersionEntity::getCheckCode, checkCode);
        return softwareConfigVersionService.getTopDatas(1, qry).stream().findFirst().orElse(null);
    }


}