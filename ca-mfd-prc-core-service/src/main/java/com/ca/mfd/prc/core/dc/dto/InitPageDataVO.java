package com.ca.mfd.prc.core.dc.dto;

import com.ca.mfd.prc.core.prm.entity.DcButtonConfigEntity;
import com.ca.mfd.prc.core.prm.entity.DcFieldConfigEntity;
import com.ca.mfd.prc.core.prm.entity.DcPageConfigEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author inkelink
 */
@Schema(title = "InitPageDataVO", description = "InitPageDataVO")
@Data
public class InitPageDataVO implements Serializable {

    /**
     * 页面配置
     */
    private DcPageConfigEntity pageInfo;

    /**
     * 按钮列表
     */
    private List<DcButtonConfigEntity> buttons = new ArrayList<>();

    /**
     * 字段列表
     */
    private List<DcFieldConfigEntity> fileds = new ArrayList<>();
}
