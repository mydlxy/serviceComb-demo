package com.ca.mfd.prc.pps.communication.service;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.communication.dto.AsBatchPiecesDto;
import com.ca.mfd.prc.pps.communication.dto.AsKeepCarDto;
import com.ca.mfd.prc.pps.communication.dto.AsOrderScrapDto;
import com.ca.mfd.prc.pps.communication.dto.AsQueueStartDto;
import com.ca.mfd.prc.pps.communication.dto.LmsLockPlanDto;
import com.ca.mfd.prc.pps.communication.dto.VehicleModelDto;
import com.ca.mfd.prc.pps.communication.remote.app.pm.entity.MidColorBaseEntity;
import com.ca.mfd.prc.pps.entity.PpsAsAviPointEntity;
import com.ca.mfd.prc.pps.entity.PpsPlanEntity;
import com.ca.mfd.prc.pps.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmAviEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 接口记录表服务
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
public interface IMidApiLogService extends IMidApiLogBaseService {
    /**
     * 获取开始物料号
     */
    List<String> getProductCodeStartsWith();

    /**
     * 获取白车身的整车物料号
     */
    String getWhitProductCode();

    /**
     * 获取bom模式
     */
    Integer getBomMode(Integer bomMode);

    /**
     * 发送AS批次进度反馈
     */
    List<AsBatchPiecesDto> getAsBatchPieces(List<PpsAsAviPointEntity> list);

    /**
     * 设置 选装包、车身色，内饰色
     */
    void setVeExtendInfo(PpsPlanEntity et);

    /**
     * 根据整车物料号获取bom版本号
     *
     * @param materialNo
     * @return
     */
    String getBomVersion(Integer bomMode, String plantCode, String materialNo, String specifyDate);

    /**
     * 根据整车物料号获取特征版本号
     *
     * @param materialNo
     * @return
     */
    String getCharacteristicsVersion(Integer bomMode, String materialNo);

    /**
     * 根据零件物料号获取零件bom版本号
     *
     * @param materialNo
     * @return
     */
    String getBomPartVersion(Integer bomMode, String materialNo);

    /**
     * 获取颜色
     *
     * @param colorCode
     * @return
     */
    List<MidColorBaseEntity> getByClorCode(String colorCode);

    /**
     * 根据整车物料号获取最新车型信息(bom提供)
     *
     * @param materialNo 整车物料号
     * @return
     */
    VehicleModelDto getBomVehicleModel(String materialNo);

    /**
     * 根据零件物料号获取整车物料号(bom提供)
     *
     * @param materialNo    零件物料号
     * @param orderCategory 1整车，2零件
     * @return 整车物料号
     */
    String getProduceCodeByMaterialNo(Integer bomMode, String materialNo, String plantCode, String specifyDate, String orderCategory);


    /**
     * 发送AS订单报废
     */
    ResultVO<String> sendOrderScrap(List<AsOrderScrapDto> fbacks);

    /**
     * 发送AS保留车
     */
    ResultVO<String> sendKeepCar(List<AsKeepCarDto> fbacks);

    /**
     * 发送AS待开工队列
     */
    ResultVO<String> sendQueueStart(List<AsQueueStartDto> fbacks);

    /**
     * 发送LMS整车计划锁定
     *
     * @param fbacks 发送数据
     * @return 处理结果
     */
    ResultVO<String> sendLmsLockPlan(List<LmsLockPlanDto> fbacks);

    /**
     * 测试(发送LMS整车计划锁定)
     *
     * @param fbacks
     * @return
     */
    ResultVO<String> sendLmsLockPlanBak(List<LmsLockPlanDto> fbacks);

    /**
     * 获取TP点对应的AVI点
     *
     * @param pmall
     * @param asTpCode
     * @return
     */
    PmAviEntity getAviByAsTpCode(PmAllDTO pmall, String asTpCode);
}