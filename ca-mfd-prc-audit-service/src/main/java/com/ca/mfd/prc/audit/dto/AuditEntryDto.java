package com.ca.mfd.prc.audit.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author inkelink
 * @Description: AUDIT评审单实体
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Data
public class AuditEntryDto {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 评审单号
     */
    @Schema(title = "评审单号")
    private String recordNo = StringUtils.EMPTY;


    /**
     * 缺陷总数
     */
    @Schema(title = "缺陷总数")
    private Integer defectCount = 0;


    /**
     * 总扣分
     */
    @Schema(title = "总扣分")
    private Integer totalScore = 0;


    /**
     * 项目代码
     */
    @Schema(title = "项目代码")
    private String projectCode = StringUtils.EMPTY;


    /**
     * 项目名称
     */
    @Schema(title = "项目名称")
    private String projectName = StringUtils.EMPTY;
    /**
     * 唯一号
     */
    @Schema(title = "唯一号")
    private String vin = StringUtils.EMPTY;


    /**
     * 阶段编码
     */
    @Schema(title = "阶段编码")
    private String stageCode = StringUtils.EMPTY;


    /**
     * 阶段编码
     */
    @Schema(title = "阶段名称")
    private String stageName = StringUtils.EMPTY;


    /**
     * 车型
     */
    @Schema(title = "车型代码")
    private String modelCode = StringUtils.EMPTY;
    /**
     * 车型
     */
    @Schema(title = "车型名称")
    private String modelName = StringUtils.EMPTY;

    @Schema(title = "工位代码")
    private String workstationCode = StringUtils.EMPTY;

    @Schema(title = "工位名称")
    private String workstationName = StringUtils.EMPTY;
    /**
     * 产品条码;除零件外，其他为VIN
     */
    @Schema(title = "产品条码;除零件外，其他为VIN")
    private String barcode = StringUtils.EMPTY;


    /**
     * 制造日期;yyyy-MM-dd  零部件手动选择，其他：各车间上线时间
     */
    @Schema(title = "制造日期;yyyy-MM-dd  零部件手动选择，其他：各车间上线时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date manufactureDt;


    /**
     * 备注
     */
    @Schema(title = "备注")
    private String remark = StringUtils.EMPTY;


    /**
     * 状态;1、未开始 2、进行中、20暂存 90完成
     */
    @Schema(title = "状态;1、未开始 2、进行中、20暂存 90完成")
    private Integer status = 0;

    /**
     * 类别:1、整车  5 冲压
     */
    @Schema(title = "类别:1、整车  5 冲压")
    private Integer category = 1;

    /**
     * 数量：用于离散，整车没有
     */
    @Schema(title = "数量：用于离散，整车没有")
    private Integer amounts = 0;

    /**
     * 车身颜色
     */
    @Schema(title = "车身颜色")
    private String characteristic2 = StringUtils.EMPTY;
}