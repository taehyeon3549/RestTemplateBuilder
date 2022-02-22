package com.winitech.cs.resttemplate.service;

import com.winitech.cs.resttemplate.RestTempleBuilder;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.yaml.snakeyaml.util.UriEncoder;

import javax.print.URIException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Log4j2
public class WiniRestTempleBuilder implements RestTempleBuilder {
    private boolean containsJsonBody = false;
    private RestTemplate restTemplate;
    private HttpHeaders httpHeaders;
    private Object body;
    private URI requestUri;

    public WiniRestTempleBuilder(){
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();
    }

    public WiniRestTempleBuilder(Map<String,Object> body){
        this.body = body;
        this.containsJsonBody = true;

        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();
    }

    @Override
    public HttpEntity<?> entity() {
        return containsJsonBody ?
                new HttpEntity<>(body, httpHeaders) :
                new HttpEntity<>(httpHeaders);
    }

    @Override
    public WiniRestTempleBuilder contentType(MediaType mediaType) {
        this.httpHeaders.setContentType(mediaType);
        return this.body((Map<String, Object>) this.body);
    }

    @Override
    public WiniRestTempleBuilder body(Map<String, Object> body) {
        this.body = body;
        return this;
    }

    @Override
    public WiniRestTempleBuilder headers(Map<String,Object> headers) {
        Set<String> keys = headers.keySet();
        for(String key : keys){
            this.httpHeaders.set(key, (String)headers.get(key));
        }
        return this.body!=null ? this.body((Map<String, Object>) this.body) : this;
    }

    @Override
    public WiniRestTempleBuilder requestUriAndPath(String uriDefault, String uriPath, MultiValueMap<String,String> queryParm)
        throws Exception {
        this.requestUri = uriPath!=null ? UriComponentsBuilder
                .fromHttpUrl(uriDefault)
                .path(uriPath)
                .queryParams(queryParm)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri()
                : UriComponentsBuilder
                .fromHttpUrl(uriDefault)
                .encode()
                .queryParams(queryParm)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

        log.info("요청 URI :: " + this.requestUri);

        return this;
    }

    @Override
    public ResponseEntity<Object> post() throws WiniRestTemplateBuilderException {
        if (this.requestUri == null) throw new WiniRestTemplateBuilderException("URI 미 설정 requestUriAndPath()를 통해 요청할 uri를 설정해주세요");

        log.info("POST 요청 : {}", this.requestUri.toString());

        return restTemplate.postForEntity(this.requestUri, entity(), Object.class);
    }

    @Override
    public ResponseEntity<Object> get() throws WiniRestTemplateBuilderException, MalformedURLException, UnsupportedEncodingException {
        if (this.requestUri == null) throw new WiniRestTemplateBuilderException("URI 미 설정 requestUriAndPath()를 통해 요청할 uri를 설정해주세요");

        log.info("GET 요청 : {}", this.requestUri.toString());

        /* 전체 MediaType 사요을 위한 messageConverter 설정 */
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        //Add the Jackson Message converter
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        // Note: here we are making this converter to process any kind of response,
        // not only application/*json, which is the default behaviour
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));

        messageConverters.add(converter);
        messageConverters.add(new FormHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter());

        restTemplate.setMessageConverters(messageConverters);

        return restTemplate.exchange(requestUri, HttpMethod.GET, new HttpEntity<>(this.httpHeaders), Object.class);
    }

    @Override
    public ResponseEntity<Object> patch() throws WiniRestTemplateBuilderException {
        if (this.requestUri == null) throw new WiniRestTemplateBuilderException("URI 미 설정 requestUriAndPath()를 통해 요청할 uri를 설정해주세요");

        log.info("PATCH 요청 : {}", this.requestUri.toString());

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        return restTemplate.exchange(this.requestUri, HttpMethod.PATCH, entity(), Object.class);
    }

    @Override
    public String getForObject() throws Exception {
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
        converters.add(new FormHttpMessageConverter());
        converters.add(new StringHttpMessageConverter());

        restTemplate.setMessageConverters(converters);

        System.out.println(new URI(URLDecoder.decode(requestUri.toString())));

        String result = restTemplate.getForObject(new URI(URLDecoder.decode(requestUri.toString(), "UTF-8")), String.class);
        System.out.println(result);
        return result;
    }
}

@Data
class WiniRestTemplateBuilderException extends RuntimeException {
    private static final long serialVersionUID = -7806029002430564887L;
    private String message;

    public WiniRestTemplateBuilderException() {
    }

    public WiniRestTemplateBuilderException(String message) {
        this.message = message;
    }
}
