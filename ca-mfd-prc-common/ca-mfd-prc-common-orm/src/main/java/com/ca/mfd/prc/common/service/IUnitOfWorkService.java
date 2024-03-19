package com.ca.mfd.prc.common.service;

/**
 * 添加对象
 *
 * @author inkelink
 * @since 1.0.0
 */
public interface IUnitOfWorkService {

    /**
     * 添加对象
     *
     * @param parameter
     * @param statement
     * @return
     */
    Boolean addContent(String statement, Object parameter);

    /**
     * 添加对象
     *
     * @param parameter
     * @param statement
     * @param batchSize
     * @return
     */
    Boolean addContent(String statement, Object parameter, int batchSize);

    /**
     * 添加对象
     *
     * @param parameter
     * @param statement
     * @param batchSize
     * @return
     */
    Boolean addContent(String statement, Object parameter, int batchSize, Integer batchType);

    /**
     * 保存变更
     */
    void saveChange();

    /**
     * 清空
     */
    void clearChange();
}
