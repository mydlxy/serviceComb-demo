package com.ca.mfd.prc.pps.communication.service.impl;

import com.ca.mfd.prc.common.exception.InkelinkException;

import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.pps.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pps.communication.dto.BomMaterialUseDto;
import com.ca.mfd.prc.pps.communication.dto.BomResultVo;

import com.ca.mfd.prc.pps.communication.entity.MidMaterialUseEntity;
import com.ca.mfd.prc.pps.communication.mapper.IMidMaterialUseMapper;

import com.ca.mfd.prc.pps.communication.service.IMidBomMaterialUseService;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

import java.util.*;

/**
 * @author inkelink
 * @Description: BOM整车车型服务实现
 * @date 2023年10月11日
 * @变更说明 BY inkelink At 2023年10月11日
 */
@Service
public class MidBomMaterialUseServiceImpl  extends MidBomBaseServiceImpl<IMidMaterialUseMapper, MidMaterialUseEntity, BomMaterialUseDto> implements IMidBomMaterialUseService {

    private static final Logger logger = LoggerFactory.getLogger(MidBomMaterialUseServiceImpl.class);


    @Override
    public List<BomMaterialUseDto> getBomMaterialUseData(String materialNo,String plantCode,String specifyDate) {
        //调用外部接口
        List<BomMaterialUseDto> infos = fetchDataFromApi(materialNo,plantCode,specifyDate);
        if (CollectionUtils.isEmpty(infos)) {
            throw new InkelinkException("接口调用失败");
        }
        return infos;
    }

    private List<BomMaterialUseDto> fetchDataFromApi(String materialNo,String plantCode,String specifyDate) {
        return super.fetchDataFromApi("bommaterialuse_receive","物料使用信息",ApiTypeConst.BOM_MATERIAL_USE,true,materialNo, plantCode, specifyDate);

//        String apiUrl = sysConfigurationProvider.getConfiguration("bommaterialuse_receive", "midapi");
//        if (StringUtils.isBlank(apiUrl)) {
//            throw new InkelinkException("没有配置上报的地址[bommaterialuse_receive]");
//        }
//
//        String reqNo = UUIDUtils.getGuid();
//        logger.info("物料使用信息[" + reqNo + "]开始接收数据");
//        MidApiLogEntity loginfo = new MidApiLogEntity();
//        loginfo.setApiType(ApiTypeConst.BOM_MATERIAL_USE);
//        int status = 1;
//        String errMsg = "";
//        loginfo.setDataLineNo(0);
//        loginfo.setRequestStartTime(new Date());
//        List<BomMaterialUseDto> infos = new ArrayList<>();
//        try {
//            loginfo.setStatus(0);
//            midApiLogBaseService.insert(loginfo);
//            midApiLogBaseService.saveChange();
//
//            // 构建API请求参数
//            Map param = new HashMap<>();
//            param.put("serviceId", reqNo);
//            param.put("bomType", "MBOM");
//            param.put("materialCode", materialNo);
//            param.put("plantCode", plantCode);
//            param.put("specifyDate", specifyDate);
//            // 发起HTTP请求
//            String responseData = apiPtService.postapi(apiUrl, param, null);
//
//            logger.warn("API平台测试url调用：" + responseData);
//            BomResultVo resultVO = JsonUtils.parseObject(responseData, BomResultVo.class);
//            infos = JsonUtils.parseArray(JsonUtils.toJsonString(resultVO.getData()), BomMaterialUseDto.class);
//            String verifyData = md5(JsonUtils.toJsonString(infos));
//            QueryWrapper<MidApiLogEntity> queryWrapper=new QueryWrapper<>();
//            queryWrapper.lambda().eq(BaseEntity::getAttribute1,verifyData);
//            List<MidApiLogEntity> data = midApiLogBaseService.getData(queryWrapper, false);
//            if(CollectionUtils.isEmpty(data)){
//                this.insertBatch(infos.stream().map(c -> {
//                    MidMaterialUseEntity entity = new MidMaterialUseEntity();
//                    BeanUtils.copyProperties(c,entity);
//                    String key = String.join(",", c.getVehicleMaterialNumbers());
//                    entity.setVehicleMaterialNumbers(key);
//                    entity.setPrcMidApiLogId(loginfo.getId());
//                    entity.setId(IdGenerator.getId());
//                    entity.setExeStatus(0);
//                    entity.setExeTime(new Date());
//                    entity.setExeMsg(StringUtils.EMPTY);
//                    return entity;
//                }).collect(Collectors.toList()),200,false,1);
//                this.saveChange();
//                status = 1;
//                errMsg = "整车车型信息[" + reqNo + "]接收保存成功";
//                logger.info(errMsg);
//            }else {
//                errMsg = "整车车型信息[" + reqNo + "]处理成功，数据已存在";
//                logger.info(errMsg);
//            }
//            loginfo.setAttribute1(verifyData);
////            infos = JsonUtils.parseArray(responseData, BomMaterialUseDto.class);
////            status = 1;
////            errMsg = "物料使用信息[" + reqNo + "]处理失败:";
////            logger.info(errMsg);
//        } catch (Exception ex) {
//            status = 5;
//            errMsg = "物料使用信息[" + reqNo + "]处理失败:";
//            logger.info(errMsg);
//            logger.error(errMsg, ex);
//        }
//        loginfo.setRequestStopTime(new Date());
//        loginfo.setStatus(status);
//        loginfo.setRemark(errMsg);
//        midApiLogBaseService.update(loginfo);
//        midApiLogBaseService.saveChange();
//        logger.info("物料使用信息[" + reqNo + "]执行完成:");
//        return infos;
    }

    private String md5(String str) {
        //.replace("-", "")
        return DigestUtils.md5Hex(str.getBytes(StandardCharsets.UTF_8)).toLowerCase();
    }

    @Override
    protected MidMaterialUseEntity getEntity(BomMaterialUseDto bomMaterialUseDto, Long loginfoId) {
        MidMaterialUseEntity entity = new MidMaterialUseEntity();
        BeanUtils.copyProperties(bomMaterialUseDto,entity);
        String key = String.join(",", bomMaterialUseDto.getVehicleMaterialNumbers());
        entity.setVehicleMaterialNumbers(key);
        entity.setPrcMidApiLogId(loginfoId);
        entity.setId(IdGenerator.getId());
        entity.setExeStatus(0);
        entity.setExeTime(new Date());
        entity.setExeMsg(StringUtils.EMPTY);
        return entity;
    }

    @Override
    protected List<BomMaterialUseDto> fetchEntity(BomResultVo resultVO) {
        return Arrays.asList(JsonUtils.parseObject(JsonUtils.toJsonString(resultVO.getData()), BomMaterialUseDto.class));
    }

    @Override
    protected Map<String, String> getParams(String... params) {
        Map<String,String> param = new HashMap<>(5);
        param.put("bomType", "MBOM");
        param.put("materialCode", params[0]);
        param.put("plantCode", params[1]);
        param.put("specifyDate", params[2]);
        return param;
    }

}