package com.ca.mfd.prc.core.report.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink
 * @Description: 报表打印机实体
 * @date 2023年09月23日
 * @变更说明 BY inkelink At 2023年09月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "报表打印机")
@TableName("PRC_RPT_PRINTER")
public class RptPrinterEntity extends BaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_RPT_PRINTER_ID", type = IdType.INPUT)
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
     * 打印数量
     */
    @Schema(title = "打印数量")
    @TableField("PRINT_NUMBER")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer printNumber = 0;


    /**
     * IP
     */
    @Schema(title = "IP")
    @TableField("IP")
    private String ip = StringUtils.EMPTY;


    /**
     * 型号
     */
    @Schema(title = "型号")
    @TableField("MODEL")
    private String model = StringUtils.EMPTY;


    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("IS_ENABLE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isEnable;


    /**
     * 打印机状态
     */
    @Schema(title = "打印机状态")
    @TableField("PRINT_STATUS")
    private String printStatus = StringUtils.EMPTY;


    /**
     * 打印类型1、DX，2、标记命令
     */
    @Schema(title = "打印类型1、DX，2、标记命令")
    @TableField("TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer type = 0;


    /**
     * 打印内容
     */
    @Schema(title = "打印内容")
    @TableField("TPL_CONTENT")
    private String tplContent = StringUtils.EMPTY;

    /**
     * 配置名称
     */
    @Schema(title = "配置名称")
    @TableField(exist = false)
    private String displayName;
}