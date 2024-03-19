package com.ca.mfd.prc.common.model.main;

import com.ca.mfd.prc.common.enums.PrintTargetType;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ReportQueue
 *
 * @author inkelink
 * @date 2023-08-17
 */
@Data
public class ReportQueue {

    /**
     * 打印代码
     */
    private String printCode;
    /**
     * 目标外键
     */
    private Long targetId;
    /**
     * 目标类型
     */
    private PrintTargetType targetType;
    /**
     * 打印时间
     */
    private Date printDt;
    /**
     * 传递给报表的参数字典
     */
    private Map<String, String> parameters;
    /**
     * 备注，关键点
     */
    private String remark;
    /**
     * 打印数量,小于等于0代表按模板配置的数量打印，否则按设置的数量打印
     */
    private int printNumber;

    public ReportQueue() {
        this.printDt = new Date();
        this.parameters = new HashMap<>();
        this.printNumber = -1;
    }
}
