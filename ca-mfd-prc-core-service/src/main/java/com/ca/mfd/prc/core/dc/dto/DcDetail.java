package com.ca.mfd.prc.core.dc.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author inkelink
 */
@Data
public class DcDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String pageKey;
    private String pageName;
    private String authorizationCode;
    private String buttonId;
    private String buttonAuthorizationCode;
    private String buttonKey;
    private String buttonName;
    private String displayNo;

}
