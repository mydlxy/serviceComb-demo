package com.ca.mfd.prc.common.service;


import lombok.Data;

/**
 * SqlContent class
 *
 * @author inkelink
 * @date 2023/04/03
 */
@Data
public class SqlContent {

    private String statement;
    private Object parameter;
    private int batchSize;
    private Integer batchType = 0;

    public SqlContent() {
        this.batchType = 0;
    }

    public SqlContent(String statement, Object parameter, int batchSize) {
        this.batchType = 0;
        this.statement = statement;
        this.parameter = parameter;
        this.batchSize = batchSize;
    }

    public SqlContent(String statement, Object parameter, int batchSize, Integer batchType) {
        this.batchType = batchType;
        this.statement = statement;
        this.parameter = parameter;
        this.batchSize = batchSize;
    }

}
