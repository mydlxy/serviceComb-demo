package com.ca.mfd.prc.avi.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * avi过点信息
 *
 * @author eric.zhou
 * @date 2023/04/10
 */
@Data
public class AviPassedRecordDTO {

    /**
     * 车辆识别码
     */
    private String tpsCode = StringUtils.EMPTY;

    /**
     * Avi代码
     */
    private String aviCode = StringUtils.EMPTY;

    /**
     * 过点信息
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDate.class)
    private Date passDate;

    /**
     * vin 号
     */
    private String vin = StringUtils.EMPTY;

    /**
     * 操作人
     */
    private String recordUser = StringUtils.EMPTY;

}
