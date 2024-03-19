package com.ca.mfd.prc.pqs.communication.entity;

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
 * @Description: 接口记录表实体
 * @author inkelink
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "接口记录表")
@TableName("PRC_MID_API_LOG")
public class MidApiLogEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_MID_API_LOG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 请求开始时间
     */
    @Schema(title = "请求开始时间")
    @TableField("REQUEST_START_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date requestStartTime = new Date();


    /**
     * 请求结束时间
     */
    @Schema(title = "请求结束时间")
    @TableField("REQUEST_STOP_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date requestStopTime = new Date();


    /**
     * 数据行数
     */
    @Schema(title = "数据行数")
    @TableField("DATA_LINE_NO")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer dataLineNo = 0;

    @Schema(title = "接口类型(参见接口文档)")
    @TableField("API_TYPE")
    private String apiType = StringUtils.EMPTY;

    /**
     * 状态 0接收开始 1接收结束 2校验开始 3校验结束 4同步开始 5同步结束 6同步失败
     */
    @Schema(title = "状态 0开始1结束")
    @TableField("STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 0;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

    /**
     * 请求数据
     */
    @Schema(title = "请求数据")
    @TableField("REQ_DATA")
    private String reqData = StringUtils.EMPTY;

    /**
     * 响应数据
     */
    @Schema(title = "响应数据")
    @TableField("RESPONSE_DATA")
    private String responseData = StringUtils.EMPTY;

}