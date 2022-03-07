package com.winitech.cs.restTemple;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 *
 * CustomRestTemplateConfig.
 *
 * <p>
 *     커스텀 RestTemplate 설정 및 Default RestTemplate 설정
 * </p>
 *
 * @author CS 김태현
 * @version 1.0.0
 * @since 2022-03-07
 * @modify
 * <p>
 * 수정일      수정자      수정내용<br>
 * ----------  --------   ---------------------------<br>
 * 2022-03-07  CS 김태현   최초작성 <br>
 * </p>
 *
 * @see CustomRestTemplateCustomizer
 * @see TaehyeonRestTempleBuilder
 *
**/
@Configuration
public class CustomRestTemplateConfig {
    @Value("${RestTemplate.HttpClient.MaxConnTotal}") private int MAX_CONN_TOTAL;
    @Value("${RestTemplate.HttpClient.MaxConnPerRoute}") private int MAX_CONN_PER_ROUTE;
    @Value("${RestTemplate.HttpRequest.ReadTimeout}") private int READ_TIMEOUT;
    @Value("${RestTemplate.HttpRequest.ConnectionTimeOut}") private int CONNECTION_TIMEOUT;


    CustomRestTemplateConfig(){
        this.MAX_CONN_TOTAL = this.MAX_CONN_TOTAL==0 ? 100 : this.MAX_CONN_TOTAL;
        this.MAX_CONN_PER_ROUTE = this.MAX_CONN_PER_ROUTE==0 ? 5 : this.MAX_CONN_PER_ROUTE;
        this.READ_TIMEOUT = this.READ_TIMEOUT==0 ? 5000 : this.READ_TIMEOUT;
        this.CONNECTION_TIMEOUT = this.CONNECTION_TIMEOUT==0 ? 3000 : this.CONNECTION_TIMEOUT;
    }

    @Bean
    HttpClient httpClient() {
        return HttpClientBuilder.create()
                .setMaxConnTotal(MAX_CONN_TOTAL)    //최대 오픈되는 커넥션 수
                .setMaxConnPerRoute(MAX_CONN_PER_ROUTE)   //IP, 포트 1쌍에 대해 수행할 커넥션 수
                .build();
    }

    @Bean
    HttpComponentsClientHttpRequestFactory factory(HttpClient httpClient) {
        return new HttpComponentsClientHttpRequestFactory(){
            {
                setReadTimeout(READ_TIMEOUT);        //읽기시간초과, ms
                setConnectTimeout(CONNECTION_TIMEOUT);     //연결시간초과, ms
                setHttpClient(httpClient);
            }
        };
    }

    @Bean
    public CustomRestTemplateCustomizer customRestTemplateCustomizer() {
        return new CustomRestTemplateCustomizer();
    }

    @Bean
    @DependsOn(value = {"customRestTemplateCustomizer"})
    public TaehyeonRestTempleBuilder taehyeonRestTemplateBuilder(HttpComponentsClientHttpRequestFactory factory, CustomRestTemplateCustomizer customRestTemplateCustomizer) {
        /* 상속받은 Builder를 통한 build를 시키면 return이 super의 RestTemplate 이고, sub의 TaehyeonRestTempleBuilder로 casting이 안됨 */
        TaehyeonRestTempleBuilder taehyeonRestTempleBuilder = new TaehyeonRestTempleBuilder(customRestTemplateCustomizer);

        taehyeonRestTempleBuilder.requestFactory(() -> new BufferingClientHttpRequestFactory(factory))
                .additionalMessageConverters(new StringHttpMessageConverter(Charset.forName("UTF-8")));

        return taehyeonRestTempleBuilder;
    }

    @Bean
    @DependsOn(value = {"taehyeonRestTemplateBuilder"})
    public RestTemplate taehyeonRestTemplate(TaehyeonRestTempleBuilder builder) {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);

        RestTemplate restTemplate = builder.build();
        restTemplate.setMessageConverters(messageConverters);

        return restTemplate;
    }

    @Bean
    public RestTemplateBuilder restTemplateBuilder(){
        return new RestTemplateBuilder();
    }

    @Bean
    @DependsOn(value = {"restTemplateBuilder"})
    public RestTemplate restTemplate(@Qualifier("restTemplateBuilder") RestTemplateBuilder builder){return builder.build();}
}
