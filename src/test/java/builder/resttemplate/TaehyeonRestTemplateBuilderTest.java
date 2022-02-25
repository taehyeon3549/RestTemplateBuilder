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
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CustomRestTemplateConfig.class})
public class TaehyeonRestTemplateBuilderTest {
    @Autowired private RestTemplate taehyeonRestTemplate;
    @Autowired private TaehyeonRestTempleBuilder taehyeonRestTempleBuilder;

    @Autowired private RestTemplate restTemplate;
    @Autowired private RestTemplateBuilder restTemplateBuilder;

    @Value("${service.baseurl}") private String DATA_BASE_URL;
    @Value("${serviceKey}") private String SERVICE_KEY;

    @Test
    public void Autowired테스트(){
        Assertions.assertNotNull(taehyeonRestTemplate);
        Assertions.assertNotNull(taehyeonRestTempleBuilder);

        Assertions.assertNotNull(restTemplate);
        Assertions.assertNotNull(restTemplateBuilder);
    }

    @Test
    public void TaehyeonRestTemplateBeanAndTaehyeonRestTempleBuildBean주입테스트(){
        System.out.println(taehyeonRestTemplate.getRequestFactory());
        System.out.println(taehyeonRestTempleBuilder.build().getRequestFactory());

        Assertions.assertEquals(taehyeonRestTemplate.getRequestFactory(), taehyeonRestTempleBuilder.build().getRequestFactory());
    }

    @Test
    public void TaehyeonRestTemplateBuilder테스트() throws Exception{
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

    @Test
    public void TaehyeonRestTemplateBuilder와기존RestTemplateBuiler테스트() throws Exception{
        // taehyeonRestTempleBuilder 설정
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
        // taehyeonRestTempleBuilder 설정 끝


        // 기존 RestTemplate 설정
        URI uri = UriComponentsBuilder
                .fromHttpUrl(DATA_BASE_URL)
                .path("/VilageFcstInfoService_2.0/getVilageFcst")
                .queryParams(new LinkedMultiValueMap<>() {{
                    add("serviceKey", SERVICE_KEY);
                    add("pageNo", "1");
                    add("dataType", "JSON");
                    add("base_date", "20220207");
                    add("base_time", "1200");
                    add("nx", "57");
                    add("ny", "127");
                }})
                .build()
                .toUri();


        RestTemplate restTemplate1 =restTemplateBuilder
                .additionalMessageConverters(new StringHttpMessageConverter(Charset.forName("UTF-8")))
                .build();

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);

        restTemplate1.setMessageConverters(messageConverters);

        ResponseEntity<Object> result1 = restTemplate1.exchange(new URI(URLDecoder.decode(uri.toString(), "UTF-8")), HttpMethod.GET,null, Object.class);

        OpenModel t1 = new ObjectMapper().convertValue(result1.getBody(), OpenModel.class);

        System.out.println(t1.getBody());
        // 기존 RestTemplate 설정 끝


        // 값 비교
        Assertions.assertEquals(t,t1);
    }
}
