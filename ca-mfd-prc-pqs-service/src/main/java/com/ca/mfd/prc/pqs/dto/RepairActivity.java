package com.ca.mfd.prc.pqs.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;

import java.math.BigDecimal;

/**
 * RepairActivity
 *
 * @Author: eric.zhou
 * @Date: 2023-04-12-14:49
 * @Description:
 */
@Data
@Schema(description = "RepairActivity")
public class RepairActivity {

    /**
     * 返修方式
     */
    @Schema(title = "repairWay")
    private String repairWay = Strings.EMPTY;

    /**
     * 返修时间
     */
    @Schema(title = "repairTime")
    private String repairTime = Strings.EMPTY;

    /**
     * 返修工时
     */
    @Schema(title = "spendTime")
    private BigDecimal spendTime = BigDecimal.valueOf(0);


    /**
     * 返修备注
     */
    @Schema(title = "repairRemark")
    private String repairRemark = Strings.EMPTY;

}
