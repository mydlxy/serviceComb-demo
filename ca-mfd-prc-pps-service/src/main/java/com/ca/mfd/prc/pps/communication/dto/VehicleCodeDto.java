package com.ca.mfd.prc.pps.communication.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 * @Description: 车型代码中间表实体
 * @author inkelink
 * @date 2023年12月08日
 * @变更说明 BY inkelink At 2023年12月08日
 */
@Data
@Schema(description= "VehicleCodeDto")
public class VehicleCodeDto  {



    /**
     * 层级
     */
    @Schema(title = "层级")
    private String level = StringUtils.EMPTY;


    /**
     * 编码
     */
    @Schema(title = "编码")
    private String nodeCode = StringUtils.EMPTY;


    /**
     * 父编码
     */
    @Schema(title = "父编码")
    private String parentNodeCode = StringUtils.EMPTY;


    /**
     * 名称
     */
    @Schema(title = "名称")
    private String nodeName = StringUtils.EMPTY;


    /**
     * 业务级别
     */
    @Schema(title = "业务级别")
    private String businessLevel = StringUtils.EMPTY;


    /**
     * 描述
     */
    @Schema(title = "描述")
    private String description = StringUtils.EMPTY;


    /**
     * 是否可构建BOM
     */
    @Schema(title = "是否可构建BOM")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isBomNode = false;


    /**
     * 是否可配置
     */
    @Schema(title = "是否可配置")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isConfigNode = false;


    /**
     * 车型生命周期状态
     */
    @Schema(title = "车型生命周期状态")
    private String vehicleLifecycleStatus = StringUtils.EMPTY;


    /**
     * 项目编码
     */
    @Schema(title = "研发代号")
    private String rdCode = StringUtils.EMPTY;



}