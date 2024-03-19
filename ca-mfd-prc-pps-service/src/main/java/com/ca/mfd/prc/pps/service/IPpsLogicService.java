package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.pps.communication.dto.LmsLockPlanDto;
import com.ca.mfd.prc.pps.dto.AffirmModuleEntryPara;
import com.ca.mfd.prc.pps.dto.BodyVehicleDTO;
import com.ca.mfd.prc.pps.dto.DownModuleEntryInfo;
import com.ca.mfd.prc.pps.dto.ModuleReportPara;
import com.ca.mfd.prc.pps.dto.OrderEntryInfo;
import com.ca.mfd.prc.pps.entity.PpsProductProcessAviEntity;

import java.util.List;

/**
 * PpsLogic
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
public interface IPpsLogicService {

    void createBln(List<String> planos);

    /**
     * 批量复制计划履历
     *
     * @param planNos
     */
    void batchCopyPlanAvi(List<String> planNos,String planNo);

    /**
     * 批量绑定吊牌
     *
     * @param planNos
     */
    void batchBodyShopStartWork(List<String> planNos,String workstationCode);

    /**
     * 开始计划
     *
     * @param entryId
     */
    void setPlanStart(Long entryId);

    /**
     * 获取模组生产工单
     *
     * @param lineCode
     */
    DownModuleEntryInfo getModuleEntry(String lineCode);

    /**
     * 拆分模组工单
     *
     * @param planNo
     */
    void splitModuleEntry(String planNo);

    /**
     * 模组报工
     *
     * @param para
     */
    void moduleReport(ModuleReportPara para);

    /**
     * 删除工单业务
     *
     * @param entryNo
     */
    void deleteModuleEntry(String entryNo);

    /**
     * 回执下发的模组工单
     *
     * @param para
     */
    void affirmModuleEntry(AffirmModuleEntryPara para);

    /**
     * 焊装车间开工并下发(指定计划号)
     *
     * @param model
     * @param workstationCode
     * @param tagNo
     */
    OrderEntryInfo bodyShopStartWorkByPlan(String tagNo, String workstationCode, String model,String planNo);

    /**
     * 焊装车间开工并下发
     *
     * @param model
     * @param workstationCode
     * @param tagNo
     */
    OrderEntryInfo bodyShopStartWork(String tagNo, String workstationCode, String model);

    /**
     * 发送AS待开工队列
     *
     * @param vin 车辆唯一标识
     */
    void sendAsQueueStartMessage(String vin);

    /**
     * 发送Lms整车锁定计划
     *
     * @param vin 车辆唯一标识
     */
    void sendLmsVeLockPlan(String vin);

    /**
     * 发送Lms整车锁定计划
     *
     * @param info
     */
    void sendLmsLockPlan(LmsLockPlanDto info);

    /**
     * 电池车间开工下发
     *
     * @param model
     */
    OrderEntryInfo packShopStartWork(String model);

    /**
     * 获取工艺路径关联的需要验证顺序的AVI
     *
     * @param barcode
     * @return
     */
    List<PpsProductProcessAviEntity> getSequenceAviList(String barcode);

    /**
     * 获取工艺路径线体
     *
     * @param barcode
     * @return
     */
    List<PpsProductProcessAviEntity> getIsMainAviList(String barcode);

    /**
     * 判断产品物料是否存在
     *
     * @param orderId
     * @param materialNo
     * @return Boolean
     */
    Boolean existProductBomMaterialNo(Long orderId, String materialNo);

    /**
     * 线体是否在计划履历中
     *
     * */
    Integer hasPlanLine(String lineCode);
}
