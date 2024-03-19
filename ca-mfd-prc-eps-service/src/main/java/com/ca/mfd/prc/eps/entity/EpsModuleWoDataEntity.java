package com.ca.mfd.prc.eps.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @Description: 模组工艺数据实体
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "模组工艺数据")
@TableName("PRC_EPS_MODULE_WO_DATA")
public class EpsModuleWoDataEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_MODULE_WO_DATA_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 产品编码
     */
    @Schema(title = "产品编码")
    @TableField("SN")
    private String sn = StringUtils.EMPTY;

    /**
     * 线体编号
     */
    @Schema(title = "线体编号")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;

    /**
     * 工位编号
     */
    @Schema(title = "工位编号")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;

    /**
     * 1 模组 2 小单元 3 电芯
     */
    @Schema(title = "1 模组 2 小单元 3 电芯")
    @TableField("PRODUCT_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer productType = 0;

    /**
     * 设备名称
     */
    @Schema(title = "设备名称")
    @TableField("DECVICE_NAME")
    private String decviceName = StringUtils.EMPTY;

    /**
     * 关联数据表名称
     */
    @Schema(title = "关联数据表名称")
    @TableField("DATA_TABLE_NAME")
    private String dataTableName = StringUtils.EMPTY;

    /**
     * 0 未知 1 OK 2 NG
     */
    @Schema(title = "0 未知 1 OK 2 NG")
    @TableField("RESULT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer result = 0;

    @TableField(exist = false)
    private List<EpsModuleWoDataItemEntity> items = new ArrayList<>();

}