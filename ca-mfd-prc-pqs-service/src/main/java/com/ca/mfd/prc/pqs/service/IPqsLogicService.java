package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.model.base.dto.ComboDataDTO;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.pqs.communication.entity.MidIccApiEntity;
import com.ca.mfd.prc.pqs.communication.entity.MidIccCategoryApiEntity;
import com.ca.mfd.prc.pqs.dto.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author eric.zhou
 * @date 2023/4/17
 */
public interface IPqsLogicService {

    /**
     * QG岗查看质量门检查图片数据
     *
     * @param qualityGateId 质量门ID
     * @return 返回图片列表
     */
    ShowQgWorkplaceAomalyDataInfo showQgWorkplaceAomalyData(String qualityGateId);

    /**
     * 获取质量门色块对应的缺陷列表
     *
     * @param qualityGateBlankId 节点ID
     * @param tpsCode            tps编码
     * @return 缺陷列表
     */
    List<GetAnomalyByQualityGateBlankIdInfo> getAnomalyByQualityGateBlankId(String qualityGateBlankId, String tpsCode);

    /**
     * 缺陷活动
     *
     * @param anomalyActivity
     */
    void modifyDefectAnomalyStatus(AnomalyActivity anomalyActivity);

    /**
     * 激活缺陷
     *
     * @param info
     */
    void activeAnomaly(ActiveAnomalyInfo info);

    /**
     * 产品缺陷记录
     *
     * @param anomalyActivity
     */
    void activeAnomalyRecord(AnomalyActivity anomalyActivity);

    /**
     * 获取已激活的缺陷列表
     *
     * @param para 条件参数
     * @return 缺陷列表
     */
    PageData<ProductDefectAnomalyReponse> getVehicleDefectAnomalyList(GetDefectAnomalyRequest para);

    /**
     * 修改已激活缺陷状态
     *
     * @param dataId 数据ID
     * @param status 状态
     */
    void modifyVehicleDefectAnomalyStatus(String dataId, Integer status);

    /**
     * 添加激活缺陷备注
     *
     * @param dataId dataId
     * @param remark remark
     */
    void addVehicleDefectAnomalyRemark(String dataId, String remark);

    /**
     * 获取岗位常用缺陷
     *
     * @param workplaceId
     * @return List<GetAnomalyWpByWorkPlaceAndSnInfo>
     */
    List<GetAnomalyWpByWorkPlaceAndSnInfo> getAnomalyWpByWorkPlaceAndSn(Serializable workplaceId);

    /**
     * 根据岗位编号获取QG岗检查项列表
     *
     * @param workplaceId
     * @param model
     * @return List<ComboInfoDTO>
     */
    List<ComboInfoDTO> getQualityGateByWorkplaceId(Serializable workplaceId, String model);

    /**
     * 获取所有岗位
     *
     * @param info
     * @return
     */
    PageData<GetWorkStationListInfo> getWorkPlaceList(GetWorkPlaceListParaInfo info);

    /**
     * 根据岗位编号获取岗位配置的常用缺陷
     *
     * @param workPlaceId
     * @return
     */
    List<DefectShowInfo> getAnomalyListByWorkPlaceId(String workPlaceId);

    /**
     * 获取QG所有岗位
     *
     * @return
     */
    List<GetWorkStationListInfo> getWorkPlaceListByQg();

    /**
     * 获取QG配置数据信息
     *
     * @param qualityGateId
     * @return
     */
    ShowQgAnomalyConfigurationInfo getQgAnomalyData(String qualityGateId);

    /**
     * 根据色块编号获取色块对应的缺陷列表
     *
     * @param gateBlankId
     * @return
     */
    InitConfigurationItemInfo initConfigurationItem(String gateBlankId);

    /**
     * 提交QG配置数据信息
     *
     * @param info
     */
    void sendQgAnomalyData(SendQgAnomalyConfigurationInfo info);

    /**
     * 获取所有AVI列表
     *
     * @return
     */
    List<AviListInfo> getAviList();

    /**
     * QMS系统激活缺陷（QMS缺陷代码0000000000）
     *
     * @param tpsCode
     * @param status
     */
    void qMsActiveAnomaly(String tpsCode, int status);


    /**
     * 获取缺陷列表
     *
     * @param info
     * @return
     */
    PageData<DefectAnomalyDto> getAnomalyShowList(DefectAnomalyParaInfo info);

    /**
     * 根据色块编号获取色块对应的缺陷列表
     *
     * @param gateBlankId
     * @return
     */
    List<DefectAnomalyDto> getGateAnomalyList(Long gateBlankId);

    /*
     * -------- 后台基础配置 ----------
     */

    /**
     * 获取所有组件
     *
     * @return
     */
    List<ComboDataDTO> getAllComponent();

    /**
     * 获取所有位置
     *
     * @return
     */
    List<ComboDataDTO> getAllPosition();

    /**
     * 获取所有列表
     *
     * @return
     */
    List<ComboDataDTO> getAllDefectCode();

    /**
     * 获取缺陷数据中组件列表
     *
     * @param key
     * @return
     */
    List<ComboInfoDTO> getAnomalyComponent(String key);

    /**
     * 根据组件代码获取缺陷数据中位置列表
     *
     * @param componentCode
     * @return
     */
    List<ComboInfoDTO> getAnomalyPosition(String componentCode);

    /**
     * 获取问题等级
     *
     * @return
     */
    List<ComboDataDTO> getGradeCombo();

    /**
     * 获取责任部门
     *
     * @return
     */
    List<ComboDataDTO> getDeptCombo();

    /**
     * 获取所有缺陷库数据
     *
     * @return
     */
    List<DefectAnomalyDto> getAllAnomalyShowList();

    /**
     * 根据工位代码获取常用缺陷
     *
     * @param workstationCode
     * @return
     */
    List<DefectAnomalyDto> getAnomalyWpList(String workstationCode);

    /**
     * 根据组件代码和位置代码获取缺陷数据中分类列表
     *
     * @param componentCode
     * @param positionCode
     * @return
     */
    List<ComboInfoDTO> getAnomalyCode(String componentCode, String positionCode);

    /**
     * 获取QG配置数据信息
     *
     * @param qualityGateId
     * @return
     */
    QgGateConfigurationInfo getQGGateData(Long qualityGateId);

    /**
     * 提交QG配置数据信息
     *
     * @param info
     */
    void submitQGGateData(QgGateConfigurationInfo info);

    /**
     * 获取QG百格图配置数据信息
     *
     * @param qualityMatrikId
     * @return
     */
    QgMatrikConfigurationInfo getQGMatrikData(Long qualityMatrikId);

    /**
     * 提交百格图配置数据信息
     *
     * @param info
     */
    void submitQualityMatrikData(QgMatrikConfigurationInfo info);

    /*
     * -------- 质量门功能 ----------
     */

    /**
     * 根据工位代码获取常用缺陷
     *
     * @param workstationCode
     * @param sn
     * @return
     */
    List<DefectAnomalyDto> getAnomalyWpListBySn(String workstationCode, String sn);

    /**
     * 根据工位代码和车型获取
     *
     * @param workstationCode
     * @param model
     * @return
     */
    List<ComboInfoDTO> getQualityGateByWorkstationCode(String workstationCode, String model);

    /**
     * QG岗查看质量门检查图片数据
     *
     * @param qualityGateId
     * @return
     */
    ShowQgGateDto showQGGateData(Long qualityGateId);

    /**
     * 获取质量门色块对应的缺陷列表
     *
     * @param qualityGateBlankId 色块ID
     * @param sn                 产品唯一码
     * @return
     */
    List<DefectAnomalyDto> getGateAnomalyByGateBlankIdAndSn(Long qualityGateBlankId, String sn);

    /**
     * 根据工位代码和车型获取
     *
     * @param workstationCode 工位
     * @param model           车型
     * @return
     */
    List<ComboInfoDTO> getQualityMatrikByWorkstationCode(String workstationCode, String model);

    /**
     * QG岗查看质量门检查图片数据
     *
     * @param qualityMatrikId 百格图ID
     * @param sn
     * @return
     */
    ShowQgMatrikDto showQgMatrikDataBySn(Long qualityMatrikId, String sn);

    /**
     * QG岗查围堵缺陷信息
     *
     * @param sn 唯一码
     * @return
     */
    List<QgRiskDto> showRiskDataBySn(String sn);

    /**
     * 风险管理
     *
     * @param info
     */
    void riskManager(QgRiskOperInfo info);

    /**
     * QG必检项清单
     *
     * @param workstationCode 工位代码
     * @param sn              唯一码
     * @return
     */
    List<ShowQgCheckListDto> showQgCheckListBySn(String workstationCode, String sn);

    /**
     * 提交qg必检项结果
     *
     * @param info 检查项清单
     */
    void submitQgCheckItemResult(SubmitCheckItemInfo info);

    /**
     * 激活缺陷
     *
     * @param info
     */
    void activeAnomaly_(ActiveAnomalyInfo info);

    /**
     * 修改缺陷等级、责任班组、责任部门信息
     *
     * @param info
     */
    void modifyDefectAnomalyRepsponsibelInfo(ModifyDefectResponsibleInfo info);

    void saveChange();

    /**
     * 获取缺陷日志
     *
     * @param anomalyId
     * @return
     */
    List<ProductAnomalyLogDto> getProductDefectAnomalyLog(Long anomalyId);

    /**
     * 添加激活缺陷备注
     *
     * @param dataId
     * @param remark
     */
    void appendDefectAnomalyRemark(Long dataId, String remark);

    /**
     * @param workstationCode 工位代码
     * @param sn              唯一码
     * @return
     */
    List<QgGateVarificationDto> getQgGateVarificationsList(String workstationCode, String sn);

    /**
     * 获取质量门阻塞信息
     *
     * @param workstationCode 工位代码
     * @param sn              唯一码
     * @return
     */
    QgGateVarificationResultDto getQgGateVarificationsResult(String workstationCode, String sn);

    /*
     * ----------------- 车辆去向指定 ----------------------
     */

    /**
     * 获取车辆去向
     *
     * @param sn 唯一码
     * @return
     */
    List<PqsRouteDto> getUnHandleRouteInfo(String sn);

    /**
     * 完成车辆去向处理
     *
     * @param dataId 路由记录ID
     */
    void setRouteHand(String dataId);

    /**
     * 删除路由去向
     *
     * @param routeDeleteRequest 路由记录ID,sn
     */
    void deleteRouteRecord(QgCheckItemInfo routeDeleteRequest);

    /**
     * 获取车辆去向路由点位
     *
     * @param workstationCode
     * @param sn
     * @return
     */
    List<ProductRoutePointDto> getProductRoutePoint(String workstationCode, String sn);

    /**
     * 设置车辆去向
     *
     * @param qgSetProductRouteInfo 车辆去向请求
     */
    void setProductRoute(QgSetProductRouteInfo qgSetProductRouteInfo);

    /**
     * 工艺完成关闭缺陷
     *
     * @param woId 工艺ID
     */
    void closeAnomlytByWoId(Long woId);

    /**
     * 接收外部接口的缺陷库数据
     *
     * @param dtos
     */
    void receiveIccData(List<MidIccApiEntity> dtos);

    /**
     * 接收外部接口缺陷等级数据
     *
     * @param dtos
     */
    void receiveIccCategaryData(List<MidIccCategoryApiEntity> dtos);


    /**
     * 设置描述
     *
     * @param defectDescriptionDto 设置描述
     */
    void setDefectDescription(DefectDescriptionDto defectDescriptionDto);
}
