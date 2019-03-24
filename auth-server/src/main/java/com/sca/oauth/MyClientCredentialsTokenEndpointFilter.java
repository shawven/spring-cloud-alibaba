package com.sca.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author XW
 * @since 2019-03-23 23:30
 */
class MyClientCredentialsTokenEndpointFilter extends ClientCredentialsTokenEndpointFilter {

    @Autowired
    private BootOAuth2AuthExceptionEntryPoint bootOAuth2AuthExceptionEntryPoint;

    public void setBootOAuth2AuthExceptionEntryPoint(BootOAuth2AuthExceptionEntryPoint bootOAuth2AuthExceptionEntryPoint) {
        this.bootOAuth2AuthExceptionEntryPoint = bootOAuth2AuthExceptionEntryPoint;
    }
    public MyClientCredentialsTokenEndpointFilter() {
        super("/oauth/token");
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        return super.attemptAuthentication(request, response);
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        this.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                if (exception instanceof BadCredentialsException) {
                    exception = new BadCredentialsException(((AuthenticationException)exception).getMessage(), new BadClientCredentialsException());
                }

                bootOAuth2AuthExceptionEntryPoint.commence(request, response, (AuthenticationException)exception);
            }
        });
        this.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                System.out.println(3);
            }
        });
    }
}

