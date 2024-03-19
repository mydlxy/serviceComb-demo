package com.ca.mfd.prc.avi.communication.service;

import com.ca.mfd.prc.avi.communication.dto.SiteInfoDto;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.avi.communication.entity.MidDjSiteSendEntity;
import com.ca.mfd.prc.common.utils.ResultVO;

import java.util.List;

/**
 *
 * @Description: 过点信息下发记录服务
 * @author inkelink
 * @date 2023年12月25日
 * @变更说明 BY inkelink At 2023年12月25日
 */
public interface IMidDjSiteSendService extends ICrudService<MidDjSiteSendEntity> {

    ResultVO<List<SiteInfoDto>> querySiteInfoByVin(String vin);
}