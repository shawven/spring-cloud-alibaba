package com.sca.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sca.common.Response;
import com.sca.common.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yuit
 * @date 2018/11/1 18:15
 * 请求拒绝，没有权限
 */
@Component
public class BootAccessDeniedHandler extends OAuth2AccessDeniedHandler {

    @Override
    protected ResponseEntity<?> enhanceResponse(ResponseEntity<?> responseEntity, Exception authException) {
        Response<?> objectResponse = new Response<>(
                false, HttpStatus.UNAUTHORIZED.value(), authException.getMessage(), null);
        return ResponseEntity
                .status(responseEntity.getStatusCodeValue())
                .headers(responseEntity.getHeaders())
                .body(objectResponse);
    }

    //    @Override
//    public void handle(HttpServletRequest request,
//                       HttpServletResponse response,
//                       AccessDeniedException e) throws IOException, ServletException {
//
//        response.setContentType("application/json,charset=utf-8");
//        response.setStatus(HttpStatus.FORBIDDEN.value());
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.writeValue(response.getOutputStream(), ResponseUtils.forbidden());
//    }
}
