package com.winitech.cs.restTemple;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import java.util.Map;

public interface RestTempleBuilder {
    Object entity();
    TaehyeonRestTempleBuilder contentType(MediaType mediaType);
    TaehyeonRestTempleBuilder body(Map<String,Object> body);
    TaehyeonRestTempleBuilder headers(Map<String,Object> headers);
    TaehyeonRestTempleBuilder requestUriAndPath(String uriDefault, String uriPath, MultiValueMap<String,String> queryParm) throws Exception;
    ResponseEntity<Object> post()  throws Exception;
    ResponseEntity<Object> get() throws Exception;
    ResponseEntity<Object> patch()throws Exception;
    String getForString() throws Exception;
}
