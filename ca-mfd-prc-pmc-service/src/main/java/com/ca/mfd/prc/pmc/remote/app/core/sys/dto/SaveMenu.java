package com.ca.mfd.prc.pmc.remote.app.core.sys.dto;

import com.ca.mfd.prc.common.constant.Constant;
import lombok.Data;

import java.io.Serializable;

/**
 * @author inkelink
 */
@Data
public class SaveMenu {
    private Serializable menuId = Constant.DEFAULT_ID;
    private String datas;
    private Integer version = 0;
}
