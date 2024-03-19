package com.ca.mfd.prc.avi.entity;

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
 * @Description: 队列发布配置表
 * @date 2023-04-28
 * @变更说明 BY inkelink At 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "队列发布配置表")
//@TableName("AVI_QUEUE_RELEASE_SET")
@TableName("PRC_AVI_QUEUE_RELEASE_SET")
public class AviQueueReleaseSetEntity extends BaseEntity {


    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_AVI_QUEUE_RELEASE_SET_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 厂商代码
     */
    //    @Schema(title = "厂商代码")
    //    @TableField("CODE")
    //    private String code = StringUtils.EMPTY;
    @Schema(title = "厂商代码")
    @TableField("QUEUE_CODE")
    private String queueCode = StringUtils.EMPTY;

    /**
     * 厂商名称
     */
    //    @Schema(title = "厂商名称")
    //    @TableField("NAME")
    //    private String name = StringUtils.EMPTY;
    @Schema(title = "厂商名称")
    @TableField("QUEUE_NAME")
    private String queueName = StringUtils.EMPTY;

    /**
     * AVI站点ID
     */
    //    @Schema(title = "AVI站点ID")
    //    @TableField("PM_AVI_ID")
    //    private String pmAviId = StringUtils.EMPTY;

    /**
     * AVI站点代码
     */
    //    @Schema(title = "AVI站点代码")
    //    @TableField("PM_AVI_CODE")
    //    private String pmAviCode = StringUtils.EMPTY;
    @Schema(title = "AVI站点代码")
    @TableField("AVI_CODE")
    private String aviCode = StringUtils.EMPTY;

    /**
     * AVI名称
     */
    @Schema(title = "AVI名称")
    @TableField("AVI_NAME")
    private String aviName = StringUtils.EMPTY;

    /**
     * 接收 PLC
     */
    //    @Schema(title = "接收PLC")
    //    @TableField("PLC_IP")
    //    private String plcIp = StringUtils.EMPTY;
    @Schema(title = "接收PLC")
    @TableField("PLC_IP")
    private String plcIp = StringUtils.EMPTY;

    /**
     * 接收PLC类型
     */
    //    @Schema(title = "接收PLC类型")
    //    @TableField("PLC_MODE")
    //    private String plcMode = StringUtils.EMPTY;
    @Schema(title = "接收PLC类型")
    @TableField("PLC_MODE")
    private String plcMode = StringUtils.EMPTY;

    /**
     * 接收 DB
     */
    //    @Schema(title = "接收DB")
    //    @TableField("DB_NAME")
    //    private String dbName = StringUtils.EMPTY;
    @Schema(title = "接收 DB")
    @TableField("DB_NAME")
    private String dbName = StringUtils.EMPTY;

}
