/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.common.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 基础服务接口，所有Service接口都要继承
 *
 * @author inkelink
 */
public interface IBaseService<T> {

    /**
     * 获取当前T的Class
     *
     * @return Class<T>
     */
    Class<T> currentModelClass();

    /**
     * 获取当前实体对象的ID值
     *
     * @param entitie
     * @return ID值
     */
    Long currentModelGetKey(T entitie);

    /**
     * <p>
     * 插入一条记录（选择字段，策略插入）
     * </p>
     *
     * @param entity 实体对象
     * @return boolean
     */
    boolean insert(T entity);

    /**
     * <p>
     * 插入（批量），该方法不支持 Oracle、SQL Server
     * </p>
     *
     * @param entityList 实体对象集合
     * @return boolean
     */
    boolean insertBatch(Collection<T> entityList);

    /**
     * <p>
     * 插入（批量），该方法不支持 Oracle、SQL Server
     * </p>
     *
     * @param entityList 实体对象集合
     * @param batchSize  插入批次数量
     * @return boolean
     */
    boolean insertBatch(Collection<T> entityList, int batchSize);

    /**
     * <p>
     * 插入（批量），该方法不支持 Oracle、SQL Server
     * </p>
     *
     * @param entityList 实体对象集合
     * @param batchSize  插入批次数量
     * @param verify  是否需要校验事件
     * @param batchType  批处理类型（0，默认，1 批量）
     * @return boolean
     */
    boolean insertBatch(Collection<T> entityList, int batchSize, boolean verify,Integer batchType);

    /**
     * <p>
     * 根据 ID 选择修改
     * </p>
     *
     * @param entity 实体对象
     * @return boolean
     */
    boolean updateById(T entity);

    /**
     * <p>
     * 根据 ID 选择修改
     * </p>
     *
     * @param entity 实体对象
     * @param very 是否验证
     * @return boolean
     */
    boolean updateById(T entity,boolean very);

//    /**
//     * <p>
//     * 根据 whereEntity 条件，更新记录
//     * </p>
//     *
//     * @param entity        实体对象
//     * @param updateWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper}
//     * @return boolean
//     */
//    boolean update(T entity, Wrapper<T> updateWrapper);

    /**
     * <p>
     * 根据 whereEntity 条件，更新记录
     * </p>
     *
     * @param lambdaUpdateWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper}
     * @return boolean
     */
    boolean update(LambdaUpdateWrapper<T> lambdaUpdateWrapper);

    /**
     * 根据 whereEntity 条件，更新记录
     * @param lambdaUpdateWrapper
     * @param very
     * @return
     */
    boolean update(LambdaUpdateWrapper<T> lambdaUpdateWrapper,boolean very);

    /**
     * <p>
     * 根据 whereEntity 条件，更新记录
     * </p>
     *
     * @param updateWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper}
     * @return boolean
     */
    boolean update(UpdateWrapper<T> updateWrapper);

    /**
     * 据 whereEntity 条件，更新记录
     * @param updateWrapper
     * @param very
     * @return
     */
    boolean update(UpdateWrapper<T> updateWrapper,boolean very);


    /**
     * <p>
     * 根据ID 批量更新
     * </p>
     *
     * @param entityList 实体对象集合
     * @return boolean
     */
    boolean updateBatchById(Collection<T> entityList);

    /**
     * <p>
     * 根据ID 批量更新
     * </p>
     *
     * @param entityList 实体对象集合
     * @param batchSize  更新批次数量
     * @return boolean
     */
    boolean updateBatchById(Collection<T> entityList, int batchSize);


    /**
     *
     * @param entityList 实体对象集合
     * @param batchSize 更新批次数量
     * @param verify 是否验证
     * @return
     */
    boolean updateBatchById(Collection<T> entityList, int batchSize,boolean verify);

    /**
     * 获取未逻辑删除的数据数量
     *
     * @param queryWrapper 查询表达式
     * @return List<T>
     */
    Long selectCount(QueryWrapper<T> queryWrapper);

    /**
     * 获取数据数量
     *
     * @param queryWrapper 查询表达式
     * @param isDelete     是否删除
     * @return List<T>
     */
    Long selectCount(QueryWrapper<T> queryWrapper, Boolean isDelete);

    /**
     * 获取类型为T的未逻辑删除的列表数据
     *
     * @param conditions 条件表达式
     * @return List<T>
     */
    List<T> getData(List<ConditionDto> conditions);

    /**
     * 获取类型为T的列表数据
     *
     * @param conditions 条件表达式
     * @param isDelete   是否删除
     * @return List<T>
     */
    List<T> getData(List<ConditionDto> conditions, Boolean isDelete);

    /**
     * 获取类型为T的列表数据
     *
     * @param conditions 条件表达式
     * @param sorts      排序表达式
     * @param isDelete   是否删除
     * @return List<T>
     */
    List<T> getData(List<ConditionDto> conditions, List<SortDto> sorts, Boolean isDelete);



    /**
     * 获取类型为T的列表数据
     *
     * @param queryWrapper 条件表达式
     * @param isDelete     是否删除
     * @return List<T>
     */
    List<T> getData(QueryWrapper<T> queryWrapper, Boolean isDelete);


    /**
     * 获取类型为T的列表数据
     *
     * @param conditions 条件表达式
     * @param sorts      排序表达式
     * @return List<T>
     */
    List<T> getTopDatas(List<ConditionDto> conditions, List<SortDto> sorts);

    /**
     *
     * @param conditions 条件表达式
     * @param sorts 排序表达式
     * @param selectColumns 显示指定字段
     * @return
     */
    List<T> getTopDatas(List<ConditionDto> conditions, List<SortDto> sorts,List<String> selectColumns);

    /**
     * 获取类型为T的列表数据
     *
     * @param top        获取前top数据
     * @param conditions 条件表达式
     * @param sorts      排序表达式
     * @return List<T>
     */
    List<T> getTopDatas(Integer top, List<ConditionDto> conditions, List<SortDto> sorts);

    /**
     * 获取类型为T的列表数据
     *
     * @param top        获取前top数据
     * @param conditions 条件表达式
     * @param sorts      排序表达式
     * @param selectColumns  显示指定字段
     * @return List<T>
     */
     List<T> getTopDatas(Integer top, List<ConditionDto> conditions, List<SortDto> sorts,List<String> selectColumns);


    /**
     * 获取类型为T的列表数据
     *
     * @param top          获取前top数据
     * @param queryWrapper 查询表达式
     * @return List<T>
     */
    List<T> getTopDatas(Integer top, QueryWrapper<T> queryWrapper);

    /**
     * 获取类型为T的列表数据
     *
     * @param top          获取前top数据
     * @param queryWrapper 查询表达式
     * @param isDelete     是否删除
     * @return List<T>
     */
    List<T> getTopDatas(Integer top, QueryWrapper<T> queryWrapper, Boolean isDelete);


    /**
     * 获取类型为T的列表数据
     *
     * @param top        获取前top数据
     * @param conditions 条件表达式
     * @param sorts      排序表达式
     * @param isDelete   是否删除
     * @return List<T>
     */
    List<T> getTopDatas(Integer top, List<ConditionDto> conditions, List<SortDto> sorts, Boolean isDelete);

    /**
     *获取类型为T的列表数据(显示指定列)
     * @param top        获取前top数据
     * @param conditions 条件表达式
     * @param sorts      排序表达式
     * @param isDelete   是否删除
     * @param selectColumns 显示指定列
     * @return
     */
    List<T> getTopDatas(Integer top, List<ConditionDto> conditions, List<SortDto> sorts, Boolean isDelete,List<String> selectColumns);


    /**
     * 获取类型为T的未逻辑删除的分页数据
     *
     * @param model 条件表达式和分页信息
     * @return List<T>
     */
    IPage<T> getDataByPage(PageDataDto model);

    /**
     * 获取类型为T的未逻辑删除的分页数据(显示指定列)
     * @param model
     * @param selectColumns
     * @return
     */
    IPage<T> getDataByPage(PageDataDto model,List<String> selectColumns);

    /**
     * 获取类型为T的分页数据
     *
     * @param model    条件表达式和分页信息
     * @param isDelete 是否删除
     * @return List<T>
     */
    IPage<T> getDataByPage(PageDataDto model, Boolean isDelete);

    /**
     * 获取类型为T的分页数据(显示指定列)
     * @param model
     * @param isDelete
     * @param selectColumns
     * @return
     */
    IPage<T> getDataByPage(PageDataDto model, Boolean isDelete,List<String> selectColumns);

    /**
     * 获取类型为T的分页数据
     *
     * @param wrapper   条件表达式和分页信息
     * @param pageIndex 分页序号
     * @param pageSize  分页大小
     * @return List<T>
     */
    IPage<T> getDataByPage(QueryWrapper<T> wrapper, int pageIndex, int pageSize);

    /**
     * 获取类型为T的分页数据
     *
     * @param wrapper   条件表达式和分页信息
     * @param pageIndex 分页序号
     * @param pageSize  分页大小
     * @param isDelete  是否删除
     * @return List<T>
     */
    IPage<T> getDataByPage(QueryWrapper<T> wrapper, int pageIndex, int pageSize, Boolean isDelete);

    /**
     * 导出
     *
     * @param conditions
     * @param sorts
     * @param fileName
     * @param response
     * @throws IOException
     */
    void export(List<ConditionDto> conditions, List<SortDto> sorts, String fileName, HttpServletResponse response) throws IOException;

    /**
     * 导出
     *
     * @param conditions
     * @param sorts
     * @param fileName
     * @param isSimpleDate 是否日期时间格式化
     * @param response
     * @throws IOException
     */
    void export(List<ConditionDto> conditions, List<SortDto> sorts, String fileName, boolean isSimpleDate, HttpServletResponse response) throws IOException;

    /**
     * 获取导入模板
     *
     * @param fileName
     * @param response
     * @throws IOException
     */
    void getImportTemplate(String fileName, HttpServletResponse response) throws IOException;

    /**
     * 导入
     *
     * @param is
     * @throws Exception
     */
    void importExcel(InputStream is) throws Exception;

    /**
     * 数据变更
     *
     * @param entity
     */
    void beforeInsert(T entity);

    /**
     * 数据变更
     *
     * @param entity 实体
     */
    void afterInsert(T entity);

    /**
     * 数据变更
     *
     * @param entity 实体
     */
    void beforeUpdate(T entity);

    /**
     * 数据变更
     *
     * @param updateWrapper 条件
     */
    void beforeUpdate(Wrapper<T> updateWrapper);

    /**
     * 数据变更
     *
     * @param entity 实体
     */
    void afterUpdate(T entity);

    /**
     * 数据变更
     *
     * @param updateWrapper 条件
     */
    void afterUpdate(Wrapper<T> updateWrapper);

    /**
     * 数据变更
     *
     * @param queryWrapper
     */
    void beforeDelete(Wrapper<T> queryWrapper);

    /**
     * 数据变更
     *
     * @param idList 实体
     */
    void beforeDelete(Collection<? extends Serializable> idList);


    /**
     * 数据变更
     *
     * @param queryWrapper
     */
    void afterDelete(Wrapper<T> queryWrapper);

    /**
     * 数据变更
     *
     * @param idList 实体
     */
    void afterDelete(Collection<? extends Serializable> idList);

    /**
     * 数据
     *
     * @return
     */
    Map<String, String> getExcelColumnNames();

    /**
     * 数据
     *
     * @param columnNames
     */
    void setExcelColumnNames(Map<String, String> columnNames);

    /**
     * 执行保存
     */
    void saveChange();

    /**
     * 清空事务
     */
    void clearChange();
}