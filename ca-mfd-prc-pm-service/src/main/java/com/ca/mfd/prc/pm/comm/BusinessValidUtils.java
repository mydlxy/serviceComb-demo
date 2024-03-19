package com.ca.mfd.prc.pm.comm;

import com.ca.mfd.prc.pm.remote.app.core.sys.entity.SysConfigurationEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author 阳波
 * @ClassName PmBusinessValidUtils
 * @description:
 * @date 2023年09月26日
 * @version: 1.0
 */
public class BusinessValidUtils {
    private static final String ALL_MODELS = "ALLMODELS";
    private static final String ALL_TYPES = "ALLTYPES";

    public static boolean checkVehicleOption(String options,
                                             List<String> vehicleMasterFeatures,
                                             List<SysConfigurationEntity> vehicleModles) {
        boolean isRight = false;
        if (StringUtils.isBlank(options)) {
            return isRight;
        }
        String[] vehicleModels = options.split(";");
        if (vehicleModels.length == 0) {
            return isRight;
        }
        for (String vehicleModel : vehicleModels) {
            String[] splitMdAndFtus = vehicleModel.split(":");

            //车辆特征数据中的数据还不完善，所以该段暂未启用
            //判断车型在车辆特征数据中是否存在
            String modelValue = splitMdAndFtus[0];
            if (!ALL_MODELS.equalsIgnoreCase(modelValue)
                    && !vehicleModles.stream().anyMatch(v -> Objects.equals(v.getValue(), modelValue))) {
                isRight = false;
                return isRight;
            } else {
                isRight = true;
            }
            if (splitMdAndFtus.length == 1) {
                continue;
            }
            String characteristicExpressions = splitMdAndFtus[1];
            if (characteristicExpressions.equalsIgnoreCase(ALL_TYPES)) {
                isRight = true;
                continue;
            }
            String[] orExpressions = characteristicExpressions.split("\\+");
            for (String orExpression : orExpressions) {
                String[] andExpressions = orExpression.split("\\*");
                boolean andExp = true;
                for (String andExpression : andExpressions) {
                    andExp = andExpressionHandel(andExpression, vehicleMasterFeatures);
                    if (!andExp) {
                        break;
                    }
                }
                if (andExp) {
                    isRight = true;
                    break;
                }
            }
        }
        return isRight;
    }

    private static boolean andExpressionHandel(String andExpression, List<String> vehicleMasterFeatures) {
        boolean res = true;
        if (andExpression.endsWith("!")) {
            String charact = com.ca.mfd.prc.common.utils.StringUtils.trimEnd(andExpression, "!");
            if (vehicleMasterFeatures.contains(charact)) {
                res = false;
            }
        } else {
            String charact = com.ca.mfd.prc.common.utils.StringUtils.trimEnd(andExpression, "#");
            if (!vehicleMasterFeatures.contains(charact)) {
                res = false;
            }
        }
        return res;
    }
}
