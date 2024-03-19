package com.ca.mfd.prc.pm.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.pm.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pm.communication.dto.BomResultVo;
import com.ca.mfd.prc.pm.communication.dto.MBomDto;
import com.ca.mfd.prc.pm.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pm.communication.mapper.IMidMBomMapper;
import com.ca.mfd.prc.pm.communication.entity.MidMBomEntity;
import com.ca.mfd.prc.pm.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pm.communication.service.IMidMBomService;
import com.ca.mfd.prc.pm.service.IPmBopBomService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @Description: 制造域BOM服务实现
 * @author inkelink
 * @date 2023年12月08日
 * @变更说明 BY inkelink At 2023年12月08日
 */
@Service
public class MidMBomServiceImpl extends MidBomBaseServiceImpl<IMidMBomMapper, MidMBomEntity,MBomDto> implements IMidMBomService {

    private static final Object lockObj = new Object();
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_MID_M_BOM";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<MidMBomEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(MidMBomEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(MidMBomEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<MidMBomEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<MidMBomEntity> getAllDatas() {
        List<MidMBomEntity> datas = localCache.getObject(cacheName);
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

    private static final Logger logger = LoggerFactory.getLogger(MidMBomServiceImpl.class);



    @Override
    public int receive(String orgCode, String specifyDate, String bomRoom) {
         List<MBomDto> bomNums = super.fetchDataFromApi("mbom_receive","MBOM信息",ApiTypeConst.BOM_MBOM,false,orgCode, specifyDate, bomRoom);
         return bomNums.size();

//        String apiUrl = sysConfigurationProvider.getConfiguration("mbom_receive", "midapi");
//        if (StringUtils.isBlank(apiUrl)) {
//            throw new InkelinkException("没有配置上报的地址[mbom_receive]");
//        }
//
//        String reqNo = UUIDUtils.getGuid();;
//        logger.info("MBOM信息[" + reqNo + "]开始接收数据");
//        MidApiLogEntity loginfo = new MidApiLogEntity();
//        loginfo.setApiType(ApiTypeConst.BOM_MBOM);
//        int status = 1;
//        String errMsg = "";
//        loginfo.setDataLineNo(0);
//        loginfo.setRequestStartTime(new Date());
//        List<MBomDto> infos = new ArrayList<>();
//        try {
//            loginfo.setStatus(0);
//            midApiLogBaseService.insert(loginfo);
//            midApiLogBaseService.saveChange();
//
//            // 构建API请求参数
//            Map param = new HashMap<>();
//            param.put("serviceId",reqNo);
//            param.put("plantCode", orgCode);
//            param.put("startDate", startDate);
//            param.put("endDate", endDate);
//            // 发起HTTP请求
//            String responseData = apiPtService.postapi(apiUrl, param, null);
//
//            logger.warn("API平台测试url调用：" + responseData);
//            BomResultVo resultVO = JsonUtils.parseObject(responseData, BomResultVo.class);
////            String verifyData=StringUtils.EMPTY;
//            if(resultVO!=null&&resultVO.getCode()==1){
//                infos = JsonUtils.parseArray(JsonUtils.toJsonString(resultVO.getData()), MBomDto.class);
////                verifyData = md5(JsonUtils.toJsonString(infos));
////                QueryWrapper<MidApiLogEntity> queryWrapper=new QueryWrapper<>();
////                queryWrapper.lambda().eq(BaseEntity::getAttribute1,verifyData);
////                List<MidApiLogEntity> data = midApiLogBaseService.getData(queryWrapper, false);
//                if(CollectionUtils.isNotEmpty(infos)){
//                    this.insertBatch(infos.stream().map(c -> {
//                        MidMBomEntity entity = new MidMBomEntity();
//                        BeanUtils.copyProperties(c,entity);
//                        entity.setId(IdGenerator.getId());
//                        entity.setMBomId(c.getId());
//                        entity.setPrcMidApiLogId(loginfo.getId());
//                        entity.setExeStatus(0);
//                        entity.setExeTime(new Date());
//                        entity.setExeMsg(StringUtils.EMPTY);
//                        return entity;
//                    }).collect(Collectors.toList()),200,false,1);
//                    this.saveChange();
//                    status = 1;
//                    errMsg = "MBOM信息[" + reqNo + "]接收保存成功";
//                    logger.info(errMsg);
//                }else {
//                    status = 1;
//                    errMsg = "MBOM信息[" + reqNo + "]处理成功，数据为空";
//                    logger.info(errMsg);
//                }
//            }else {
//                errMsg=resultVO.getMsg();
//            }
////            loginfo.setAttribute1(verifyData);
//
//        } catch (Exception ex) {
//            status = 5;
//            errMsg = "MBOM信息[" + reqNo + "]处理失败:";
//            logger.info(errMsg);
//            logger.error(errMsg, ex);
//        }
//        loginfo.setRequestStopTime(new Date());
//        loginfo.setStatus(status);
//        loginfo.setRemark(errMsg);
//        midApiLogBaseService.update(loginfo);
//        midApiLogBaseService.saveChange();
//        logger.info("MBOM信息[" + reqNo + "]执行完成:");
    }

    private String md5(String str) {
        //.replace("-", "")
        return DigestUtils.md5Hex(str.getBytes(StandardCharsets.UTF_8)).toLowerCase();
    }

    @Override
    protected MidMBomEntity getEntity(MBomDto mBomDto, Long loginfoId) {
        MidMBomEntity entity = new MidMBomEntity();
        BeanUtils.copyProperties(mBomDto,entity);
        entity.setId(IdGenerator.getId());
        entity.setMBomId(mBomDto.getId());
        entity.setPrcMidApiLogId(loginfoId);
        entity.setExeStatus(0);
        entity.setExeTime(new Date());
        entity.setExeMsg(StringUtils.EMPTY);
        entity.setAttribute1(mBomDto.getBomRoom());
        return entity;
    }

    @Override
    protected List<MBomDto> fetchEntity(BomResultVo resultVO,String... params) {
        List<MBomDto> mBomDtoList = fetchEntity(resultVO);
        if(!mBomDtoList.isEmpty()){
            for(MBomDto mBomDto : mBomDtoList){
                mBomDto.setBomRoom(params[2]);
            }
        }
        return mBomDtoList;
    }

    @Override
    protected List<MBomDto> fetchEntity(BomResultVo resultVO) {
        return JsonUtils.parseArray(JsonUtils.toJsonString(resultVO.getData()), MBomDto.class);
    }

//    @Override
//    protected Map<String, String> getParams(String... params) {
//            Map<String,String> param = new HashMap<>(4);
//            param.put("plantCode", params[0]);
//            param.put("startDate", params[1]);
//            param.put("endDate", params[2]);
//            return param;
//    }

    @Override
    protected Map<String, String> getParams(String... params) {
        Map<String,String> param = new HashMap<>(4);
        param.put("plantCode", params[0]);
        param.put("specifyDate", params[1]);
        param.put("bomRoom", params[2]);
        return param;
    }


    @Override
    public void excute(String logid) {
        IMidApiLogService midApiLogService = SpringContextUtils.getBean(IMidApiLogService.class);
        IPmBopBomService bopBomService = SpringContextUtils.getBean(IPmBopBomService.class);
        List<MidApiLogEntity> apilogs = midApiLogService.getDoList(ApiTypeConst.BOM_MBOM, ConvertUtils.stringToLong(logid));
        if (apilogs == null || apilogs.isEmpty()) {
            return;
        }
        for (MidApiLogEntity apilog : apilogs) {
            boolean success = false;
            try {
                UpdateWrapper<MidApiLogEntity> uplogStart = new UpdateWrapper<>();
                uplogStart.lambda().set(MidApiLogEntity::getStatus, 4)
                        .eq(MidApiLogEntity::getId, apilog.getId());
                midApiLogService.update(uplogStart);
                midApiLogService.saveChange();

                List<MidMBomEntity> datas = this.getListByLog(apilog.getId());
                bopBomService.syncFromMBom(datas);
                bopBomService.saveChange();
                success = true;

            } catch (Exception exception) {
                logger.error("数据保存异常：{}", exception.getMessage(),exception);
            }
            try {
                midApiLogService.clearChange();
                UpdateWrapper<MidApiLogEntity> uplogEnd = new UpdateWrapper<>();
                uplogEnd.lambda().set(MidApiLogEntity::getStatus, success ? 5 : 6)
                        .eq(MidApiLogEntity::getId, apilog.getId());
                midApiLogService.update(uplogEnd);
                midApiLogService.saveChange();
            } catch (Exception exception) {
                logger.error("日志保存异常：{}", exception.getMessage(),exception);
            }
        }
    }

    @Override
    public List<MidMBomEntity> getListByLog(Long logid) {
        QueryWrapper<MidMBomEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(MidMBomEntity::getPrcMidApiLogId, logid);
        return selectList(qry);
    }
}