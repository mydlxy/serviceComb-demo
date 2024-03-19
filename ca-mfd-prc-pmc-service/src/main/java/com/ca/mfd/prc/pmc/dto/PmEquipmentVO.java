package com.ca.mfd.prc.pmc.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonAlias;
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
 * @Description: BOP实体
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Data
public class PmEquipmentVO {
    /**
     * 主键
     */
    @Schema(title = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long cmcPmEquipmentId = Constant.DEFAULT_ID;
    /**
     * 工作区域ID
     */
    @Schema(title = "工作区域ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long cmcPmAreaId = Constant.DEFAULT_ID;
    /**
     * 工作中心ID
     */
    @Schema(title = "工作中心ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long cmcPmWorkCenterId = Constant.DEFAULT_ID;
    /**
     * 工作单元ID
     */
    @Schema(title = "工作单元ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long cmcPmWorkUnitId = Constant.DEFAULT_ID;
    /**
     * 代码
     */
    @Schema(title = "代码")
    private String equipmentCode = StringUtils.EMPTY;
    /**
     * 名称
     */
    @Schema(title = "名称")
    private String equipmentName = StringUtils.EMPTY;
    /**
     * 供应商名称
     */
    @Schema(title = "供应商名称")
    private String supplierName = StringUtils.EMPTY;
    /**
     * 供应商代码
     */
    @Schema(title = "供应商代码")
    private String supplierCode = StringUtils.EMPTY;
    /**
     * 报废日期
     */
    @Schema(title = "报废日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date scrapdDate;
    /**
     * 安装位置
     */
    @Schema(title = "安装位置")
    private String address = StringUtils.EMPTY;
    /**
     * 使用部门
     */
    @Schema(title = "使用部门")
    private String useDepartment = StringUtils.EMPTY;
    /**
     * 所属班组
     */
    @Schema(title = "所属班组")
    private String useTeam = StringUtils.EMPTY;
    /**
     * 出厂编号
     */
    @Schema(title = "出厂编号")
    private String barCode = StringUtils.EMPTY;
}
