package com.ca.mfd.prc.core.prm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.InkelinkExcelUtils;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.core.prm.dto.PrmUserPermissionView;
import com.ca.mfd.prc.core.prm.entity.PrmPermissionEntity;
import com.ca.mfd.prc.core.prm.entity.PrmUserEntity;
import com.ca.mfd.prc.core.prm.entity.PrmUserPermissionEntity;
import com.ca.mfd.prc.core.prm.mapper.IPrmUserMapper;
import com.ca.mfd.prc.core.prm.mapper.IPrmUserPermissionMapper;
import com.ca.mfd.prc.core.prm.service.IPrmUserPermissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户权限关联表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class PrmUserPermissionServiceImpl extends AbstractCrudServiceImpl<IPrmUserPermissionMapper, PrmUserPermissionEntity> implements IPrmUserPermissionService {

    @Autowired
    private IPrmUserMapper prmUserDao;

    @Autowired
    private IPrmUserPermissionMapper prmUserPermissionDao;

    /**
     * 获取用户权限集合
     *
     * @param userId 用户ID
     */
    @Override
    public List<PrmUserPermissionView> getUserPermissions(Serializable userId) {
        return prmUserPermissionDao.getUserPermissions(userId);
    }

    /**
     * 获取用户无效的权限ID
     */
    @Override
    public List<String> getUserPermissionRemoves() {
        return prmUserPermissionDao.getUserPermissionRemoves();
    }

    /**
     * 获取用户权限
     *
     * @param userId       用户外键
     * @param permissionId 权限ID
     * @return 权限集合
     */
    @Override
    public PrmUserPermissionEntity getUserFirst(Serializable userId, Serializable permissionId) {
        QueryWrapper<PrmUserPermissionEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PrmUserPermissionEntity::getPrcPrmUserId, userId)
                .eq(PrmUserPermissionEntity::getPrcPrmPermissionId, permissionId);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    /**
     * 获取用户权限
     *
     * @param userId 用户外键
     * @return 权限集合
     */
    @Override
    public List<PrmPermissionEntity> getUserTemporaryPermissions(Serializable userId) {
        return prmUserDao.getUserTemporaryPermissions(userId);
    }

    /**
     * 保存用户临时权限数据
     *
     * @param datas  临时权限集合
     * @param userId 用户外键
     */
    @Override
    public void save(List<PrmUserPermissionEntity> datas, Serializable userId) {
        List<ConditionDto> conditions = new ArrayList<>();
        conditions.add(new ConditionDto("PRC_PRM_USER_ID", userId.toString(), ConditionOper.Equal));
        conditions.add(new ConditionDto("IS_DELETE", "false", ConditionOper.Equal));

        List<PrmUserPermissionEntity> existDatas = getData(conditions);
        List<PrmUserPermissionEntity> addDatas = datas.stream().filter(o ->
                        !existDatas.stream().anyMatch(p ->
                                Objects.equals(p.getPrcPrmPermissionId(), o.getPrcPrmPermissionId())))
                .collect(Collectors.toList());
        List<PrmUserPermissionEntity> updateDatas = existDatas.stream().filter(o -> datas.stream().anyMatch(p ->
                        Objects.equals(p.getPrcPrmPermissionId(), o.getPrcPrmPermissionId())))
                .collect(Collectors.toList());
        List<PrmUserPermissionEntity> delDatas = existDatas.stream().filter(o -> !datas.stream().anyMatch(p ->
                        Objects.equals(p.getPrcPrmPermissionId(), o.getPrcPrmPermissionId())))
                .collect(Collectors.toList());

        this.insertBatch(addDatas);
        for (PrmUserPermissionEntity updateData : updateDatas) {
            PrmUserPermissionEntity data = datas.stream().filter(o -> Objects.equals(o.getPrcPrmPermissionId(), updateData.getPrcPrmPermissionId()))
                    .findFirst().orElse(null);
            updateData.setRecycleDt(data.getRecycleDt());
            this.update(updateData);
        }
        List<Serializable> delIds = delDatas.stream().map(o -> o.getId()).collect(Collectors.toList());
        this.delete(delIds.toArray(new Serializable[delIds.size()]));
    }

    /**
     * 保存用户临时权限数据-新
     *
     * @param userId 权限外键
     * @param datas  临时权限集合
     */
    @Override
    public void userSave(Serializable userId, Map<String, Date> datas) {
        QueryWrapper<PrmUserPermissionEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PrmUserPermissionEntity::getPrcPrmUserId, userId);

        List<PrmUserPermissionEntity> existDatas = selectList(qry);

        for (Map.Entry<String, Date> item : datas.entrySet()) {
            if (UUIDUtils.isGuidEmpty(item.getKey())) {
                continue;
            }
            PrmUserPermissionEntity aa = existDatas.stream().filter(w -> Objects.equals(w.getPrcPrmPermissionId(), Long.valueOf(item.getKey())))
                    .findFirst().orElse(null);
            if (aa == null) {
                PrmUserPermissionEntity et = new PrmUserPermissionEntity();
                et.setPrcPrmPermissionId(Long.valueOf(item.getKey()));
                et.setPrcPrmUserId(Long.valueOf(userId.toString()));
                et.setRecycleDt(item.getValue());
                insert(et);
            } else {
                existDatas.remove(aa);
                UpdateWrapper<PrmUserPermissionEntity> upset = new UpdateWrapper<>();
                upset.lambda().set(PrmUserPermissionEntity::getRecycleDt, item.getValue())
                        .eq(PrmUserPermissionEntity::getId, aa.getId());
                update(upset);
            }
        }
        if (existDatas.size() > 0) {
            List<Serializable> delIds = existDatas.stream().map(o -> o.getId()).collect(Collectors.toList());
            this.delete(delIds.toArray(new Serializable[0]));
        }
    }

    /**
     * 导出所有临时权限
     */
    @Override
    public void exportDatas(String fileName, HttpServletResponse response) throws IOException {
        Map<String, String> fieldParam = new HashMap<>(10);
        //TODO 国际化
        fieldParam.put("PermissionName", "权限名称");
        fieldParam.put("PermissionModel", "模块");
        fieldParam.put("RecycleDt", "回收时间");

        List<String> data = prmUserDao.getPrmUserInfos()
                .stream().map(o -> o.getCnName() + "(" + o.getJobNo() + ")")
                .collect(Collectors.toList());
        List<PrmPermissionEntity> prmpermisonData = prmUserDao.prmPermissionInfos("");
        //var excelDatas = new Dictionary<string, string>();
        List<List<Map<String, Object>>> list = new ArrayList<>();
        if (prmpermisonData != null && prmpermisonData.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                int finalNum = i;
                List<PrmPermissionEntity> dataList = prmpermisonData.stream().filter(t ->
                        StringUtils.equals(t.getUserName() + "(" + t.getUserNo() + ")", data.get(finalNum))).collect(Collectors.toList());
                //List<Dictionary<string, string>> exceldata = ExcelHelper.GetDicDatasFromModel(prmpermisonData.Where(t => t.UserName + "(" + t.UserNo + ")" == data[i]).ToList());
                List<Map<String, Object>> exceldata = InkelinkExcelUtils.getListMap(dataList);
                //DealExcelColumnNames(ref exceldata, columns);
                list.add(exceldata);
            }
        }
        InkelinkExcelUtils.exportSheets(data, fieldParam, list, fileName, response);
    }

    /**
     * ua使用 获取已经授权的用户外键集合
     *
     * @return 用户外键集合
     */
    @Override
    public List<String> getPrmPermissionUserIdInfos() {
        return prmUserDao.getPrmPermissionUserIdInfos();
    }

    /**
     * 获取已经授权的用户集合
     *
     * @return 用户集合
     */
    @Override
    public List<PrmUserEntity> getPrmUserInfos() {
        return prmUserDao.getPrmUserInfos();
    }

    /**
     * 获取用户权限集合
     *
     * @param userId 用户外键
     * @return 权限集合
     */
    @Override
    public List<PrmPermissionEntity> getPrmPermissionEntitys(Serializable userId) {
        return prmUserDao.prmPermissionInfos(userId);
    }

}