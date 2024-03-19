package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.pps.communication.dto.MidLmsAviQueueDto;
import com.ca.mfd.prc.pps.dto.AsSendRptDTO;
import com.ca.mfd.prc.pps.dto.InsertAsAviPointInfo;
import com.ca.mfd.prc.pps.entity.PpsAsAviPointEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;
import java.util.Map;

/**
 * @author inkelink
 * @Description: AS车辆实际过点服务
 * @date 2023年10月18日
 * @变更说明 BY inkelink At 2023年10月18日
 */
public interface IPpsAsAviPointService extends ICrudService<PpsAsAviPointEntity> {

    /**
     * 写入as中间表的过点，报工数据
     *
     * @param data
     */
    void insertAsAviPoint(InsertAsAviPointInfo data);

    /**
     * 获取未发送数据
     *
     * @return
     */
    List<PpsAsAviPointEntity> getNoSendList(Integer top);

    /**
     * 获取Lms未发送数据
     *
     * @param top
     * @return
     */
    List<MidLmsAviQueueDto> getNoLmsSendList(Integer top);

    /**
     * 获取未发送数据(批次反馈数据)
     *
     * @return
     */
    List<PpsAsAviPointEntity> getNoAsBatchPieces(Integer top);


    /**
     * 获取零部件过点数据
     *
     * @return
     */
    List<PpsAsAviPointEntity> getPartSendList();

    /**
     * 更新零部件标识
     *
     * @param ids 主键ids
     */
    void updatePartSendStatus(List<Long> ids);

    /**
     * 写入as中间表的过点 AVI过点补录
     *
     * @param vehicleSn
     * @param aviCode
     * @param AviType
     */
    void insertDataAsAviPoint(String vehicleSn, String aviCode, int AviType);

    /**
     * 更新发送状态
     *
     * @param ids 主键ids
     */
    void updateAsSendStatus(List<Long> ids);

    /**
     * 获取AS统计数据
     *
     * @param pms 查询条数
     * @return 数据
     */
    List<AsSendRptDTO> getAsSendRpt(Map pms);
}