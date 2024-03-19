package com.ca.mfd.prc;


import com.ca.mfd.prc.avi.entity.AviOperEntity;
import com.ca.mfd.prc.avi.service.IAviOperService;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
public class AviTest {

    @Autowired
    IAviOperService aviOperService;


    @Test
    public void getData() {
        List<ConditionDto> conditionDtos = new ArrayList<>();
        ConditionDto condition = new ConditionDto("groupName", "key", ConditionOper.Equal);
        conditionDtos.add(condition);
        List<AviOperEntity> list = aviOperService.getData(conditionDtos);
//        Assert.assertEquals(list, null);


    }
}
