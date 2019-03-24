package com.sca.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sca.common.Response;
import com.sca.common.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yuit
 * @date 2018/11/2 10:48
 *
 */
@Component
public class BootOAuth2AuthExceptionEntryPoint extends OAuth2AuthenticationEntryPoint {

    @Override
    protected ResponseEntity<?> enhanceResponse(ResponseEntity<?> responseEntity, Exception exception) {
        if (exception != null) {
            Throwable cause = exception.getCause();
            if (cause instanceof InvalidTokenException) {
                Response<?> objectResponse = new Response<>(
                        false, HttpStatus.UNAUTHORIZED.value(), cause.getMessage(), null);
                return ResponseEntity
                        .status(responseEntity.getStatusCodeValue())
                        .headers(responseEntity.getHeaders())
                        .body(objectResponse);
            }

            Response<?> objectResponse = new Response<>(
                    false, HttpStatus.UNAUTHORIZED.value(), exception.getMessage(), null);
            return ResponseEntity
                    .status(responseEntity.getStatusCodeValue())
                    .headers(responseEntity.getHeaders())
                    .body(objectResponse);
        }

        return ResponseEntity
                .status(responseEntity.getStatusCodeValue())
                .headers(responseEntity.getHeaders())
                .build();
    }

//    @Override
//    public void commence(HttpServletRequest request,
//                         HttpServletResponse response,
//                         AuthenticationException e) throws IOException, ServletException {
//
//        response.setContentType("application/json,charset=utf-8");
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        Response<Object> objectResponse = new Response<>(false, HttpStatus.UNAUTHORIZED.value(), e.getMessage(), null);
//        objectMapper.writeValue(response.getOutputStream(), objectResponse);
//    }

}
