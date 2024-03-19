package com.ca.mfd.prc.pqs.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: joel
 * @Date: 2023-04-13-11:00
 * @Description:
 */
@Data
public class CheckItemResult {
    List<PqsEntryCheckItemDto> checkItem;
    private Boolean recheck = false;

    public CheckItemResult(Boolean recheck, List<PqsEntryCheckItemDto> checkItem) {
        this.recheck = recheck;
        this.checkItem = checkItem;
    }
}
