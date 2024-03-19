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
import com.ca.mfd.prc.pps.communication.dto.BomResultVo;
import com.ca.mfd.prc.pps.communication.dto.SoftwareBomListDto;

import com.ca.mfd.prc.pps.communication.entity.MidSoftwareBomListEntity;
import com.ca.mfd.prc.pps.communication.entity.MidSoftwareBomVersionEntity;
import com.ca.mfd.prc.pps.communication.mapper.IMidSoftwareBomListMapper;

import com.ca.mfd.prc.pps.communication.service.IMidSoftwareBomListService;
import com.ca.mfd.prc.pps.communication.service.IMidSoftwareBomVersionService;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;

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
import java.util.stream.Collectors;

/**
 *
 * @Description: 单车软件清单服务实现
 * @author inkelink
 * @date 2023年11月23日
 * @变更说明 BY inkelink At 2023年11月23日
 */
@Service
public class MidSoftwareBomListServiceImpl extends MidBomBaseServiceImpl<IMidSoftwareBomListMapper, MidSoftwareBomListEntity, SoftwareBomListDto> implements IMidSoftwareBomListService {

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_MID_SOFTWARE_BOM_LIST";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<MidSoftwareBomListEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(MidSoftwareBomListEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(MidSoftwareBomListEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<MidSoftwareBomListEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<MidSoftwareBomListEntity> getAllDatas() {
        Function<Object, ? extends List<MidSoftwareBomListEntity>> getDataFunc = (obj) -> {
            List<MidSoftwareBomListEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<MidSoftwareBomListEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    @Autowired
    private IMidSoftwareBomVersionService softwareBomVersionService;

    @Override
    public List<SoftwareBomListDto> getSoftBom(String materialNo,String effectiveDate) {
        //调用外部接口
        Map<Long,List<SoftwareBomListDto>>  tempInfos = fetchDataFromApi(materialNo,effectiveDate);
        if(tempInfos==null){
            throw new InkelinkException("接口调用失败");
        }
        Long logId = tempInfos.keySet().stream().findFirst().orElse(null);
        List<SoftwareBomListDto> infos = tempInfos.values().stream().findFirst().orElse(null);
        //获取校验数据
        String verifyData = md5(JsonUtils.toJsonString(infos));
        //版本号
        String versions = materialNo + "_" + DateUtils.format(new Date(), "yyyyMMddHHmmssSSS");
        MidSoftwareBomVersionEntity versionEntity = getByMaterialNo(materialNo, verifyData);
        if (versionEntity == null) {
            versionEntity = new MidSoftwareBomVersionEntity();
            versionEntity.setPrcMidApiLogId(logId);
            versionEntity.setId(IdGenerator.getId());
            versionEntity.setVehicleMaterialNumber(materialNo);
            versionEntity.setPublishChangeCode(versions);
            versionEntity.setCheckCode(verifyData);
            versionEntity.setIsEnable(true);
            softwareBomVersionService.insert(versionEntity);
            softwareBomVersionService.saveChange();
            Long bomVersionId = versionEntity.getId();
            this.insertBatch(infos.stream().map(c -> {
                MidSoftwareBomListEntity entity = new MidSoftwareBomListEntity();
                BeanUtils.copyProperties(c,entity);
                entity.setId(IdGenerator.getId());
                entity.setPrcMidSoftwareBomVersionId(bomVersionId);
                return entity;
            }).collect(Collectors.toList()));
            this.saveChange();

            LambdaUpdateWrapper<MidSoftwareBomVersionEntity> upset = new LambdaUpdateWrapper<>();
            upset.set(MidSoftwareBomVersionEntity::getIsEnable, false);
            upset.eq(MidSoftwareBomVersionEntity::getVehicleMaterialNumber, materialNo);
            upset.ne(MidSoftwareBomVersionEntity::getId,bomVersionId);
            softwareBomVersionService.update(upset);
            softwareBomVersionService.saveChange();
        }
        return infos;
    }

    private Map<Long,List<SoftwareBomListDto>> fetchDataFromApi(String materialNo,String effectiveDate) {
        return super.fetchDataFromSoftApi("softwarebomlist_receive","软件BOM清单信息",ApiTypeConst.BOM_SOFTWARE_LIST,false,false,materialNo, effectiveDate);
//        String apiUrl = sysConfigurationProvider.getConfiguration("softwarebomlist_receive", "midapi");
//        if (StringUtils.isBlank(apiUrl)) {
//            throw new InkelinkException("没有配置上报的地址[softwarebomlist_receive]");
//        }
//
//        String reqNo = UUIDUtils.getGuid();
//        logger.info("软件BOM清单信息[" + reqNo + "]开始接收数据");
//        MidApiLogEntity loginfo = new MidApiLogEntity();
//        loginfo.setApiType(ApiTypeConst.BOM_SOFTWARE_LIST);
//        int status = 1;
//        String errMsg = "";
//        loginfo.setDataLineNo(0);
//        loginfo.setRequestStartTime(new Date());
//        List<SoftwareBomListDto> infos = new ArrayList<>();
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
//            infos = JsonUtils.parseArray(JsonUtils.toJsonString(resultVO.getData()), SoftwareBomListDto.class);
//            status = 1;
//            errMsg = "软件BOM清单信息[" + reqNo + "]处理失败:";
//            logger.info(errMsg);
//        } catch (Exception ex) {
//            status = 5;
//            errMsg = "软件BOM清单信息[" + reqNo + "]处理失败:"+ex.getMessage();
//            logger.info(errMsg);
//            logger.error(errMsg, ex);
//        }
//        loginfo.setRequestStopTime(new Date());
//        loginfo.setStatus(status);
//        loginfo.setRemark(errMsg);
//        midApiLogService.update(loginfo);
//        midApiLogService.saveChange();
//        logger.info("软件BOM清单信息[" + reqNo + "]执行完成:");
//        return infos;
    }

    private String md5(String str) {
        //.replace("-", "")
        return DigestUtils.md5Hex(str.getBytes(StandardCharsets.UTF_8)).toLowerCase();
    }

    @Override
    protected MidSoftwareBomListEntity getEntity(SoftwareBomListDto softwareBomListDto, Long loginfoId) {
        return null;
    }

    @Override
    protected List<SoftwareBomListDto> fetchEntity(BomResultVo resultVO) {
        return JsonUtils.parseArray(JsonUtils.toJsonString(resultVO.getData()), SoftwareBomListDto.class);
    }

    @Override
    protected Map<String, String> getParams(String... params) {
        Map<String,String> param =new HashMap<>(3);
        param.put("vehicleMaterialNumber",params[0]);
        param.put("effectiveDate",params[1]);
        return param;
    }

    private MidSoftwareBomVersionEntity getByMaterialNo(String productMaterialNo, String checkCode) {
        QueryWrapper<MidSoftwareBomVersionEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(MidSoftwareBomVersionEntity::getVehicleMaterialNumber, productMaterialNo).eq(MidSoftwareBomVersionEntity::getCheckCode, checkCode);
        return softwareBomVersionService.getTopDatas(1, qry).stream().findFirst().orElse(null);
    }
}