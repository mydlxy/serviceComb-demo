package com.ca.mfd.prc.eps.remote.app.pqs.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class DefectAnomalyDto {

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id;

    /**
     * 组合代码
     */
    private String defectAnomalyCode;

    /**
     * ICC缺陷
     */
    private String defectAnomalyDescription;

    /**
     * 缺陷等级代码
     */
    private String gradeCode;

    /**
     * 缺陷等级
     */
    private String gradeName;

    /**
     * 责任部门代码
     */
    private String responsibleDeptCode;


    /**
     * 责任部门
     */
    private String responsibleDeptName;

    /**
     * 拼音简码
     */
    private String shortCode;

    /**
     * 是否激活缺陷
     */
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isActived = false;
}
