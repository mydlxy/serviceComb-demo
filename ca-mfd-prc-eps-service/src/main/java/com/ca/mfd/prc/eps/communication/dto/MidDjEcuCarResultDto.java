package com.ca.mfd.prc.eps.communication.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 *
 * @Description: 电检软件结果信息数据
 * @author inkelink
 * @date 2024年2月19日
 */
@Data
@Schema(description= "电检软件结果信息数据DTO")
public class MidDjEcuCarResultDto {

    @Schema(title = "vin号")
    private String vinCode = StringUtils.EMPTY;

    @Schema(title = "结果集")
    private List<MidDjEcuCarResultEcuListDto> ecuList;

    @Schema(title = "测试时间")
    @TableField("TEST_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date testTime = new Date();

    @Schema(title = "结果上传时间")
    @TableField("UPLOAD_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Date uploadDate = new Date();
}
