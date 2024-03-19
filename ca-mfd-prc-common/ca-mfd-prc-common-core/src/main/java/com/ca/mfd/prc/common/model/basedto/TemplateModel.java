package com.ca.mfd.prc.common.model.basedto;

import lombok.Data;

import java.util.Map;

/**
 * @Author: joel
 * @Date: 2023-08-29-10:44
 * @Description:
 */
@Data
public class TemplateModel {
    /**
     * 导出文件名称
     */
    private String fileName;
    /**
     * 导出文件字段
     */
    private Map<String, String> field;
}
