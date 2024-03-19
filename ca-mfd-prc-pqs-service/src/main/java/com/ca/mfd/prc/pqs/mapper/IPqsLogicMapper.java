package com.ca.mfd.prc.pqs.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.pqs.dto.DefectAnomalyDto;
import com.ca.mfd.prc.pqs.dto.DefectAnomalyParaInfo;
import com.ca.mfd.prc.pqs.dto.DefectShowInfo;
import com.ca.mfd.prc.pqs.dto.GetAnomalyWpByWorkPlaceAndSnInfo;
import com.ca.mfd.prc.pqs.dto.GetGateAnomalyListInfo;
import com.ca.mfd.prc.pqs.dto.QgGateVarificationDto;
import com.ca.mfd.prc.pqs.dto.ShowQgCheckListDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * QG质量门
 *
 * @author eric.zhou
 * @since 1.0.0 2023-08-14
 */
@Mapper
public interface IPqsLogicMapper {

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
     * 根据岗位编号获取岗位配置的常用缺陷
     *
     * @param workplaceId
     * @return
     */
    List<DefectShowInfo> getAnomalyListByWorkPlaceId(String workplaceId);

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
     * @param key
     * @return
     */
    List<ComboInfoDTO> getAnomalyPosition(String key);

    /**
     * 根据组件代码和位置代码获取缺陷数据中分类列表
     *
     * @param componentCode
     * @param positionCode
     * @return
     */
    List<ComboInfoDTO> getAnomalyCode(@Param("componentCode") String componentCode, @Param("positionCode") String positionCode);

    /**
     * getGateAnomalyListInfo
     *
     * @param gateBlankId
     * @return
     */
    List<GetGateAnomalyListInfo> getGateAnomalyListInfo(String gateBlankId);

    List<DefectAnomalyDto> getDefectAnomalyWpListInfo(@Param("workstationCode") String workstationCode);

    /**
     * 根据工位代码和车型获取
     *
     * @param workstationCode
     * @param model
     * @return
     */
    List<ComboInfoDTO> getQualityGateByWorkstationCode(@Param("workstationCode") String workstationCode, @Param("model") String model);

    /**
     * 获取质量门色块对应的缺陷列表
     *
     * @param qualityGateBlankId
     * @param sn
     * @return
     */
    List<DefectAnomalyDto> getGateAnomalyByGateBlankIdAndSn(@Param("qualityGateBlankId") Long qualityGateBlankId, @Param("sn") String sn);

    /**
     * 根据工位代码和车型获取百格图
     *
     * @param workstationCode
     * @param model
     * @return
     */
    List<ComboInfoDTO> getQualityMatrikByWorkstationCode(@Param("workstationCode") String workstationCode, @Param("model") String model);

    /**
     * 获取质量门色块对应的缺陷列表
     *
     * @param qualityMatrikId
     * @param sn
     * @return
     */
    List<DefectAnomalyDto> getShowQGMatrikDataBySn(@Param("qualityMatrikId") Long qualityMatrikId, @Param("sn") String sn);

    /**
     * QG必检项清单
     *
     * @param workstationCode
     * @param sn
     * @param model
     * @return
     */
    List<ShowQgCheckListDto> getShowQgCheckListBySn(@Param("workstationCode") String workstationCode, @Param("sn") String sn, @Param("model") String model);

    List<QgGateVarificationDto> getQgGateVarificationsResult(Map<String, Object> maps);

    /**
     * 获取
     *
     * @param para
     * @return
     */
    Page<DefectAnomalyDto> getDefectAnomalyList(Page<DefectAnomalyDto> page, @Param("para") DefectAnomalyParaInfo para);
}