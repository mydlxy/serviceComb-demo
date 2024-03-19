package com.ca.mfd.prc.avi.remote.app.pm.dto;

import com.ca.mfd.prc.avi.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmOtEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmWorkStationEntity;
import lombok.Data;

import java.util.List;

/**
 * @author 阳波
 * @ClassName OtDto
 * @description:
 * @date 2023年09月18日
 * @version: 1.0
 */
@Data
public class OtDto {
    private List<PmWorkShopEntity> pmShopInfos;
    private List<PmLineEntity> pmAreaInfos;
    private List<PmWorkStationEntity> pmStationInfos;
    private List<PmOtEntity> pmOtInfos;

    public OtDto(List<PmWorkShopEntity> pmShopInfos,
                 List<PmLineEntity> pmAreaInfos,
                 List<PmWorkStationEntity> pmStationInfos,
                 List<PmOtEntity> pmOtInfos) {
        this.pmShopInfos = pmShopInfos;
        this.pmAreaInfos = pmAreaInfos;
        this.pmStationInfos = pmStationInfos;
        this.pmOtInfos = pmOtInfos;
    }

}
