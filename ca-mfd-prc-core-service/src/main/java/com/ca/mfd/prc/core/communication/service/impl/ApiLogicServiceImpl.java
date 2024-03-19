package com.ca.mfd.prc.core.communication.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.core.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.core.communication.dto.ApiResultVo;
import com.ca.mfd.prc.core.communication.dto.CountryDto;
import com.ca.mfd.prc.core.communication.dto.IccCategoryDto;
import com.ca.mfd.prc.core.communication.dto.IccDto;
import com.ca.mfd.prc.core.communication.dto.ShiftDto;
import com.ca.mfd.prc.core.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.core.communication.entity.MidIccApiEntity;
import com.ca.mfd.prc.core.communication.entity.MidIccCategoryApiEntity;
import com.ca.mfd.prc.core.communication.entity.MidPmCountryEntity;
import com.ca.mfd.prc.core.communication.entity.MidPmShiftEntity;
import com.ca.mfd.prc.core.communication.service.IApiLogicService;
import com.ca.mfd.prc.core.communication.service.IMidApiLogService;
import com.ca.mfd.prc.core.communication.service.IMidIccApiService;
import com.ca.mfd.prc.core.communication.service.IMidIccCategoryApiService;
import com.ca.mfd.prc.core.communication.service.IMidPmCountryService;
import com.ca.mfd.prc.core.communication.service.IMidPmShiftService;
import com.google.common.base.Strings;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @Description: ICC接口中间表服务实现
 * @author inkelink
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@Service
public class ApiLogicServiceImpl implements IApiLogicService {

    @Autowired
    private IMidIccApiService iccApiService;
    @Autowired
    private IMidApiLogService apiLogService;
    @Autowired
    private IMidIccCategoryApiService iccCategoryApiService;
    @Autowired
    private IMidPmCountryService countryService;
    @Autowired
    private IMidPmShiftService shiftService;

    private static final Logger logger = LoggerFactory.getLogger(ApiLogicServiceImpl.class);

    @Override
    public List<ApiResultVo> receiveData(JSONArray iccTemp, String type) {
        //校验
        if (iccTemp == null || iccTemp.size() == 0 || Strings.isNullOrEmpty(type)) {
            throw new InkelinkException("数据不能为空");
        }
        logger.info("[" + type + "]开始接收数据:" + (iccTemp == null ? 0 : iccTemp.size()));
        List<ApiResultVo> datas = new ArrayList<>();
        String s = JsonUtils.toJsonString(iccTemp);
        long id = IdGenerator.getId();
        switch (type){
            case "ICC":
                List<IccDto> iccDtos = JsonUtils.parseArray(s, IccDto.class);
                datas.addAll(this.receiveIccData(iccDtos,id));
                break;
            case "ICC_CLASSIFICATION_STANDARD":
                List<IccCategoryDto> iccCategoryDtos = JsonUtils.parseArray(s, IccCategoryDto.class);
                datas.addAll(this.receiveIccCategoryData(iccCategoryDtos,id));
                break;
            case "NATION":
                List<CountryDto> countryDtos = JsonUtils.parseArray(s, CountryDto.class);
                datas.addAll(this.receiveCountryData(countryDtos,id));
                break;
            case "CLASSES":
                List<ShiftDto> shiftDtos = JsonUtils.parseArray(s, ShiftDto.class);
                datas.addAll(this.receiveShiftData(shiftDtos,id));
                break;
        }
        logger.info("[" + type + "]执行完成");
        return datas;
    }

    private List<ApiResultVo>  receiveShiftData(List<ShiftDto> shiftDtos, long id) {
        //记接口记录
        recordLog(id,ApiTypeConst.MAIN_SHIFT,shiftDtos.size(),0,null);
        if(CollectionUtils.isEmpty(shiftDtos)){
            return new ArrayList<ApiResultVo>();
        }

        List<ApiResultVo> datas=new ArrayList<>();
        shiftDtos.stream().forEach(c->{
            MidPmShiftEntity shiftEntity = new MidPmShiftEntity();
            ApiResultVo resultVo=new ApiResultVo();
            BeanUtils.copyProperties(c,shiftEntity);
            shiftEntity.setId(IdGenerator.getId());
            shiftEntity.setPrcMidApiLogId(id);
            shiftService.save(shiftEntity);
            resultVo.setStatus("true");
            resultVo.setSeqId(shiftEntity.getSubId());
            datas.add(resultVo);
        });
        try {
            shiftService.saveChange();
            recordLog(id,ApiTypeConst.MAIN_SHIFT,shiftDtos.size(),1,new Date());
        }catch (Exception e){
            recordLog(id,ApiTypeConst.MAIN_SHIFT,shiftDtos.size(),5,new Date(),"插入失败");
            datas.clear();
        }

        return datas;
    }

    private List<ApiResultVo>  receiveCountryData(List<CountryDto> countryDtos, long id) {
        //记接口记录
        recordLog(id,ApiTypeConst.MAIN_COUNTRY,countryDtos.size(),0,null);
        if(CollectionUtils.isEmpty(countryDtos)){
            return new ArrayList<ApiResultVo>();
        }

        List<ApiResultVo> datas=new ArrayList<>();
        countryDtos.stream().forEach(c->{
            MidPmCountryEntity countryEntity = new MidPmCountryEntity();
            ApiResultVo resultVo=new ApiResultVo();
            BeanUtils.copyProperties(c,countryEntity);
            countryEntity.setId(IdGenerator.getId());
            countryEntity.setPrcMidApiLogId(id);
            countryService.save(countryEntity);
            resultVo.setStatus("true");
            resultVo.setSeqId(countryEntity.getSubId());
            datas.add(resultVo);
        });
        try {
            countryService.saveChange();
            recordLog(id,ApiTypeConst.MAIN_COUNTRY,countryDtos.size(),1,new Date());
        }catch (Exception e){
            recordLog(id,ApiTypeConst.MAIN_COUNTRY,countryDtos.size(),5,new Date(),"插入失败");
            datas.clear();
        }

        return datas;
    }


    /**
     * ICC接收数据
     * @param iccDtos
     * @param id
     * @return
     */
    public List<ApiResultVo> receiveIccData(List<IccDto> iccDtos,Long id) {
        //记接口记录
        recordLog(id,ApiTypeConst.PMS_ICC_DATA,iccDtos.size(),0,null);
        if(CollectionUtils.isEmpty(iccDtos)){
            return new ArrayList<ApiResultVo>();
        }

        List<ApiResultVo> datas=new ArrayList<>();
        iccDtos.stream().forEach(c->{
            MidIccApiEntity midIccApiEntity = new MidIccApiEntity();
            ApiResultVo resultVo=new ApiResultVo();
            BeanUtils.copyProperties(c,midIccApiEntity);
            midIccApiEntity.setId(IdGenerator.getId());
            midIccApiEntity.setPrcMidApiLogId(id);
            iccApiService.save(midIccApiEntity);
            resultVo.setStatus("true");
            resultVo.setSeqId(midIccApiEntity.getSubId());
            datas.add(resultVo);
        });
        try {
            iccApiService.saveChange();
            recordLog(id,ApiTypeConst.PMS_ICC_DATA,iccDtos.size(),1,new Date());
        }catch (Exception e){
            recordLog(id,ApiTypeConst.PMS_ICC_DATA,iccDtos.size(),5,new Date(),"插入失败");
            datas.clear();
        }

        return datas;
    }

    /**
     * ICC分类数据
     * @param iccDtos
     * @param id
     * @return
     */
    public List<ApiResultVo> receiveIccCategoryData(List<IccCategoryDto> iccDtos,Long id) {
        if(CollectionUtils.isEmpty(iccDtos)){
            return new ArrayList<ApiResultVo>();
        }
        //记接口记录
        recordLog(id,ApiTypeConst.PMS_ICC_CATEGORY,iccDtos.size(),0,null);
        List<ApiResultVo> datas=new ArrayList<>();
        iccDtos.stream().forEach(c->{
            MidIccCategoryApiEntity iccCategoryApiEntity = new MidIccCategoryApiEntity();
            ApiResultVo resultVo=new ApiResultVo();
            BeanUtils.copyProperties(c,iccCategoryApiEntity);
            iccCategoryApiEntity.setId(IdGenerator.getId());
            iccCategoryApiEntity.setPrcMidApiLogId(id);
            iccCategoryApiService.save(iccCategoryApiEntity);
            resultVo.setStatus("true");
            resultVo.setSeqId(iccCategoryApiEntity.getSubId());
            datas.add(resultVo);
        });
        try {
            iccCategoryApiService.saveChange();
            recordLog(id, ApiTypeConst.PMS_ICC_CATEGORY,iccDtos.size(),1,new Date());
        }catch (Exception e){
            recordLog(id,ApiTypeConst.PMS_ICC_CATEGORY,iccDtos.size(),5,new Date(),"插入失败");
            datas.clear();
        }
        return datas;
    }

    //记录接口日志
    private void recordLog(Long id,String apiType,Integer dataLine,Integer status,Date stopDate) {
        MidApiLogEntity logEntity=new MidApiLogEntity();
        logEntity.setId(id);
        logEntity.setApiType(apiType);
        logEntity.setRequestStartTime(new Date());
        if(stopDate!=null){
            logEntity.setRequestStopTime(stopDate);
        }
        logEntity.setStatus(status);
        logEntity.setDataLineNo(dataLine);
        MidApiLogEntity logEntity1 = apiLogService.get(id);
        if(logEntity1!=null){
            apiLogService.update(logEntity);
        }else {
            apiLogService.save(logEntity);
        }
        apiLogService.saveChange();
    }
    //记录接口日志
    private void recordLog(Long id,String apiType,Integer dataLine,Integer status,Date stopDate,String remark) {
        MidApiLogEntity logEntity=new MidApiLogEntity();
        logEntity.setId(id);
        logEntity.setApiType(apiType);
        logEntity.setRemark(remark);
        logEntity.setRequestStartTime(new Date());
        if(stopDate!=null){
            logEntity.setRequestStopTime(stopDate);
        }
        logEntity.setStatus(status);
        logEntity.setDataLineNo(dataLine);
        MidApiLogEntity logEntity1 = apiLogService.get(id);
        if(logEntity1!=null){
            apiLogService.update(logEntity);
        }else {
            apiLogService.save(logEntity);
        }
        apiLogService.saveChange();
    }
}