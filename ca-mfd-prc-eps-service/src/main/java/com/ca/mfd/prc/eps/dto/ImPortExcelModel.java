package com.ca.mfd.prc.eps.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ImPortExcelModel
 *
 * @author inkelink
 * @date 2023/09/05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "ImPortExcelModel")
public class ImPortExcelModel {

    private String equmentNo;
    private String pointName;
    private String pointCode;
    private String des;
    private int p;
    private int status;
    private String dbType;
    private String pointLamb;

    public ImPortExcelModel() {
    }

    public ImPortExcelModel(String equmentNo, String pointCode, String pointName, String des, String dbType, String pointLamb) {
        this.equmentNo = equmentNo;
        this.pointName = pointName;
        this.pointCode = pointCode;
        this.des = des;
        this.dbType = dbType;
        this.pointLamb = pointLamb;
    }
}
