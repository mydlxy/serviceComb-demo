package com.ca.mfd.prc.core.prm.dto;

import com.ca.mfd.prc.core.prm.entity.DcButtonConfigEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author inkelink
 */
@Data
public class ButtonBatchSetPara implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<DcButtonConfigEntity> buttons;
    private String pageId;

}
