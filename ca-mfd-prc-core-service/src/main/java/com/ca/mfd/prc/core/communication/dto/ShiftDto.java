package com.ca.mfd.prc.core.communication.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @Description: 班次实体
 * @author inkelink
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@Data
public class ShiftDto {



    /**
     * 班次代码
     */
    @Schema(title = "班次代码")
    @JsonAlias(value = {"SHIFT_CODE","shiftCode"})
    private String shiftCode = StringUtils.EMPTY;


    /**
     * 班次名称
     */
    @Schema(title = "班次名称")
    @JsonAlias(value = {"SHIFT_NAME","shiftName"})
    private String shiftName = StringUtils.EMPTY;


    @Schema(title = "班次开始时间")
    @JsonAlias(value = {"SHIFT_START_TIME","shiftStartTime"})
    private String shiftStartTime = StringUtils.EMPTY;

    @Schema(title = "班次结束时间")
    @JsonAlias(value = {"SHIFT_END_TIME","shiftEndTime"})
    private String shiftEndTime = StringUtils.EMPTY;

    @Schema(title = "班次是否跨天")
    @JsonAlias(value = {"SHIFT_ISCROSS","shiftIscross"})
    private Integer shiftIscross;





    /**
     * 主数据状态0：正常， 2：废止
     */
    @Schema(title = "主数据状态0：正常， 2：废止")
    @JsonAlias(value = {"STATUS","status"})
    private Integer status = 0;


    /**
     * 变化记录序列号
     */
    @Schema(title = "变化记录序列号")
    @JsonAlias(value = {"SUB_ID","subId"})
    private Long subId = Constant.DEFAULT_ID;


    /**
     * 数据变化类型1：新增 2：更新 3：删除 4：更新或新增
     */
    @Schema(title = "数据变化类型1：新增 2：更新 3：删除 4：更新或新增")
    @JsonAlias(value = {"OP_CODE","opCode"})
    private Integer opCode = 0;

    /**
     * 校验结果
     */
    @Schema(title = "校验结果")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer checkResult = 0;


    /**
     * 校验结果说明
     */
    @Schema(title = "校验结果说明")
    private String checkResultDesc = StringUtils.EMPTY;


}