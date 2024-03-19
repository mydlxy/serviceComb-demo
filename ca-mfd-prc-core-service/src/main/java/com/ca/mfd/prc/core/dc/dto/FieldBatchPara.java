package com.ca.mfd.prc.core.dc.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.core.prm.entity.DcFieldConfigEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author inkelink
 */
@Data
public class FieldBatchPara implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<DcFieldConfigEntity> fields;
    private Long pageId = Constant.DEFAULT_ID;
}
