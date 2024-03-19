package com.ca.mfd.prc.pqs.entity;

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
 * @Description: 追溯件阻塞实体
 * @date 2023年09月08日
 * @变更说明 BY inkelink At 2023年09月08日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "追溯件阻塞")
@TableName("PRC_PQS_PRODUCT_MM_LOCK_POLICY")
public class PqsProductMmLockPolicyEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_PRODUCT_MM_LOCK_POLICY_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 工位代码
     */
    @Schema(title = "工位代码")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


    /**
     * 工位名称
     */
    @Schema(title = "工位名称")
    @TableField("WORKSTATION_NAME")
    private String workstationName = StringUtils.EMPTY;


    /**
     * 类别;1 整车 2压铸 3机加 4冲压
     */
    @Schema(title = "类别;1 整车 2压铸 3机加 4冲压")
    @TableField("CATEGORY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer category = 0;


    /**
     * 锁定说明
     */
    @Schema(title = "锁定说明")
    @TableField("BLOCK_NOTE")
    private String blockNote = StringUtils.EMPTY;


    /**
     * 零件编号
     */
    @Schema(title = "零件编号")
    @TableField("MATERIAL_NO")
    private String materialNo = StringUtils.EMPTY;


    /**
     * 零件名称
     */
    @Schema(title = "零件名称")
    @TableField("MATERIAL_NAME")
    private String materialName = StringUtils.EMPTY;


    /**
     * 批次
     */
    @Schema(title = "批次")
    @TableField("LOT_NO")
    private String lotNo = StringUtils.EMPTY;


    /**
     * 是否启用
     */
    @Schema(title = "是否启用")
    @TableField("ENABLED")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean enabled = false;


}