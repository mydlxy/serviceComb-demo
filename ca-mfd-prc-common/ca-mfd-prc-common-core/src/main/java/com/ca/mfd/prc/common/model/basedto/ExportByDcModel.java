package com.ca.mfd.prc.common.model.basedto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * 摘要:
 * 导出文件模型
 *
 * @author lty
 * @date 2023/3/31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ExportByDcModel extends ExportModel {
    /**
     * 导出文件字段
     */
    private Map<String, String> field;
}
