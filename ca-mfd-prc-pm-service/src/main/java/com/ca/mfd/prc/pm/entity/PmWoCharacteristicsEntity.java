package com.ca.mfd.prc.pm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @Description: 工艺特征数据实体
 * @author inkelink
 * @date 2023年10月17日
 * @变更说明 BY inkelink At 2023年10月17日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "工艺特征数据")
@TableName("PRC_PM_WO_CHARACTERISTICS")
public class PmWoCharacteristicsEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PM_WO_CHARACTERISTICS_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 特征项
     */
    @Schema(title = "特征项")
    @TableField("PRC_CHARACTERISTICS_NAME")
    private String prcCharacteristicsName = StringUtils.EMPTY;


    /**
     * 特征项描述
     */
    @Schema(title = "特征项描述")
    @TableField("DESCRIPTION_CN")
    private String descriptionCn = StringUtils.EMPTY;


    /**
     * 工艺代码
     */
    @Schema(title = "工艺代码")
    @TableField("PRC_PM_WO_CODE")
    private String prcPmWoCode = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


    /**
     * 工艺名称
     */
    @Schema(title = "工艺名称")
    @TableField("PRC_PM_WO_NAME")
    private String prcPmWoName = StringUtils.EMPTY;


}