package com.ca.mfd.prc.pm.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.pm.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pm.communication.dto.BomResultVo;
import com.ca.mfd.prc.pm.communication.dto.MidBomInfo;
import com.ca.mfd.prc.pm.communication.dto.MidBomPartInfo;
import com.ca.mfd.prc.pm.communication.entity.MidMaterialEntity;
import com.ca.mfd.prc.pm.communication.entity.MidMaterialMasterEntity;
import com.ca.mfd.prc.pm.communication.mapper.IMidMaterialMapper;
import com.ca.mfd.prc.pm.communication.service.IMidMaterialMasterService;
import com.ca.mfd.prc.pm.communication.service.IMidMaterialService;
import com.ca.mfd.prc.pm.entity.PmProductBomEntity;
import com.ca.mfd.prc.pm.entity.PmProductBomVersionsEntity;
import com.ca.mfd.prc.pm.service.IPmProductBomService;
import com.ca.mfd.prc.pm.service.IPmProductBomVersionsService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @Description: 单车BOM服务实现
 * @author inkelink
 * @date 2023年10月24日
 * @变更说明 BY inkelink At 2023年10月24日
 */
@Service
public class MidMaterialServiceImpl extends MidBomBaseServiceImpl<IMidMaterialMapper, MidMaterialEntity,MidBomInfo> implements IMidMaterialService {
    private static final Logger logger = LoggerFactory.getLogger(MidMaterialServiceImpl.class);
    @Autowired
    private IPmProductBomVersionsService bomVersionsService;
    @Autowired
    private IPmProductBomService bomService;
    @Autowired
    private IMidMaterialMasterService materialMasterService;

    @Autowired
    @Qualifier("pmThreadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public String getBomVersions(String plantCode,String materialNo,String specifyDate) {
        //调用外部接口
        List<MidBomInfo> infos = fetchDataFromApi(plantCode,materialNo,specifyDate);
        if(CollectionUtils.isEmpty(infos)){
            throw new InkelinkException("从BOM获取物料号："+materialNo+"；对应SINGLEBOM无数据");
        }
        //获取校验数据
        List<MidBomInfo> tmp = new ArrayList<>();
        for(MidBomInfo et:infos){
            MidBomInfo item = new MidBomInfo();
            BeanUtils.copyProperties(et,item);
            //item.setEffectiveFrom(null);
            //item.setEffectiveTo(null);
            tmp.add(item);
        }
        String verifyData = md5(JsonUtils.toJsonString(tmp));
        //版本号
        String versions = materialNo + "_" + DateUtils.format(new Date(), "yyyyMMddHHmmssSSS");
        PmProductBomVersionsEntity bomVersionsInfo = getByProductMaterialNo(materialNo, verifyData);
        if (bomVersionsInfo == null) {
            List<MidMaterialMasterEntity> midmaters = materialMasterService.getAllDatas();
            bomVersionsInfo = new PmProductBomVersionsEntity();
            bomVersionsInfo.setId(IdGenerator.getId());
            bomVersionsInfo.setProductMaterialNo(materialNo);
            bomVersionsInfo.setBomVersions(versions);
            bomVersionsInfo.setCheckCode(verifyData);
            bomVersionsInfo.setIsEnable(true);
            bomVersionsInfo.setAttribute1("1");

            LambdaUpdateWrapper<PmProductBomVersionsEntity> upset = new LambdaUpdateWrapper<>();
            upset.set(PmProductBomVersionsEntity::getIsEnable, false);
            upset.eq(PmProductBomVersionsEntity::getProductMaterialNo, materialNo);
            bomVersionsService.update(upset);

            bomVersionsService.insert(bomVersionsInfo);
            bomVersionsService.saveChange();
            Long bomVersionId = bomVersionsInfo.getId();
            String finalVersions = versions;
            threadPoolTaskExecutor.submit(() -> {
                batchAddBom(infos,midmaters, bomVersionId,materialNo, finalVersions);
            });
        }else {
            versions=bomVersionsInfo.getBomVersions();
        }
        return versions;
    }

    @Override
    public List<MidBomInfo> getSingleBom(String plantCode, String materialNo, String specifyDate) {
        return fetchDataFromApi(plantCode,materialNo,specifyDate);
    }


    private void batchAddBom(List<MidBomInfo> infos
            ,List<MidMaterialMasterEntity> midmaters
            ,Long bomVersionId
            ,String materialNo
            ,String versions){
        bomService.insertBatch(infos.stream().map(c -> {
            PmProductBomEntity et = new PmProductBomEntity();
            MidMaterialMasterEntity midMaterialMasterEntity = midmaters.stream().filter(m -> m.getMaterialCode().equals(c.getMaterialCode())).findFirst().orElse(null);
            et.setBomVersionsId(bomVersionId);
            et.setMaterialNo(c.getMaterialCode());
            if(midMaterialMasterEntity!=null){
                et.setMaterialCn(midMaterialMasterEntity.getMaterialName()==null?"":midMaterialMasterEntity.getMaterialName());
                et.setMaterialEn(midMaterialMasterEntity.getMaterialNameEn()==null?"":midMaterialMasterEntity.getMaterialNameEn());
            }
            et.setQuantity(new BigDecimal(c.getQuantity()));
            et.setEffectiveDateStart(c.getEffectiveFrom());
            et.setEffectiveDateEnd(c.getEffectiveTo());
            et.setAttribute1(c.getLineNumber());
            et.setAttribute2(c.getUseProcessType());
            et.setAttribute3(c.getUseProcessTypeCode());
            et.setAttribute4(c.getEcuTypeCode());
            return et;
        }).collect(Collectors.toList()),300,false,1);
        bomService.saveChange();
        //保存组件
        bomVersionsService.saveBomToCom(materialNo, versions);
        bomVersionsService.saveChange();
    }



    private List<MidBomInfo> fetchDataFromApi(String plantCode,String materialNo,String specifyDate) {
        return super.fetchDataFromApi("material_receive","物料数据信息", ApiTypeConst.BOM_MATERIAL,true, plantCode, materialNo, specifyDate);
//        String apiUrl = sysConfigurationProvider.getConfiguration("material_receive", "midapi");
//        if (StringUtils.isBlank(apiUrl)) {
//            throw new InkelinkException("没有配置上报的地址[material_receive]");
//        }
//        String reqNo = UUIDUtils.getGuid();
//        logger.info("物料数据信息[" + reqNo + "]开始接收数据");
//        MidApiLogEntity loginfo = new MidApiLogEntity();
//        loginfo.setApiType(ApiTypeConst.BOM_MATERIAL);
//        int status = 0;
//        String errMsg = "";
//        loginfo.setDataLineNo(0);
//        loginfo.setRequestStartTime(new Date());
//        List<MidBomInfo> infos = new ArrayList<>();
//        try {
//            loginfo.setStatus(status);
//            midApiLogService.insert(loginfo);
//            midApiLogService.saveChange();
//
//            // 构建API请求参数
//            Map param =new HashMap<>(4);
//            param.put("serviceId",reqNo);
//            param.put("vehicleMaterialNumber",materialNo);
//            param.put("specifyDate",specifyDate);
//            param.put("plantCode",plantCode);
//            // 发起HTTP请求
//            String responseData = apiPtService.postapi(apiUrl, param, null);
//            logger.warn("API平台测试url调用：" + responseData);
//            BomResultVo resultVO = JsonUtils.parseObject(responseData, BomResultVo.class);
//            if(resultVO != null && resultVO.getCode() == 1){
//                infos = JsonUtils.parseArray(JsonUtils.toJsonString(resultVO.getData()), MidBomInfo.class);
//                if(infos.isEmpty()){
//                    status = 5;
//                    errMsg = "物料数据信息[" + reqNo + "]获取成功,BOM系统返回空数据";
//                    logger.info(errMsg);
//                }else{
//                    String verifyData = md5(JsonUtils.toJsonString(infos));
//                    QueryWrapper<MidApiLogEntity> queryWrapper=new QueryWrapper<>();
//                    queryWrapper.lambda().eq(BaseEntity::getAttribute1,verifyData);
//                    List<MidApiLogEntity> data = midApiLogService.getData(queryWrapper, false);
//                    if(CollectionUtils.isEmpty(data)){
//                        this.insertBatch(infos.stream().map(c -> {
//                            MidMaterialEntity entity = new MidMaterialEntity();
//                            BeanUtils.copyProperties(c,entity);
//                            entity.setPrcMidApiLogId(loginfo.getId());
//                            entity.setId(IdGenerator.getId());
//                            entity.setExeStatus(0);
//                            entity.setExeTime(new Date());
//                            entity.setExeMsg(StringUtils.EMPTY);
//                            return entity;
//                        }).collect(Collectors.toList()),200,false,1);
//                        this.saveChange();
//                        errMsg = "物料数据信息[" + reqNo + "]接收保存成功";
//                        logger.info(errMsg);
//                        loginfo.setAttribute1(verifyData);
//                        status = 1;
//                    }else {
//                        status = 5;
//                        errMsg = "物料数据信息[" + reqNo + "]处理成功，数据已存在";
//                        logger.info(errMsg);
//                    }
//                }
//
//            }else{
//                status = 5;
//                errMsg = "物料数据信息[" + reqNo + "]获取失败,BOM系统返回:"+ resultVO.getMsg();
//                logger.info(errMsg);
//            }
//        } catch (Exception ex) {
//            status = 5;
//            errMsg = "物料数据信息[" + reqNo + "]处理失败,失败原因:"+ ex.getMessage();
//            logger.info(errMsg);
//            logger.error(errMsg, ex);
//        }
//        loginfo.setRequestStopTime(new Date());
//        loginfo.setStatus(status);
//        loginfo.setRemark(errMsg);
//        midApiLogService.update(loginfo);
//        midApiLogService.saveChange();
//        logger.info("物料数据信息[" + reqNo + "]执行完成:");
//        if(status == 5){
//            throw new InkelinkException(errMsg);
//        }
//        return infos;
    }

    private String md5(String str) {
        //.replace("-", "")
        return DigestUtils.md5Hex(str.getBytes(StandardCharsets.UTF_8)).toLowerCase();
    }

    @Override
    protected MidMaterialEntity getEntity(MidBomInfo midBomInfo, Long loginfoId) {
        MidMaterialEntity entity = new MidMaterialEntity();
        BeanUtils.copyProperties(midBomInfo,entity);
        entity.setPrcMidApiLogId(loginfoId);
        entity.setId(IdGenerator.getId());
        entity.setExeStatus(0);
        entity.setExeTime(new Date());
        entity.setExeMsg(StringUtils.EMPTY);
        return entity;
    }

    @Override
    protected List<MidBomInfo> fetchEntity(BomResultVo resultVO) {
        return JsonUtils.parseArray(JsonUtils.toJsonString(resultVO.getData()), MidBomInfo.class);
    }

    @Override
    protected Map<String, String> getParams(String... params) {
        Map<String,String> param =new HashMap<>(4);
        param.put("vehicleMaterialNumber",params[1]);
        param.put("specifyDate",params[2]);
        param.put("plantCode",params[0]);
        return param;
    }

    private PmProductBomVersionsEntity getByProductMaterialNo(String productMaterialNo, String checkCode) {
        QueryWrapper<PmProductBomVersionsEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PmProductBomVersionsEntity::getProductMaterialNo, productMaterialNo).eq(PmProductBomVersionsEntity::getCheckCode, checkCode);
        return bomVersionsService.getTopDatas(1, qry).stream().findFirst().orElse(null);
    }
}