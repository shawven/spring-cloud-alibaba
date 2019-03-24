package com.sca.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sca.common.Response;
import com.sca.common.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

/**
 * @author yuit
 * @date 2018/11/5 11:38
 * 认证不带客户端信息参数处理 filter
 *
 */
@Component
public class BootBasicAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private ClientDetailsService clientDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (!"/oauth/token".equals(request.getRequestURI()) ||
                !"password".equals(request.getParameter("grant_type"))) {
            filterChain.doFilter(request, response);
            return;
        }

        String[] clientDetail = this.isHasClientDetails(request);
        if (clientDetail == null) {
            response.setContentType("application/json,charset=utf-8");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            ObjectMapper objectMapper = new ObjectMapper();
            Response<Object> objectResponse = new Response<>(
                    false, HttpStatus.BAD_REQUEST.value(),"请求中未包含客户端信息", null);
            objectMapper.writeValue(response.getOutputStream(), objectResponse);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            filterChain.doFilter(request,response);
            return;
        }

        try {
            ClientDetails client = this.clientDetailsService.loadClientByClientId(clientDetail[0]);
            if (client == null) {
                throw new NoSuchClientException("No client with requested id: " + clientDetail[0]);
            }
        } catch (ClientRegistrationException e) {
            response.setContentType("application/json,charset=utf-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            ObjectMapper objectMapper = new ObjectMapper();
            Response<Object> objectResponse = new Response<>(
                    false, HttpStatus.UNAUTHORIZED.value(),"客户端或密码错误", null);
            objectMapper.writeValue(response.getOutputStream(), objectResponse);
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 判断请求头中是否包含client信息，不包含返回false
     *
     * @param request
     * @return
     */
    private String[] isHasClientDetails(HttpServletRequest request) {
        String[] params = null;

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null) {
            String basic = header.substring(0, 5);

            if (basic.toLowerCase().contains("basic")) {
                String tmp = header.substring(6);
                String[] clientArrays  = new String(Base64.getDecoder().decode(tmp)).split(":");
                if (clientArrays.length != 2) {
                    return params;
                } else {
                    params = clientArrays;
                }
            }
        }

        String id = request.getParameter("client_id");
        String secret = request.getParameter("client_secret");

        if (header == null && id != null) {
            params = new String[]{id, secret};
        }

        return params;
    }
}
