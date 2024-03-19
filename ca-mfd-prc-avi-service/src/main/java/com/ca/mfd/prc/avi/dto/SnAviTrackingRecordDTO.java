package com.ca.mfd.prc.avi.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class SnAviTrackingRecordDTO implements Serializable {
    /**
     * 产品唯一标识
     */
    private List<String> sn = new ArrayList<>();

    /**
     * avi ->Code
     */
    private List<String> pmAviCode = new ArrayList<>();
}
