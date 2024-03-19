package com.ca.mfd.prc.pm.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.pm.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pm.communication.dto.BomResultVo;
import com.ca.mfd.prc.pm.communication.dto.MidCharacteristicsInfo;
import com.ca.mfd.prc.pm.communication.entity.MidCharacteristicsMasterEntity;
import com.ca.mfd.prc.pm.communication.mapper.IMidCharacteristicsMapper;
import com.ca.mfd.prc.pm.communication.service.IMidCharacteristicsMasterService;
import com.ca.mfd.prc.pm.communication.service.IMidCharacteristicsService;
import com.ca.mfd.prc.pm.entity.PmProductCharacteristicsEntity;
import com.ca.mfd.prc.pm.entity.PmProductCharacteristicsVersionsEntity;
import com.ca.mfd.prc.pm.service.IPmProductCharacteristicsService;
import com.ca.mfd.prc.pm.service.IPmProductCharacteristicsVersionsService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ca.mfd.prc.pm.communication.entity.MidCharacteristicsEntity;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @Description: 单车特征服务实现
 * @author inkelink
 * @date 2023年10月24日
 * @变更说明 BY inkelink At 2023年10月24日
 */
@Service
public class MidCharacteristicsServiceImpl extends MidBomBaseServiceImpl<IMidCharacteristicsMapper, MidCharacteristicsEntity,MidCharacteristicsInfo> implements IMidCharacteristicsService {
    private static final Logger logger = LoggerFactory.getLogger(MidCharacteristicsServiceImpl.class);
    @Autowired
    private IPmProductCharacteristicsVersionsService characteristicsVersionsService;
    @Autowired
    private IPmProductCharacteristicsService characteristicsService;
    @Autowired
    private IMidCharacteristicsMasterService characteristicsMasterService;
    @Override
    public String getCharacteristicsVersions(String materialNo) {
        //调用外部接口
        List<MidCharacteristicsInfo> infos = fetchDataFromApi(materialNo);
        if(CollectionUtils.isEmpty(infos)){
            throw new InkelinkException("接口返回数据为空");
        }
        //获取校验数据
        String verifyData = md5(JsonUtils.toJsonString(infos));
        //版本号
        String versions = materialNo + "_" + DateUtils.format(new Date(), "yyyyMMddHHmmssSSS");
        PmProductCharacteristicsVersionsEntity versionsInfo = getByProductMaterialNo(materialNo, verifyData);
        if (versionsInfo == null) {
            LambdaUpdateWrapper<PmProductCharacteristicsVersionsEntity> upset = new LambdaUpdateWrapper<>();
            upset.set(PmProductCharacteristicsVersionsEntity::getIsEnable, false);
            upset.eq(PmProductCharacteristicsVersionsEntity::getProductMaterialNo, materialNo);
            characteristicsVersionsService.update(upset);

            versionsInfo = new PmProductCharacteristicsVersionsEntity();
            versionsInfo.setId(IdGenerator.getId());
            versionsInfo.setProductMaterialNo(materialNo);
            versionsInfo.setVersions(versions);
            versionsInfo.setCheckCode(verifyData);
            versionsInfo.setIsEnable(true);
            characteristicsVersionsService.insert(versionsInfo);
            characteristicsVersionsService.saveChange();

            Long versionId = versionsInfo.getId();
            List<MidCharacteristicsMasterEntity> chars= characteristicsMasterService.getAllDatas();
            characteristicsService.insertBatch(infos.stream().map(c -> {
                MidCharacteristicsMasterEntity midCharacteristicsMasterEntity = chars.stream().filter(m -> m.getFamilyCode().equals(c.getFamilyCode()) && m.getFeatureCode().equals(c.getFeatureCode())).findFirst().orElse(null);

                PmProductCharacteristicsEntity et = new PmProductCharacteristicsEntity();
                et.setProductCharacteristicsName(c.getFamilyCode());
                et.setProductCharacteristicsValue(c.getFeatureCode());
                et.setCharacteristicsVersionsId(versionId);
                if(midCharacteristicsMasterEntity!=null){
                    et.setDescriptionCn(midCharacteristicsMasterEntity.getFamilyName()==null?"":midCharacteristicsMasterEntity.getFamilyName());
                    et.setDescriptionEn(midCharacteristicsMasterEntity.getFamilyNameEn()==null?"":midCharacteristicsMasterEntity.getFamilyNameEn());
                    et.setValueCn(midCharacteristicsMasterEntity.getFeatureName()==null?"":midCharacteristicsMasterEntity.getFeatureName());
                    et.setValueEn(midCharacteristicsMasterEntity.getFamilyNameEn()==null?"":midCharacteristicsMasterEntity.getFamilyNameEn());
                }
                return et;
            }).collect(Collectors.toList()),200,false,1);
            characteristicsService.saveChange();


        }else {
            versions=versionsInfo.getVersions();
        }
        return versions;
    }

    private List<MidCharacteristicsInfo> fetchDataFromApi(String materialNo) {
        return super.fetchDataFromApi("characteristics_receive","特征数据信息",ApiTypeConst.BOM_CHARACTERISTICS,true,materialNo);

//        String apiUrl = sysConfigurationProvider.getConfiguration("characteristics_receive", "midapi");
//        if (StringUtils.isBlank(apiUrl)) {
//            throw new InkelinkException("没有配置上报的地址[characteristics_receive]");
//        }
//
//        String reqNo = UUIDUtils.getGuid();
//        logger.info("特征数据信息[" + reqNo + "]开始接收数据");
//        MidApiLogEntity loginfo = new MidApiLogEntity();
//        loginfo.setApiType(ApiTypeConst.BOM_CHARACTERISTICS);
//        int status = 1;
//        String errMsg = "";
//        loginfo.setDataLineNo(0);
//        loginfo.setRequestStartTime(new Date());
//        List<MidCharacteristicsInfo> infos = new ArrayList<>();
//        try {
//            loginfo.setStatus(0);
//            midApiLogService.insert(loginfo);
//            midApiLogService.saveChange();
//
//            // 构建API请求参数
//            Map param =new HashMap<>();
//            param.put("serviceId",reqNo);
//            param.put("vehicleMaterialNumber",materialNo);
//            // 发起HTTP请求
//            String responseData = apiPtService.postapi(apiUrl, param, null);
//
//            logger.warn("API平台测试url调用：" + responseData);
//            BomResultVo resultVO = JsonUtils.parseObject(responseData, BomResultVo.class);
//            infos = JsonUtils.parseArray(JsonUtils.toJsonString(resultVO.getData()), MidCharacteristicsInfo.class);
//
//            String verifyData = md5(JsonUtils.toJsonString(infos));
//            QueryWrapper<MidApiLogEntity> queryWrapper=new QueryWrapper<>();
//            queryWrapper.lambda().eq(BaseEntity::getAttribute1,verifyData);
//            List<MidApiLogEntity> data = midApiLogService.getData(queryWrapper, false);
//            if(CollectionUtils.isEmpty(data)){
//                this.insertBatch(infos.stream().map(c -> {
//                    MidCharacteristicsEntity entity = new MidCharacteristicsEntity();
//                    BeanUtils.copyProperties(c,entity);
//                    entity.setPrcMidApiLogId(loginfo.getId());
//                    entity.setId(IdGenerator.getId());
//                    entity.setExeStatus(0);
//                    entity.setExeTime(new Date());
//                    entity.setExeMsg(StringUtils.EMPTY);
//                    return entity;
//                }).collect(Collectors.toList()),200,false,1);
//                this.saveChange();
//                errMsg = "特征数据信息[" + reqNo + "]接收保存成功";
//                logger.info(errMsg);
//            }else {
//                errMsg = "特征数据信息[" + reqNo + "]处理成功，数据已存在";
//                logger.info(errMsg);
//            }
//            loginfo.setAttribute1(verifyData);
//
//            status = 1;
//            errMsg = "特征数据信息[" + reqNo + "]处理失败:";
//            logger.info(errMsg);
//        } catch (Exception ex) {
//            status = 5;
//            errMsg = "特征数据信息[" + reqNo + "]处理失败:";
//            logger.info(errMsg);
//            logger.error(errMsg, ex);
//        }
//        loginfo.setRequestStopTime(new Date());
//        loginfo.setStatus(status);
//        loginfo.setRemark(errMsg);
//        midApiLogService.update(loginfo);
//        midApiLogService.saveChange();
//        logger.info("特征数据信息[" + reqNo + "]执行完成:");
//        return infos;
    }

    private String md5(String str) {
        //.replace("-", "")
        return DigestUtils.md5Hex(str.getBytes(StandardCharsets.UTF_8)).toLowerCase();
    }

    @Override
    protected MidCharacteristicsEntity getEntity(MidCharacteristicsInfo midCharacteristicsInfo, Long loginfoId) {
        MidCharacteristicsEntity entity = new MidCharacteristicsEntity();
        BeanUtils.copyProperties(midCharacteristicsInfo,entity);
        entity.setPrcMidApiLogId(loginfoId);
        entity.setId(IdGenerator.getId());
        entity.setExeStatus(0);
        entity.setExeTime(new Date());
        entity.setExeMsg(StringUtils.EMPTY);
        return entity;
    }

    @Override
    protected List<MidCharacteristicsInfo> fetchEntity(BomResultVo resultVO) {
        return  JsonUtils.parseArray(JsonUtils.toJsonString(resultVO.getData()), MidCharacteristicsInfo.class);
    }

    @Override
    protected Map<String, String> getParams(String... params) {
        Map<String,String> param =new HashMap<>(2);
        param.put("vehicleMaterialNumber",params[0]);
        return param;
    }

    private PmProductCharacteristicsVersionsEntity getByProductMaterialNo(String productMaterialNo, String checkCode) {
        QueryWrapper<PmProductCharacteristicsVersionsEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PmProductCharacteristicsVersionsEntity::getProductMaterialNo, productMaterialNo)
                .eq(PmProductCharacteristicsVersionsEntity::getCheckCode, checkCode);

        return characteristicsVersionsService.getTopDatas(1, qry).stream().findFirst().orElse(null);
    }
}