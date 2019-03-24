package com.sca.config;

import com.sca.oauth.BootAccessDeniedHandler;
import com.sca.oauth.BootBasicAuthenticationFilter;
import com.sca.oauth.BootOAuth2AuthExceptionEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * Created by wangyunfei on 2017/6/9.
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WebResponseExceptionTranslator<OAuth2Exception> bootWebResponseExceptionTranslator;

    @Autowired
    private BootOAuth2AuthExceptionEntryPoint bootOAuth2AuthExceptionEntryPoint;

    @Autowired
    private BootBasicAuthenticationFilter bootBasicAuthenticationFilter;

    @Autowired
    private BootAccessDeniedHandler bootAccessDeniedHandler;


    @Value("${security.oauth2.resource.jwt.key-value}")
    private String signKey;

    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtTokenConverter());
    }

    @Bean
    protected JwtAccessTokenConverter jwtTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(signKey);
        return converter;
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient("app")
                .secret(passwordEncoder.encode("secret"))
                .scopes("all")
                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
        ;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(jwtTokenStore())
                .tokenEnhancer(jwtTokenConverter())
                .tokenServices(defaultTokenServices())
                .userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager)
                .exceptionTranslator(bootWebResponseExceptionTranslator)
        ;

    }

    /**
     * <p>注意，自定义TokenServices的时候，需要设置@Primary，否则报错，</p>
     * @return
     */
    @Primary
    @Bean
    public DefaultTokenServices defaultTokenServices(){
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(jwtTokenStore());
        tokenServices.setTokenEnhancer(jwtTokenConverter());
        tokenServices.setSupportRefreshToken(true);
        // token有效期自定义设置，默认2小时
        tokenServices.setAccessTokenValiditySeconds(30);
        //默认30天，这里修改
        tokenServices.setRefreshTokenValiditySeconds(60);
        return tokenServices;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .accessDeniedHandler(bootAccessDeniedHandler)
                .authenticationEntryPoint(bootOAuth2AuthExceptionEntryPoint)
                .addTokenEndpointAuthenticationFilter(bootBasicAuthenticationFilter)
        ;
    }


}
