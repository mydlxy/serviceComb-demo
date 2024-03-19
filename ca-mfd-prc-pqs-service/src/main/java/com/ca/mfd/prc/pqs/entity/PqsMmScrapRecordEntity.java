package com.ca.mfd.prc.pqs.entity;

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

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @Description: 压铸废料管理实体
 * @author inkelink
 * @date 2023年10月27日
 * @变更说明 BY inkelink At 2023年10月27日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "压铸废料管理")
@TableName("PRC_PQS_MM_SCRAP_RECORD")
public class PqsMmScrapRecordEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PQS_MM_SCRAP_RECORD_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 质检单号
     */
    @Schema(title = "质检单号")
    @TableField("INSPECTION_NO")
    private String inspectionNo = StringUtils.EMPTY;


    /**
     * 废料类型;1、融化铝渣 2、后处理铝削、3、加加工铝削、4、综合固废
     */
    @Schema(title = "废料类型;1、融化铝渣 2、后处理铝削、3、加加工铝削、4、综合固废")
    @TableField("SCRAP_TYPE")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer scrapType = 0;


    /**
     * 记录时间
     */
    @Schema(title = "记录时间")
    @TableField("RECORD_DT")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date recordDt = new Date();


    /**
     * 送料人
     */
    @Schema(title = "送料人")
    @TableField("SEND_USER")
    private String sendUser = StringUtils.EMPTY;


    /**
     * 收料人
     */
    @Schema(title = "收料人")
    @TableField("RECIEVE_USER")
    private String recieveUser = StringUtils.EMPTY;


    /**
     * 车牌
     */
    @Schema(title = "车牌")
    @TableField("PLATE_NO")
    private String plateNo = StringUtils.EMPTY;


    /**
     * 空车重量
     */
    @Schema(title = "空车重量")
    @TableField("EMPTY_WEIGHT")
    private BigDecimal emptyWeight = BigDecimal.valueOf(0);


    /**
     * 满车重量
     */
    @Schema(title = "满车重量")
    @TableField("FINAL_WEIGHT")
    private BigDecimal finalWeight = BigDecimal.valueOf(0);


    /**
     * 真实重量
     */
    @Schema(title = "真实重量")
    @TableField("REAL_WEIGHT")
    private BigDecimal realWeight = BigDecimal.valueOf(0);


    /**
     * 单位
     */
    @Schema(title = "单位")
    @TableField("UNIT")
    private String unit = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


}