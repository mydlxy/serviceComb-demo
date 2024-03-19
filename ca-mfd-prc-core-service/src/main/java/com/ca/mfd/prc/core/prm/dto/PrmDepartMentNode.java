package com.ca.mfd.prc.core.prm.dto;

import com.ca.mfd.prc.common.utils.UUIDUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author eric.zhou
 */
@Schema(title = "PrmDepartMentNode", description = "部门菜单")
@Data
public class PrmDepartMentNode implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "部门ID")
    private String id = UUIDUtils.getEmpty();

    @Schema(description = "icon")
    private String icon = StringUtils.EMPTY;

    @Schema(description = "select")
    private Boolean select = false;

    @Schema(description = "departmentCode")
    private String departmentCode = StringUtils.EMPTY;

    @Schema(description = "organizationName")
    private String organizationName = StringUtils.EMPTY;

    @Schema(description = "remark")
    private String remark = StringUtils.EMPTY;

    @Schema(description = "isEnable")
    private Boolean isEnable;

    @Schema(description = "parentId")
    private String parentId = UUIDUtils.getEmpty();

    @Schema(description = "departmentName")
    private String departmentName = StringUtils.EMPTY;

    @Schema(description = "seqNumber")
    private Integer seqNumber;

    @Schema(description = "children")
    private List<PrmDepartMentNode> children = new ArrayList<>();

}

