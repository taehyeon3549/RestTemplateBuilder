package builder.resttemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winitech.cs.opendata.VO.OpenModel;
import com.winitech.cs.resttemplate.service.WiniRestTempleBuilder;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;

//@TestPropertySource("classpath:/application.properties")
//@ContextConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WiniRestTempleBuilder.class})
@ActiveProfiles("test")
public class ResttemplateBuilderTest {
    @Value("${serviceKey}") private String SERVICE_KEY;
    @Value("${service.baseurl}") private String DATA_BASE_URL;

    @Test
    public void contextLoads() {
    }

    @Test
    public void 동네예보검색() throws Exception{
        System.out.println(DATA_BASE_URL);

//        ResponseEntity<Object> result;
        String result;

        result = new WiniRestTempleBuilder()
                .requestUriAndPath(DATA_BASE_URL
                        , "/VilageFcstInfoService_2.0/getVilageFcst"
                        , new LinkedMultiValueMap<>() {{
                            add("serviceKey", SERVICE_KEY);
                            add("pageNo", "1");
                            add("dataType", "JSON");
                            add("base_date", "20220207");
                            add("base_time", "1200");
                            add("nx", "57");
                            add("ny", "127");
                        }})
                .getForObject();

        OpenModel getVilageFcst = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .convertValue(result, OpenModel.class);

//        Assertions.assertSame(result.getStatusCode(),HttpStatus.OK);
        Assertions.assertNotNull(getVilageFcst.getBody());

    }


}
