package com.ca.mfd.prc.pmc.service.impl;

import com.ca.mfd.prc.pmc.service.IEqumentConsume;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author inkelink
 * @date 2023年4月4日
 */
@Service
public class EqumentConsumeImpl implements IEqumentConsume {

    private static final Logger logger = LoggerFactory.getLogger(EqumentConsumeImpl.class);


    @Override
    @Async
    public void start() {
    }
}
