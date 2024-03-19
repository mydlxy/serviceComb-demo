package com.ca.mfd.prc.eps.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * 员工信息表
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "员工信息表")
public class WorkPlaceEntity {

    /**
     * 家庭地址
     */
    @Schema(title = "家庭地址")
    private String address = StringUtils.EMPTY;
    /**
     * 家属电话
     */
    @Schema(title = "家属电话")
    private String familyPhone = StringUtils.EMPTY;
    /**
     * 归属地
     */
    @Schema(title = "归属地")
    private String homeAddress = StringUtils.EMPTY;
    /**
     * 业务线
     */
    @Schema(title = "业务线")
    private String bsArea = StringUtils.EMPTY;
    /**
     * 学历
     */
    @Schema(title = "学历")
    private String eduCation = StringUtils.EMPTY;
    /**
     * 职位
     */
    @Schema(title = "职位")
    private String jobTitle = StringUtils.EMPTY;

}