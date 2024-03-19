package com.ca.mfd.prc.pqs.entity;

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

/**
 *
 * @Description: 用户文件操作记录实体
 * @author inkelink
 * @date 2023年11月30日
 * @变更说明 BY inkelink At 2023年11月30日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "用户文件操作记录")
@TableName("PRC_PQS_SPC_FILE_USER_LOG")
public class PqsSpcFileUserLogEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_SPC_FILE_USER_LOG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 文件ID
     */
    @Schema(title = "文件ID")
    @TableField("PRC_PQS_SPC_FILE_RECORD_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPqsSpcFileRecordId = Constant.DEFAULT_ID;


    /**
     * 文件自定义名
     */
    @Schema(title = "文件自定义名")
    @TableField("CUSTOMER_NAME")
    private String customerName;


}