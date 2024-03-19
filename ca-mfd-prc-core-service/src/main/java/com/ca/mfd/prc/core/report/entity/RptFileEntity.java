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
 * @Description: 报表文件存储实体
 * @date 2023年09月23日
 * @变更说明 BY inkelink At 2023年09月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "报表文件存储")
@TableName("PRC_RPT_FILE")
public class RptFileEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_RPT_FILE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 配置名称
     */
    @Schema(title = "配置名称")
    @TableField("DISPLAY_NAME")
    private String displayName = StringUtils.EMPTY;


    /**
     * 报表内容
     */
    @Schema(title = "报表内容")
    @TableField("PATH")
    private String path = StringUtils.EMPTY;

    /**
     * 打印类型
     */
    @Schema(title = "打印类型")
    @TableField("PAGEKIND")
    private String pagekind;

    /**
     * 编辑报表内容的地址
     */
    @Schema(title = "编辑报表内容的地址")
    @TableField(exist = false)
    private String editPath ;

    /**
     * 预览的报表内容
     */
   /* @Schema(title = "预览的报表内容")
    @TableField(exist = false)
    private String previewPath ;*/

    /**
     * 预览的报表内容
     */
    @Schema(title = "预览的报表内容")
    @TableField(exist = false)
    private String uParamPath;

}