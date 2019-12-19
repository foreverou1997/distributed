package com.demo.distributed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 *@author oulei
 * 项目启动类
 */
@SpringBootApplication
@EnableSwagger2
@EnableEurekaClient
@EnableFeignClients
@EnableHystrix
public class DistributedApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributedApplication.class, args);
    }

}
