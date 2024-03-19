package com.ca.mfd.prc.pps.remote.app.core.sys.entity;

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
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author inkelink ${email}
 * @Description: 媒体库
 * @date 2023-04-28
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "媒体库")
@TableName("PRC_SYS_MEDIA")
public class SysMediaEntity extends BaseEntity {

    @Schema(title = "主键")
    @TableId(value = "PRC_SYS_MEDIA_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;

    /**
     * 文件名
     */
    @Schema(title = "文件名")
    @TableField("FILE_NAME")
    private String fileName = StringUtils.EMPTY;

    /**
     * 文件路径
     */
    @Schema(title = "文件路径")
    @TableField("FILE_PATH")
    private String filePath = StringUtils.EMPTY;

    /**
     * 物料路径
     */
    @Schema(title = "物料路径")
    @TableField("PHYSICAL_PATH")
    private String physicalPath = StringUtils.EMPTY;

    /**
     * 分类
     */
    @Schema(title = "分类")
    @TableField("CATEGORY")
    private String category = StringUtils.EMPTY;


    public SysMediaEntity(String category, String fileName, String filePath, String physicalPath) {
        this.category = category;
        this.fileName = fileName;
        this.filePath = filePath;
        this.physicalPath = physicalPath;
    }
}
