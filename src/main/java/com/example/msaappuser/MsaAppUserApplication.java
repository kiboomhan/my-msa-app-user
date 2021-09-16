package com.example.msaappuser;

import com.example.msaappuser.error.FeignErrorDecoder;
import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MsaAppUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsaAppUserApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    @LoadBalanced
//    public RestTemplate getRestTemplate() {
//        return new RestTemplate();
//    }

    /**
     * feign client log 출력
     * @return
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

//    @Bean => Component 전환
//    public FeignErrorDecoder getFeignErrorDecoder() {
//        return new FeignErrorDecoder();
//    }
}
