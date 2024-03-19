package com.ca.mfd.prc.common.dto.plc;

import lombok.Data;

/**
 * PlcAnalysisModel
 *
 * @author inkelink
 * @date 2023-08-17
 */
@Data
public class PlcAnalysisModel {
    private Double index;
    private Integer byteLength;
    private String type;
    private Object value;
}
