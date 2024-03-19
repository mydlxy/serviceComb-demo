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
 * @Description: 产品-QG必检项实体
 * @date 2023年09月07日
 * @变更说明 BY inkelink At 2023年09月07日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "产品-QG必检项")
@TableName("PRC_PQS_PRODUCT_QG_CHECK_LIST_RECORD")
public class PqsProductQgCheckListRecordEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_PRODUCT_QG_CHECK_LIST_RECORD_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 检查清单ID
     */
    @Schema(title = "检查清单ID")
    @TableField("PRC_PQS_QG_CHECK_LIST_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcPqsQgCheckListId = Constant.DEFAULT_ID;


    /**
     * 分组
     */
    @Schema(title = "分组")
    @TableField("`GROUP`")
    private String group = StringUtils.EMPTY;


    /**
     * 顺序号
     */
    @Schema(title = "顺序号")
    @TableField("DISPLAY_NO")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer displayNo = 0;


    /**
     * 产品唯一码
     */
    @Schema(title = "产品唯一码")
    @TableField("SN")
    private String sn = StringUtils.EMPTY;


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
     * 检查内容
     */
    @Schema(title = "检查内容")
    @TableField("CONTENT")
    private String content = StringUtils.EMPTY;


    /**
     * 结果;0 未处理 1:OK 2:NG 3ByPass
     */
    @Schema(title = "结果;0 未处理 1:OK 2:NG 3ByPass")
    @TableField("RESULT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer result = 0;


}