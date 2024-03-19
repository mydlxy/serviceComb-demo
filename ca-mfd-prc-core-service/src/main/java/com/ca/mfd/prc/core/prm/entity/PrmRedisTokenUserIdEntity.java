package com.ca.mfd.prc.core.prm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author inkelink ${email}
 * @Description: token(门户集成使用)
 * @date 2023-09-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "token(门户集成使用)")
@TableName("PRC_PRM_REDIS_TOKEN_USER_ID")
public class PrmRedisTokenUserIdEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PRM_REDIS_TOKEN_USER_ID_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * token
     */
    @Schema(title = "代码")
    @TableField("TOKEN")
    private String token;

    /**
     * 用户id
     */
    @Schema(title = "用户id")
    @TableField("CA_USER_ID")
    private Long caUserId;

    /**
     * token过期时间
     */
    @Schema(title = "token过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("EXPIRE_DT")
    private Date expireDt;
}
