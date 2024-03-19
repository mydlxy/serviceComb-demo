package com.ca.mfd.prc.core.main.dto;

import lombok.Data;

/**
 * @author inkelink
 */
@Data
public class SysActionOperateLogDTO {
    private Long id;
    private String module;
    private String actionname;
    private String urlpath;
    private String dickey;
    private String template;
    private String comment;
}
