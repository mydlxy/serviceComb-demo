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
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @Description: SPC模块_操作记录表实体
 * @author inkelink
 * @date 2023年11月30日
 * @变更说明 BY inkelink At 2023年11月30日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "SPC模块_操作记录表")
@TableName("PRC_PQS_SPC_OPERATION_RECORD")
public class PqsSpcOperationRecordEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_SPC_OPERATION_RECORD_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * SPC模块_文件记录表ID;一个文件ID可以关联多次操作
     */
    @Schema(title = "SPC模块_文件记录表ID;一个文件ID可以关联多次操作")
    @TableField("PRC_PQS_SPC_FILE_RECORD_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPqsSpcFileRecordId = Constant.DEFAULT_ID;


    /**
     * 操作参数值
     */
    @Schema(title = "操作参数值")
    @TableField("OPERATION_PARAMS")
    private String operationParams = StringUtils.EMPTY;


    /**
     * 操作参数MD5;确认操作是否可以复用，文件MD5+操作参数MD5确定唯一值
     */
    @Schema(title = "操作参数MD5;确认操作是否可以复用，文件MD5+操作参数MD5确定唯一值")
    @TableField("OPERATION_PARAMS_MD5")
    private String operationParamsMd5 = StringUtils.EMPTY;


    /**
     * 操作访问次数;操作总计数
     */
    @Schema(title = "操作访问次数;操作总计数")
    @TableField("OPERATION_COUNT")
    private String operationCount = StringUtils.EMPTY;


    /**
     * 结果地址;操作MD5重复时直接返回地址不用重复操作
     */
    @Schema(title = "结果地址;操作MD5重复时直接返回地址不用重复操作")
    @TableField("BACK_FILE_PATH")
    private String backFilePath = StringUtils.EMPTY;


}