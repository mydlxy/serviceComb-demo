package com.ca.mfd.prc.common.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单信息
 *
 * @author inkelink eric.zhou@hg2mes.com
 * @date 2023-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "菜单信息")
public class MenuNodeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(title = "id")
    @JsonProperty("id")
    private String id;

    @Schema(title = "名称")
    @JsonProperty("label")
    private String label;

    @Schema(title = "icon")
    @JsonProperty("icon")
    private String icon;

    @Schema(title = "是否显示")
    @JsonProperty("isCurrent")
    private Boolean isCurrent;

    @Schema(title = "内嵌的URL地址")
    @JsonProperty("routerLink")
    private String routerLink;

    @Schema(title = "弹出的URL地址")
    @JsonProperty("url")
    private String url;

    @Schema(title = "触发的方式 内嵌/外联")
    @JsonProperty("target")
    private String target;

    @Schema(title = "是否有子节点")
    @JsonProperty("isLeaf")
    private Boolean isLeaf;

    @Schema(title = "子节点")
    @JsonProperty("items")
    private List<MenuNodeDTO> items;

}
