package com.ca.mfd.prc.core.prm.entity;

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

/**
 * @author inkelink ${email}
 * @Description: token关联用户信息(门户集成使用)
 * @date 2023-09-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "token关联用户信息(门户集成使用)")
@TableName("PRC_PRM_REDIS_USER_ID_USER_INFO")
public class PrmRedisUserIdUserInfoEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_PRM_REDIS_USER_ID_USER_INFO_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 用户id
     */
    @Schema(title = "用户id")
    @TableField("CA_USER_ID")
    private Long caUserId;

    /**
     * 用户信息(json格式)
     */
    @Schema(title = "用户信息(json格式)")
    @TableField("USER_INFO")
    private String userInfo;
}
