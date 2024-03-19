package com.ca.mfd.prc.common.dto.eswitch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 接收Switch点位对象(私有模型)
 *
 * @author inkelink eric.zhou
 * @date 2023-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "接收Switch点位对象(私有模型)")
public class ReceivePointDataInfo {

    private String pointAddress;

    private String dataType;

    private String value;

    private String time;
}
