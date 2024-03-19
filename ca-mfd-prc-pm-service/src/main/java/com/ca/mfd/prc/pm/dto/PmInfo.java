package com.ca.mfd.prc.pm.dto;

import com.ca.mfd.prc.pm.entity.PmAviEntity;
import com.ca.mfd.prc.pm.entity.PmBopEntity;
import com.ca.mfd.prc.pm.entity.PmEquipmentEntity;
import com.ca.mfd.prc.pm.entity.PmEquipmentPowerEntity;
import com.ca.mfd.prc.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pm.entity.PmOrganizationEntity;
import com.ca.mfd.prc.pm.entity.PmOtEntity;
import com.ca.mfd.prc.pm.entity.PmPullCordEntity;
import com.ca.mfd.prc.pm.entity.PmToolEntity;
import com.ca.mfd.prc.pm.entity.PmToolJobEntity;
import com.ca.mfd.prc.pm.entity.PmWoEntity;
import com.ca.mfd.prc.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.pm.entity.PmWorkstationMaterialEntity;
import com.ca.mfd.prc.pm.entity.PmWorkstationOperBookEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luowenbing
 * @Description: PmInfo class
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */

@Data
public class PmInfo implements Serializable {
    private PmOrganizationEntity organization;
    private List<PmWorkShopEntity> workShops = new ArrayList<>();
    private List<PmLineEntity> lines = new ArrayList<>();
    private List<PmWorkStationEntity> workStations = new ArrayList<>();
    private List<PmAviEntity> avis = new ArrayList<>();
    private List<PmPullCordEntity> pullCords = new ArrayList<>();
    private List<PmOtEntity> ots = new ArrayList<>();
    private List<PmWoEntity> wos = new ArrayList<>();
    private List<PmToolEntity> tools = new ArrayList<>();
    private List<PmToolJobEntity> toolJobs = new ArrayList<>();
    private List<PmWorkStationEntity> allWorkStations = new ArrayList<>();
    private List<PmWorkstationMaterialEntity> workstationMaterials = new ArrayList<>();
    private List<PmBopEntity> bops = new ArrayList<>();
    private List<PmEquipmentEntity> equipments = new ArrayList<>();
    private List<PmEquipmentPowerEntity> equipmentPowers = new ArrayList<>();
    private List<PmWorkstationOperBookEntity> operBooks = new ArrayList<>();

}
