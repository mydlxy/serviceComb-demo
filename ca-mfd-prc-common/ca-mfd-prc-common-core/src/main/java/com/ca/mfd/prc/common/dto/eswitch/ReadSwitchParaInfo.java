package com.ca.mfd.prc.common.dto.eswitch;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 交互Switch读取对象(私有模型)
 *
 * @author inkelink eric.zhou
 * @date 2023-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "交互Switch读取对象(私有模型)")
public class ReadSwitchParaInfo {

    @Schema(title = "点位集合")
    private List<ReadSwitchModel> pointAddress = new ArrayList<>();

    @Schema(title = "请求标识")
    private String token;

}
