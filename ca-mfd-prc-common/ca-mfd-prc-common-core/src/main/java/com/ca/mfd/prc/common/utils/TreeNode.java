/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.common.utils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 树节点，所有需要实现树节点的，都需要继承该类
 *
 * @author inkelink
 * @since 1.0.0
 */
@Data
@Schema(title = "TreeNode", description = "树节点")
public class TreeNode<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 唯一标识
     */
    @Schema(description = "唯一标识")
    private String id;
    /**
     * 上级ID
     */
    @Schema(description = "上级ID")
    private String pid;
    /**
     * 编号
     */
    @Schema(description = "编号")
    private String code;
    /**
     * 组名
     */
    @Schema(description = "组名")
    private String groupName;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String description;
    /**
     * 节点名称
     */
    @Schema(description = "节点名称")
    private String text;
    /**
     * 节点提示
     */
    @Schema(description = "节点提示")
    private String qTip;
    /**
     * 提示信息
     */
    @Schema(description = "提示信息")
    private String message;
    /**
     * 附加信息
     */
    @Schema(description = "附加信息")
    private Object extendData;
    /**
     * 是否根节点
     */
    @Schema(description = "是否根节点")
    private boolean leaf;
    /**
     * 节点图标
     */
    @Schema(description = "节点图标")
    private String icon;
    /**
     * 节点图标样式
     */
    @Schema(description = "节点图标样式")
    private String iconCls;
    /**
     * 是否带有复选框
     */
    @Schema(description = "是否带有复选框")
    private boolean checked;
    /**
     * 是否展开
     */
    @Schema(description = "是否展开")
    private boolean expanded;
    /**
     * 数据来源
     */
    @Schema(description = "数据来源")
    private String dataSource;
    /**
     * 节点样式
     */
    @Schema(description = "节点样式")
    private String style;
    /**
     * 节点样式
     */
    @Schema(description = "节点样式")
    private String styleClass;
    /**
     * 子节点
     */
    @Schema(description = "子节点")
    private List<T> children = new ArrayList<>();

    public String getLabel() {
        return this.text;
    }

    public void setLabel(String text) {
        this.text = text;
    }

    public String getTitle() {
        return getLabel();
    }

    public void setTitle(String text) {
        setLabel(text);
    }

    public boolean isExpand() {
        return this.expanded;
    }

    public void setExpand(boolean expanded) {
        this.expanded = expanded;
    }

}