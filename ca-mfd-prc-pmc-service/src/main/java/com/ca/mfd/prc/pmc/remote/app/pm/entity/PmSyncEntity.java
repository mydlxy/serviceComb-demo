package com.ca.mfd.prc.pmc.remote.app.pm.entity;

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
 * @Description: 数据同步缓存表实体
 * @date 2023年09月01日
 * @变更说明 BY inkelink At 2023年09月01日
 */
@Data
@EqualsAndHashCode(callSuper = true, of = "prcId")
@Schema(description = "数据同步缓存表")
@TableName("PRC_PM_SYNC")
public class PmSyncEntity extends BaseEntity {

    /**
     * 主键
     */
    @Schema(title = "主键")
    @TableId(value = "PRC_PM_SYNC_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = JsonDeserializeLong.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * MOM工厂建模ID
     */
    @Schema(title = "MOM工厂建模ID")
    @TableField("PRC_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prcId = Constant.DEFAULT_ID;


    /**
     * 通用工厂建模ID
     */
    @Schema(title = "通用工厂建模ID")
    @TableField("CMC_ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long cmcId = Constant.DEFAULT_ID;


    /**
     * 数据类型
     */
    @Schema(title = "数据类型")
    @TableField("DATA_TYPE")
    private String dataType = StringUtils.EMPTY;


}