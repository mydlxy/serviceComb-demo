package com.ca.mfd.prc.core.prm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.InkelinkExcelUtils;
import com.ca.mfd.prc.core.prm.entity.PrmPermissionEntity;
import com.ca.mfd.prc.core.prm.entity.PrmTokenEntity;
import com.ca.mfd.prc.core.prm.entity.PrmTokenPermissionEntity;
import com.ca.mfd.prc.core.prm.mapper.IPrmTokenMapper;
import com.ca.mfd.prc.core.prm.service.IPrmTokenPermissionService;
import com.ca.mfd.prc.core.prm.service.IPrmTokenService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 令牌表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class PrmTokenServiceImpl extends AbstractCrudServiceImpl<IPrmTokenMapper, PrmTokenEntity> implements IPrmTokenService {

    private static final Logger logger = LoggerFactory.getLogger(PrmTokenServiceImpl.class);
    private static final String cacheName = "PRC_PRM_TOKEN";
    @Autowired
    private IPrmTokenMapper prmTokenDao;
    @Autowired
    private IPrmTokenPermissionService prmTokenPermissionService;
    @Autowired
    private LocalCache localCache;

    @Override
    public void beforeInsert(PrmTokenEntity model) {
        validData(model);
    }

    @Override
    public void beforeUpdate(PrmTokenEntity model) {
        validData(model);
    }

    private void validData(PrmTokenEntity model) {
        validDataUnique(model.getId(), "TOKEN_NAME", model.getTokenName(), "已经存在名称为%s的数据", "", "");
    }

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PrmTokenEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PrmTokenEntity model) {
        removeCache();
        savePermissions(model);
    }

    @Override
    public void afterUpdate(Wrapper<PrmTokenEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void afterUpdate(PrmTokenEntity model) {
        removeCache();
        savePermissions(model);
    }

    /**
     * 获取所有的数据
     */
    @Override
    public List<PrmTokenEntity> getAllDatas() {
        try {
            Function<Object, ? extends List<PrmTokenEntity>> getDataFunc = (obj) -> {
                List<PrmTokenEntity> lst = getData(null);
                if (lst == null || lst.size() == 0) {
                    return new ArrayList<>();
                }
                return lst;
            };
            List<PrmTokenEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
            if (caches == null) {
                return new ArrayList<>();
            }
            return caches;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * 保存令牌权限
     *
     * @param model 待保存的模型
     */
    private void savePermissions(PrmTokenEntity model) {
        if (model.getPermissions() == null) {
            model.setPermissions(new ArrayList<>());
        }

        List<ConditionDto> conditions = new ArrayList<>();
        conditions.add(new ConditionDto("PRC_PRM_TOKEN_ID", model.getId().toString(), ConditionOper.Equal));

        prmTokenPermissionService.delete(conditions, false);
        for (PrmPermissionEntity permission : model.getPermissions()) {
            PrmTokenPermissionEntity et = new PrmTokenPermissionEntity();
            et.setId(IdGenerator.getId());
            et.setPrcPrmPermissionId(permission.getId());
            et.setPrcPrmTokenId(model.getId());
            prmTokenPermissionService.insert(et);
        }
    }

    /**
     * 获取令牌下面的权限
     *
     * @param tokenId 令牌外键
     * @return 权限集合
     */
    @Override
    public List<PrmPermissionEntity> getPermissons(String tokenId) {
        return prmTokenDao.getPermissions(tokenId);
    }

    /**
     * 查询所有的token权限
     *
     * @return 权限集合
     */
    @Override
    public List<PrmPermissionEntity> getTokenPermisions() {
        return prmTokenDao.getTokenPermisions();
    }

    /**
     * 获取查询数据
     *
     * @param keyword 查询值
     * @return 数据
     */
    @Override
    public List<PrmTokenEntity> getListByKey(String keyword) {
        QueryWrapper<PrmTokenEntity> qry = new QueryWrapper<>();
        LambdaQueryWrapper<PrmTokenEntity> lamdQry = qry.lambda().orderByAsc(PrmTokenEntity::getGroupName);
        if (!StringUtils.isBlank(keyword)) {
            lamdQry.like(PrmTokenEntity::getGroupName, keyword)
                    .or(c -> c.like(PrmTokenEntity::getTokenName, keyword));
        }
        return selectList(qry);
    }


    /**
     * 基于token外键导出权限
     *
     * @param tokenId  令牌外键
     * @param fileName
     * @param response
     */
    @Override
    public void exportPermesion(String tokenId, String fileName, HttpServletResponse response) throws IOException {
        Map<String, String> columns = new LinkedHashMap<>();
        columns.put("Model", "模块名称");
        columns.put("Name", "权限名称");
        columns.put("Description", "权限描述");

        List<PrmPermissionEntity> data = prmTokenDao.getPermissions(tokenId);
        List<Map<String, Object>> list = InkelinkExcelUtils.getListMap(data);
        dealExcelDatas(list);
        InkelinkExcelUtils.exportByDc(columns, list, fileName, response);
    }

    /**
     * 导出所有权限 根据角色分组
     *
     * @param fileName
     * @param response
     */
    @Override
    public void exportAllPermesionByToken(String fileName, HttpServletResponse response) throws IOException {
        Map<String, String> columns = new LinkedHashMap<>();
        columns.put("Model", "模块名称");
        columns.put("Name", "权限名称");
        columns.put("Description", "权限描述");

        List<PrmPermissionEntity> data = prmTokenDao.getTokenPermisions();
        List<String> tokenArray = getData(null).stream().map(o -> o.getGroupName() + "(" + o.getGroupName() + ")")
                .distinct().collect(Collectors.toList());

        List<List<Map<String, Object>>> list = new ArrayList<>();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < tokenArray.size(); i++) {
                int finalNum = i;
                List<PrmPermissionEntity> dataList = data.stream().filter(t ->
                        StringUtils.equals(t.getGroupName() + "(" + t.getRoleName() + ")", tokenArray.get(finalNum))).collect(Collectors.toList());
                List<Map<String, Object>> exceldata = InkelinkExcelUtils.getListMap(dataList);

                list.add(exceldata);
            }
        }
        InkelinkExcelUtils.exportSheets(tokenArray, columns, list, fileName, response);
    }


    @Override
    public PrmTokenEntity getTokenEntityByAppId(String appId) {
        QueryWrapper<PrmTokenEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PrmTokenEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PrmTokenEntity::getTokenName, appId);
        return this.getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }
}