package com.ca.mfd.prc.pps.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.dto.OutsourceEntryAreaDTO;
import com.ca.mfd.prc.pps.dto.SplitEntryPara;
import com.ca.mfd.prc.pps.entity.PpsEntryPartsEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 工单-零部件服务
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
public interface IPpsEntryPartsService extends ICrudService<PpsEntryPartsEntity> {

    /**
     * 没有锁定的工单
     *
     * @param status
     * @param pageIndex
     * @param pageSize
     * @param orderCategory
     * @return
     */
    IPage<PpsEntryPartsEntity> getNoLockWith(Integer status,int pageIndex, int pageSize, String orderCategory);

    /**
     * 预备生产
     *
     * @param status
     * @param entryId
     */
    void entryLock(Integer status,Long entryId);

    /**
     * 获取
     *
     * @param entryNo
     * @param orderCategory
     * @return
     */
    PpsEntryPartsEntity getFirstByEntryNoAndCategory(String entryNo, Integer orderCategory);

    /**
     * 修改工单数量
     *
     * @param entryNo
     * @param count
     * @return
     */
    void changeWorkOrderCount(String entryNo, Integer count);

    /**
     * 获取
     *
     * @param entryNo
     * @return
     */
    List<OutsourceEntryAreaDTO> getOutsourceEntryArea(String entryNo);

    /**
     * 计划冻结
     *
     * @param entryNo
     * @return
     */
    PpsEntryPartsEntity getFirstByEntryNo(String entryNo);

    /**
     * 根据计划单号或工单号查询
     *
     * @param planNoOrEntryNo
     * @return
     */
    PpsEntryPartsEntity getFirstByPlanNoOrEntryNo(String planNoOrEntryNo);

    /**
     * 计划冻结
     *
     * @param ppsPlanIds
     */
    void freeze(List<Long> ppsPlanIds);

    /**
     * 取消工单冻结
     *
     * @param ppsPlanIds
     */
    void unFreeze(List<Long> ppsPlanIds);

    /**
     * 关闭工单
     *
     * @param entryNo
     */
    void closeEntry(List<Long> entryNo);

    /**
     * 拆分工单
     *
     * @param request
     */
    void splitEntry(SplitEntryPara request);

    /**
     * 预备生产
     *
     * @param entryId
     */
    void preLock(Long entryId);

    /**
     * 开始生产
     *
     * @param id
     */
    PpsEntryPartsEntity beginProduct(Long id);

    /**
     * 统计合格数量
     *
     * @param planNo 计划编码
     * @return 统计合格数量
     */
    int getEntryPartNumByPlanNo(String planNo);
}