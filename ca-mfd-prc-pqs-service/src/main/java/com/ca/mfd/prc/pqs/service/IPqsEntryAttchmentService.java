package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.dto.AttchmentDto;
import com.ca.mfd.prc.pqs.entity.PqsEntryAttchmentEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 质检附件服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsEntryAttchmentService extends ICrudService<PqsEntryAttchmentEntity> {

    /**
     * 获取质检附件数据
     *
     * @return
     */
    List<PqsEntryAttchmentEntity> getAllDatas();

    /**
     * 获取附件列表
     *
     * @param inspectionNo 质检工单号
     * @return 附件列表
     */
    List<AttchmentDto> getAttachMent(String inspectionNo);

    /**
     * 保存附件
     *
     * @param attchmentDto 附件
     */
    void saveAttachMent(AttchmentDto attchmentDto);

    /**
     * 删除附件列表
     *
     * @param id 附件ID
     */
    void deleteAttachMent(Long id);
}