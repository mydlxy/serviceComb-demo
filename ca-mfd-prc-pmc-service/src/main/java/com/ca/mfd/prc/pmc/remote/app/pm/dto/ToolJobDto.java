package com.ca.mfd.prc.pmc.remote.app.pm.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;

/**
 * @author inkelink
 * @Description: 导出工厂建模报表用的JOB实体类
 * @date 2023年08月28日
 * @变更说明 BY inkelink At 2023年08月28日
 */
@Data
public class ToolJobDto {


    private String workshopCode = Strings.EMPTY;


    private String lineCode = Strings.EMPTY;

    private String stationName = Strings.EMPTY;

    private String toolCode = Strings.EMPTY;


    /**
     * 作业号
     */
    @Schema(title = "作业号")
    private String jobNo = StringUtils.EMPTY;


    private String woCode = Strings.EMPTY;


    /**
     * 车辆特征
     */
    @Schema(title = "车辆特征")
    @TableField("FEATURE_CODE")
    private String featureCode = StringUtils.EMPTY;

    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Boolean isDelete = false;


}