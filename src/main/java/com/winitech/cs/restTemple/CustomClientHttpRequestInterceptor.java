package com.winitech.cs.restTemple;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
/**
 *
 * CustomClientHttpRequestInterceptor.
 *
 * <p>
 *     Http 요청 log 설정
 * </p>
 *
 * @author CS 김태현
 * @version 1.0.0
 * @since 2022-03-07
 * @modify
 * <p>
 *  수정일      수정자      수정내용<br>
 *  ----------  --------   ---------------------------<br>
 *  2022-03-07  CS 김태현   최초작성 <br>
 * </p>
 *
 * @see ClientHttpRequestInterceptor
**/
@Log4j2
public class CustomClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            logRequestDetails(request);
            return execution.execute(request, body);
    }

    private void logRequestDetails(HttpRequest request) {
        log.info("Headers: {}", request.getHeaders());
        log.info("Request Method: {}", request.getMethod());
        log.info("Request URI: {}", request.getURI());
    }
}
