package com.sca.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@EnableDiscoveryClient
@SpringBootApplication
@EnableResourceServer
public class ProviderApplication extends ResourceServerConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/hello/**").permitAll()
                .and().authorizeRequests().anyRequest().authenticated();
    }
}


@RestController
class EchoController {
    @RequestMapping(value = "/user")
    public Principal user(Principal principal) {
        return principal;
    }

    @RequestMapping(value = "/echo/{string}", method = RequestMethod.GET)
    public String echo(@PathVariable String string) {
        System.out.println("service-provide1-echo: " + string);
        return "service-provide1-echo: " + string;
    }

    @RequestMapping(value = "/hello/{string}", method = RequestMethod.GET)
    public String hello(@PathVariable String string) {
        System.out.println("service-provide1-hello: " + string);
        return "service-provide1-hello: " + string;
    }
}

