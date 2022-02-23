package builder.resttemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.winitech.cs.opendata.VO.OpenModel;
import com.winitech.cs.restTemple.TaehyeonRestTempleBuilder;
import com.winitech.cs.restTemple.CustomRestTemplateConfig;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CustomRestTemplateConfig.class})
public class TaehyeonRestTemplateBuilderTest {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TaehyeonRestTempleBuilder taehyeonRestTempleBuilder;

    @Value("${service.baseurl}") private String DATA_BASE_URL;
    @Value("${serviceKey}") private String SERVICE_KEY;

    @Test
    public void Autowired테스트(){
        Assertions.assertNotNull(restTemplate);
        Assertions.assertNotNull(taehyeonRestTempleBuilder);
    }

    @Test
    public void RestTemplateBeanAndWiniRestTempleBuildBean주입테스트(){
        System.out.println(restTemplate.getRequestFactory());
        System.out.println(taehyeonRestTempleBuilder.build().getRequestFactory());

        Assertions.assertEquals(restTemplate.getRequestFactory(), taehyeonRestTempleBuilder.build().getRequestFactory());
    }

    @Test
    public void WiniRestTemplateBuilder테스트() throws Exception{
        ResponseEntity<Object> result = taehyeonRestTempleBuilder
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
                .get();

        OpenModel t = new ObjectMapper().convertValue(result.getBody(), OpenModel.class);

        System.out.println(t.getBody());

        Assertions.assertNotNull(t.getBody());
    }
}
