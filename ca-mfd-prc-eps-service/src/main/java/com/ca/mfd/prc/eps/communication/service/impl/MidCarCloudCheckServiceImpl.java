package com.ca.mfd.prc.eps.communication.service.impl;

import com.ca.mfd.prc.common.exception.ErrorCode;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.eps.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.eps.communication.dto.CarCloudCheckDto;
import com.ca.mfd.prc.eps.communication.dto.CheyunTestDto;
import com.ca.mfd.prc.eps.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.eps.communication.mapper.IMidCarCloudCheckMapper;
import com.ca.mfd.prc.eps.communication.entity.MidCarCloudCheckEntity;
import com.ca.mfd.prc.eps.communication.remote.app.pps.provider.PpsOrderProvider;
import com.ca.mfd.prc.eps.communication.service.IMidApiLogService;
import com.ca.mfd.prc.eps.communication.service.IMidCarCloudCheckService;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsOrderEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author inkelink
 * @Description: 终检完成数据中间表（车云）服务实现
 * @date 2023年12月12日
 * @变更说明 BY inkelink At 2023年12月12日
 */
@Service
public class MidCarCloudCheckServiceImpl extends AbstractCrudServiceImpl<IMidCarCloudCheckMapper, MidCarCloudCheckEntity> implements IMidCarCloudCheckService {
    final Logger logger = LoggerFactory.getLogger(MidCarCloudCheckServiceImpl.class);

    @Autowired
    private IMidApiLogService midApiLogBaseService;
    @Autowired
    private PpsOrderProvider ppsOrderProvider;

    @Override
    public ResultVO<CarCloudCheckDto> carCloudCheckSend(String vinCode) {

        ResultVO resultVO = new ResultVO();
        resultVO.setCode(ErrorCode.SUCCESS);
        String reqNo = UUIDUtils.getGuid();
        logger.info("车云终检推送数据信息[" + reqNo + "]开始组装数据");
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.CAR_CLOUD_CHECK);
        int status = 1;
        String errMsg = "";
        loginfo.setDataLineNo(0);
        loginfo.setRequestStartTime(new Date());

        CarCloudCheckDto dto = new CarCloudCheckDto();
        dto.setVinCode(vinCode);
        try {
            loginfo.setStatus(0);
            midApiLogBaseService.insert(loginfo);
            midApiLogBaseService.saveChange();

            PpsOrderEntity ppsOrderEntity = ppsOrderProvider.getPpsOrderBySnOrBarcode(dto.getVinCode());
            if (ppsOrderEntity == null) {
                throw new InkelinkException("未查到车辆订单数据");
            }
            if(ppsOrderEntity.getActualEndDt()!=null){
                dto.setActualEndDt(ppsOrderEntity.getActualEndDt());
            }
            dto.setWsFlag(ppsOrderEntity.getOrderStatus() == 5 ? "1" : "0");
            MidCarCloudCheckEntity entity = new MidCarCloudCheckEntity();
            BeanUtils.copyProperties(dto, entity);
            entity.setPrcMidApiLogId(loginfo.getId());
            this.insert(entity);
            this.saveChange();
        } catch (Exception ex) {
            status = 5;
            errMsg = "车云终检数据信息组装[" + reqNo + "]处理失败:";
            resultVO.setCode(ErrorCode.INTERNAL_SERVER_ERROR);
            resultVO.setMessage(ex.getMessage());
            resultVO.setData(null);
            logger.info(errMsg);
            logger.error(errMsg, ex);
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(errMsg);
        midApiLogBaseService.update(loginfo);
        midApiLogBaseService.saveChange();
        logger.info("车云终检数据信息组装[" + reqNo + "]执行完成:");

        resultVO.setData(dto);
        return resultVO;
    }

    @Override
    public ResultVO carCloudCheckSendTest(CheyunTestDto dto) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(ErrorCode.SUCCESS);
        String reqNo = UUIDUtils.getGuid();
        ;
        logger.info("车云终检推送数据信息[" + reqNo + "]开始组装数据");
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setApiType(ApiTypeConst.CAR_CLOUD_CHECK);
        int status = 1;
        String errMsg = "";
        loginfo.setDataLineNo(0);
        loginfo.setRequestStartTime(new Date());

        CarCloudCheckDto checkDto = new CarCloudCheckDto();
        checkDto.setVinCode(dto.getVin());
        try {
            loginfo.setStatus(0);
            midApiLogBaseService.insert(loginfo);
            midApiLogBaseService.saveChange();


           /* PpsOrderEntity ppsOrderEntity = ppsOrderProvider.getPpsOrderBySnOrBarcode(dto.getVin());
            if (ppsOrderEntity == null) {
                throw new InkelinkException("未查到车辆订单数据");
            }
            if(ppsOrderEntity.getActualEndDt()!=null){
                checkDto.setActualEndDt(ppsOrderEntity.getActualEndDt());
            }
            checkDto.setWsFlag(ppsOrderEntity.getOrderStatus() == 5 ? "1" : "0");*/
            checkDto.setActualEndDt(new Date());
            checkDto.setWsFlag("1");
            MidCarCloudCheckEntity entity = new MidCarCloudCheckEntity();
            BeanUtils.copyProperties(checkDto, entity);
            entity.setPrcMidApiLogId(loginfo.getId());
            this.insert(entity);
            this.saveChange();
        } catch (Exception ex) {
            status = 5;
            errMsg = "车云终检数据信息组装[" + reqNo + "]处理失败:";
            resultVO.setCode(ErrorCode.INTERNAL_SERVER_ERROR);
            resultVO.setMessage(ex.getMessage());
            resultVO.setData(null);
            logger.info(errMsg);
            logger.error(errMsg, ex);
        }
        loginfo.setRequestStopTime(new Date());
        loginfo.setStatus(status);
        loginfo.setRemark(errMsg);
        midApiLogBaseService.update(loginfo);
        midApiLogBaseService.saveChange();
        logger.info("车云终检数据信息组装[" + reqNo + "]执行完成:");

        resultVO.setData(checkDto);
        return resultVO;
    }
}