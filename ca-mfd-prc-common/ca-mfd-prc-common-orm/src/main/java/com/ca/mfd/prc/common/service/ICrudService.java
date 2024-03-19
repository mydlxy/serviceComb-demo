/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.common.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.ConditionsDto;
import com.ca.mfd.prc.common.model.base.dto.DataDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;

import java.io.Serializable;
import java.util.List;

/**
 * @author inkelink
 * @Description: CRUD基础服务接口
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
public interface ICrudService<T> extends IBaseService<T> {

    /**
     * 返回分页的数据
     *
     * @param model 分页信息
     * @return 分页数据
     */
    PageData<T> page(PageDataDto model);

    /**
     * 返回分页的数据(显示指定列)
     * @param model
     * @param selectColumns
     * @return
     */
    PageData<T> page(PageDataDto model,List<String> selectColumns);

    /**
     * 返回列表数据
     *
     * @param model 查询信息
     * @return 列表数据
     */
    List<T> list(DataDto model);

    /**
     * 返回查询的数据量
     *
     * @param model 查询条件
     * @return 查询的数据量
     */
    Integer count(ConditionsDto model);

    /**
     * 返回查询的数据量
     *
     * @param conditions 查询条件列表
     * @return 查询的数据量
     */
    Integer count(List<ConditionDto> conditions);


    /**
     * 获取的数据
     *
     * @param id 主键
     * @return 获取数据dto
     */
    T get(Serializable id);

    /**
     * 获取的数据
     *
     * @param idList 主键列表
     * @return 获取数据dto
     */
    List<T> getByIds(List<Serializable> idList);

    /**
     * 新增数据
     *
     * @param dto 需要新增的信息
     */
    void save(T dto);

    /**
     * 更新数据
     *
     * @param dto 需要保存的信息
     */
    void update(T dto);

    /**
     * 按照id逻辑删除数据
     *
     * @param id 需要删除的数据主键
     */
    void delete(Long id);

    /**
     * 按照ids逻辑删除数据
     *
     * @param ids 需要删除的数据主键
     */
    void delete(Serializable[] ids);

    /**
     * 按照ids删除数据
     *
     * @param id      需要删除的数据主键
     * @param isLogic 是否逻辑删除
     */
    void delete(Long id, Boolean isLogic);


    /**
     * 按照ids删除数据
     *
     * @param ids     需要删除的数据主键
     * @param isLogic 是否逻辑删除
     */
    void delete(Serializable[] ids, Boolean isLogic);

    /**
     * 按照条件逻辑删除数据
     *
     * @param conditions 需要删除的条件
     */
    void delete(List<ConditionDto> conditions);

    /**
     * 按照条件删除数据
     *
     * @param conditions 需要删除的条件
     * @param isLogic    是否逻辑删除
     */
    void delete(List<ConditionDto> conditions, Boolean isLogic);

    /**
     * 按照条件删除数据
     *
     * @param updateWrapper 需要删除的条件
     */
    void delete(UpdateWrapper<T> updateWrapper);

    /**
     * 按照条件删除数据
     *
     * @param updateWrapper 需要删除的条件
     * @param isLogic       是否逻辑删除
     */
    void delete(UpdateWrapper<T> updateWrapper, Boolean isLogic);


}