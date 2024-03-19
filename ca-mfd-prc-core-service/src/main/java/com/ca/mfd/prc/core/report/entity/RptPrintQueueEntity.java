package com.ca.mfd.prc.core.report.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author inkelink
 * @Description: 报表打印队列实体
 * @date 2023年09月23日
 * @变更说明 BY inkelink At 2023年09月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "报表打印队列")
@TableName("PRC_RPT_PRINT_QUEUE")
public class RptPrintQueueEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_RPT_PRINT_QUEUE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "")
    @TableField("PRC_RPT_FILE_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long prcRptFileId = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "")
    @TableField("PRC_RPT_PRINTER_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long prcRptPrinterId = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "")
    @TableField("TARGET_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long targetId = Constant.DEFAULT_ID;


    /**
     * 目标类型
     */
    @Schema(title = "目标类型")
    @TableField("TARGET_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer targetType = 0;


    /**
     * 打印代码
     */
    @Schema(title = "打印代码")
    @TableField("BIZ_CODE")
    private String bizCode = StringUtils.EMPTY;


    /**
     * 打印名称
     */
    @Schema(title = "打印名称")
    @TableField("BIZ_NAME")
    private String bizName = StringUtils.EMPTY;


    /**
     * 打印机名称
     */
    @Schema(title = "打印机名称")
    @TableField("PRINT_NAME")
    private String printName = StringUtils.EMPTY;


    /**
     * 打印机IP
     */
    @Schema(title = "打印机IP")
    @TableField("IP")
    private String ip = StringUtils.EMPTY;


    /**
     * 打印机型号
     */
    @Schema(title = "打印机型号")
    @TableField("MODEL")
    private String model = StringUtils.EMPTY;


    /**
     * 打印时间
     */
    @Schema(title = "打印时间")
    @TableField("PRINT_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date printDt = new Date();


    /**
     * 打印次数
     */
    @Schema(title = "打印次数")
    @TableField("PRINT_QTY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer printQty = 0;


    /**
     * 配置名称
     */
    @Schema(title = "配置名称")
    @TableField("DISPLAY_NAME")
    private String displayName = StringUtils.EMPTY;


    /**
     * 报表文件
     */
    @Schema(title = "报表文件")
    @TableField("PATH")
    private String path = StringUtils.EMPTY;


    /**
     * 传入参数
     */
    @Schema(title = "传入参数")
    @TableField("PARAMATERS")
    private String paramaters = StringUtils.EMPTY;


    /**
     * 状态
     */
    @Schema(title = "状态")
    @TableField("STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 0;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


    /**
     * 状态备注
     */
    @Schema(title = "状态备注")
    @TableField("STATUS_REMARK")
    private String statusRemark = StringUtils.EMPTY;


    /**
     * 打印数量
     */
    @Schema(title = "打印数量")
    @TableField("PRINT_NUMBER")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer printNumber = 0;

    /**
     * 编辑报表内容的地址
     */
   /* @Schema(title = "编辑报表内容的地址")
    @TableField(exist = false)
    private String editPath ;*/

    /**
     * 预览的报表内容
     */
    @Schema(title = "预览的报表内容")
    @TableField(exist = false)
    private String previewPath ;

    /**
     * 预览的报表内容
     */
    @Schema(title = "预览的报表内容")
    @TableField(exist = false)
    private String uParamPath;


}