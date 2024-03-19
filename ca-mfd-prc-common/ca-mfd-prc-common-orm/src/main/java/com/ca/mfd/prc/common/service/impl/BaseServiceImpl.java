/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.entity.ExcelImprotAttribute;
import com.ca.mfd.prc.common.enums.ConditionDirection;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.enums.ConditionRelation;
import com.ca.mfd.prc.common.enums.FieldNameAndGategory;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.IBaseService;
import com.ca.mfd.prc.common.service.IUnitOfWorkService;
import com.ca.mfd.prc.common.utils.ClassUtil;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.InkelinkExcelUtils;
import com.ca.mfd.prc.common.utils.MpSqlUtils;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 基础服务类，所有Service都要继承
 *
 * @author inkelink
 */
public abstract class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> implements IBaseService<T> {
    private static final String URL_PRE_STR = "/";
    private static final String IS_DELETE_STR = "IS_DELETE";

    protected Log log = LogFactory.getLog(getClass());


    @Autowired
    IdentityHelper identityHelper;
    TableInfo tbInfo;
    List<TableFieldInfo> tbFieldinfos;
    @Autowired
    private M baseDao;
    @Autowired
    private IUnitOfWorkService unitOfWorkService;
    private Map<String, String> excelColumnNames = new HashMap<>();


    /**
     * <p>
     * 判断数据库操作是否成功
     * </p>
     * <p>
     * 注意！！ 该方法为 Integer 判断，不可传入 int 基本类型
     * </p>
     *
     * @param result 数据库操作返回影响条数
     * @return boolean
     */
    protected static boolean retBool(Integer result) {
        return SqlHelper.retBool(result);
    }

    private IPage<T> getPage(PageDataDto model) {
        //分页参数
        long curPage = 1;
        long limit = 10;

        if (model.getPageIndex() == null) {
            model.setPageIndex(0);
        }
        if (model.getPageIndex() != null) {
            curPage = model.getPageIndex();
        }
        if (model.getPageSize() != null) {
            limit = model.getPageSize();
        }

        //分页对象
        Page<T> page = new Page<>(curPage, limit);

        if (model.getSorts().stream().count() > 0) {
            TableInfo tableInfo = currentModelTableInfo();
            List<TableFieldInfo> fields = currentModelFieldInfos();
            //前端字段排序
            for (SortDto sort : model.getSorts()) {
                String columnName = MpSqlUtils.getColumnName(fields, sort.getColumnName(), tableInfo);
                if (!StringUtils.isBlank(columnName)) {
                    if (ConditionDirection.ASC == sort.getDirection()) {
                        page.addOrder(OrderItem.asc(columnName));
                    } else {
                        page.addOrder(OrderItem.desc(columnName));
                    }
                }
            }
        }


        return page;
    }

    protected <T> PageData<T> getPageData(List<?> list, long total, Class<T> target) {
        List<T> targetList = ConvertUtils.sourceToTarget(list, target);

        return new PageData<>(targetList, total);
    }

    protected <T> PageData<T> getPageData(IPage page, Class<T> target) {
        return getPageData(page.getRecords(), page.getTotal(), target);
    }

    @Override
    public IPage<T> getDataByPage(QueryWrapper<T> wrapper, int pageIndex, int pageSize) {
        return getDataByPage(wrapper, pageIndex, pageSize, false);
    }

    @Override
    public IPage<T> getDataByPage(QueryWrapper<T> wrapper, int pageIndex, int pageSize, Boolean isDelete) {
        if (pageIndex < 1) {
            pageIndex = 1;
        }
        if (pageSize < 1) {
            pageIndex = 1;
        }
        this.delCondition(wrapper, isDelete);
        Page<T> pageModel = new Page<>(pageIndex, pageSize);
        IPage<T> page = baseDao.selectPage(pageModel, wrapper);
        return page;
    }

    @Override
    public IPage<T> getDataByPage(PageDataDto model) {
        return this.getDataByPage(model, false);
    }

    @Override
    public IPage<T> getDataByPage(PageDataDto model,List<String> selectColumns) {
        return this.getDataByPage(model, false, selectColumns);
    }

    @Override
    public IPage<T> getDataByPage(PageDataDto model, Boolean isDelete) {
       return getDataByPage(model,isDelete,null);
    }

    @Override
    public IPage<T> getDataByPage(PageDataDto model, Boolean isDelete,List<String> selectColumns) {
        QueryWrapper<T> wrapper = this.getWrapper(model.getConditions());
        if(selectColumns != null && !selectColumns.isEmpty()){
            wrapper.select(selectColumns);
        }
        this.delCondition(wrapper, isDelete);
        IPage<T> page = baseDao.selectPage(
                getPage(model), wrapper
        );
        return page;
    }

    @Override
    public List<T> getData(List<ConditionDto> conditions) {
        return this.getData(conditions, false);
    }

    @Override
    public List<T> getData(List<ConditionDto> conditions, Boolean isDelete) {
        QueryWrapper<T> wrapper = this.getWrapper(conditions);
        this.delCondition(wrapper, isDelete);
        List<T> page = baseDao.selectList(
                wrapper
        );
        return page;
    }

    @Override
    public List<T> getData(List<ConditionDto> conditions, List<SortDto> sorts, Boolean isDelete) {
        QueryWrapper<T> wrapper = this.getWrapper(conditions);
        this.delCondition(wrapper, isDelete);
        wrapper = wrapperAddSorts(wrapper, sorts);
        List<T> page = baseDao.selectList(
                wrapper
        );
        return page;
    }


    @Override
    public List<T> getData(QueryWrapper<T> queryWrapper, Boolean isDelete) {
        QueryWrapper<T> wrapper = queryWrapper;
        this.delCondition(wrapper, isDelete);
        List<T> page = baseDao.selectList(
                wrapper
        );
        return page;
    }

    @Override
    public List<T> getTopDatas(List<ConditionDto> conditions, List<SortDto> sorts) {
        return this.getTopDatas(10, conditions, sorts, false);
    }

    @Override
    public List<T> getTopDatas(List<ConditionDto> conditions, List<SortDto> sorts, List<String> selectColumns) {
        return this.getTopDatas(10, conditions, sorts, false, selectColumns);
    }

    @Override
    public List<T> getTopDatas(Integer top, List<ConditionDto> conditions, List<SortDto> sorts) {
        return this.getTopDatas(top, conditions, sorts, false);
    }

    @Override
    public List<T> getTopDatas(Integer top, List<ConditionDto> conditions, List<SortDto> sorts, List<String> selectColumns) {
        return this.getTopDatas(top, conditions, sorts, false, selectColumns);
    }


    @Override
    public List<T> getTopDatas(Integer top, QueryWrapper<T> queryWrapper) {
        return getTopDatas(top, queryWrapper, false);
    }

    @Override
    public List<T> getTopDatas(Integer top, QueryWrapper<T> queryWrapper, Boolean isDelete) {
        IPage<T> page = this.getDataByPage(queryWrapper, 1, top, isDelete);
        return page.getRecords();
    }

    @Override
    public List<T> getTopDatas(Integer top, List<ConditionDto> conditions, List<SortDto> sorts, Boolean isDelete) {
       return getTopDatas(top,conditions,sorts,isDelete,null);
    }

    @Override
    public List<T> getTopDatas(Integer top, List<ConditionDto> conditions, List<SortDto> sorts, Boolean isDelete, List<String> selectColumns) {
        PageDataDto model = new PageDataDto();
        if (conditions == null) {
            conditions = new ArrayList<>();
        }
        model.setConditions(conditions);
        if (sorts == null) {
            sorts = new ArrayList<>();
            SortDto sort = new SortDto();
            sort.setColumnName("lastUpdatedDate");
            sort.setDirection(ConditionDirection.DESC);
            sorts.add(sort);
        }

        model.setSorts(sorts);
        model.setPageIndex(1);
        if (top != null) {
            model.setPageSize(top);
        }
        IPage<T> page = this.getDataByPage(model, isDelete,selectColumns);
        return page.getRecords();

    }

    protected boolean setWrapper(QueryWrapper<T> wrapper, List<TableFieldInfo> fields, ConditionDto model, TableInfo tableInfo) {
        String columnName = MpSqlUtils.getColumnName(fields, model.getColumnName(), tableInfo);
        if (StringUtils.isBlank(columnName)) {
            throw new InkelinkException("没有找到表[" + tableInfo.getTableName() + "]字段[" + model.getColumnName() + "]对应的数据库列");
        }
        if (model.getRelation() == ConditionRelation.Or) {
            wrapper = wrapper.or();
        }
        Object val = model.getDbval();
        if (val == null) {
            //空值不生成查询
//            if (model.getOperator() == ConditionOper.Equal) {
//                wrapper.isNull(columnName);
//            } else if (model.getOperator() == ConditionOper.Unequal) {
//                wrapper.isNotNull(columnName);
//            } else {
//                throw new InkelinkException("null条件，只能使用“Equal”和“Unequal”比对");
//            }
            return false;
        } else {
            if (model.getOperator() == ConditionOper.Equal) {
                wrapper.eq(columnName, val);
            } else if (model.getOperator() == ConditionOper.Unequal) {
                wrapper.ne(columnName, val);
            } else if (model.getOperator() == ConditionOper.GreaterThan) {
                wrapper.gt(columnName, val);
            } else if (model.getOperator() == ConditionOper.GreaterThanEqual) {
                wrapper.ge(columnName, val);
            } else if (model.getOperator() == ConditionOper.LessThanEqual) {
                wrapper.le(columnName, val);
            } else if (model.getOperator() == ConditionOper.LessThan) {
                wrapper.lt(columnName, val);
            } else if (model.getOperator() == ConditionOper.LeftLike) {
                wrapper.likeLeft(columnName, val);
            } else if (model.getOperator() == ConditionOper.RightLike) {
                wrapper.likeRight(columnName, val);
            } else if (model.getOperator() == ConditionOper.AllLike) {
                wrapper.like(columnName, val);
            } else if (model.getOperator() == ConditionOper.Exclusive) {
                wrapper.notLike(columnName, val);
            } else if (model.getOperator() == ConditionOper.In) {
                wrapper.in(columnName, (List<Object>) val);
            } else if (model.getOperator() == ConditionOper.NotIn) {
                wrapper.notIn(columnName, val);
            } else if (model.getOperator() == ConditionOper.Exists) {
                wrapper.exists(columnName, val);
            } else if (model.getOperator() == ConditionOper.NotExists) {
                wrapper.notExists(columnName, val);
            } else {
                return false;
            }
            return true;
        }
    }

    protected QueryWrapper<T> getWrapper(List<ConditionDto> models) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        if (models == null || models.size() == 0) {
            return wrapper;
        }
        TableInfo tableInfo = currentModelTableInfo();
        List<TableFieldInfo> fields = currentModelFieldInfos();
        MpSqlUtils.setConditionDataType(fields, models);
        Map<String, List<ConditionDto>> wheres = MpSqlUtils.getConditionGroup(models);
        if (wheres.size() > 0) {
            wrapper.and(a -> {
                for (Map.Entry<String, List<ConditionDto>> modelgps : wheres.entrySet()) {
                    String groupkey = modelgps.getKey();
                    if (modelgps.getValue() != null && modelgps.getValue().size() > 0) {
                        if (StringUtils.isNotBlank(groupkey) && groupkey.trim().startsWith("OR_")) {
                            a.or(c -> {
                                for (ConditionDto model : modelgps.getValue()) {
                                    setWrapper(c, fields, model, tableInfo);
                                }
                            });
                        } else {
                            a.and(c -> {
                                for (ConditionDto model : modelgps.getValue()) {
                                    setWrapper(c, fields, model, tableInfo);
                                }
                            });
                        }
                    }
                }
            });
        }
        return wrapper;
    }

    protected boolean setUpdateWrapper(UpdateWrapper<T> wrapper, List<TableFieldInfo> fields, ConditionDto model, TableInfo tableInfo) {
        String columnName = MpSqlUtils.getColumnName(fields, model.getColumnName(), tableInfo);
        if (StringUtils.isBlank(columnName)) {
            throw new InkelinkException("没有找到表[" + tableInfo.getTableName() + "]字段[" + model.getColumnName() + "]对应的数据库列");
        }
        if (model.getRelation() == ConditionRelation.Or) {
            wrapper = wrapper.or();
        }
        Object val = model.getDbval();
        if (val == null) {
            //空值不生成查询
//            if (model.getOperator() == ConditionOper.Equal) {
//                wrapper.isNull(columnName);
//            } else if (model.getOperator() == ConditionOper.Unequal) {
//                wrapper.isNotNull(columnName);
//            } else {
//                throw new InkelinkException("null条件，只能使用“Equal”和“Unequal”比对");
//            }
            return false;
        } else {
            if (model.getOperator() == ConditionOper.Equal) {
                wrapper.eq(columnName, val);
            } else if (model.getOperator() == ConditionOper.Unequal) {
                wrapper.ne(columnName, val);
            } else if (model.getOperator() == ConditionOper.GreaterThan) {
                wrapper.gt(columnName, val);
            } else if (model.getOperator() == ConditionOper.GreaterThanEqual) {
                wrapper.ge(columnName, val);
            } else if (model.getOperator() == ConditionOper.LessThanEqual) {
                wrapper.le(columnName, val);
            } else if (model.getOperator() == ConditionOper.LessThan) {
                wrapper.lt(columnName, val);
            } else if (model.getOperator() == ConditionOper.LeftLike) {
                wrapper.likeLeft(columnName, val);
            } else if (model.getOperator() == ConditionOper.RightLike) {
                wrapper.likeRight(columnName, val);
            } else if (model.getOperator() == ConditionOper.AllLike) {
                wrapper.like(columnName, val);
            } else if (model.getOperator() == ConditionOper.Exclusive) {
                wrapper.notLike(columnName, val);
            } else if (model.getOperator() == ConditionOper.In) {
                wrapper.in(columnName, (List<Object>) val);
            } else if (model.getOperator() == ConditionOper.NotIn) {
                wrapper.notIn(columnName, val);
            } else if (model.getOperator() == ConditionOper.Exists) {
                wrapper.exists(columnName, val);
            } else if (model.getOperator() == ConditionOper.NotExists) {
                wrapper.notExists(columnName, val);
            } else {
                return false;
            }
            return true;
        }
    }

    protected UpdateWrapper<T> getUpdateWrapper(List<ConditionDto> models) {
        UpdateWrapper<T> wrapper = new UpdateWrapper<>();
        if (models == null || models.size() == 0) {
            return wrapper;
        }
        TableInfo tableInfo = currentModelTableInfo();
        List<TableFieldInfo> fields = currentModelFieldInfos();
        Map<String, List<ConditionDto>> wheres = MpSqlUtils.getConditionGroup(models);

        if (wheres.size() > 0) {
            wrapper.and(a -> {
                for (Map.Entry<String, List<ConditionDto>> modelgps : wheres.entrySet()) {
                    String groupkey = modelgps.getKey();
                    setUpWrap(groupkey, modelgps, a, fields, tableInfo);
                }
            });
        }
        return wrapper;
    }

    private void setUpWrap(String groupkey, Map.Entry<String, List<ConditionDto>> modelgps, UpdateWrapper<T> a
            , List<TableFieldInfo> fields, TableInfo tableInfo) {
        if (modelgps.getValue() != null && modelgps.getValue().size() > 0) {
            if (StringUtils.isNotBlank(groupkey) && groupkey.trim().startsWith("OR_")) {
                a.or(c -> {
                    for (ConditionDto model : modelgps.getValue()) {
                        setUpdateWrapper(c, fields, model, tableInfo);
                    }
                });
            } else {
                a.and(c -> {
                    for (ConditionDto model : modelgps.getValue()) {
                        setUpdateWrapper(c, fields, model, tableInfo);
                    }
                });
            }
        }
    }

    protected void paramsToLike(Map<String, Object> params, String... likes) {
        for (String like : likes) {
            String val = (String) params.get(like);
            if (StringUtils.isNotBlank(val)) {
                params.put(like, "%" + val + "%");
            } else {
                params.put(like, null);
            }
        }
    }

    protected Class<M> currentMapperClass() {
        return (Class<M>) ReflectionKit.getSuperClassGenericType(this.getClass(), BaseServiceImpl.class, 0);
    }

    @Override
    public Class<T> currentModelClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(this.getClass(), BaseServiceImpl.class, 1);
    }

    protected TableInfo currentModelTableInfo() {
        if (tbInfo == null) {
            tbInfo = TableInfoHelper.getTableInfo(this.currentModelClass());
        }
        return tbInfo;
    }

    protected List<TableFieldInfo> currentModelFieldInfos() {
        if (tbFieldinfos == null) {
            tbFieldinfos = currentModelTableInfo().getFieldList();
        }
        return tbFieldinfos;
    }

    @Override
    public Long currentModelGetKey(T entitie) {
        try {
            Object val = getField(entitie, currentModelKeyProperty());
            return val == null ? null : Long.valueOf(val.toString());
        } catch (Exception e) {
            log.error("获取ID值错误", e);
        }
        return null;
    }

    protected void currentModelSetKey(T entitie, Object keyVal) {
        try {
            setField(entitie, currentModelKeyProperty(), keyVal);
        } catch (Exception e) {
            log.error("设置ID值错误", e);
            //throw e;
        }
    }


    protected String currentModelKeyColumn() {
        return currentModelTableInfo().getKeyColumn();
    }

    protected String currentModelKeyProperty() {
        return currentModelTableInfo().getKeyProperty();
    }

    protected String getSqlStatement(SqlMethod sqlMethod) {
        return SqlHelper.getSqlStatement(this.currentMapperClass(), sqlMethod);
    }

    @Override
    public void beforeInsert(T entity) {

    }


    @Override
    public void afterInsert(T entity) {

    }

    private void setCreater(T entity) {
        BaseEntity baseEt = entity;
        Long idVal = currentModelGetKey(entity);
        if (idVal == null || idVal == 0) {
            currentModelSetKey(entity, IdGenerator.getId());
        }

        // 强制赋值
        baseEt.setCreationDate(new Date());
        baseEt.setCreatedUser(getUserName());
        baseEt.setCreatedBy(identityHelper.getUserId());
        // 强制赋值
        baseEt.setLastUpdateDate(new Date());
        baseEt.setLastUpdatedUser(getUserName());
        baseEt.setLastUpdatedBy(identityHelper.getUserId());

        if (StringUtils.isBlank(baseEt.getFlag())) {
            baseEt.setFlag(Constant.SYS_FLAGS_YES);
        }
        if (baseEt.getIsDelete() == null) {
            baseEt.setIsDelete(false);
        }
        if (StringUtils.isBlank(baseEt.getAttribute1())) {
            baseEt.setAttribute1(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(baseEt.getAttribute2())) {
            baseEt.setAttribute2(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(baseEt.getAttribute3())) {
            baseEt.setAttribute3(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(baseEt.getAttribute4())) {
            baseEt.setAttribute4(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(baseEt.getAttribute5())) {
            baseEt.setAttribute5(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(baseEt.getAttribute6())) {
            baseEt.setAttribute6(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(baseEt.getAttribute7())) {
            baseEt.setAttribute7(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(baseEt.getAttribute8())) {
            baseEt.setAttribute8(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(baseEt.getAttribute9())) {
            baseEt.setAttribute9(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(baseEt.getAttribute10())) {
            baseEt.setAttribute10(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(baseEt.getLastUpdatedTraceid())) {
            baseEt.setLastUpdatedTraceid(StringUtils.EMPTY);
        }
        setUpdater(entity);
    }

    private String getUserName() {
        String uname = identityHelper.getUserName() + URL_PRE_STR + identityHelper.getLoginName();
        if (URL_PRE_STR.equals(uname)) {
            return Constant.SYS_DEFAULT_USER;
        }
        return uname;
    }

    private void setUpdater(T entity) {
        BaseEntity baseEt = entity;
        // 强制赋值
        baseEt.setLastUpdateDate(new Date());
        baseEt.setLastUpdatedUser(getUserName());
        baseEt.setLastUpdatedBy(identityHelper.getUserId());
        if (baseEt.getIsDelete() == null) {
            baseEt.setIsDelete(false);
        }
    }

    @Override
    public boolean insert(T entity) {
       return insert(entity,true);
    }

    public boolean insert(T entity,boolean verify){
        if(verify){
            beforeInsert(entity);
        }
        //boolean flag = BaseServiceImpl.retBool(baseDao.insert(entity));
        setCreater(entity);
        boolean flag = unitOfWorkService.addContent(this.getSqlStatement(SqlMethod.INSERT_ONE), entity);
        afterInsert(entity);
        return flag;
    }

    @Override
    public boolean insertBatch(Collection<T> entityList) {
        return insertBatch(entityList, 100);
    }

    /**
     * 批量插入
     */
    @Override
    public boolean insertBatch(Collection<T> entityList, int batchSize) {
        return insertBatch(entityList, batchSize, true, 0);
    }

    /**
     * 批量插入
     */
    @Override
    public boolean insertBatch(Collection<T> entityList, int batchSize, boolean verify,Integer batchType) {
        if (entityList == null || entityList.size() == 0) {
            return true;
        }
        if (verify) {
            for (T et : entityList) {
                beforeInsert(et);
            }
        }
        for (T et : entityList) {
            setCreater(et);
        }

        String sqlStatement = getSqlStatement(SqlMethod.INSERT_ONE);
        boolean flag = unitOfWorkService.addContent(sqlStatement, entityList, batchSize,batchType);
//        boolean flag = executeBatch(entityList, batchSize, (sqlSession, entity) -> sqlSession.insert(sqlStatement, entity));
        for (T et : entityList) {
            afterInsert(et);
        }
        return flag;

    }

    /**
     * 执行批量操作
     */
    protected <E> boolean executeBatch(Collection<E> list, int batchSize, BiConsumer<SqlSession, E> consumer) {
        return SqlHelper.executeBatch(this.currentModelClass(), this.log, list, batchSize, consumer);
    }

    @Override
    public void beforeUpdate(T entity) {

    }

    @Override
    public void beforeUpdate(Wrapper<T> updateWrapper) {

    }


    @Override
    public void afterUpdate(T entity) {

    }

    @Override
    public void afterUpdate(Wrapper<T> updateWrapper) {

    }


    @Override
    public boolean updateById(T entity) {
       return updateById(entity,true);
    }

    @Override
    public boolean updateById(T entity,boolean verify) {
        if(verify){
            beforeUpdate(entity);
        }
        setUpdater(entity);
        //boolean flag = BaseServiceImpl.retBool(baseDao.updateById(entity));
        MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
        param.put(Constants.ENTITY, entity);
        boolean flag = unitOfWorkService.addContent(this.getSqlStatement(SqlMethod.UPDATE_BY_ID), param);
        afterUpdate(entity);
        return flag;
    }

    @Override
    public boolean update(LambdaUpdateWrapper<T> lambdaUpdateWrapper) {
       return update(lambdaUpdateWrapper,true);
    }

    @Override
    public boolean update(LambdaUpdateWrapper<T> lambdaUpdateWrapper,boolean very) {
        if(very){
            beforeUpdate(lambdaUpdateWrapper);
        }
        lambdaUpdateWrapper.set(T::getLastUpdateDate, new Date());
        lambdaUpdateWrapper.set(T::getLastUpdatedBy, identityHelper.getUserId());
        lambdaUpdateWrapper.set(T::getLastUpdatedUser, getUserName());

        boolean flag = unitOfWorkService.addContent(this.getSqlStatement(SqlMethod.UPDATE), this.getWapperMap(lambdaUpdateWrapper));
        if(very){
            afterUpdate(lambdaUpdateWrapper);
        }
        return flag;
    }

    @Override
    public boolean update(UpdateWrapper<T> updateWrapper) {
       return update(updateWrapper,true);
    }

    @Override
    public boolean update(UpdateWrapper<T> updateWrapper,boolean very) {
        if(very){
            beforeUpdate(updateWrapper);
        }
        boolean flag = updateWrapper(updateWrapper);
        if(very){
            afterUpdate(updateWrapper);
        }
        return flag;
    }

    private boolean updateWrapper(UpdateWrapper<T> updateWrapper) {
        updateWrapper.set("LAST_UPDATE_DATE", new Date());
        updateWrapper.set("LAST_UPDATED_BY", identityHelper.getUserId());
        updateWrapper.set("LAST_UPDATED_USER", getUserName());

        boolean flag = unitOfWorkService.addContent(this.getSqlStatement(SqlMethod.UPDATE), this.getWapperMap(updateWrapper));
        return flag;
    }

    private Map<String, Object> getWapperMap(Wrapper<T> wrapper) {
        Map<String, Object> map = Maps.newHashMapWithExpectedSize(1);
        map.put(Constants.WRAPPER, wrapper);
        return map;
    }


    @Override
    public boolean updateBatchById(Collection<T> entityList) {
        return updateBatchById(entityList, 30);
    }

    @Override
    public boolean updateBatchById(Collection<T> entityList, int batchSize) {
        return updateBatchById(entityList,batchSize,true);
    }

    @Override
    public boolean updateBatchById(Collection<T> entityList, int batchSize,boolean verify) {
        if (entityList == null || entityList.isEmpty()) {
            return true;
        }
        if(verify){
            for (T et : entityList) {
                beforeUpdate(et);
                setUpdater(et);
            }
        }else{
            for (T et : entityList) {
                setUpdater(et);
            }
        }
        String sqlStatement = getSqlStatement(SqlMethod.UPDATE_BY_ID);
        List<MapperMethod.ParamMap<T>> mapperList = new ArrayList<>();
        for (T entity : entityList) {
            MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
            param.put(Constants.ENTITY, entity);
            mapperList.add(param);
        }
        boolean flag = unitOfWorkService.addContent(sqlStatement, mapperList, batchSize);
        if(verify){
            for (T et : entityList) {
                afterUpdate(et);
            }
        }
        return flag;
    }


    protected T selectById(Serializable id) {
        T entity = baseDao.selectById(id);
        if (entity != null && entity.getIsDelete()) {
            return null;
        }
        return entity;
    }


    protected List<T> selectBatchIds(Collection<? extends Serializable> idList) {
        return baseDao.selectBatchIds(idList);
    }


    @Override
    public void beforeDelete(Wrapper<T> queryWrapper) {

    }

    @Override
    public void beforeDelete(Collection<? extends Serializable> idList) {

    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {

    }

    @Override
    public void afterDelete(Wrapper<T> queryWrapper) {

    }


    protected boolean deleteById(Serializable id, Boolean isLogic) {
        beforeDelete(Arrays.asList(id));
//        boolean flag = SqlHelper.retBool(baseDao.deleteById(id));
        boolean flag = false;
        if (isLogic) {
            UpdateWrapper<T> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set(IS_DELETE_STR, 1)
                    .eq(currentModelKeyColumn(), id);
            flag = this.updateWrapper(updateWrapper);
        } else {
            flag = unitOfWorkService.addContent(this.getSqlStatement(SqlMethod.DELETE_BY_ID), id);
        }
        afterDelete(Arrays.asList(id));
        return flag;
    }


    protected boolean deleteByWarp(UpdateWrapper<T> queryWrapper, Boolean isLogic) {
        beforeDelete(queryWrapper);

        boolean flag = false;
        if (isLogic) {
            queryWrapper.set("IS_DELETE", 1);
            flag = updateWrapper(queryWrapper);
        } else {
            Map<String, Object> param = Maps.newHashMapWithExpectedSize(1);
            param.put(Constants.WRAPPER, queryWrapper);
            flag = unitOfWorkService.addContent(this.getSqlStatement(SqlMethod.DELETE), param);
        }
        afterDelete(queryWrapper);
        return flag;
    }

    protected boolean deleteBatchIds(Collection<? extends Serializable> idList, Boolean isLogic) {
        beforeDelete(idList);
//        boolean flag = SqlHelper.retBool(baseDao.deleteBatchIds(idList));
        boolean flag = false;
        if (isLogic) {
            UpdateWrapper<T> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("IS_DELETE", 1)
                    .in(currentModelKeyColumn(), idList);
            flag = this.updateWrapper(updateWrapper);
        } else {
            Map<String, Object> param = Maps.newHashMapWithExpectedSize(1);
            param.put(Constants.COLL, idList);
            flag = unitOfWorkService.addContent(this.getSqlStatement(SqlMethod.DELETE_BATCH_BY_IDS), param);
        }
        afterDelete(idList);
        return flag;
    }

    protected List<T> selectList(QueryWrapper<T> queryWrapper) {
        return this.selectList(queryWrapper, false);
    }


    protected List<T> selectList(QueryWrapper<T> queryWrapper, Boolean isDelete) {
        this.delCondition(queryWrapper, isDelete);
        return baseDao.selectList(queryWrapper);
    }

    @Override
    public Long selectCount(QueryWrapper<T> queryWrapper) {
        return this.selectCount(queryWrapper, false);
    }

    @Override
    public Long selectCount(QueryWrapper<T> queryWrapper, Boolean isDelete) {
        this.delCondition(queryWrapper, isDelete);
        return baseDao.selectCount(queryWrapper);
    }

    private void delCondition(QueryWrapper<T> queryWrapper, Boolean isDelete) {
        if (isDelete != null) {
            queryWrapper.and(a -> a.eq("IS_DELETE", isDelete.booleanValue()));
        }
    }

    @Override
    public void export(List<ConditionDto> conditions, List<SortDto> sorts, String fileName, HttpServletResponse response) throws IOException {
        export(conditions, sorts, fileName, false, response);
    }

    @Override
    public void export(List<ConditionDto> conditions, List<SortDto> sorts, String fileName, boolean isSimpleDate, HttpServletResponse response) throws IOException {
        Map<String, String> fieldParam = getExcelColumnNames();
        if (sorts == null || sorts.size() == 0) {
            SortDto sortDto = new SortDto();
            sortDto.setColumnName("LAST_UPDATE_DATE");
            sortDto.setDirection(ConditionDirection.DESC);
            if (sorts != null) {
                sorts.add(sortDto);
            } else {
                sorts = new ArrayList<>();
                sorts.add(sortDto);
            }
        }
        QueryWrapper<T> wrapper = wrapperAddSorts(getWrapper(conditions), sorts);
        List<T> entityList = this.selectList(wrapper);
        List<Map<String, Object>> mapList = InkelinkExcelUtils.getListMap(entityList, isSimpleDate);
        dealExcelDatas(mapList,fieldParam);
        InkelinkExcelUtils.exportByDc(fieldParam, mapList, fileName, response);
    }

    /**
     * 处理即将导出的数据
     *
     * @param datas
     */
    public void dealExcelDatas(List<Map<String, Object>> datas) {
        for(Map<String,Object> eachItem : datas){
            formatDate(eachItem);
        }
    }

    public void dealExcelDatas(List<Map<String, Object>> datas,Map<String, String> fieldParam) {
        dealExcelDatas(datas);
    }

    private void formatDate(Map<String, Object> eachRow){
        for(Map.Entry<String,Object> eachItem : eachRow.entrySet()){
            if(eachItem.getValue() != null && eachItem.getValue().getClass().equals(Date.class)){
                eachRow.put(eachItem.getKey(), DateUtils.format((Date) eachItem.getValue(),DateUtils.DATE_TIME_PATTERN));
            }
        }
    }

    /**
     * 获取导入模板
     *
     * @param fileName
     * @param response
     * @throws IOException
     */
    @Override
    public void getImportTemplate(String fileName, HttpServletResponse response) throws IOException {
        Map<String, String> fieldParam = getExcelColumnNames();
        InkelinkExcelUtils.getImportTemplate(fieldParam, fileName, response);
    }

    /**
     * 导入
     *
     * @param is
     * @throws Exception
     */
    @Override
    public void importExcel(InputStream is) throws Exception {
        Map<String, String> fieldParam = getExcelColumnNames();
        List<Map<String, String>> dicDatasFromExcel = InkelinkExcelUtils.importExcel(is, fieldParam);
        validImportDatas(dicDatasFromExcel, fieldParam);
        List<T> entities = convertExcelDataToEntity(dicDatasFromExcel);
        saveExcelData(entities);
    }

    public List<T> convertExcelDataToEntity(List<Map<String, String>> datas) throws Exception {
        if (datas.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> entities = new ArrayList<>(datas.size());
        for (Map<String, String> map : datas) {
            T entitie = BeanUtil.fillBeanWithMap(map, this.currentModelClass().newInstance(), false);
            Object id = getField(entitie,currentModelKeyProperty());
            if(id instanceof Long){
                Long lId = (Long)id;
                if(lId == 0){
                    setField(entitie, currentModelKeyProperty(), IdGenerator.getId());
                }
            }else{
                setField(entitie, currentModelKeyProperty(), IdGenerator.getId());
            }
            setField(entitie, "lastUpdateDate", new Date());
            setField(entitie, "lastUpdatedBy", identityHelper.getUserId());
            setField(entitie, "lastUpdatedUser", identityHelper.getUserName());
            setField(entitie, "createdBy", identityHelper.getUserId());
            setField(entitie, "createdUser", identityHelper.getUserName());
            setField(entitie, "creationDate", new Date());
            ClassUtil.defaultValue(entitie);
            entities.add(entitie);
        }
        return entities;
    }

    public List<T> convertExcelDataToEntity(List<Map<String, String>> listData, Map<String, Map<String, String>> mapSysConfigByCategory,
                                            String sheetName) throws Exception {
        List<Map<String, String>> newListData = setExcelFromSys(listData, mapSysConfigByCategory, sheetName);
        return convertExcelDataToEntity(newListData);
    }

    /**
     * 从系统配置表中替换导入数据
     */
    public <T> Map<String, String> convertExcelDataBySysConfig(Class<T> type, Map<String, String> data, Function<String,Map<String, String>> getConfigMaps) {
        Map<String, String> newData = Maps.newHashMapWithExpectedSize(data.size());
        data.forEach((c, k) -> {
            newData.put(c, k);
        });
        Field[] fields = type.getDeclaredFields();
        if (fields != null) {
            for (Field property : fields) {
                String fname = property.getName();
                if (data.containsKey(fname)) {
                    String value = data.get(fname);
                    ExcelImprotAttribute[] attr = property.getAnnotationsByType(ExcelImprotAttribute.class);
                    if (!StringUtils.isBlank(value)) {
                        if (attr != null && attr.length > 0) {
                            for (ExcelImprotAttribute importAttr : attr) {
                                try {
                                    Map<String, String> k = getConfigMaps.apply(importAttr.configCategoryKey());
                                    if (k.containsKey(value)) {
                                        value = k.get(value);
                                    } else {
                                        throw new Exception("数据模型" + importAttr.errorMessage() + "值" + value + "在系统配置文件中没有找到对应的值");
                                    }
                                } catch (Exception exe) {
                                    log.error(exe.getMessage());
                                    throw new InkelinkException("请检查系统配置是否正确，" + importAttr.configCategoryKey().toLowerCase() + "，出现重复对象" + exe.getMessage());
                                }
                            }
                        }
                    }
                    //重新写入map
                    newData.put(fname, value);
                }
            }
        }
        return newData;
    }

    public List<Map<String, String>> setExcelFromSys(List<Map<String, String>> listData,
                                                     Map<String, Map<String, String>> mapSysConfigByCategory,
                                                     String sheetName) {
        if (listData == null || listData.isEmpty()) {
            return Collections.emptyList();
        }
        List<Map<String, String>> newListData = setLowerFirst(listData);
        for (Map<String, String> eachRowData : newListData) {
            setBooleanVal(eachRowData);
            setExcelFromSys(eachRowData, mapSysConfigByCategory, sheetName);
        }
        return newListData;
    }

    protected void setBooleanVal(Map<String, String> eachRowData) {

    }

    protected String getSysConfig(String fieldName, Map<String, String> eachRowData) {
        return FieldNameAndGategory.getGategoryName(fieldName);

    }

    private void setExcelFromSys(Map<String, String> eachRowData,
                                 Map<String, Map<String, String>> mapSysConfigByCategory,
                                 String sheetName) {
        if (eachRowData == null || eachRowData.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> eachColumnData : eachRowData.entrySet()) {
            String gategory = getSysConfig(eachColumnData.getKey(),eachRowData);
            Map<String, String> mapSysConfig = mapSysConfigByCategory.get(gategory);
            String res = customSettings(eachColumnData, mapSysConfig);
            if (StringUtils.isNotBlank(res)) {
                throw new InkelinkException("导入Sheet[" + sheetName + "]数据失败,失败原因:列名为[" + eachColumnData.getKey() + "]包含的值为[" + res + "]不存在" +
                        "或没有在系统配置里配置");
            }
            if (canBeNullOrEmpty(eachColumnData) && StringUtils.isNotBlank(gategory) && mapSysConfig != null) {
                if (!mapSysConfig.containsKey(eachColumnData.getValue())) {
                    //如果是数字说明不需要转化
                    try {
                        Integer.valueOf(eachColumnData.getValue());
                    } catch (Exception e) {
                        throw new InkelinkException("导入Sheet[" + sheetName + "]数据失败,失败原因:列名为[" + eachColumnData.getKey() + "]值为[" + eachColumnData.getValue() + "]不存在" +
                                "或没有在系统配置里配置");
                    }
                } else {
                    eachColumnData.setValue(mapSysConfig.get(eachColumnData.getValue()));
                }
            }
        }
    }


    protected boolean canBeNullOrEmpty(Map.Entry<String, String> eachColumnData) {
        return true;
    }

    protected String customSettings(Map.Entry<String, String> eachColumnData,
                                    Map<String, String> mapSysConfig) {
        return null;
    }

    protected String setMultiValue(Map.Entry<String, String> eachColumnData, Map<String, String> valueAndKeyMapping, char segment) {
        if (StringUtils.isBlank(eachColumnData.getValue())) {
            return null;
        }
        String[] texts = StringUtils.split(eachColumnData.getValue(), segment);
        StringJoiner sj = new StringJoiner(String.valueOf(segment));
        for (String text : texts) {
            String val = valueAndKeyMapping.get(text);
            if (StringUtils.isBlank(val)) {
                return text;
            }
            sj.add(val);
        }
        eachColumnData.setValue(sj.toString());
        return null;
    }


    /**
     * 属性首字母小写
     *
     * @param listData
     */
    private List<Map<String, String>> setLowerFirst(List<Map<String, String>> listData) {
        if (listData == null || listData.isEmpty()) {
            return Collections.emptyList();
        }
        List<Map<String, String>> listOfTargetData = new ArrayList<>(listData.size());
        for (Map<String, String> eachRow : listData) {
            Map<String, String> mapOfEachRow = new HashMap<>(eachRow.size());
            for (Map.Entry<String, String> entry : eachRow.entrySet()) {
                mapOfEachRow.put(CharSequenceUtil.lowerFirst(entry.getKey()), entry.getValue());
            }
            listOfTargetData.add(mapOfEachRow);
        }
        return listOfTargetData;
    }

    public void saveExcelData(List<T> entities) {
        insertBatch(entities, entities.size());
    }

    /**
     * @param entities
     * @param verify   是否进行验证，传false的话，基类不调beforeInsert和afterInsert，需业务端自行处理前置和后置逻辑
     */
    public void saveExcelData(List<T> entities, boolean verify) {
        insertBatch(entities, entities.size(), verify, 0);
    }

    public void setField(T entitie, String fieldName, Object fieldValue) throws Exception {
        Field field = getAllField(entitie.getClass(), fieldName);
        if (field != null) {
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, entitie, fieldValue);
        }
    }

    public Object getField(T entitie, String fieldName) throws IllegalAccessException {
        Field field = getAllField(entitie.getClass(), fieldName);
        if (field != null) {
            ReflectionUtils.makeAccessible(field);
            return ReflectionUtils.getField(field, entitie);
        }
        return null;
    }


    protected QueryWrapper<T> wrapperAddSorts(QueryWrapper<T> wrapper, List<SortDto> sorts) {
        if (null == sorts || sorts.size() == 0) {
            return wrapper;
        }
        TableInfo tableInfo = currentModelTableInfo();
        List<TableFieldInfo> tbFields = currentModelFieldInfos();
        for (SortDto info : sorts) {
            /** 根据属性升序排序 **/
            if (ConditionDirection.ASC == info.getDirection()) {
                String[] objects = info.getColumnName().split("\\|");
                List<String> sortColumns = new ArrayList<>();
                for (int i = 0; i < objects.length; i++) {
                    String t = objects[i];
                    String columnName = MpSqlUtils.getColumnName(tbFields, t, tableInfo);
                    if (!StringUtils.isBlank(columnName)) {
                        sortColumns.add(columnName);
                    }
                }
                if (sortColumns.size() > 0) {
                    wrapper.orderByAsc(sortColumns);
                }
            }
            /** 根据属性降序排序 **/
            else if (ConditionDirection.DESC == info.getDirection()) {
                String[] objects = info.getColumnName().split("\\|");
                List<String> sortColumns = new ArrayList<>();
                for (int i = 0; i < objects.length; i++) {
                    String t = objects[i];
                    String columnName = MpSqlUtils.getColumnName(tbFields, t, tableInfo);
                    if (!StringUtils.isBlank(columnName)) {
                        sortColumns.add(columnName);
                    }
                }
                if (sortColumns.size() > 0) {
                    wrapper.orderByDesc(sortColumns);
                }
            }
        }
        return wrapper;
    }

    private Field getAllField(Class<?> clazz, String name) {
        Field field = null;
        while (clazz != null) {
            try {
                field = clazz.getDeclaredField(name);
                break;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return field;
    }

    /**
     * 验证导入的数据
     *
     * @param datas
     * @param fieldParam
     */
    public void validImportDatas(List<Map<String, String>> datas, Map<String, String> fieldParam) {
        if (CollUtil.isEmpty(datas)) {
            throw new InkelinkException("没有需要导入的数据");
        }
        List<String> keys = datas.get(0).keySet().stream().collect(Collectors.toList());
        List<String> list = fieldParam.values().stream().collect(Collectors.toList());
        //数据中没有模版参数所有的列
        List<String> list2 = list.stream().filter(item -> keys.stream().allMatch(each -> !item.equals(each))).collect(Collectors.toList());
        if (!list2.isEmpty()) {
            throw new InkelinkException(String.format("模板不符合规范，字段（%s）在模板中不存在", StringUtils.join(list2, ",")));
        }
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> dictionary = datas.get(i);
            Map<String, String> dictionary2 = new HashMap<String, String>(dictionary.size());
            dictionary.forEach((key, value) -> {
                if (list.contains(key)) {
                    Map<String, String> exchangeMap = fieldParam.entrySet().stream().collect(Collectors.toMap(o -> o.getValue(), o -> o.getKey()));
                    String text = exchangeMap.get(key);
                    if (StringUtils.isNotBlank(text)) {
                        dictionary2.put(text, value);
                    }
                }
            });
            datas.set(i, dictionary2);
        }
    }

    /**
     * 验证导入的数据（多Sheet）
     *
     * @param datas
     * @param fieldParam
     */
    public void validImportDatas(List<List<Map<String, String>>> datas, List<Map<String, String>> fieldParam) {
        if (CollUtil.isEmpty(datas)) {
            throw new InkelinkException("没有需要导入的数据");
        }
        if (CollUtil.isEmpty(fieldParam) || datas.size() != fieldParam.size()) {
            throw new InkelinkException("导入数据列跟字段不匹配");
        }
        boolean emptyFlag = true;
        for (int i = 0; i < datas.size(); i++) {
            if (!CollUtil.isEmpty(datas.get(i))) {
                emptyFlag = false;
            }
        }
        if (emptyFlag) {
            //所有sheet都是空
            throw new InkelinkException("没有需要导入的数据");
        }
        for (int i = 0; i < datas.size(); i++) {
            if (!CollUtil.isEmpty(datas.get(i))) {
                validImportDatas(datas.get(i), fieldParam.get(i));
            }
        }
    }

    /**
     * 判断是否具有重复数据
     */
    public void validExcelDataUnique(List<Map<String, String>> datas, String key, String msg) {
        Map<String, List<Map<String, String>>> dataGps = datas.stream().filter(c -> c != null && c.size() > 0)
                .collect(Collectors.groupingBy(map -> map.get(key)));
        final String[] newMsg = {msg};
        dataGps.forEach((nkey, list) -> {
            if (list.size() > 1) {
                if (StringUtils.isBlank(newMsg[0])) {
                    newMsg[0] = "“" + key + "”列数据重复";
                } else {
                    newMsg[0] = newMsg[0].replace("{1}", key);
                }
                throw new InkelinkException(newMsg[0]);
            }
        });
    }

    /**
     * 判断是否具有重复数据(多字段校验)
     */
    public void validExcelDataUnique(List<Map<String, String>> datas, List<String> keys, String msg) {
        Map<String, List<Map<String, String>>> dataGps = datas.stream().filter(c -> c != null && c.size() > 0)
                .collect(Collectors.groupingBy(map -> {
                    String temp = "";
                    for (String k : keys) {
                        temp += map.get(k) + ",";
                    }
                    return temp.substring(0, temp.lastIndexOf(','));

                }));
        final String[] newMsg = {msg};
        dataGps.forEach((nkey, list) -> {
            if (list.size() > 1) {
                String key = String.join(",", keys);
                if (StringUtils.isBlank(newMsg[0])) {
                    newMsg[0] = "“" + key + "”列数据重复";
                } else {
                    newMsg[0] = newMsg[0].replace("{1}", key);
                }
                throw new InkelinkException(newMsg[0]);
            }
        });
    }

    /**
     * 验证EXCEL数据
     */
    protected void validExcelDataRequire(String columnName, int rowIndex, String value, String msg) {
        rowIndex++;
        if (StringUtils.isBlank(value) && StringUtils.isNotBlank(columnName)) {
            if (StringUtils.isBlank(msg)) {
                msg = "第“{0}”行，“{1}”列：不能为空"
                        .replace("{0}", String.valueOf(rowIndex)).replace("{1}", columnName);
            } else {
                msg = msg.replace("{0}", String.valueOf(rowIndex)).replace("{1}", columnName);
            }
            throw new InkelinkException(msg);
        }
    }

    /**
     * 验证EXCEL数据--最大长度
     */
    protected void validExcelDataMaxLength(String columnName, int rowIndex, String value, int length,
                                           String msg) {
        rowIndex++;
        if (!StringUtils.isBlank(value) && value.length() > length) {

            if (StringUtils.isBlank(msg)) {
                msg = "第“{0}”行，“{1}”列：最多{2}个字符".replace("{0}", String.valueOf(rowIndex)).replace("{1}", columnName)
                        .replace("{2}", String.valueOf(length));
            } else {
                msg = msg.replace("{0}", String.valueOf(rowIndex)).replace("{1}", columnName)
                        .replace("{2}", String.valueOf(length));
            }
            throw new InkelinkException(msg);
        }
    }

    /**
     * 验证EXCEL数据--最小长度
     */
    protected void validExcelDataMinLength(String columnName, int rowIndex, String value, int length,
                                           String msg) {
        rowIndex++;
        if (!StringUtils.isBlank(value)) {
            if (value.length() < length) {
                if (StringUtils.isBlank(msg)) {
                    msg = "第“{0}”行，“{1}”列：最少{2}个字符".replace("{0}", String.valueOf(rowIndex)).replace("{1}", columnName)
                            .replace("{2}", String.valueOf(length));
                } else {
                    msg = msg.replace("{0}", String.valueOf(rowIndex)).replace("{1}", columnName)
                            .replace("{2}", String.valueOf(length));
                }
                throw new InkelinkException(msg);
            }
        }
    }

    @Override
    public Map<String, String> getExcelColumnNames() {
        return excelColumnNames;
    }

    @Override
    public void setExcelColumnNames(Map<String, String> columnNames) {
        excelColumnNames = columnNames;
    }


    @Override
    public void saveChange() {
        unitOfWorkService.saveChange();
    }

    @Override
    public void clearChange() {
        unitOfWorkService.clearChange();
    }


//    @Override
//    public List<String> addTest() {
//
//        return unitOfWorkService.addTest();
//    }

}