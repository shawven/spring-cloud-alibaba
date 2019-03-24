package com.sca.controller;

import com.sca.common.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Map;

/**
 * @author XW
 * @since 2019-03-23 15:45
 */
@Controller
public class OauthTokenEndpoint {

    @Autowired
    TokenEndpoint tokenEndpoint;

    @ResponseBody
    @PostMapping("/oauth/token")
    public ResponseEntity postAccessToken(Principal principal, @RequestParam Map<String, String> parameters)
            throws HttpRequestMethodNotSupportedException {
        ResponseEntity<OAuth2AccessToken> responseEntity;
        try {
            responseEntity = tokenEndpoint.postAccessToken(principal, parameters);
        } catch (InvalidTokenException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new Response<>(false, HttpStatus.UNAUTHORIZED.value(), e.getMessage(), null));
        }

        String  message = "refresh_token".equals(parameters.get("grant_type"))
                    ? "刷新token成功"
                    : "获取token成功";
        return ResponseEntity
                .status(responseEntity.getStatusCodeValue())
                .headers(responseEntity.getHeaders())
                .body( new Response<>(message, responseEntity.getBody()));
    }
}
