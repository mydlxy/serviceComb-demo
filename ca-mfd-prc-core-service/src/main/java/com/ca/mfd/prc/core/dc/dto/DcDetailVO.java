package com.ca.mfd.prc.core.dc.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author inkelink
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DcDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String key;
    private String name;
    private String authorizationCode;
    private String type;

    public DcDetailVO(String id, String key, String name, String authorizationCode, String type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.authorizationCode = authorizationCode;
        this.type = type;
    }
}
