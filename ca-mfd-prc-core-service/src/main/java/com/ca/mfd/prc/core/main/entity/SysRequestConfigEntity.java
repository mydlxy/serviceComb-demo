package com.ca.mfd.prc.core.main.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink ${email}
 * @Description: 系统内部站点访问
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "系统内部站点访问")
@TableName("PRC_SYS_REQUEST_CONFIG")
public class SysRequestConfigEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_SYS_REQUEST_CONFIG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 请求KEY
     */
    @Schema(title = "请求KEY")
    @TableField("REQUEST_KEY")
    private String requestKey = StringUtils.EMPTY;

    /**
     * APPId
     */
    @Schema(title = "APPId")
    @TableField("SERVICE_DOMAIN_KEY")
    private String serviceDomainKey = StringUtils.EMPTY;

    /**
     * 请求地址
     */
    @Schema(title = "请求地址")
    @TableField("REQUEST_URL")
    private String requestUrl = StringUtils.EMPTY;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    /**
     * 请求方式
     */
    @Schema(title = "请求方式")
    @TableField("REQUEST_METHOD")
    private Integer requestMethod = 0;

    /**
     * 状态
     */
    @Schema(title = "状态")
    @TableField("STATUS")
    private Integer status = 0;

    /**
     * 请求参数
     */
    @Schema(title = "请求参数")
    @TableField("BODY_PAR")
    private String bodyPar = StringUtils.EMPTY;

}
