package com.ca.mfd.prc.pm.remote.app.core.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
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
 * @author inkelink ${email}
 * @Description: 服务执行命令
 * @date 2023-04-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "服务执行命令")
@TableName("PRC_SYS_SERVICE_PROCESS")
public class SysServiceProcessEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_SYS_SERVICE_PROCESS_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 服务
     */
    @Schema(title = "服务")
    @TableField("PRC_SYS_SERVICE_MANAGER_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long prcSysServiceManagerId = Constant.DEFAULT_ID;

    /**
     * {t:"操作",d:["1.安装;2.停止;3.启动;4.卸载"]}
     */
    @Schema(title = "{t:\"操作\",d:[\"1.安装;2.停止;3.启动;4.卸载\"]}")
    @TableField("OPER")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer oper = 0;

    /**
     * 执行时间
     */
    @Schema(title = "执行时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    @TableField("OPER_DT")
    private Date operDt;

    /**
     * {t:"状态",d:["0.未执行;1.失败;2.成功"]}
     */
    @Schema(title = "{t:\"状态\",d:[\"0.未执行;1.失败;2.成功\"]}")
    @TableField("STATUS")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer status = 0;

    /**
     * 描述
     */
    @Schema(title = "描述")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;

}
