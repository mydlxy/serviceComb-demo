package com.ca.mfd.prc.avi.remote.app.pm.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 阳波
 * @ClassName CmcPmOrganizationEntity
 * @description: TODO
 * @date 2023年08月26日
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(of = "sourceId")
@Schema(description = "工厂")
public class CmcPmOrganizationVo {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long cmcPmOrganizationId = Constant.DEFAULT_ID;


    /**
     * 代码
     */
    @Schema(title = "代码")
    private String organizationCode = StringUtils.EMPTY;


    /**
     * 名称
     */
    @Schema(title = "名称")
    private String organizationName = StringUtils.EMPTY;


    /**
     * 地址
     */
    @Schema(title = "地址")
    private String address = StringUtils.EMPTY;


    /**
     * 是否可用
     */
    @Schema(title = "是否可用")
    private Boolean isEnable = false;


    /**
     * 国家
     */
    @Schema(title = "国家")
    private String country = StringUtils.EMPTY;

    private Integer source = 1;


    /**
     * 来源ID
     */
    private Long sourceId = Constant.DEFAULT_ID;


    private String remark = StringUtils.EMPTY;


}
