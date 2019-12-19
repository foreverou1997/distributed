package com.demo.distributed.feign.test.fallback;

import com.demo.distributed.feign.test.ITestFeign;
import org.springframework.stereotype.Component;

/**
 * @author oulei
 * @Description
 * @date 2019/12/18 10:41
 */
@Component
public class TestFeignFallBack implements ITestFeign {
    @Override
    public String getInfo() {
        return "hystrix--服务器忙，请稍后再试!";
    }

    @Override
    public String getInfoSleep() {
        return "hystrix--服务器忙，请稍后再试!";
    }

    @Override
    public String getInfoException() {
        return "hystrix--服务器忙，请稍后再试!";
    }
}
