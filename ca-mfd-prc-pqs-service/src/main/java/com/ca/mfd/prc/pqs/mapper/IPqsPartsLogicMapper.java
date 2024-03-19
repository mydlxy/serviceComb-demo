package com.ca.mfd.prc.pqs.mapper;

import com.ca.mfd.prc.pqs.dto.DefectAnomalyDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * QG质量门
 *
 * @author eric.zhou
 * @since 1.0.0 2023-08-14
 */
@Mapper
public interface IPqsPartsLogicMapper {

    /**
     * 根据产品唯一号获取质量门色块对应的缺陷列表
     *
     * @param qualityGateBlankId 色块ID
     * @param sn 产品唯一号
     * @return
     */
    List<DefectAnomalyDto> getGateAnomalyByGateBlankIdAndSn(@Param("qualityGateBlankId") Long qualityGateBlankId, @Param("sn") String sn);

    /**
     * 根据评审工单号获取质量门色块对应的缺陷列表
     *
     * @param qualityGateBlankId 色块ID
     * @param inspectionNo 评审工单号
     * @return
     */
    List<DefectAnomalyDto> getGateAnomalyByGateBlankIdAndInspectionNo(@Param("qualityGateBlankId") Long qualityGateBlankId, @Param("inspectionNo") String inspectionNo);

    /**
     * QG岗根据产品唯一号查看百格图数据
     *
     * @param qualityMatrikId 百格图ID
     * @param sn 产品唯一号
     * @return
     */
    List<DefectAnomalyDto> showQGMatrikDataBySn(@Param("qualityMatrikId") Long qualityMatrikId, @Param("sn") String sn);

    /**
     * QG岗根据评审工单号查看质量门检查图片数据
     *
     * @param qualityMatrikId 百格图ID
     * @param inspectionNo 评审工单号
     * @return
     */
    List<DefectAnomalyDto> showQGMatrikDataByInspectionNo(@Param("qualityMatrikId") Long qualityMatrikId, @Param("inspectionNo") String inspectionNo);
}