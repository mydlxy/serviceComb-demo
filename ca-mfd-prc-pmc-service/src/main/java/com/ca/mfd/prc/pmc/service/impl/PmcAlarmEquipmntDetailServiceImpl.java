package com.ca.mfd.prc.pmc.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.enums.ConditionDirection;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.enums.ConditionRelation;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.ArraysUtils;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.pmc.dto.TeamLeaderInfoDTO;
import com.ca.mfd.prc.pmc.dto.TeamleaderFillInfoDTO;
import com.ca.mfd.prc.pmc.entity.PmcAlarmEquipmntDetailEntity;
import com.ca.mfd.prc.pmc.entity.PmcStopCodeEntity;
import com.ca.mfd.prc.pmc.mapper.IPmcAlarmEquipmntDetailMapper;
import com.ca.mfd.prc.pmc.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pmc.remote.app.pm.entity.PmTeamLeaderConfigEntity;
import com.ca.mfd.prc.pmc.remote.app.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.pmc.remote.app.pm.provider.PmTeamLeaderConfigProvider;
import com.ca.mfd.prc.pmc.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.pmc.service.IPmcAlarmEquipmntDetailService;
import com.ca.mfd.prc.pmc.service.IPmcStopCodeService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 单个设备报警数据沉淀
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@Service
public class PmcAlarmEquipmntDetailServiceImpl extends AbstractCrudServiceImpl<IPmcAlarmEquipmntDetailMapper, PmcAlarmEquipmntDetailEntity> implements IPmcAlarmEquipmntDetailService {

    private static final Object LOCK_OBJ = new Object();
    @Autowired
    private PmTeamLeaderConfigProvider pmTeamLeaderConfigProvider;
    @Autowired
    private IPmcStopCodeService pmcStopCodeService;
    @Autowired
    private PmVersionProvider pmVersionProvider;
    @Autowired
    private IdentityHelper identityHelper;

    @Override
    public List<PmcAlarmEquipmntDetailEntity> getAlarmDetailList(String shopCode, String startTime, String endTime, String postion) {
        List<ConditionDto> conditionInfos = new ArrayList<>();
        ConditionDto dto = new ConditionDto();
        dto.setColumnName("WORKSHOP_CODE");
        dto.setValue(shopCode);
        conditionInfos.add(dto);

        dto = new ConditionDto();
        dto.setColumnName("POSITION");
        dto.setValue(postion);
        dto.setOperator(ConditionOper.RightLike);
        conditionInfos.add(dto);

        if (!Strings.isNullOrEmpty(startTime)) {
            dto = new ConditionDto();
            dto.setColumnName("BEGIN_DT");
            dto.setValue(startTime);
            dto.setOperator(ConditionOper.GreaterThanEqual);
            conditionInfos.add(dto);
        }
        if (!Strings.isNullOrEmpty(endTime)) {
            dto = new ConditionDto();
            dto.setColumnName("END_DT");
            dto.setValue(endTime);
            dto.setOperator(ConditionOper.LessThanEqual);
            conditionInfos.add(dto);
        }

        SortDto sortDto = new SortDto();
        sortDto.setColumnName("BEGIN_DT");
        sortDto.setDirection(ConditionDirection.ASC);
        List<SortDto> sorts = Arrays.asList(sortDto);
        return this.getData(conditionInfos, sorts, false);
    }


    @Override
    public List<PmcAlarmEquipmntDetailEntity> getTeamleaderData(String userId) {
        List<ConditionDto> conditionDtos = new ArrayList<>();
        conditionDtos.add(new ConditionDto("ucUserId", userId, ConditionOper.Equal));
        List<PmTeamLeaderConfigEntity> data = pmTeamLeaderConfigProvider.getData(conditionDtos);
        List<TeamLeaderInfoDTO> teamLeaderInfos = data.stream().map(c -> {
            TeamLeaderInfoDTO dto = new TeamLeaderInfoDTO();
            dto.setLineCode(c.getLineCode());
            dto.setTeamNo(c.getTeamNo());
            return dto;
        }).collect(Collectors.toList());
        if (teamLeaderInfos.isEmpty()) {
            return new ArrayList<>();
        }
        for (TeamLeaderInfoDTO item : teamLeaderInfos) {
            PmLineEntity areaInfo = pmVersionProvider.getObjectedPm().getLines().stream().filter(c -> c.getLineCode().equals(item.getLineCode())).findFirst().orElse(null);
            item.getWorkstationCodes().addAll(pmVersionProvider.getObjectedPm().getStations().stream().filter(c -> c.getPrcPmLineId().equals(areaInfo.getId()) && c.getTeamNo().equals(item.getTeamNo())).map(PmWorkStationEntity::getWorkstationName).collect(Collectors.toList()));
        }

        List<String> areaCodes = teamLeaderInfos.stream().map(TeamLeaderInfoDTO::getLineCode).distinct().collect(Collectors.toList());

        List<ConditionDto> dtos = new ArrayList<>();
        dtos.add(new ConditionDto("lineCode", String.join("|", areaCodes), ConditionOper.In));
        dtos.add(new ConditionDto("isFill", "0", ConditionOper.Equal));
        dtos.add(new ConditionDto("duration", "0", ConditionOper.GreaterThan));
        List<PmcAlarmEquipmntDetailEntity> datas = this.getData(dtos);

        List<PmcAlarmEquipmntDetailEntity> returndata = datas.stream().filter(c -> Strings.isNullOrEmpty(c.getWorkplaceName())).collect(Collectors.toList());

        for (PmcAlarmEquipmntDetailEntity item : datas) {
            String wname = teamLeaderInfos.stream().flatMap(c -> c.getWorkstationCodes().stream()).filter(v -> v.equals(item.getWorkplaceName())).findFirst().orElse(null);
            if (!Strings.isNullOrEmpty(wname)) {
                returndata.add(item);
            }
        }
        return returndata.stream().sorted(Comparator.comparing(PmcAlarmEquipmntDetailEntity::getBeginDt)).collect(Collectors.toList());
    }

    @Override
    public void teamleaderFill(TeamleaderFillInfoDTO para) {
        synchronized (LOCK_OBJ) {
            List<ConditionDto> conditionDtos = new ArrayList<>();
            conditionDtos.add(new ConditionDto("id", para.getStopCodeId(), ConditionOper.Equal));
            PmcStopCodeEntity pmcStopCodeEntity = pmcStopCodeService.getData(conditionDtos).stream().findFirst().orElse(null);
            if (pmcStopCodeEntity == null) {
                throw new InkelinkException("未找到该停线代码，请查询下配置或刷新列表");
            }
            List<ConditionDto> dtos = new ArrayList<>();
            dtos.add(new ConditionDto("id", para.getId(), ConditionOper.Equal));
            if (this.getData(dtos).stream().findFirst().orElse(null).getIsFill()) {
                throw new InkelinkException("该数据已被处理，请刷新列表");
            }

            String userCode = identityHelper.getLoginUser().getCode();
            String nikcName = identityHelper.getLoginUser().getNickName();

            UpdateWrapper<PmcAlarmEquipmntDetailEntity> updateWrapper = new UpdateWrapper<>();
            LambdaUpdateWrapper<PmcAlarmEquipmntDetailEntity> lambdaUpdateWrapper = updateWrapper.lambda();
            lambdaUpdateWrapper.eq(PmcAlarmEquipmntDetailEntity::getId, para.getId());
            lambdaUpdateWrapper.set(PmcAlarmEquipmntDetailEntity::getIsFill, true);
            lambdaUpdateWrapper.set(PmcAlarmEquipmntDetailEntity::getFillUserNo, userCode);
            lambdaUpdateWrapper.set(PmcAlarmEquipmntDetailEntity::getFillUserNick, nikcName);
            lambdaUpdateWrapper.set(PmcAlarmEquipmntDetailEntity::getStopCode, pmcStopCodeEntity.getStopCode());
            lambdaUpdateWrapper.set(PmcAlarmEquipmntDetailEntity::getStopName, pmcStopCodeEntity.getStopName());
            lambdaUpdateWrapper.set(PmcAlarmEquipmntDetailEntity::getStopCodeDesc, pmcStopCodeEntity.getStopCodeDesc());
            lambdaUpdateWrapper.set(PmcAlarmEquipmntDetailEntity::getStopType, pmcStopCodeEntity.getStopType());
            lambdaUpdateWrapper.set(PmcAlarmEquipmntDetailEntity::getStopTypeCode, pmcStopCodeEntity.getStopTypeCode());
            lambdaUpdateWrapper.set(PmcAlarmEquipmntDetailEntity::getStopDepartment, pmcStopCodeEntity.getStopDepartment());
            lambdaUpdateWrapper.set(PmcAlarmEquipmntDetailEntity::getStopCauseType, pmcStopCodeEntity.getStopCauseType());
            this.update(updateWrapper);
            this.saveChange();


        }
    }

    @Override
    public int getNotFinishCount(String shopCode, String postion, Date startTime, Date endTime) {
        List<ConditionDto> conditionDtos = new ArrayList<>();
        conditionDtos.add(new ConditionDto("workshopCode", shopCode, ConditionOper.Equal));
        conditionDtos.add(new ConditionDto("beginDt", DateUtils.format(startTime, DateUtils.DATE_TIME_PATTERN), ConditionOper.GreaterThanEqual));
        conditionDtos.add(new ConditionDto("endDt", DateUtils.format(endTime, DateUtils.DATE_TIME_PATTERN), ConditionOper.LessThanEqual));

        ConditionDto endDt = new ConditionDto("endDt", null, ConditionOper.Equal, ConditionRelation.And, "endDt");
        endDt.setGroupRelation(ConditionRelation.Or);
        ConditionDto endDt2 = new ConditionDto("endDt", DateUtils.format(endTime, DateUtils.DATE_TIME_PATTERN), ConditionOper.GreaterThanEqual, ConditionRelation.And, "endDt");
        endDt2.setGroupRelation(ConditionRelation.Or);
        conditionDtos.add(endDt);
        conditionDtos.add(endDt2);
        List<PmcAlarmEquipmntDetailEntity> countLinq = this.getData(conditionDtos);

        List<String> positionStrList = new ArrayList<>();

        List<String> positons = ArraysUtils.splitNoEmpty(postion, ",");
        int index = 0;
        for (String item : positons) {
            String positionStr = item;
            if (!positionStr.contains("++")) {
                positionStr += "++";
            }
            String[] positionStrs = positionStr.split("\\+\\+");
            if (positionStrs.length > 1 && !positionStrs[1].contains("+")) {
                positionStr += "+";
            }

            positionStrList.add(positionStr);
        }

        if (!positionStrList.isEmpty()) {
            List<String> collect = countLinq.stream().map(PmcAlarmEquipmntDetailEntity::getPosition).collect(Collectors.toList());
            index = (int) collect.stream().filter(positons::contains).count();
        }

        return index;
    }
}