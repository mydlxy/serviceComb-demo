package com.ca.mfd.prc.core.prm.dto;

import com.ca.mfd.prc.core.dc.dto.DcDetailVO;
import lombok.Data;

import java.util.List;

@Data
public class PermissionsVo {
    List<DcDetailVO> permissions;
}
