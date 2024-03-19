package com.ca.mfd.prc.core.prm.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.InkelinkExcelUtils;
import com.ca.mfd.prc.core.prm.entity.PrmPermissionEntity;
import com.ca.mfd.prc.core.prm.entity.PrmRoleEntity;
import com.ca.mfd.prc.core.prm.entity.PrmRolePermissionEntity;
import com.ca.mfd.prc.core.prm.entity.PrmUserEntity;
import com.ca.mfd.prc.core.prm.mapper.IPrmRoleMapper;
import com.ca.mfd.prc.core.prm.mapper.IPrmUserMapper;
import com.ca.mfd.prc.core.prm.service.IPrmRolePermissionService;
import com.ca.mfd.prc.core.prm.service.IPrmRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 角色表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class PrmRoleServiceImpl extends AbstractCrudServiceImpl<IPrmRoleMapper, PrmRoleEntity> implements IPrmRoleService {

    @Autowired
    IPrmRolePermissionService prmRolePermissionService;
    @Autowired
    IPrmUserMapper prmUserDao;
    @Autowired
    private IPrmRoleMapper prmRoleDao;

    @Override
    public void beforeInsert(PrmRoleEntity model) {
        validData(model);
    }

    @Override
    public void beforeUpdate(PrmRoleEntity model) {
        validData(model);
    }

    @Override
    public void afterInsert(PrmRoleEntity model) {
        savePermissions(model);
    }

    @Override
    public void afterUpdate(PrmRoleEntity model) {
        savePermissions(model);
    }

    private void validData(PrmRoleEntity model) {
        // model.Code = string.Empty;
        validDataUnique(model.getId(), "ROLE_NAME", model.getRoleName(), "已经存在名称为[" + model.getRoleName() + "]的数据", "", "");
    }


    private void savePermissions(PrmRoleEntity model) {
        if (model.getPermissions() == null) {
            model.setPermissions(new ArrayList<>());
        }

        List<ConditionDto> conditions = new ArrayList();
        conditions.add(new ConditionDto("PRC_PRM_ROLE_ID", model.getId().toString(), ConditionOper.Equal));
        prmRolePermissionService.delete(conditions, false);
        for (PrmPermissionEntity permission : model.getPermissions()) {
            PrmRolePermissionEntity et = new PrmRolePermissionEntity();
            et.setId(IdGenerator.getId());
            et.setPrcPrmPermissionId(permission.getId());
            et.setPrcPrmRoleId(model.getId());
            prmRolePermissionService.insert(et);
        }
    }

    /**
     * 获取查询数据
     *
     * @param keyword 查询值
     * @return 数据
     */
    @Override
    public List<PrmRoleEntity> getListByKey(String keyword) {
        QueryWrapper<PrmRoleEntity> qry = new QueryWrapper<>();
        LambdaQueryWrapper<PrmRoleEntity> lamdQry = qry.lambda().orderByAsc(PrmRoleEntity::getGroupName);
        if (!StringUtils.isBlank(keyword)) {
            lamdQry.like(PrmRoleEntity::getRoleCode, keyword)
                    .or(c -> c.like(PrmRoleEntity::getRoleName, keyword))
                    .or(c -> c.like(PrmRoleEntity::getGroupName, keyword));
        }
        return selectList(qry);
    }


    /**
     * 查询所有权限
     *
     * @return 权限集合
     */
    @Override
    public List<PrmPermissionEntity> getRolePermisions() {
        return prmRoleDao.getRolePermisions();
    }

    /**
     * 获取角色权限
     *
     * @param roleId
     * @return 权限集合
     */
    @Override
    public List<PrmPermissionEntity> getPermissons(Serializable roleId) {
        return prmRoleDao.getPermissions(roleId);
    }

    /**
     * 基于角色保存权限数据
     *
     * @param role  角色
     * @param datas 权限
     */
    @Override
    public void roleSave(PrmRoleEntity role, List<Serializable> datas) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        datas = datas.stream().distinct().collect(Collectors.toList());
        if (role.getId() == null || role.getId() <= 0) {
            role.setId(IdGenerator.getId());
            insert(role);
        } else {
            UpdateWrapper<PrmRoleEntity> upset = new UpdateWrapper<>();
            upset.lambda().set(PrmRoleEntity::getRoleCode, role.getRoleCode())
                    .set(PrmRoleEntity::getRoleName, role.getRoleName())
                    .set(PrmRoleEntity::getRemark, role.getRemark())
                    .set(PrmRoleEntity::getGroupName, role.getGroupName())
                    .eq(PrmRoleEntity::getId, role.getId());
            update(upset);
        }
        List<PrmRolePermissionEntity> existDatas = prmRolePermissionService.getByPrmRoleId(role.getId());
        for (Serializable item : datas) {
            PrmRolePermissionEntity aa = existDatas.stream().filter(w -> Objects.equals(w.getPrcPrmPermissionId(), Long.valueOf(item.toString())))
                    .findFirst().orElse(null);
            if (aa == null) {
                PrmRolePermissionEntity et = new PrmRolePermissionEntity();
                et.setPrcPrmPermissionId(Long.valueOf(item.toString()));
                et.setPrcPrmRoleId(role.getId());
                prmRolePermissionService.insert(et);
            } else {
                existDatas.remove(aa);
            }
        }

        if (existDatas.size() > 0) {
            List<Serializable> delIds = existDatas.stream().map(o -> o.getId()).collect(Collectors.toList());
            prmRolePermissionService.delete(delIds.toArray(new Serializable[delIds.size()]));
        }
    }

    /**
     * 获取绑定了该角色的用户
     *
     * @param roleId 角色外键
     * @return 绑定了该角色的用户集合
     */
    @Override
    public List<PrmUserEntity> getUsers(Serializable roleId) {
        List<PrmUserEntity> list = new ArrayList<>();
        List<PrmUserEntity> query = prmUserDao.getUsers(roleId);
        if (CollectionUtil.isNotEmpty(query)) {
            list.addAll(query);
        }
        List<PrmUserEntity> userTempPermissionQuery = prmUserDao.userTempPermissionQuery();
        if (CollectionUtil.isNotEmpty(userTempPermissionQuery)) {
            list.addAll(userTempPermissionQuery);
        }
        List<PrmUserEntity> returnList = list.stream().collect(Collectors.toMap(PrmUserEntity::getId, Function.identity(), (a, b) -> a)).values().stream().collect(Collectors.toList());
        returnList = returnList.stream().filter(u -> !u.getUserName().equals(Constant.SYSTEM_MANAGER)).collect(Collectors.toList());
        return returnList;
    }


    /**
     * 导出绑定了该角色的用户
     *
     * @param roleId
     * @param fileName
     * @param response
     */
    @Override
    public void exportPermesion(Serializable roleId, String fileName, HttpServletResponse response) throws IOException {
        Map<String, String> columns = new LinkedHashMap<>();
        columns.put("Model", "模块名称");
        columns.put("Name", "权限名称");
        columns.put("Description", "权限描述");

        List<PrmPermissionEntity> data = prmRoleDao.getPermissions(roleId);
        List<Map<String, Object>> list = InkelinkExcelUtils.getListMap(data);
        dealExcelDatas(list);
        InkelinkExcelUtils.exportByDc(columns, list, fileName, response);

    }


    /**
     * 导出绑定了该角色的用户
     *
     * @param roleId
     * @param fileName
     * @param response
     */
    @Override
    public void exportUsers(Serializable roleId, String fileName, HttpServletResponse response) throws IOException {
        Map<String, String> columns = new LinkedHashMap<>();
        columns.put("No", "工号");
        columns.put("CnName", "中文名称");
        columns.put("EnName", "英文名称");
        columns.put("CnGroupName", "组名");

        List<PrmUserEntity> data = prmUserDao.getUsers(roleId);
        List<Map<String, Object>> list = InkelinkExcelUtils.getListMap(data);
        dealExcelDatas(list);
        InkelinkExcelUtils.exportByDc(columns, list, fileName, response);

    }

    /**
     * 导出所有角色下的所有用户
     *
     * @param fileName
     * @param response
     */
    @Override
    public void exportAllUsersByRole(String fileName, HttpServletResponse response) throws IOException {
        Map<String, String> columns = new LinkedHashMap<>();
        columns.put("No", "工号");
        columns.put("CnName", "中文名称");
        columns.put("EnName", "英文名称");
        columns.put("CnGroupName", "组名");

        List<PrmUserEntity> data = prmUserDao.getRoleUsers();
        List<String> roleArray = getData(null).stream().map(o ->
                o.getGroupName() + "(" + o.getRoleName() + ")").distinct().collect(Collectors.toList());


        List<List<Map<String, Object>>> list = new ArrayList<>();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < roleArray.size(); i++) {
                int finalNum = i;
                List<PrmUserEntity> dataList = data.stream().filter(t ->
                        StringUtils.equals(t.getRoleGroupName() + "(" + t.getRoleName() + ")", roleArray.get(finalNum))).collect(Collectors.toList());
                List<Map<String, Object>> exceldata = InkelinkExcelUtils.getListMap(dataList);

                list.add(exceldata);
            }
        }
        InkelinkExcelUtils.exportSheets(roleArray, columns, list, fileName, response);

    }

    /**
     * 导出所有权限 根据角色分组
     *
     * @param fileName
     * @param response
     */
    @Override
    public void exportAllPermesionByRole(String fileName, HttpServletResponse response) throws IOException {
        Map<String, String> columns = new LinkedHashMap<>();
        columns.put("Model", "模块名称");
        columns.put("Name", "权限名称");
        columns.put("Description", "权限描述");

        List<PrmPermissionEntity> data = prmRoleDao.getRolePermisions();

        List<String> roleArray = getData(null).stream().map(o ->
                o.getGroupName() + "(" + o.getRoleName() + ")").distinct().collect(Collectors.toList());

        List<List<Map<String, Object>>> list = new ArrayList<>();
        if (data != null && data.size() > 0) {
            for (int i = 0; i < roleArray.size(); i++) {
                int finalNum = i;
                List<PrmPermissionEntity> dataList = data.stream().filter(t ->
                        StringUtils.equals(t.getGroupName() + "(" + t.getRoleName() + ")", roleArray.get(finalNum))).collect(Collectors.toList());
                List<Map<String, Object>> exceldata = InkelinkExcelUtils.getListMap(dataList);

                list.add(exceldata);
            }
        }
        InkelinkExcelUtils.exportSheets(roleArray, columns, list, fileName, response);

    }
}