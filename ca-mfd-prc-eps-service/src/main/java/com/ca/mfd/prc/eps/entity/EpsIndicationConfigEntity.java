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
 * @Description: 作业指示配置实体
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "作业指示配置")
@TableName("PRC_EPS_INDICATION_CONFIG")
public class EpsIndicationConfigEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_INDICATION_CONFIG_ID", type = IdType.INPUT)
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
     * 触发类型（2、PLC监听触发 4、点击按钮触发 5、物料验证触发）
     */
    @Schema(title = "触发类型（2、PLC监听触发 4、点击按钮触发 5、物料验证触发）")
    @TableField("TRIGGER_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer triggerType = 0;


    /**
     * 组件名称
     */
    @Schema(title = "组件名称")
    @TableField("COMPONENT_NAME")
    private String componentName = StringUtils.EMPTY;


    /**
     * 组件编码
     */
    @Schema(title = "组件编码")
    @TableField("COMPONENT_CODE")
    private String componentCode = StringUtils.EMPTY;


    /**
     * 验证物料数量
     */
    @Schema(title = "验证物料数量")
    @TableField("VERIFY_MATERIAL_COUNT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer verifyMaterialCount = 0;


    /**
     * 上料倒计时（秒）
     */
    @Schema(title = "上料倒计时（秒）")
    @TableField("COUNT_DOWN")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer countDown = 0;


    /**
     * 显示图片模板
     */
    @Schema(title = "显示图片模板")
    @TableField("SHOW_TEMPLATE")
    private String showTemplate = StringUtils.EMPTY;


}