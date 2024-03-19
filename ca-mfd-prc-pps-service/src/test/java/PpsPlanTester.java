import com.ca.mfd.prc.PpsServiceApplication;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.pps.service.IPpsPlanService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = PpsServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PpsPlanTester extends BaseMvcTester {

    @Autowired
    private PmVersionProvider ppsVersionProvider;
    @Autowired
    private IPpsPlanService ppsPlanService;

    /**
     * 获取计划BOM测试
     */
    @Test
    void getPlanBomTest() throws Exception {
        String result = mockGet("http://localhost:19122/ppsplan/getplanbom", "", "planNo", "000000000001");
        Assert.assertEquals(result, "");
    }

    /**
     * 获取计划测试
     */
    @Test
    void getFirstByPlanNoTest() {
        ppsPlanService.getFirstByPlanNo("000000000001");
    }

    /**
     * 测试pm服务调用
     */
    @Test
    void checkFrontZoneTest() {
        Assert.assertEquals(ppsVersionProvider.getObjectedPm().getShops().size() > 0, true);
    }
}
