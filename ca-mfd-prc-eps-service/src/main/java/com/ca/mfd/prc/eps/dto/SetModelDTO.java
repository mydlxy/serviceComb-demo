package com.ca.mfd.prc.eps.dto;

import com.ca.mfd.prc.eps.entity.EpsAssembleDetailEntity;
import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * SetModelDTO
 *
 * @author inkelink
 * @date 2023/09/05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "SetModelDTO")
public class SetModelDTO {

    @JsonAlias(value = {"Id", "id"})
    private String id;

    @JsonAlias(value = {"Details", "details"})
    private List<EpsAssembleDetailEntity> details;
}
