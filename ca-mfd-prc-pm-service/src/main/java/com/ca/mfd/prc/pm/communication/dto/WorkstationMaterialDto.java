package com.ca.mfd.prc.pm.communication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(title = "物料清单")
public class WorkstationMaterialDto implements Serializable {
    /**
     * 车间ID
     */
    private Long workshopId;

    /**
     * 车间编码
     */
    //private String shopCode;
    private String workshopCode;

    /**
     * 线体ID
     */

    private Long lineId;

    /**
     * 线体编码
     */
    private String lineCode;
    /**
     * 工位ID
     */

    private Long workstationId;


    /**
     * 工位编码
     */
    private String workstationCode;

    /**
     * 物料号
     */

    //private String materialNo;
    private String materialCode;

    /**
     * 物料描述
     */

    private String materialName;

    /**
     * 物料数量
     */
    //private Integer materialNum;
    private Double workstationUseQuantity;

    /**
     * 工位顺序号
     */
    //private Integer workstationNo;
    private Integer workstationSeq;

    /**
     * 工位方位顺序
     */
    private Integer workstationDisplay;

    /**
     * 制造供货状态
     */
    private String manufacturingSupplyStatus;
    /**
     * bom行标识
     */
    private String bomRowNum;
    /**
     * 合件分组号
     */
    private String compositesNum;
    /**
     * 使用车间
     */
    private String useWorkshop;
    /**
     * 制造车间
     */
    private String manufacturingWorkshop;

    /**
     * 使用类型
     */
    private int useType;

}
