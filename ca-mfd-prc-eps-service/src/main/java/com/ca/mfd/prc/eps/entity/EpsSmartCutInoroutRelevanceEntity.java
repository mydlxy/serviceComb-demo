package com.ca.mfd.prc.eps.entity;

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
 * @author inkelink
 * @Description: PACK切入或切出关联实体
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "PACK切入或切出关联")
@TableName("PRC_EPS_SMART_CUT_INOROUT_RELEVANCE")
public class EpsSmartCutInoroutRelevanceEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_EPS_SMART_CUT_INOROUT_RELEVANCE_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 上一个AVI站点编码
     */
    @Schema(title = "上一个AVI站点编码")
    @TableField("LAST_AVI_CODE")
    private String lastAviCode = StringUtils.EMPTY;


    /**
     * 工位组（,分割）
     */
    @Schema(title = "工位组（,分割）")
    @TableField("WORKSTATION_CODES")
    private String workstationCodes = StringUtils.EMPTY;


    /**
     * 下一个AVI站点编码
     */
    @Schema(title = "下一个AVI站点编码")
    @TableField("NEXT_AVI_CODE")
    private String nextAviCode = StringUtils.EMPTY;


}