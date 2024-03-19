package com.ca.mfd.prc.core.prm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.core.prm.entity.PrmPermissionEntity;
import com.ca.mfd.prc.core.prm.mapper.IPrmPermissionMapper;
import com.ca.mfd.prc.core.prm.service.IPrmPermissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 权限
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class PrmPermissionServiceImpl extends AbstractCrudServiceImpl<IPrmPermissionMapper, PrmPermissionEntity> implements IPrmPermissionService {


    private static final String cacheName = "PRC_PRM_PERMISSION";
    private static final String allpermissions = "ALL_PRC_PRM_PERMISSION";
    private static final Object lockObj = new Object();
    @Autowired
    IdentityHelper identityHelper;
    @Autowired
    private LocalCache localCache;

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(allpermissions);
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PrmPermissionEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PrmPermissionEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PrmPermissionEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PrmPermissionEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取所有的数据
     */
    @Override
    public List<PrmPermissionEntity> getAllDatas() {
        List<PrmPermissionEntity> datas = localCache.getObject(cacheName);
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

    private void validData(PrmPermissionEntity model) {
        QueryWrapper<PrmPermissionEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PrmPermissionEntity::getPermissionCode, model.getPermissionCode())
                .ne(PrmPermissionEntity::getId, model.getId());
        if (selectCount(qry) > 0) {
            throw new InkelinkException("已经存在代码为" + model.getPermissionCode() + "的权限");
        }
    }

    private List<String> getLoginPermissions() {
        List<String> permissions = identityHelper.getLoginUser().getPermissions();
        if (permissions == null) {
            permissions = new ArrayList<>();
        }
        return permissions;
    }

    /**
     * 从缓存中获取，该用户拥有的权限数据
     *
     * @return 用户拥有的权限数据
     */
    @Override
    public List<PrmPermissionEntity> getAllOwnDatas() {
        List<String> finalPermissions = getLoginPermissions();
        return getAllDatas().stream().filter(o -> finalPermissions.contains(o.getPermissionCode()))
                .collect(Collectors.toList());
    }

    /**
     * 获取首条数据
     *
     * @param id
     * @return 数据
     */
    @Override
    public PrmPermissionEntity getFirstById(Long id) {
        QueryWrapper<PrmPermissionEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PrmPermissionEntity::getId, id);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    /**
     * 获取查询数据
     *
     * @param keyword 查询值
     * @return 数据
     */
    @Override
    public List<PrmPermissionEntity> getListByKey(String keyword) {
        QueryWrapper<PrmPermissionEntity> qry = new QueryWrapper<>();
        LambdaQueryWrapper<PrmPermissionEntity> lamdQry = qry.lambda().orderByAsc(PrmPermissionEntity::getModel);
        if (!StringUtils.isBlank(keyword)) {
            lamdQry.like(PrmPermissionEntity::getPermissionCode, keyword)
                    .or(c -> c.like(PrmPermissionEntity::getPermissionName, keyword))
                    .or(c -> c.like(PrmPermissionEntity::getModel, keyword));
        }
        return selectList(qry);
    }


    /**
     * 获取类型为T的列表数据
     *
     * @param conditions 条件表达式
     * @param sorts      排序表达式
     * @param isDelete   是否逻辑删除
     * @return List<T>
     */
    @Override
    public List<PrmPermissionEntity> getData(List<ConditionDto> conditions, List<SortDto> sorts, Boolean isDelete) {
        List<PrmPermissionEntity> datas = super.getData(conditions, sorts, isDelete);
        List<String> finalPermissions = getLoginPermissions();
        return datas.stream().filter(o -> finalPermissions.contains(o.getPermissionCode()))
                .collect(Collectors.toList());

    }

    /**
     * 获取类型为T的分页数据
     *
     * @param model    条件表达式和分页信息
     * @param isDelete 是否可用, 0表示不可用，1表示可用
     * @return List<T>
     */
    @Override
    public IPage<PrmPermissionEntity> getDataByPage(PageDataDto model, Boolean isDelete) {
        //super.getDataByPage(model,isDelete);
        //--2002-09-16 Adam:权限数据不大，通过获取所有，然后内存分页，保持一致
        Page page = new Page();
        List<PrmPermissionEntity> datas = getData(model.getConditions(), model.getSorts(), isDelete);
        if (datas == null) {
            datas = new ArrayList<>();
        }
        if (model.getPageIndex() <= 0) {
            model.setPageIndex(1);
        }
        page.setSize(model.getPageSize());
        page.setCurrent(model.getPageIndex());
        page.setTotal(datas.size());
        if (datas.size() > 0) {
            int fromIndex = (model.getPageIndex() - 1) * model.getPageSize();
            if (fromIndex >= datas.size()) {
                page.setRecords(new ArrayList<>());
            } else {
                int toIndex = model.getPageIndex() * model.getPageSize() - 1;
                if (toIndex >= datas.size()) {
                    toIndex = datas.size() - 1;
                }
                List<PrmPermissionEntity> pdata = datas.subList(fromIndex, toIndex);
                if (pdata == null) {
                    pdata = new ArrayList<>();
                }
                page.setRecords(pdata);
            }
        } else {
            page.setRecords(new ArrayList<>());
        }
        return page;
    }

    @Override
    public Map<String, String> getExcelColumnNames() {
        Map<String, String> columns = new LinkedHashMap<>();
        columns.put("Code", "代码");
        columns.put("Name", "名称");
        columns.put("Model", "模块");
        columns.put("Description", "描述");
        return columns;
    }

    /**
     * 验证导入的数据
     *
     * @param datas
     * @param fieldParam
     */
    @Override
    public void validImportDatas(List<Map<String, String>> datas, Map<String, String> fieldParam) {

        super.validImportDatas(datas, fieldParam);
        validExcelDataUnique(datas, "Code", "");

        List<ConditionDto> conditions = new ArrayList<>();
        conditions.add(new ConditionDto("CODE", String.join("|", datas.stream()
                .map(o -> o.get("Code")).collect(Collectors.toList())), ConditionOper.In));
        List<String> existCodes = getData(conditions).stream().map(o -> o.getPermissionCode())
                .collect(Collectors.toList());
        if (existCodes.size() > 0) {
            throw new InkelinkException("代码在数据库中已存在：" + String.join(",", existCodes));
        }
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> data = datas.get(i);
            validExcelDataRequire(fieldParam.get("Code"), i + 1, data.get("Code"), "");
            validExcelDataMaxLength(fieldParam.get("Code"), i + 1, data.get("Code"), 50, "");
            validExcelDataRequire(fieldParam.get("Name"), i + 1, data.get("Name"), "");
            validExcelDataMaxLength(fieldParam.get("Name"), i + 1, data.get("Name"), 50, "");
            validExcelDataRequire(fieldParam.get("Model"), i + 1, data.get("Model"), "");
            validExcelDataMaxLength(fieldParam.get("Model"), i + 1, data.get("Model"), 50, "");
        }
    }
}