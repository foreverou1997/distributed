package com.demo.distributed.feign.test;

import com.demo.distributed.feign.test.fallback.TestFeignFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author oulei
 * @feign客户端调用接口
 * @FeignClient name为注册中心服务的名字
 * @date 2019/12/17 17:38
 */

@FeignClient(name="producer",fallback = TestFeignFallBack.class)
public interface ITestFeign {

    @RequestMapping("/getinfo")
     String getInfo();

    @RequestMapping("/getinfoSleep")
     String getInfoSleep();

    @RequestMapping("/getinfoException")
    String getInfoException();
}
