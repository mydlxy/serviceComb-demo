package com.ca.mfd.prc.core.prm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author eric.zhou
 */
@Schema(title = "PrmJoinUserDto", description = "菜单项配置")
@Data
public class TreeItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Key")
    private String key = StringUtils.EMPTY;
    @Schema(description = "Icon")
    private String icon = StringUtils.EMPTY;
    @Schema(description = "Label")
    private String label = StringUtils.EMPTY;
    @Schema(description = "IsPrm")
    private Boolean isPrm = false;

    @Schema(description = "data")
    private Object data;
    @Schema(description = "children")
    private List<TreeItem> children = new ArrayList();

    @Schema(description = "PartialSelected")
    private Boolean partialSelected = false;
    @Schema(description = "isSelected")
    private Boolean isSelected = false;

    public Boolean getPartialSelected() {
        if (this.children == null || this.children.size() <= 0) {
            return false;
        }
        if (this.children.size() == this.children.stream().filter(w -> w.getIsSelected()).count()) {
            return false;
        }
        return this.children.stream().filter(w -> w.getIsSelected()).count() != 0
                || this.children.stream().filter(w -> w.getPartialSelected()).count() != 0;
    }

    public void setPartialSelected(Boolean value) {
        this.partialSelected = value;
    }

    public Boolean getIsSelected() {
        if (this.children == null || this.children.size() <= 0) {
            return isSelected;
        }
        Boolean isSelect = children.size() == children.stream().filter(w -> w.getIsSelected()).count();
        return isSelect;
    }

    public void setIsSelected(Boolean value) {
        this.isSelected = value;
    }

}
