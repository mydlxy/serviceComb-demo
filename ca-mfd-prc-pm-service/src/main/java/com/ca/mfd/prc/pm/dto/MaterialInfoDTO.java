package com.ca.mfd.prc.pm.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author 阳波
 * @ClassName MaterialInfoDTO
 * @description:
 * @date 2023年07月25日
 * @version: 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MaterialInfoDTO implements Serializable {
    private String materialNo;
    private String materialName;

    public MaterialInfoDTO() {

    }

    public MaterialInfoDTO(String materialNo, String materialName) {
        this.materialNo = materialNo;
        this.materialName = materialName;
    }
}
