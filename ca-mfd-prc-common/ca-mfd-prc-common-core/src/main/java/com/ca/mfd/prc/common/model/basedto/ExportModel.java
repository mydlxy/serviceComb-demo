package com.ca.mfd.prc.common.model.basedto;

import com.ca.mfd.prc.common.model.base.dto.DataDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 摘要:
 * 导出文件模型
 *
 * @author lty
 * @date 2023/3/31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExportModel extends DataDto {
    /**
     * 导出文件名称
     */
    private String fileName;
}
