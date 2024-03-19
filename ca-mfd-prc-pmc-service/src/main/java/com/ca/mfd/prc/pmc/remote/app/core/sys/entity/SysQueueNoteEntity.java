package com.ca.mfd.prc.pmc.remote.app.core.sys.entity;

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
 * @Description: 队列笔记
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "队列笔记")
@TableName("PRC_SYS_QUEUE_NOTE")
public class SysQueueNoteEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_SYS_QUEUE_NOTE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 组名
     */
    @Schema(title = "组名")
    @TableField("GROUP_NAME")
    private String groupName = StringUtils.EMPTY;

    /**
     * 参数json对象
     */
    @Schema(title = "参数json对象")
    @TableField("CONTENT")
    private String content = StringUtils.EMPTY;

    /**
     * 用户信息
     */
    @Schema(title = "用户信息")
    @TableField("ONLINE_USER")
    private String onlineUser = StringUtils.EMPTY;

}
