package com.ca.mfd.prc.core.prm.dto;

import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author inkelink
 */
@Data
@Schema(title = "MenuNodeInfo", description = "菜单")
public class MenuNodeInfo {
    @JsonProperty("id")
    @Schema(description = "id")
    private String id = UUIDUtils.getEmpty();

    @JsonProperty("label")
    @Schema(description = "label")
    private String title;

    @JsonProperty("icon")
    @Schema(description = "icon")
    private String icon;

    @Schema(description = "是否显示")
    @JsonProperty("isCurrent")
    private Boolean isCurrent = false;

    @Schema(description = "内嵌的URL地址")
    @JsonProperty("routerLink")
    private String href;

    @JsonProperty("url")
    @Schema(description = "弹出的URL地址")
    private String url;

    @Schema(description = "触发的方式 内嵌/外联")
    @JsonProperty("target")
    private String targetType = StringUtils.EMPTY;

    @JsonProperty("isLeaf")
    @Schema(description = "isLeaf")
    private Boolean isLeaf;
    @JsonProperty("items")
    @Schema(description = "items")
    private List<MenuNodeInfo> children = new ArrayList<>();

    public Boolean getIsLeaf() {
        return children == null || children.size() == 0;
    }
}
