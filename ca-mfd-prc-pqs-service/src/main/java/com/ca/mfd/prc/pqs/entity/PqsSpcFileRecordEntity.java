package com.ca.mfd.prc.pqs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @Description: 实体
 * @author inkelink
 * @date 2023年11月30日
 * @变更说明 BY inkelink At 2023年11月30日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "文件记录表")
@TableName("PRC_PQS_SPC_FILE_RECORD")
public class PqsSpcFileRecordEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_SPC_FILE_RECORD_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 文件MD5;文件二进制数据取MD5，确保唯一性。
     */
    @Schema(title = "文件MD5;文件二进制数据取MD5，确保唯一性。")
    @TableField("FILE_MD5")
    private String fileMd5 = StringUtils.EMPTY;


    /**
     * 源文件名
     */
    @Schema(title = "源文件名")
    @TableField("ORI_FILE_NAME")
    private String oriFileName = StringUtils.EMPTY;


    /**
     * 后台文件存储地址
     */
    @Schema(title = "后台文件存储地址")
    @TableField("BACK_FILE_NAME")
    private String backFileName;


    /**
     * 0:json 1：xlsx 2：database 3：interface
     */
    @Schema(title = "0:json 1：xlsx 2：database 3：interface")
    @TableField("SOURSE")
    private Integer sourse = 0;


    /**
     * 文件状态;0:已上传 1:上传失败2:验证失败3:已解析4：解析失败
     */
    @Schema(title = "文件状态;0:已上传 1:上传失败2:验证失败3:已解析4：解析失败")
    @TableField("STATE")
    private Integer state;


}