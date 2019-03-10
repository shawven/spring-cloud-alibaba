package com.sca.customer;

import com.sca.common.MyOAuth2FeignRequestInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@EnableResourceServer
public class CustomerApplication extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/echo-hello2/**", "/echo-hello12/**").permitAll()
                .and().authorizeRequests().anyRequest().authenticated();
    }


    @Bean
    public MyOAuth2FeignRequestInterceptor basicAuthRequestInterceptor() {
        return new MyOAuth2FeignRequestInterceptor();
    }

    @FeignClient(name = "service-provider",
            fallback = EchoServiceFallback.class,
            configuration = FeignConfiguration.class)
    public interface EchoService {
        @RequestMapping(value = "/echo/{str}", method = RequestMethod.GET)
        String echo(@PathVariable("str") String str);

        @RequestMapping(value = "/hello/{str}", method = RequestMethod.GET)
        String hello(@PathVariable("str") String str);

        @RequestMapping(value = "/divide", method = RequestMethod.GET)
        String divide(@RequestParam("a") Integer a, @RequestParam("b") Integer b);

        @RequestMapping(value = "/notFound", method = RequestMethod.GET)
        String notFound();
    }

    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }
}


class FeignConfiguration {
    @Bean
    public EchoServiceFallback echoServiceFallback() {
        return new EchoServiceFallback();
    }
}

class EchoServiceFallback implements CustomerApplication.EchoService {

    @Override
    public String echo(@PathVariable("str") String str) {
        return "echo fallback";
    }

    @Override
    public String hello(String str) {
        return "hello fallback";
    }

    @Override
    public String divide(@RequestParam Integer a, @RequestParam Integer b) {
        return "divide fallback";
    }

    @Override
    public String notFound() {
        return "notFound fallback";
    }
}

