package com.ca.mfd.prc.pmc.remote.app.pm.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ca.mfd.prc.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author 阳波
 * @ClassName PmBaseEntity
 * @description: pm模块基类
 * @date 2023年09月28日
 * @version: 1.0
 */
@Data
public class PmBaseEntity extends BaseEntity {
    /**
     * 版本号
     */
    @Schema(title = "版本号")
    @TableField("VERSION")
    private Integer version = 0;
}
