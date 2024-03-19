package com.ca.mfd.prc.avi.remote.app.pm.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 车间对象
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "车间对象")
public class ShopDTO {

    @Schema(title = "车间ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long pmShopId;

    @Schema(title = "车间代码")
    private String pmShopCode;

    @Schema(title = "车间名称")
    private String pmShopName;

    @Schema(title = "车间图标")
    private String iconCls;

    /**
     * 线体
     */
    @Schema(title = "线体")
    @TableField(exist = false)
    private List<LineDTO> children;
}
