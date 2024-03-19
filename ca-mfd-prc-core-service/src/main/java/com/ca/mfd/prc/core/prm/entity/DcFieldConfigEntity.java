package com.ca.mfd.prc.core.prm.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink ${email}
 * @Description: 字段配置
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "BTREE")
@TableName("PRC_DC_FIELD_CONFIG")
public class DcFieldConfigEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_DC_FIELD_CONFIG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    @Schema(title = "页面外键")
    @TableField("PRC_DC_PAGE_CONFIG_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcDcPageConfigId;

    @Schema(title = "字段名称")
    @TableField("FILED_NAME")
    private String filedName = StringUtils.EMPTY;

    @Schema(title = "字段属性名称")
    @TableField("FILED_PROPERTY_NAME")
    private String filedPropertyName = StringUtils.EMPTY;

    @Schema(title = "字段类型")
    @TableField("FILED_TYPE")
    private String filedType = StringUtils.EMPTY;

    @Schema(title = "验证表达式")
    @TableField("VERIFY_EXPRESSION")
    private String verifyExpression = StringUtils.EMPTY;

    @Schema(title = "自定义列表值渲染")
    @TableField("RENDERDER")
    private String renderder = StringUtils.EMPTY;

    @Schema(title = "字段转换数据源")
    @TableField("FILED_CONVERT_SOURCE")
    private String filedConvertSource = StringUtils.EMPTY;

    @Schema(title = "列表是否显示")
    @TableField("IS_SHOW_LIST")
    private Boolean isShowList = false;

    @Schema(title = "列表顺序号")
    @TableField("LIST_DISPLAY_NO")
    private Integer listDisplayNo;

    @Schema(title = "更新是否显示")
    @TableField("IS_SHOW_UPDATE")
    private Boolean isShowUpdate = false;

    @Schema(title = "更新顺序号")
    @TableField("UPDATE_DISPLAY_NO")
    private Integer updateDisplayNo;

    @Schema(title = "添加是否显示")
    @TableField("IS_SHOW_ADD")
    private Boolean isShowAdd = false;

    @Schema(title = "添加顺序号")
    @TableField("ADD_DISPLAY_NO")
    private Integer addDisplayNo;

    @Schema(title = "查询是否显示")
    @TableField("IS_SHOW_SELECT")
    private Boolean isShowSelect = false;

    @Schema(title = "查询顺序号")
    @TableField("SELECT_DISPLAY_NO")
    private Integer selectDisplayNo;

    @Schema(title = "是否范围查询")
    @TableField("IS_SELECT_RANGE")
    private Boolean isSelectRange = false;

    @Schema(title = "高度")
    @TableField("WIDTH")
    private Integer width = 0;

    @Schema(title = "是否导入")
    @TableField("IS_IMPORT")
    private Boolean isImport = false;

    @Schema(title = "是否导出")
    @TableField("IS_EXPORT")
    private Boolean isExport = false;

    @Schema(title = "是否排序")
    @TableField("IS_SORT")
    private Boolean isSort = false;

    @Schema(title = "自定义参数")
    @TableField("PARAMS")
    private String params = StringUtils.EMPTY;

    @Schema(title = "查询条件")
    @TableField("CONDITION_FILED")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private String conditionFiled = StringUtils.EMPTY;
}
