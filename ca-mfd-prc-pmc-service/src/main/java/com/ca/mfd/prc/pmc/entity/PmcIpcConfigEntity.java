package com.ca.mfd.prc.pmc.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 * @Description: 工控机配置;实体
 * @author inkelink
 * @date 2023年10月26日
 * @变更说明 BY inkelink At 2023年10月26日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "工控机配置;")
@TableName("PRC_PMC_IPC_CONFIG")
public class PmcIpcConfigEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PMC_IPC_CONFIG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 车间代码
     */
    @Schema(title = "车间代码")
    @TableField("WORKSHOP_CODE")
    private String workshopCode = StringUtils.EMPTY;


    /**
     * 工控机名称
     */
    @Schema(title = "工控机名称")
    @TableField("NAME")
    private String name = StringUtils.EMPTY;


    /**
     * 工控机代码
     */
    @Schema(title = "工控机代码")
    @TableField("CODE")
    private String code = StringUtils.EMPTY;


    /**
     * IP地址
     */
    @Schema(title = "IP地址")
    @TableField("IP_ADDRESS")
    private String ipAddress = StringUtils.EMPTY;


    /**
     * 端口号
     */
    @Schema(title = "端口号")
    @TableField("PORT")
    private String port = StringUtils.EMPTY;


    /**
     * 用户名
     */
    @Schema(title = "用户名")
    @TableField("USERNAME")
    private String username = StringUtils.EMPTY;


    /**
     * 用户密码
     */
    @Schema(title = "用户密码")
    @TableField("PASSWORD")
    private String password = StringUtils.EMPTY;


}