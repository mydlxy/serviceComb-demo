package com.ca.mfd.prc.pm.remote.app.core.provider;


import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.remote.app.core.ISysSequenceNumberService;
import com.ca.mfd.prc.pm.remote.app.core.sys.entity.SysSequenceNumberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysSequenceNumberProvider {
    @Autowired
    ISysSequenceNumberService sysSequenceNumberService;

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

    /**
     * 新增
     *
     * @param entity 实体
     */
    public void insert(SysSequenceNumberEntity entity) {
        ResultVO<String> result = sysSequenceNumberService.insert(entity);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-syssequencenumber调用失败" + result.getMessage());
        }
    }

    public Long getSequenceTypeCount(String sequenceType){
        ResultVO<Long> result = sysSequenceNumberService.getSequenceTypeCount(sequenceType);
        if(!result.getSuccess()){
            throw new InkelinkException("服务inkelink-core-syssequencenumber调用失败" + result.getMessage());
        }else{
            return result.getData();
        }
    }
}
