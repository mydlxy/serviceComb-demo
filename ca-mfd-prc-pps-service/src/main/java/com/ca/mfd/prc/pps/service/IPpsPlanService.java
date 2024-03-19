package com.ca.mfd.prc.pps.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.pps.entity.PpsProductProcessEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductBomEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductCharacteristicsEntity;
import com.ca.mfd.prc.pps.dto.UpdatePlanVersionsPara;
import com.ca.mfd.prc.pps.entity.PpsPlanEntity;

import java.util.Date;
import java.util.List;

/**
 * 生产计划
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
public interface IPpsPlanService extends ICrudService<PpsPlanEntity> {

    /**
     * 设置计划完成
     */
    void setPlanEnd(PpsOrderEntity orderInfo, Date passDt, PpsPlanEntity plan);

    /**
     * 创建电池分线计划
     *
     * */
    void createPackBranchPlan(String planNo,Integer planQty);

    /**
     * 完善计划信息
     *
     * @param planNo
     * @return
     */
    void setplaninfo(String planNo);
    /**
     * 获取BOM最新的版本
     *
     * @param productMaterialNo
     * @return
     */
    String getBomVersions(String productMaterialNo) ;

    /**
     * 获取特征最新的版本
     *
     * @param productMaterialNo
     * @return
     */
    String getCharacteristicsVersions(String productMaterialNo);

    /**
     * 查询工艺路径设置
     *
     * @param orderCategory
     * @return
     */
    PpsProductProcessEntity getProcess(String orderCategory);


    /**
     * 根据计划编码
     *
     * @param planNo 计划编号
     * @return PpsPlanEntity
     */
    PpsPlanEntity getFirstByPlanNo(String planNo);

    /**
     * 没有锁定的计划(排除预计上线时间为null)
     *
     * @param pageIndex
     * @param pageSize
     * @param orderCategory
     * @return
     */
    IPage<PpsPlanEntity> getNoLockWith(int pageIndex, int pageSize, String orderCategory);

    /**
     * 计划锁定
     *
     * @param planNos  计划
     * @param lockType 1 自动 2 手动
     */
    Boolean planLock(List<String> planNos, int lockType);

    /**
     * 获取计划的bom数据
     *
     * @param planNo 计划号
     * @return BOM数据集合
     */
    List<PmProductBomEntity> getPlanBom(String planNo);

    /**
     * 修改计划BOM
     *
     * @param para BOM版本更新对象
     */
    void updatePlanBom(UpdatePlanVersionsPara para);


    /**
     * 修改计划特征
     *
     * @param para 版本更新对象
     */
    void updatePlanCharacteristics(UpdatePlanVersionsPara para);

    /**
     * 获取计划的特征数据
     *
     * @param planNo 计划号
     * @return 特征数据集合
     */
    List<PmProductCharacteristicsEntity> getPlanCharacteristic(String planNo);

    /**
     * 计划冻结
     *
     * @param ppsPlanIds 生产计划ID
     */
    void freeze(List<Long> ppsPlanIds);

    /**
     * 取消计划冻结
     *
     * @param ppsPlanIds 生产计划ID
     */
    void unFreeze(List<Long> ppsPlanIds);

    /**
     * 设置工艺路径
     *
     * @param planIds   生产计划ID
     * @param processId 工艺路径ID
     */
    void setProcess(List<Long> planIds, Long processId);

    /**
     * 创建备件计划（现场需要创建备件工单）
     *
     * @param materialNo   备件物料名称
     * @param materialName 备件物料号
     * @param lineCode     生产线体编码
     * @param endAvi
     * @param planQty      备件生产数量
     */
    void createSparePartsPlan(String materialNo, String materialName, String lineCode, String endAvi, int planQty);

    /**
     * 删除备件计划（手动创建的备件）
     *
     * @param ids
     */
    void deleteStamping(List<Long> ids);
}