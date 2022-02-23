package com.winitech.cs.restTemple;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

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
    public TaehyeonRestTempleBuilder restTemplateBuilder(HttpComponentsClientHttpRequestFactory factory) {
        /* 상속받은 Builder를 통한 build를 시키면 return이 super의 RestTemplate 이고, sub의 TaehyeonRestTempleBuilder로 casting이 안됨 */
        TaehyeonRestTempleBuilder taehyeonRestTempleBuilder = new TaehyeonRestTempleBuilder(customRestTemplateCustomizer());

        taehyeonRestTempleBuilder.requestFactory(() -> new BufferingClientHttpRequestFactory(factory))
                .additionalMessageConverters(new StringHttpMessageConverter(Charset.forName("UTF-8")));

        return taehyeonRestTempleBuilder;
    }

    @Bean
    @DependsOn(value = {"restTemplateBuilder"})
    public RestTemplate restTemplate(TaehyeonRestTempleBuilder builder) {
        return builder.build();
    }
}
