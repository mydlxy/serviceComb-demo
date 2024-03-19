package com.ca.mfd.prc.eps.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
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
 * @author inkelink
 * @Description: 虚拟队列校验配置实体
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "虚拟队列校验配置")
@TableName("PRC_EPS_QUEUE_VERIFY_CONFIG")
public class EpsQueueVerifyConfigEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_QUEUE_VERIFY_CONFIG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 当前产品唯一码
     */
    @Schema(title = "当前产品唯一码")
    @TableField("SN")
    private String sn = StringUtils.EMPTY;


    /**
     * 工位名称
     */
    @Schema(title = "工位名称")
    @TableField("WORKSTATION_NAME")
    private String workstationName = StringUtils.EMPTY;


    /**
     * 工位编号
     */
    @Schema(title = "工位编号")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


    /**
     * 工艺代码
     */
    @Schema(title = "工艺代码")
    @TableField("WO_CODE")
    private String woCode = StringUtils.EMPTY;

    /**
     * AVI队列订阅码
     */
    @Schema(title = "AVI队列订阅码")
    @TableField("QUEUE_SUBSCRIBE_CODE")
    private String queueSubscribeCode = StringUtils.EMPTY;


    /**
     * 网络地址
     */
    @Schema(title = "网络地址")
    @TableField("ADDRESS_IP")
    private String addressIp = StringUtils.EMPTY;


    /**
     * 数据DB
     */
    @Schema(title = "数据DB")
    @TableField("DATA_DB")
    private String dataDb = StringUtils.EMPTY;


    /**
     * 显示已上料条数
     */
    @Schema(title = "显示已上料条数")
    @TableField("COMPLETE_COUNT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer completeCount = 0;


    /**
     * 显示未上料条数
     */
    @Schema(title = "显示未上料条数")
    @TableField("UNFINISHED_COUNT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer unfinishedCount = 0;


    /**
     * 校验类型（1 VIN校验  2 车型校验）
     */
    @Schema(title = "校验类型（1 VIN校验  2 车型校验）")
    @TableField("VERIFY_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer verifyType = 0;

    /**
     * 校验方式 1 队列 2 工位
     */
    @Schema(title = "校验方式 1 队列 2 工位")
    @TableField("VERIFY_FUN")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer verifyFun = 1;

    /**
     * 切换队列 是否成功
     */
    @Schema(title = "切换队列 是否成功")
    @TableField("IS_VERIFY")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isVerify = false;


    /**
     * 描述
     */
    @Schema(title = "描述")
    @TableField("DESCRIPTION")
    private String description = StringUtils.EMPTY;


}