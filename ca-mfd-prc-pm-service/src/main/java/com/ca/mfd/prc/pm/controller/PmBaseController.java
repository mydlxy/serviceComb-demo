package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.utils.ClassUtil;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.entity.PmBopEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author 阳波
 * @ClassName PmBaseController
 * @description:
 * @date 2023年11月22日
 * @version: 1.0
 */
public class PmBaseController<T extends BaseEntity> extends BaseController<T> {
    @Override
    @PostMapping(value = "edit", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "更新")
    public ResultVO<?> edit(@RequestBody @Valid T dto) {
        ClassUtil.defaultValue(dto);
        Long id = crudService.currentModelGetKey(dto);
        if (id == null || id <= 0) {
            crudService.save(dto);
        } else {
            crudService.update(dto);
        }
        crudService.saveChange();
        return new ResultVO<>().ok(dto, "保存成功");
    }
}
