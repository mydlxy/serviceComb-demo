package com.ca.mfd.prc.core.prm.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * @author inkelink
 */
@Data
@Schema(description = "ChangeDataInfoEntity")
public class ChangeDataInfoEntity {
    private String type;
    private Map<String, Object> data;
    private String oper;
    private Map<String, Object> diffFields;
}
