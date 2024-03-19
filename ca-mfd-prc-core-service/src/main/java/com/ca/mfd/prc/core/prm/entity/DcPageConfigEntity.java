package com.ca.mfd.prc.core.prm.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
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
 * @Description: 用户授权记录
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "页面配置")
@TableName("PRC_DC_PAGE_CONFIG")
public class DcPageConfigEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_DC_PAGE_CONFIG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    @Schema(title = "页面标识")
    @TableField("PAGE_KEY")
    private String pageKey = StringUtils.EMPTY;


    @Schema(title = "页面名称")
    @TableField("PAGE_NAME")
    private String pageName = StringUtils.EMPTY;


    @Schema(title = "是否具备多选")
    @TableField("IS_SHOW_CHECKBOX")
    private Boolean isShowCheckbox;


    @Schema(title = "是否具备行操作")
    @TableField("IS_SHOW_OPER")
    private Boolean isShowOper;


    @Schema(title = "数据提取地址")
    @TableField("DATA_URL")
    private String dataUrl = StringUtils.EMPTY;


    @Schema(title = "默认提取数据条件")
    @TableField("DEFAULT_CONDITIONS")
    private String defaultConditions = "[]";


    @Schema(title = "默认数据排序")
    @TableField("DEFAULT_SORTS")
    private String defaultSorts = "[]";


    @Schema(title = "权限代码")
    @TableField("AUTHORIZATION_CODE")
    private String authorizationCode = StringUtils.EMPTY;


    @Schema(title = "页面模板")
    @TableField("PAGE_TEMPLATE")
    private String pageTemplate = StringUtils.EMPTY;


    @Schema(title = "高度")
    @TableField("WIDTH")
    private Integer width;


    @Schema(title = "是否使用排序")
    @TableField("USE_SORT")
    private Boolean useSort;


    @Schema(title = "是否使用拖拽")
    @TableField("USE_REORDER")
    private Boolean useReorder;


    @Schema(title = "是否使用分组")
    @TableField("USE_ROW_GROUP")
    private Boolean useRowGroup;

    @Schema(title = "分组的字段")
    @TableField("ROW_GROUP_KEY")
    private String rowGroupKey = StringUtils.EMPTY;

    @Schema(title = "页面扩展属性")
    @TableField("EXTEND")
    private String extend = StringUtils.EMPTY;
}
