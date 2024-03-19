package com.ca.mfd.prc.pmc.remote.app.pm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class AtomicTime {
    /**
     *
     */
    private Date startTime;
    /**
     *
     */
    private Date endTime;

    public AtomicTime() {

    }
}
