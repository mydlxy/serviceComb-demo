package com.ca.mfd.prc.pqs.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pqs.dto.PqsEntryProcessReportingDto;
import com.ca.mfd.prc.pqs.entity.PqsEntryProcessEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author inkelink
 * @Description: 过程检验Mapper
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Mapper
public interface IPqsEntryProcessMapper extends IBaseMapper<PqsEntryProcessEntity> {

    /**
     * 根据条件查询
     *
     * @param areaCode
     * @param processCode
     * @param entryType
     * @param key
     * @param status
     * @return
     */
    List<PqsEntryProcessReportingDto> getDataByCondition(String areaCode, String processCode, String entryType, String key, List<Integer> status);
}