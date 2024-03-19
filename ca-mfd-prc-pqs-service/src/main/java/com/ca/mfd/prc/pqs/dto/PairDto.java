package com.ca.mfd.prc.pqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "pair参对象")
public class PairDto {

    private float attribute1;

    private float attribute2;



}

