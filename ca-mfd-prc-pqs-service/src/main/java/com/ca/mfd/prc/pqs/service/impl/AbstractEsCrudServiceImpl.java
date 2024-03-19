/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.pqs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.ConditionsDto;
import com.ca.mfd.prc.common.model.base.dto.DataDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.service.impl.BaseServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * CRUD基础服务类
 *
 * @author inkelink
 */
public abstract class AbstractEsCrudServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends AbstractCrudServiceImpl<M, T>{

    private void handleBoolean(Map<String,Object> eachItem, String key){
        if(eachItem.containsKey(key)){
            try{
                eachItem.put(key,"true".equals(eachItem.get(key)) ? "是":"否");
            }catch (Exception e){
                eachItem.put(key,"否");
            }
        }
    }
    public void dealExcelDatas(List<Map<String, Object>> datas) {
        for(Map<String,Object> eachItem : datas){
            handleBoolean(eachItem,"qualifiedFlag");
            handleBoolean(eachItem,"qualifiedOrNot");
            handleBoolean(eachItem,"qualifiedOrNot1");
            handleBoolean(eachItem,"qualifiedOrNot2");
            formatDate(eachItem);
        }
    }

    private void formatDate(Map<String, Object> eachRow){
        for(Map.Entry<String,Object> eachItem : eachRow.entrySet()){
            if(eachItem.getValue() != null && eachItem.getValue().getClass().equals(Date.class)){
                eachRow.put(eachItem.getKey(), DateUtils.format((Date) eachItem.getValue(),DateUtils.DATE_TIME_PATTERN));
            }
        }
    }

}