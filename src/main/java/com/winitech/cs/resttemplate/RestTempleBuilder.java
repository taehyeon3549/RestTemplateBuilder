package com.winitech.cs.resttemplate;

import com.winitech.cs.resttemplate.service.WiniRestTempleBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import java.util.Map;

public interface RestTempleBuilder {
    Object entity();
    WiniRestTempleBuilder contentType(MediaType mediaType);
    WiniRestTempleBuilder body(Map<String,Object> body);
    WiniRestTempleBuilder headers(Map<String,Object> headers);
    WiniRestTempleBuilder requestUriAndPath(String uriDefault, String uriPath, MultiValueMap<String,String> queryParm) throws Exception;
    ResponseEntity<Object> post()  throws Exception;
    ResponseEntity<Object> get() throws Exception;
    ResponseEntity<Object> patch()throws Exception;
    String getForObject() throws Exception;
}
