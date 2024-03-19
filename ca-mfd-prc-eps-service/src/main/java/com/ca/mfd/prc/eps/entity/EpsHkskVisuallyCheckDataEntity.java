package com.ca.mfd.prc.eps.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 * @Description: 海克斯康视觉检测数据实体
 * @author inkelink
 * @date 2024年03月14日
 * @变更说明 BY inkelink At 2024年03月14日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "海克斯康视觉检测数据")
@TableName("PRC_EPS_HKSK_VISUALLY_CHECK_DATA")
public class EpsHkskVisuallyCheckDataEntity extends BaseEntity {

    /**
     * 主键ID
     */
    @Schema(title = "主键ID")
    @TableId(value = "PRC_EPS_HKSK_VISUALLY_CHECK_DATA_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 条码
     */
    @Schema(title = "条码")
    @TableField("BARCODE")
    private String barcode = StringUtils.EMPTY;


    /**
     * 检测工位
     */
    @Schema(title = "检测工位")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;


    /**
     * 相机编号
     */
    @Schema(title = "相机编号")
    @TableField("NO")
    private String no = StringUtils.EMPTY;


    /**
     * 缺陷代码
     */
    @Schema(title = "缺陷代码")
    @TableField("DEFECT_CODE")
    private String defectCode = StringUtils.EMPTY;


    /**
     * 检测时间
     */
    @Schema(title = "检测时间")
    @TableField("CHECK_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date checkDt = new Date();


    /**
     * 文件路径
     */
    @Schema(title = "文件路径")
    @TableField("FILE_URL")
    private String fileUrl = StringUtils.EMPTY;


}