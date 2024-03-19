package com.ca.mfd.prc.eps.communication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @Description: 车云(整车数据)接口电机相关实体
 * @author inkelink
 * @date 2024年01月04日
 * @变更说明 BY inkelink At 2023年12月12日
 */
@Data
@Schema(description= "车云(整车数据)驱电电机相关实体")
public class CarCloudCarInfoQddjDto {
    /**
     * 发动机型号
     */
    @Schema(title = "发动机型号")
    private String engineModel = StringUtils.EMPTY;

    /**
     * 发动机号(编号)
     */
    @Schema(title = "发动机号(编号)")
    private String engineNo = StringUtils.EMPTY;

    /**
     * 前电机型号
     */
    @Schema(title = "前电机型号")
    private String motorFModel = StringUtils.EMPTY;

    /**
     * 前电机号(编号)
     */
    @Schema(title = "前电机号(编号)")
    private String motorFNo = StringUtils.EMPTY;

    /**
     * 后电机型号
     */
    @Schema(title = "后电机型号")
    private String motorBModel = StringUtils.EMPTY;

    /**
     * 后电机号(编号)
     */
    @Schema(title = "后电机号(编号)")
    private String motorBNo = StringUtils.EMPTY;
}
