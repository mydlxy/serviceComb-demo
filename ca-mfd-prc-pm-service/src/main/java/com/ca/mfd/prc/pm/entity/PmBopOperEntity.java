package com.ca.mfd.prc.pm.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @Description: BOP操作实体
 * @author inkelink
 * @date 2023年11月24日
 * @变更说明 BY inkelink At 2023年11月24日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "BOP操作")
@TableName("PRC_PM_BOP_OPER")
public class PmBopOperEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PM_BOP_OPER_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 车间编码
     */
    @Schema(title = "车间编码")
    @TableField("PRC_PM_WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;


    /**
     * 分组编码
     */
    @Schema(title = "分组编码")
    @TableField("GROUP_CODE")
    private String groupCode = StringUtils.EMPTY;


    /**
     * 操作编码
     */
    @Schema(title = "操作编码")
    @TableField("OPER_CODE")
    private String operCode = StringUtils.EMPTY;


    /**
     * 操作名称
     */
    @Schema(title = "操作名称")
    @TableField("OPER_NAME")
    private String operName = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;



    public String getGroupCodeForDc(){
        return this.groupCode;
    }


}