package com.ca.mfd.prc.core.prm.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author inkelink
 */
@Data
@Schema(description = "树节点")
public class TreeNodeInfoEntity {
    /**
     * id
     */
    @Schema(title = "id")
    private String id;

    /**
     * 编号
     */
    @Schema(title = "编号")
    private String code;

    /**
     * 组名
     */
    @Schema(title = "组名")
    private String groupName;

    /**
     * 描述
     */
    @Schema(title = "描述")
    private String description;

    /**
     * 节点名称
     */
    @Schema(title = "节点名称")
    private String text;

    /**
     * 节点名称-兼容前端(angular)
     */
    @Schema(title = "节点名称-兼容前端(angular)")
    private String label;

    /**
     * 节点名称-兼容前端(iview)
     */
    @Schema(title = "节点名称-兼容前端(iview)")
    private String title;

    /**
     * 节点提示
     */
    @Schema(title = "节点提示")
    private String qTip;

    /**
     * 提示信息
     */
    @Schema(title = "提示信息")
    private String message;

    /**
     * 附加信息
     */
    @Schema(title = "提示信息")
    private Map<String, Object> extendData;

    /**
     * 是否是根节点
     */
    @Schema(title = "是否是根节点")
    private Boolean leaf;

    /**
     * 节点图标
     */
    @Schema(title = "节点图标")
    private String icon;

    /**
     * 节点图标样式
     */
    @Schema(title = "节点图标样式")
    private String iconCls;

    /**
     * 是否带有复选框
     */
    @Schema(title = "是否带有复选框")
    private Boolean checked;

    /**
     * 是否展开
     */
    @Schema(title = "是否展开")
    private Boolean expanded;

    /**
     * 兼容前端，iview
     */
    @Schema(title = "兼容前端，iview")
    private Boolean expand;


    /**
     * 数据来源
     */
    @Schema(title = "数据来源")
    private String dataSource;

    /**
     * 节点样式
     */
    @Schema(title = "节点样式")
    private String style;

    /**
     * 节点样式
     */
    @Schema(title = "节点样式")
    private String styleClass;

    /**
     * 子节点
     */
    @Schema(title = "子节点")
    private List<TreeNodeInfoEntity> children;
}