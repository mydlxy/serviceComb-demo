package com.ca.mfd.prc.pps.remote.app.core.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.remote.app.core.sys.entity.SysSequenceNumberEntity;
import com.ca.mfd.prc.pps.remote.app.core.ISysSequenceNumberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * SysSequenceNumberProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service
public class SysSequenceNumberProvider {

    private static final Logger logger = LoggerFactory.getLogger(SysSequenceNumberProvider.class);

    @Autowired
    private ISysSequenceNumberService sysSequenceNumberService;

    public List<SysSequenceNumberEntity> getData(List<ConditionDto> conditions) {
        ResultVO<List<SysSequenceNumberEntity>> result = sysSequenceNumberService.getData(conditions);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-syssequencenumber调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public String getSeqNumWithTransaction(String seqType) {
        ResultVO<String> result = sysSequenceNumberService.getSeqNumWithTransaction(seqType);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-syssequencenumber调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public String getSeqNum(String sequenceType) {
        ResultVO<String> result = sysSequenceNumberService.getSeqNum(sequenceType);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-syssequencenumber调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public String insert(SysSequenceNumberEntity body) {
        ResultVO<String> result = sysSequenceNumberService.insert(body);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-syssequencenumber调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 判断是否已经配置了工单生成规则
     *
     * @param sequenceType 流水号类型
     * @return 查询实体
     */
    public SysSequenceNumberEntity getSysSequenceInfoByType(String sequenceType) {
        ResultVO<SysSequenceNumberEntity> result = sysSequenceNumberService.getSysSequenceInfoByType(sequenceType);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-syssequencenumber调用失败" + result.getMessage());
        }
        return result.getData();
    }


}