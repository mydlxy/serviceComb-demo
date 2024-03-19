import com.ca.mfd.prc.PpsServiceApplication;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@SpringBootTest(classes = PpsServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseMvcTester {
    @Autowired
    protected WebApplicationContext wac;

    protected MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).alwaysExpect(MockMvcResultMatchers.status().isOk())
                .build();
    }

    /**
     * 封装Post方式测试用例MockMvc的公用代码
     *
     * @param url        要测试的请求url,例如"/XXAction/save"
     * @param resultType 1或0
     * @param paramKV    以参数名:参数值 的方式mockPost(url,"name","dourx","age",15)
     * @throws Exception
     */
    protected String mockPost(String url, String resultType, String... paramKV) throws Exception {
        Map<String, String> param = null;
        //if ((paramKV != null) && (paramKV.length > 0)) {
        param = new HashMap<String, String>();
        for (int i = 0; i < paramKV.length; i = i + 2) {
            param.put(paramKV[i], paramKV[i + 1]);

        }
        //}
        return mockReqTest(url, "post", param, resultType);
    }

    /**
     * 封装GET方式测试用例MockMvc的公用代码
     *
     * @param url        要测试的请求url,例如"/XXAction/save"
     * @param resultType 1或0
     * @param paramKV    以参数名:参数值 的方式mockGet(url,"name","dourx","age",15)
     * @throws Exception
     * @throws UnsupportedEncodingException
     */
    protected String mockGet(String url, String resultType, String... paramKV) throws Exception, UnsupportedEncodingException {
        Map<String, String> param = null;
        //if ((paramKV != null) && (paramKV.length > 0)) {
        param = new HashMap<String, String>();
        for (int i = 0; i < paramKV.length; i = i + 2) {
            param.put(paramKV[i], paramKV[i + 1]);

        }
        //}
        return mockReqTest(url, "get", param, resultType);
    }

    /**
     * 封装测试用例MockMvc的公用代码
     *
     * @param url   要测试的请求url,例如"/XXAction/save"
     * @param param 请求的参数（多个）,例如:data:input
     * @throws Exception
     */
    protected String mockReqTest(String url, String method, Map<String, String> param, String result) throws Exception {

        /* 设置request的url和参数 */
        MockHttpServletRequestBuilder requestBuilder = "post".equals(method) ? MockMvcRequestBuilders.post(url)
                .characterEncoding("utf-8") : MockMvcRequestBuilders.get(url).characterEncoding("utf-8");
        if (param != null) {
            //param.put("token", token);
            Set<Entry<String, String>> entrySet = param.entrySet();
            for (Entry<String, String> entry : entrySet) {
                requestBuilder.param(entry.getKey(), entry.getValue());
            }
        }

        MvcResult mvcResult = this.mockMvc.perform(requestBuilder).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(result)).andReturn();
        return mvcResult.getResponse().getContentAsString();

    }
}
