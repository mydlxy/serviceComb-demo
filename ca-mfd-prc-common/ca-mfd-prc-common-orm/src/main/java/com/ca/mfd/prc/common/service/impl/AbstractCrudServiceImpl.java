/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.common.service.impl;

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
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CRUD基础服务类
 *
 * @author inkelink
 */
public abstract class AbstractCrudServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends BaseServiceImpl<M, T> implements ICrudService<T> {


    @Override
    public PageData<T> page(PageDataDto model) {
        return page(model,null);
    }

    @Override
    public PageData<T> page(PageDataDto model, List<String> selectColumns) {
        IPage<T> page = this.getDataByPage(model,selectColumns);
        PageData<T> result = getPageData(page, this.currentModelClass());
        if (model.getPageIndex() != null) {
            result.setPageIndex(model.getPageIndex());
        }
        if (model.getPageSize() != null) {
            result.setPageSize(model.getPageSize());
        }
        return result;
    }

    @Override
    public List<T> list(DataDto model) {
        QueryWrapper<T> wrapper = this.getWrapper(model.getConditions());
        wrapper = wrapperAddSorts(wrapper, model.getSorts());
        return this.selectList(wrapper);
    }

    public List<T> list(List<ConditionDto> conditions) {
        return this.selectList(getWrapper(conditions));
    }

    @Override
    public Integer count(ConditionsDto model) {
        return this.count(model.getConditions());
    }

    @Override
    public Integer count(List<ConditionDto> conditions) {
        long counts = this.selectCount(getWrapper(conditions));

        return Integer.valueOf((int) counts);
    }

    @Override
    public T get(Serializable id) {
        return  this.selectById(id);
    }

    @Override
    public List<T> getByIds(List<Serializable> idList) {
        return this.selectBatchIds(idList);
    }

    @Override
    public void save(T dto) {

        insert(dto);
    }

    @Override
    public void update(T dto) {

        updateById(dto);
    }

    @Override
    public void delete(Long id) {
        this.delete(id, true);
    }

    @Override
    public void delete(Long id, Boolean isLogic) {
        this.deleteById(id, isLogic);
    }

    @Override
    public void delete(Serializable[] ids) {
        this.delete(ids, true);
    }

    @Override
    public void delete(Serializable[] ids, Boolean isLogic) {
        this.deleteBatchIds(Arrays.asList(ids),isLogic);
    }

    @Override
    public void delete(UpdateWrapper<T> updateWrapper) {
        delete(updateWrapper, true);
    }

    @Override
    public void delete(UpdateWrapper<T> updateWrapper, Boolean isLogic) {
        this.deleteByWarp(updateWrapper,isLogic);
    }

    @Override
    public void delete(List<ConditionDto> conditions) {
        this.delete(conditions, true);
    }

    @Override
    public void delete(List<ConditionDto> conditions, Boolean isLogic) {
        UpdateWrapper<T> updateWrapper = getUpdateWrapper(conditions);
        delete(updateWrapper, isLogic);
    }

    /**
     * 验证字段是否唯一
     *
     * @param id
     * @param columnName
     * @param value
     * @param errMsg
     * @param parentColunName
     * @param parentValue
     */
    public void validDataUnique(Serializable id, String columnName, String value, String errMsg, String parentColunName, String parentValue) {
        List<ConditionDto> list = new ArrayList<>();
        list.add(new ConditionDto(currentModelKeyProperty(), id.toString(), ConditionOper.Unequal));
        list.add(new ConditionDto(columnName, value, ConditionOper.Equal));
        if (StringUtils.isNotBlank(parentColunName)) {
            list.add(new ConditionDto(parentColunName, parentValue, ConditionOper.Equal));
        }
        if (count(list) > 0) {
            throw new InkelinkException(String.format(errMsg, value));
        }
    }

    public void validDataUnique(Serializable id, String columnName, String value, String errMsg, String... otherColumnNameAndValue) {
        List<ConditionDto> list = new ArrayList<>();
        list.add(new ConditionDto(currentModelKeyProperty(), id.toString(), ConditionOper.Unequal));
        list.add(new ConditionDto(columnName, value, ConditionOper.Equal));
        if(otherColumnNameAndValue != null && otherColumnNameAndValue.length % 2 == 0){
            for(int i = 0; i < otherColumnNameAndValue.length; i++){
                if(i % 2 == 1 && StringUtils.isNotBlank(otherColumnNameAndValue[i-1])){
                    list.add(new ConditionDto(otherColumnNameAndValue[i-1], otherColumnNameAndValue[i], ConditionOper.Equal));
                }
            }
        }
        if (count(list) > 0) {
            throw new InkelinkException(String.format(errMsg, value));
        }
    }


}