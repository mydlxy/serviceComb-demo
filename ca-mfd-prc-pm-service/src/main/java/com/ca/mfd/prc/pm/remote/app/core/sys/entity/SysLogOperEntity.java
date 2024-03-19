package com.ca.mfd.prc.pm.remote.app.core.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.utils.UUIDUtils;
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
 * @author inkelink ${email}
 * @Description: 关键操作日志
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "关键操作日志")
@TableName("PRC_SYS_LOG_OPER")
public class SysLogOperEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_SYS_LOG_OPER_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 类型
     */
    @Schema(title = "类型")
    @TableField("CAETGORY")
    private String caetgory = StringUtils.EMPTY;

    /**
     * 工号
     */
    @Schema(title = "工号")
    @TableField("OPER_USER_CODE")
    private String operUserCode = StringUtils.EMPTY;

    /**
     * 操作人
     */
    @Schema(title = "操作人")
    @TableField("OPER_USER_NAME")
    private String operUserName = StringUtils.EMPTY;

    /**
     * 操作人ID
     */
    @Schema(title = "操作人ID")
    @TableField("OPER_USER_ID")
    private String operUserId = UUIDUtils.getEmpty();

    /**
     * 操作时间
     */
    @Schema(title = "操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("OPER_DT")
    private Date operDt = new Date();

    /**
     * 行为
     */
    @Schema(title = "行为")
    @TableField("OPER")
    private Integer oper = -1;

    /**
     * 内容
     */
    @Schema(title = "内容")
    @TableField("CONTENT")
    private String content = StringUtils.EMPTY;

    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

}
