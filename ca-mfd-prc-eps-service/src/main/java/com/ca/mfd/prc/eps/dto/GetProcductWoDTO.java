package com.ca.mfd.prc.eps.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 车辆数据集合
 *
 * @author eric.zhou
 * @date 2023/04/12
 */
@Data
public class GetProcductWoDTO {

    /**
     * 自检工艺列表
     */
    @Schema(title = "自检工艺列表")
    private List<SeftCheckWoDTO> seftCheckWoList;
    /**
     * 操作工艺列表
     */
    @Schema(title = "操作工艺列表")
    private List<OperWoWoDTO> operWoList;
    /**
     * 追溯工艺列表
     */
    @Schema(title = "追溯工艺列表")
    private List<TraceWoDTO> traceWoList;

    public GetProcductWoDTO() {
        this.seftCheckWoList = new ArrayList<>();
        this.operWoList = new ArrayList<>();
        this.traceWoList = new ArrayList<>();
    }
}
