package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.entity.PmEquipmentPowerEntity;
import com.ca.mfd.prc.pm.entity.PmWoEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author 阳波
 * @ClassName IPmBusinessBaseService
 * @description: 车间、线体、工位等基类型
 * @date 2023年08月24日
 * @version: 1.0
 */
public interface IPmBusinessBaseService<T extends BaseEntity> extends ICrudService<T> {
    List<T> getByShopId(Long shopId);

    /**
     * execl导出表头定义
     *
     * @return
     */
    Map<String, String> getExcelHead();

    /**
     * 发布
     *
     * @param workShopId
     * @param version
     */
    void publishByWorkShopId(Long workShopId, Integer version);

    /**
     * 插入
     * @param entity
     * @param verify
     * @return
     */
    boolean insert(T entity, boolean verify);

    /**
     *
     * @param id
     * @param version
     * @return
     */
    T get(Serializable id, Integer version);

    List<T> convertExcelDataToEntity(List<Map<String, String>> datas) throws Exception;



}
