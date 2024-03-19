package com.ca.mfd.prc.avi.service;

import com.ca.mfd.prc.avi.dto.AviPassedRecordDTO;
import com.ca.mfd.prc.avi.dto.SnAviTrackingRecordVO;
import com.ca.mfd.prc.avi.entity.AviTrackingRecordEntity;
import com.ca.mfd.prc.avi.enums.AviTrackingEnum;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.avi.remote.app.pps.entity.PpsOrderEntity;

import java.util.Date;
import java.util.List;

/**
 * 产品过点信息
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
public interface IAviTrackingRecordService extends ICrudService<AviTrackingRecordEntity> {

    /**
     * 第三方过点数据
     *
     * @param sn              车辆识别码
     * @param aviCode
     * @param aviType
     * @param avitrackingenum
     * @param isProcess
     * @param passTime
     */
    void saveThirdPointData(String sn, String aviCode, Integer aviType, AviTrackingEnum avitrackingenum, Boolean isProcess, Date passTime);

    /**
     * 手动过点数据（web）
     *
     * @param tpsCode 车辆识别码
     * @param aviId
     * @return Result<PpsOrderEntity>
     */
    ResultVO<PpsOrderEntity> saveManualPointWeb(String tpsCode, String aviId);

    /**
     * 保存手动过点数据
     *
     * @param tpsCode 车辆识别码
     * @param aviId
     */
    void saveManualPoint(String tpsCode, String aviId);

    /**
     * 保存手动过点数据
     *
     * @param tpsCode         车辆识别码
     * @param aviId
     * @param aviType
     * @param avitrackingenum
     */
    void saveManualPoint(String tpsCode, String aviId, int aviType, AviTrackingEnum avitrackingenum);

    /**
     * 保存Pack过点记录数据
     *
     * @param barCode 车辆识别码
     * @param aviCode
     */
    void savePackPoint(String barCode, String aviCode);

    /**
     * 保存Pack过点记录数据
     *
     * @param barCode         车辆识别码
     * @param aviCode
     * @param aviType         4 Pack过点
     * @param avitrackingenum
     */
    void savePackPoint(String barCode, String aviCode, int aviType, AviTrackingEnum avitrackingenum);

    /**
     * 保存过点数据
     *
     * @param tpsCode 车辆识别码
     * @param aviId
     */
    void savePointData(String tpsCode, String aviId);

    /**
     * 保存过点数据
     *
     * @param tpsCode         车辆识别码
     * @param aviId
     * @param aviType         4 Pack过点
     * @param avitrackingenum
     */
    void savePointData(String tpsCode, String aviId, int aviType, AviTrackingEnum avitrackingenum, boolean isProcess);

    /**
     * 整车订单
     *
     * @param pageInfo
     * @param conditions
     * @param sorts
     */
    void getAviTrackingRecordInfo(PageData<AviTrackingRecordEntity> pageInfo, List<ConditionDto> conditions, List<SortDto> sorts);

    /**
     * 整车订单
     *
     * @param conditions
     * @param sorts
     * @param page
     */
    void getVehiceOrderPageDatas(List<ConditionDto> conditions, List<SortDto> sorts, PageData<AviTrackingRecordEntity> page);

    /**
     * 获取自定车辆过点记录
     *
     * @param aviCode
     * @return List<AviPassedRecordDTO>
     */
    List<AviPassedRecordDTO> getAviPassedRecord(String aviCode);

    /**
     * 获取
     *
     * @param sn
     * @param avicode
     * @return AviTrackingRecordEntity
     */
    AviTrackingRecordEntity getTopAviPassedRecord(String sn, String avicode);

    /**
     * 获取
     *
     * @param isProcess
     * @param top
     * @return AviTrackingRecordEntity
     */
    List<AviTrackingRecordEntity> getTopAviPassedRecord(Boolean isProcess, Integer top);


    /**
     * @param conditions
     * @param sorts
     * @param pageIndex
     * @param pageSize
     * @return
     */
    PageData<AviTrackingRecordEntity> getVehiceOrderPageDatas(List<ConditionDto> conditions, List<SortDto> sorts,
                                                              int pageIndex, int pageSize);

    /**
     * 根据sn / aviCode 查询列表
     *
     * @param sn      产品唯一标识
     * @param aviCode avicode
     * @return 返回列表
     */
    List<SnAviTrackingRecordVO> getEntityBySnAndAviCode(List<String> sn, List<String> aviCode);

    /**
     * 报错AS过点记录
     *
     * @param vehicleSn
     * @param aviCode
     * @param AviType
     */
    void saveAsPointData(String vehicleSn, String aviCode, int AviType);
}