package com.ca;

import com.ca.mfd.prc.pm.service.IPmVersionService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PmTester {

    @Autowired
    IPmVersionService pmVersionService;

    @Test
    public void test() {
        List<Date> dates = new ArrayList<>();
        dates.add(new Date());
        for (int i = 0; i < 10; i++) {
            pmVersionService.getObjectedPm();
            dates.add(new Date());
        }
        for (int i = 0; i < dates.stream().count() - 1; i++) {
            System.out.println(i + ":" + (dates.get(i + 1).getTime() - dates.get(i).getTime()));
        }
//        Date date1 = new Date();
//        PmAllDTO dto = pmVersionService.getObjectedPM();
//        Date date2 = new Date();
//        System.out.println (date2.getTime() - date1.getTime());
    }
}
