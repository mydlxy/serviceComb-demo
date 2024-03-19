package com.ca.mfd.prc.audit.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class ModifyDefectResponsibleInfo {

    private List<Long> dataIds;

    /**
     * 责任部门
     */
    private String responsibleDeptCode = StringUtils.EMPTY;

    /**
     * 缺陷等级
     */
    private String gradeCode = StringUtils.EMPTY;

    /**
     * 责任班组
     */
    private String responsibleTeamNo = StringUtils.EMPTY;
}
