package com.ca.mfd.prc.pps.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.IApiPtformService;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.pps.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pps.communication.controller.MidLmsWeOnlineQueueController;
import com.ca.mfd.prc.pps.communication.dto.AsResultVo;
import com.ca.mfd.prc.pps.communication.dto.MidLmsPartQueueDto;
import com.ca.mfd.prc.pps.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pps.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pps.dto.LmsWeOnlineQueueDTO;
import com.ca.mfd.prc.pps.entity.PpsAsAviPointEntity;
import com.ca.mfd.prc.pps.entity.PpsEntryEntity;
import com.ca.mfd.prc.pps.entity.PpsMainUpLineConfigEntity;
import com.ca.mfd.prc.pps.entity.PpsMainUpLineQueueEntity;
import com.ca.mfd.prc.pps.mapper.IPpsMainUpLineQueueMapper;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductBomEntity;
import com.ca.mfd.prc.pps.service.IPpsEntryService;
import com.ca.mfd.prc.pps.service.IPpsMainUpLineConfigService;
import com.ca.mfd.prc.pps.service.IPpsMainUpLineQueueService;
import com.ca.mfd.prc.pps.service.IPpsOrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @Description: 焊装主体上线队列服务实现(未)
 * @author inkelink
 * @date 2024年01月18日
 * @变更说明 BY inkelink At 2024年01月18日
 */
@Service
public class PpsMainUpLineQueueServiceImpl extends AbstractCrudServiceImpl<IPpsMainUpLineQueueMapper, PpsMainUpLineQueueEntity> implements IPpsMainUpLineQueueService {
    private static final Logger logger = LoggerFactory.getLogger(PpsMainUpLineQueueServiceImpl.class);

}