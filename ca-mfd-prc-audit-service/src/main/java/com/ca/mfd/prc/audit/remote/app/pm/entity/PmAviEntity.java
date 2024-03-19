package com.ca.mfd.prc.audit.remote.app.pm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author inkelink
 * @Description: AVI站点实体
 * @date 2023年08月28日
 * @变更说明 BY inkelink At 2023年08月28日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "AVI站点")
@TableName("PRC_PM_AVI")
public class PmAviEntity extends PmBaseEntity {

    /**
     *
     */
    @Schema(title = "")
    @TableId(value = "PRC_PM_AVI_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "")
    @TableField("PRC_PM_WORKSHOP_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmWorkshopId = Constant.DEFAULT_ID;


    /**
     *
     */
    @Schema(title = "")
    @TableField("PRC_PM_LINE_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPmLineId = Constant.DEFAULT_ID;


    /**
     * 代码
     */
    @Schema(title = "代码")
    @TableField("AVI_CODE")
    @NotBlank(message = "AVI代码不能为空")
    private String aviCode = StringUtils.EMPTY;


    /**
     * 名称
     */
    @Schema(title = "名称")
    @TableField("AVI_NAME")
    @NotBlank(message = "AVI名称不能为空")
    private String aviName = StringUtils.EMPTY;


    /**
     * 类型（1.上线点;2.下线点;3. CUT IN/CUT OUT;）
     */
    @Schema(title = "类型（1.正常;2.上线;3. 下线;4.开工；5.完工）")
    @TableField("AVI_TYPE")
    @NotBlank(message = "AVI类型不能为空")
    private String aviType = StringUtils.EMPTY;


    /**
     * 特性  (1.实际站点;2.虚拟站点)
     */
    @Schema(title = "特性  (1.实际站点;2.虚拟站点)")
    @TableField("AVI_ATTRIBUTE")
    @NotBlank(message = "AVI特性不能为空")
    private Integer aviAttribute = 0;


    /**
     * IP地址
     */
    @Schema(title = "IP地址")
    @TableField("IP_ADDRESS")
    private String ipAddress = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


    /**
     * 是否导入
     */
    @Schema(title = "是否导入")
    @TableField("IS_ENABLE")
    private Boolean isEnable = false;


    /**
     * 序号
     */
    @Schema(title = "序号")
    @TableField("AVI_DISPLAY_NO")
    @NotNull(message = "AVI序号不能为空")
    private Integer aviDisplayNo = 0;


    /**
     * 是否关键点
     */
    @Schema(title = "是否关键点")
    @TableField("IS_MAIN")
    private Boolean isMain = false;


    /**
     * AVI功能
     */
    @Schema(title = "AVI功能")
    @TableField("AVI_FUNCTIONS")
    @NotBlank(message = "AVI功能不能为空")
    private String aviFunctions = StringUtils.EMPTY;


    /**
     *
     */
    @Schema(title = "")
    @TableField("STATION_CODE")
    private String stationCode = StringUtils.EMPTY;


    /**
     * PLC链接对象
     */
    @Schema(title = "PLC链接对象")
    @TableField("OPC_CONNECTOR")
    private String opcConnector = StringUtils.EMPTY;


    /**
     * 站点DB
     */
    @Schema(title = "站点DB")
    @TableField("POINT_DB")
    private String pointDb = StringUtils.EMPTY;


    /**
     * 备用DB3
     */
    @Schema(title = "备用DB3")
    @TableField("DB3")
    private String db3 = StringUtils.EMPTY;


    /**
     * 备用DB4
     */
    @Schema(title = "备用DB4")
    @TableField("DB4")
    private String db4 = StringUtils.EMPTY;


    /**
     * AVI默认访问页面
     */
    @Schema(title = "AVI默认访问页面")
    @TableField("DEFAULT_PAGE")
    @NotBlank(message = "AVI默认访问页面不能为空")
    private String defaultPage = StringUtils.EMPTY;

    /**
     * 是否同步到能力中心
     */
    @Schema(title = "是否同步到能力中心")
    @TableField("IS_SYNC")
    private Boolean isSync = Boolean.TRUE;

    @TableField(exist = false)
    @JsonIgnore
    private String workshopCode;

    @TableField(exist = false)
    @JsonIgnore
    private String lineCode;

    @TableField(exist = false)
    @JsonIgnore
    private String deleteFlag;

    @TableField(exist = false)
    @JsonIgnore
    private String mainFlag;

    @TableField(exist = false)
    @JsonIgnore
    private String enableFlag;


}