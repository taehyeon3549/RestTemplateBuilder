package com.winitech.cs.restTemple;

import lombok.Data;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TaehyeonRestTempleBuilder extends RestTemplateBuilder implements CustomRestTempleBuilder {
    private boolean containsJsonBody = false;
    private RestTemplate restTemplate;
    private HttpHeaders httpHeaders;
    private Object body;
    private URI requestUri;

    TaehyeonRestTempleBuilder(){
        super();

        init();
        this.restTemplate = super.build();
    }

    TaehyeonRestTempleBuilder(CustomRestTemplateCustomizer customRestTemplateCustomizer){
        super(customRestTemplateCustomizer);

        init();
        this.restTemplate = super.build();
    }

    void init(){
        this.httpHeaders = new HttpHeaders();
    }

    @Override
    public HttpEntity<?> entity() {
        return containsJsonBody ?
                new HttpEntity<>(body, httpHeaders) :
                new HttpEntity<>(httpHeaders);
    }

    @Override
    public TaehyeonRestTempleBuilder contentType(MediaType mediaType) {
        this.httpHeaders.setContentType(mediaType);
        return this.body((Map<String, Object>) this.body);
    }

    @Override
    public TaehyeonRestTempleBuilder body(Map<String, Object> body) {
        this.body = body;
        return this;
    }

    @Override
    public TaehyeonRestTempleBuilder headers(Map<String,Object> headers) {
        Set<String> keys = headers.keySet();
        for(String key : keys){
            this.httpHeaders.set(key, (String)headers.get(key));
        }
        return this.body!=null ? this.body((Map<String, Object>) this.body) : this;
    }

    @Override
    public TaehyeonRestTempleBuilder requestUriAndPath(String uriDefault, String uriPath, MultiValueMap<String,String> queryParm){
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
        return this;
    }

    @Override
    public ResponseEntity<Object> post() throws TaehyeonRestTemplateBuilderException {
        if (this.requestUri == null) throw new TaehyeonRestTemplateBuilderException("URI 미 설정 requestUriAndPath()를 통해 요청할 uri를 설정해주세요");

        return restTemplate.postForEntity(this.requestUri, entity(), Object.class);
    }

    @Override
    public ResponseEntity<Object> get() throws TaehyeonRestTemplateBuilderException, MalformedURLException, UnsupportedEncodingException, URISyntaxException {
        if (this.requestUri == null) throw new TaehyeonRestTemplateBuilderException("URI 미 설정 requestUriAndPath()를 통해 요청할 uri를 설정해주세요");

        return restTemplate.exchange(new URI(URLDecoder.decode(requestUri.toString(), "UTF-8")), HttpMethod.GET, new HttpEntity<>(this.httpHeaders), Object.class);
    }

    @Override
    public ResponseEntity<Object> patch() throws TaehyeonRestTemplateBuilderException {
        if (this.requestUri == null) throw new TaehyeonRestTemplateBuilderException("URI 미 설정 requestUriAndPath()를 통해 요청할 uri를 설정해주세요");

        return restTemplate.exchange(this.requestUri, HttpMethod.PATCH, entity(), Object.class);
    }

    @Override
    public String getForString() throws Exception {
        if (this.requestUri == null) throw new TaehyeonRestTemplateBuilderException("URI 미 설정 requestUriAndPath()를 통해 요청할 uri를 설정해주세요");

        return restTemplate.getForObject(new URI(URLDecoder.decode(requestUri.toString(), "UTF-8")), String.class);
    }

    public RestTemplate build() {
        return this.restTemplate;
    }
}

@Data
class TaehyeonRestTemplateBuilderException extends RuntimeException {
    private static final long serialVersionUID = -7806029002430564887L;
    private String message;

    TaehyeonRestTemplateBuilderException() {
    }

    TaehyeonRestTemplateBuilderException(String message) {
        this.message = message;
    }
}
