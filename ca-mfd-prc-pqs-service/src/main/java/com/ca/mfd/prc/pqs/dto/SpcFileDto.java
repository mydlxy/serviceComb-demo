package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
@Schema(description = "spc对象")
public class SpcFileDto implements Cloneable{
    private String featureName;

    private Float featureValue;

    private String conditionValue;

    private Boolean isSpcParam;

    private Float upperLimit;

    private Float lowerLimit;

    private String unit;

    private String vin;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpcFileDto that = (SpcFileDto) o;
        return Objects.equals(featureName, that.featureName) && Objects.equals(featureValue, that.featureValue) && Objects.equals(conditionValue, that.conditionValue) && Objects.equals(isSpcParam, that.isSpcParam) && Objects.equals(upperLimit, that.upperLimit) && Objects.equals(lowerLimit, that.lowerLimit) && Objects.equals(unit, that.unit) && Objects.equals(vin, that.vin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(featureName, featureValue, conditionValue, isSpcParam, upperLimit, lowerLimit, unit, vin);
    }
}

