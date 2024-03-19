package com.ca.mfd.prc.core.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.utils.ArraysUtils;
import com.ca.mfd.prc.common.utils.MpSqlUtils;
import com.ca.mfd.prc.core.main.entity.SysServiceHostManagerEntity;
import com.ca.mfd.prc.core.main.entity.SysServiceManagerEntity;
import com.ca.mfd.prc.core.main.service.ISysServiceHostManagerService;
import com.ca.mfd.prc.core.main.service.ISysServiceManagerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务寄宿管理
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class SysServiceHostManagerServiceImpl extends SysServiceHostManagerBaseImpl implements ISysServiceHostManagerService {

    private static final Map<String, String> excelColumnNames = new LinkedHashMap<>();
    @Autowired
    private ISysServiceManagerService sysServiceManagerService;

    {
        excelColumnNames.put(MpSqlUtils.getColumnName(SysServiceHostManagerEntity::getName), "服务名称");
        excelColumnNames.put(MpSqlUtils.getColumnName(SysServiceHostManagerEntity::getIp), "集群IP");
        excelColumnNames.put(MpSqlUtils.getColumnName(SysServiceHostManagerEntity::getServiceHostType), "启动类型（0多活/1单活）");
        excelColumnNames.put(MpSqlUtils.getColumnName(SysServiceHostManagerEntity::getRunPath), "程序启动路径");
        excelColumnNames.put(MpSqlUtils.getColumnName(SysServiceHostManagerEntity::getRunIp), "运行机器IP(|)分割");

        excelColumnNames.put(MpSqlUtils.getColumnName(SysServiceHostManagerEntity::getNewVersion), "版本");
        excelColumnNames.put(MpSqlUtils.getColumnName(SysServiceHostManagerEntity::getRemark), "启动排序");
        excelColumnNames.put(MpSqlUtils.getColumnName(SysServiceHostManagerEntity::getHostDescription), "描述");
    }

    @Override
    public void beforeDelete(Collection<? extends Serializable> idList) {
        List<Long> serviceManagerlist = sysServiceManagerService.getIdsByServiceIds(idList);
        if (serviceManagerlist != null && serviceManagerlist.size() > 0) {
            sysServiceManagerService.delete(serviceManagerlist.toArray(new Long[0]));
        }
    }

    @Override
    public void beforeUpdate(SysServiceHostManagerEntity entity) {
        List<SysServiceManagerEntity> serviceManagerlist = sysServiceManagerService.getByServiceId(entity.getId());
        for (SysServiceManagerEntity item : serviceManagerlist) {
            UpdateWrapper<SysServiceManagerEntity> upset = new UpdateWrapper<>();
            upset.lambda().set(SysServiceManagerEntity::getPhysicalType, entity.getServiceHostType())
                    .set(SysServiceManagerEntity::getNewVersion, entity.getNewVersion())
                    .eq(SysServiceManagerEntity::getId, entity.getId());
            sysServiceManagerService.update(upset);
        }
    }

    @Override
    public void saveExcelData(List<SysServiceHostManagerEntity> entities) {

        for (SysServiceHostManagerEntity entity : entities) {
            insert(entity);
            if (!StringUtils.isBlank(entity.getRunIp())) {
                List<String> runip = ArraysUtils.splitNoEmpty(entity.getRunIp(), "[\\|\\,\\;]");
                for (String item : runip) {
                    SysServiceManagerEntity et = new SysServiceManagerEntity();
                    et.setName(entity.getName());
                    et.setPath(entity.getRunPath());
                    et.setPhysicalType(entity.getServiceHostType());
                    et.setServiceType(0);
                    et.setServiceDescription(entity.getHostDescription());
                    et.setServiceId(entity.getId());
                    et.setIp(item);
                    et.setRemark("导入服务" + entity.getName());
                    et.setNowVersion(entity.getNewVersion());
                    et.setNewVersion(entity.getNewVersion());
                    sysServiceManagerService.insert(et);
                }
            }
        }
    }

    @Override
    public Map<String, String> getExcelColumnNames() {
        return excelColumnNames;
    }

}