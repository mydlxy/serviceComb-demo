package com.ca.mfd.prc.eps.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 * @Description: 预成组入箱位置配置实体
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "预成组入箱位置配置")
@TableName("PRC_EPS_MODULE_INBOX_LOCATION_CONFIG")
public class EpsModuleInboxLocationConfigEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_MODULE_INBOX_LOCATION_CONFIG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 预成组生产线体
     */
    @Schema(title = "预成组生产线体")
    @TableField("LINE_CODE")
    private String lineCode = StringUtils.EMPTY;


    /**
     * 涂胶工位
     */
    @Schema(title = "涂胶工位")
    @TableField("GELATINIZE_WORKSTATION_CODE")
    private String gelatinizeWorkstationCode = StringUtils.EMPTY;


    /**
     * 入箱工位
     */
    @Schema(title = "入箱工位")
    @TableField("INBOX_WORKSTATION_CODE")
    private String inboxWorkstationCode = StringUtils.EMPTY;


    /**
     * 路由标识
     */
    @Schema(title = "路由标识")
    @TableField("ROUTE_SIGN")
    private String routeSign = StringUtils.EMPTY;


}