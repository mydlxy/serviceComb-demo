package com.ca.mfd.prc.pps.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @Author: changan 202306215@any3.com
 * @description:车辆信息-汽车研究院查询
 * @date: 2024-02-20 13:17
 */
@Data
@Schema(description = "车辆信息-汽车研究院查询")
public class QcyjyVehicleInfoDTO {

    // PRODUCT_CODE 整车编码
    @Schema(title = "整车编码")
    private String productCode = StringUtils.EMPTY;

    // prc_pps_order SN 产品识别码
    @Schema(title = "车辆识别码")
    private String vin = StringUtils.EMPTY;

    // prc_pps_order PRODUCT_CODE
    // 整车编码 SC6501AAABEV.CNH3001-L+00007.GJ8中的第二段中CNH3001-L+00007
    @Schema(title = "opt")
    private String opt = StringUtils.EMPTY;

    // 订单表PRODUCT_TYPE 默认传1
    @Schema(title = "车辆分类")
    private String vclTyp = "1";

    // prc_pps_order PRODUCT_CODE
    // 整车编码 SC6501AAABEV.CNH3001-L+00007.GJ8中的第一段中的车辆型号SC6501AAABEV
    @Schema(title = "车型")
    private String vclMdl = StringUtils.EMPTY;

    // prc_pps_order表的最后更新时间
    @Schema(title = "清除基准日")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date delDt;

    // prc_pps_order ORDER_SIGN 555
    @Schema(title = "定制编码")
    private String customCd = StringUtils.EMPTY;

    // 出厂日期(总装下线日期) prc_pps_order ACTUAL_END_DT 实际下下时间
    @Schema(title = "制造日期")
    private String prdDt = StringUtils.EMPTY;

    // 车辆外观颜色中文名称 quc_qps_cer_print_queue  BODY_COLOR_DESC 闪光冰晶白
    @Schema(title = "颜色名")
    private String colName = StringUtils.EMPTY;

    // quc_qps_announcement_cer  RLZL 燃料类型
    @Schema(title = "燃油类型")
    private String gasType = StringUtils.EMPTY;

    // quc_qps_announcement_cer  FDJLX 发动机类型
    @Schema(title = "发动机类型代码")
    private String engineName = StringUtils.EMPTY;

    // quc_qps_announcement_cer WKCCC 外廓尺寸长
    @Schema(title = "外廓尺寸长")
    private String bodyLength = StringUtils.EMPTY;

    // quc_qps_announcement_cer WKCCK 外廓尺寸宽
    @Schema(title = "外廓尺寸宽")
    private String bodyWidth = StringUtils.EMPTY;

    // quc_qps_announcement_cer WKCCG 外廓尺寸高
    @Schema(title = "外廓尺寸高")
    private String bodyHeight = StringUtils.EMPTY;

    // quc_qps_announcement_cer ZS 轴数
    @Schema(title = "轴数")
    private String axisCount = StringUtils.EMPTY;

    // quc_qps_announcement_cer ZJ 轴距(mm)
    @Schema(title = "轴距")
    private String wheelBase = StringUtils.EMPTY;

    // quc_qps_announcement_cer LTS 轮胎数
    @Schema(title = "轮胎数")
    private String tires = StringUtils.EMPTY;

    // quc_qps_announcement_cer LJQ 轮距（前)
    @Schema(title = "轮距（前）")
    private String treadf = StringUtils.EMPTY;

    // quc_qps_announcement_cer LJH 轮距（后)
    @Schema(title = "轮距（后）")
    private String treadr = StringUtils.EMPTY;

    // quc_qps_announcement_cer ZZL 总质量
    @Schema(title = "总质量")
    private String weight = StringUtils.EMPTY;

    // quc_qps_announcement_cer EDZK 额定载客
    @Schema(title = "定额载客")
    private String capacity = StringUtils.EMPTY;

    // quc_qps_announcement_cer ZDXTABSZT 制动系统ABS状态
    @Schema(title = "制动系统ABS状态")
    private String absStatus = StringUtils.EMPTY;

    // prc_pps_order MODEL 型号 CD701EV
    @Schema(title = "车辆系列")
    private String vclSer = StringUtils.EMPTY;

    // quc_qps_cer_print_queue ENGINE_NO 发动机号
    @Schema(title = "发动机号")
    private String engineType = StringUtils.EMPTY;

    // quc_qps_cer_upload  CERTIFICATE_NO 完整合格证编号
    @Schema(title = "合格证号")
    private String certificateNum = StringUtils.EMPTY;

}
