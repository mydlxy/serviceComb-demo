package com.ca.mfd.prc.core.main.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.main.entity.SysSequenceNumberEntity;

import java.util.List;

/**
 * 业务编码配规则配置
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
public interface ISysSequenceNumberService extends ICrudService<SysSequenceNumberEntity> {
    /**
     * 获取所有的数据
     *
     * @return
     */
    List<SysSequenceNumberEntity> getAllDatas();

    /**
     * 获取流水号/需要提交统一事务，如果在循环中使用生成的流水号是一个
     *
     * @param seqType 流水号类别
     * @return 流水号
     */
    String getSeqNumWithTransaction(String seqType);

    /**
     * 查询sequenceType的数量
     *
     * @param sequenceType 流水号类型
     * @return 数量
     */
    Long getSequenceTypeCount(String sequenceType);

    /**
     * 获取序列号（自动增长）
     *
     * @param squenceType 水水好类别
     * @return 流水号
     */
    String getSeqNum(String squenceType);

    /**
     * 判断是否已经配置了工单生成规则
     *
     * @param sequenceType 流水号类型
     * @return 查询实体
     */
    SysSequenceNumberEntity getSysSequenceInfoByType(String sequenceType);
}