package com.ca.mfd.prc.eps.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
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
 * @author inkelink
 * @Description: PLC收集追溯条码配置实体
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "PLC收集追溯条码配置")
@TableName("PRC_EPS_TRC_GATHER_PLC_CONFIG")
public class EpsTrcGatherPlcConfigEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_TRC_GATHER_PLC_CONFIG_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 工位名称
     */
    @Schema(title = "工位名称")
    @TableField("WORKSTATION_NAME")
    private String workstationName = StringUtils.EMPTY;


    /**
     * 工位编号
     */
    @Schema(title = "工位编号")
    @TableField("WORKSTATION_CODE")
    private String workstationCode = StringUtils.EMPTY;

    /**
     * 工艺编码
     */
    @Schema(title = "工艺编码")
    @TableField("WO_CODE")
    private String woCode = StringUtils.EMPTY;

    /**
     * PLC IP地址
     */
    @Schema(title = "PLC IP地址")
    @TableField("PLC_IP")
    private String plcIp = StringUtils.EMPTY;


    /**
     * 收集条码DB地址
     */
    @Schema(title = "收集条码DB地址")
    @TableField("GATHER_DB")
    private String gatherDb = StringUtils.EMPTY;


    /**
     * 当前收集条码
     */
    @Schema(title = "当前收集条码")
    @TableField("NOW_BARCODE")
    private String nowBarcode = StringUtils.EMPTY;


    /**
     * 执行结果(1 OK 1 NOK)
     */
    @Schema(title = "执行结果(1 OK 2 NOK)")
    @TableField("EXEC_RESULT")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer execResult = 0;


    /**
     * 执行追溯失败异常信息
     */
    @Schema(title = "执行追溯失败异常信息")
    @TableField("ERROR_MESSAGE")
    private String errorMessage = StringUtils.EMPTY;


}