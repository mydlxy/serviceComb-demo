package com.ca.mfd.prc.pps.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.dto.ModuleOrderListInfo;
import com.ca.mfd.prc.pps.dto.ModuleSplitDataInfo;
import com.ca.mfd.prc.pps.dto.PlanPartsSplitEntryReckonInfo;
import com.ca.mfd.prc.pps.dto.PlanPartsSplitEntryReckonPara;
import com.ca.mfd.prc.pps.entity.PpsPlanPartsEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductBomEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 生产计划-零部件服务
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
public interface IPpsPlanPartsService extends ICrudService<PpsPlanPartsEntity> {


    /**
     * 获取未拆分计划
     *
     * @param pageIndex
     * @param pageSize
     * @return 计划
     */
    IPage<PpsPlanPartsEntity> getNoLockWith(int pageIndex, int pageSize, Integer orderCategory);

    /**
     * 计划自动拆分
     *
     * @param plan 锁定计划参数模型
     * @return 计划拆分结果模型
     */
   void planPartsAutoLock(PpsPlanPartsEntity plan);

    /**
     * 开始计划
     *
     * @param entryId
     */
    void setPlanStart(Long entryId);

    /**
     * 結束计划
     *
     * @param planNo
     */
    void setPlanEnd(String planNo);

    /**
     * 发送Lms批次锁定计划(预成组)
     *
     * @param ppsPlanIds
     */
    void sendLmsModuleLockPlan( List<Long> ppsPlanIds);

    /**
     * 发送Lms批次锁定计划(批次)
     *
     * @param request
     */
    void sendLmsPartsLockPlan(List<PlanPartsSplitEntryReckonInfo> request);

    /**
     * 获取
     *
     * @param planNo
     * @return
     */
    PpsPlanPartsEntity getFirstByPlanNo(String planNo);

    /**
     * 根据顺序获取还未拆分完的订单（生产未完成,未冻结）
     *
     * @param orderCategory
     * @return
     */
    List<PpsPlanPartsEntity> getListNoCompele(Integer orderCategory);

    /**
     * 计划冻结
     *
     * @param ppsPlanIds 主键集合
     */
    void freeze(List<Long> ppsPlanIds);

    /**
     * 取消计划冻结
     *
     * @param ppsPlanIds 主键集合
     */
    void unFreeze(List<Long> ppsPlanIds);

    /**
     * 获取计划的bom数据
     *
     * @param planNo 计划号
     * @return BOM数据集合
     */
    List<PmProductBomEntity> getPlanBom(String planNo);

    /**
     * 获取预成组生产订单列表
     *
     * @param workstationCode 工位编码
     * @return 预成组生产订单列表
     */
    List<ModuleOrderListInfo> getModuleOrderList(String workstationCode);

    /**
     * 计划拆分结果模型
     *
     * @param request 锁定计划参数模型
     * @return 计划拆分结果模型
     */
    List<PlanPartsSplitEntryReckonInfo> planPartsSplitEntryReckon(PlanPartsSplitEntryReckonPara request);

    /**
     * 计划拆分工单
     *
     * @param request
     */
    void planPartsLockEntryReckon(List<PlanPartsSplitEntryReckonInfo> request);

    /**
     * 锁定模组计划
     *
     * @param ppsPlanIds
     * @return
     */
    void lockModulePlan(List<Long> ppsPlanIds);

    /**
     * 获取拆分数据描述
     *
     * @param planNo 锁定计划
     * @return 计划拆分结果模型
     */
    ModuleSplitDataInfo getModuleSplitData(String planNo);
}