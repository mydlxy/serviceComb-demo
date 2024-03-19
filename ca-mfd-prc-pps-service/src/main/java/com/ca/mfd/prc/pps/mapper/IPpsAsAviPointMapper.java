package com.ca.mfd.prc.pps.mapper;

import com.ca.mfd.prc.pps.communication.dto.MidLmsAviQueueDto;
import com.ca.mfd.prc.pps.dto.AsSendRptDTO;
import com.ca.mfd.prc.pps.entity.PpsAsAviPointEntity;
import com.ca.mfd.prc.common.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author inkelink
 * @Description: AS车辆实际过点Mapper
 * @date 2023年10月18日
 * @变更说明 BY inkelink At 2023年10月18日
 */
@Mapper
public interface IPpsAsAviPointMapper extends IBaseMapper<PpsAsAviPointEntity> {

    /**
     * 获取未发送数据
     *
     * @return
     */
    List<PpsAsAviPointEntity> getNoSendList(Integer top);

    /**
     * 获取Lms未发送数据
     *
     * @param top 查询条数
     * @return 未发送数据
     */
    List<MidLmsAviQueueDto> getLmsNoSendList(Integer top);

    /**
     * 获取AS统计数据
     *
     * @param pms 查询条数
     * @return 数据
     */
    List<AsSendRptDTO> getAsSendRpt(Map pms);
}