package com.ca.mfd.prc.eps.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * EpsVehicleEqumentConfigExcel
 *
 * @author inkelink
 * @date 2023/09/05
 */
@Data
public class EpsVehicleEqumentConfigExcel {
    @Excel(name = "设备编码")
    private String equmentNo;
    @Excel(name = "点位名称")
    private String pointName;
    @Excel(name = "点位编码")
    private String pointCode;
    @Excel(name = "描述")
    private String des;
    @Excel(name = "扫描速率(毫秒)")
    private int p;
    @Excel(name = "状态(0 禁用 1 启用 2 模拟)")
    private int status;
    @Excel(name = "数据类型")
    private String dbType;
    @Excel(name = "点位表达式")
    private String pointLamb;
}
