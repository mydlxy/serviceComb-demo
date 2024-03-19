package com.ca.mfd.prc.pmc.ability.hc;

import com.ca.mfd.prc.pmc.service.IPmcIpcConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class IpcRegisterRunner implements ApplicationRunner {

    @Autowired
    IPmcIpcConfigService ipcConfigService;

    @Override
    public void run(ApplicationArguments args) {
        ipcConfigService.registerIpc(false);
    }
}
