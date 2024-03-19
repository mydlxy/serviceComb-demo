package com.ca.mfd.prc.pqs.dto;

import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @Author: edwards.qu
 * @Date:
 * @Description:
 */
@Data
public class ProductAnomalyLogDto {

    /**
     * 备注
     */
    private String remark = StringUtils.EMPTY;

    /**
     * 状态;1.未修复、2.已修复、3未发现 4合格 、5不合格
     */
    private int status;

    /**
     * 操作人
     */
    private String createdUser = StringUtils.EMPTY;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date operDt;
}
