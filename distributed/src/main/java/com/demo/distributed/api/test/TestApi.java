package com.demo.distributed.api.test;

import com.demo.distributed.annotation.RateLimiterAnnotation;
import com.demo.distributed.feign.test.ITestFeign;
import com.demo.distributed.utils.RedisStringUtil;
import groovy.util.logging.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author oulei
 * @Description
 * @date 2019/12/13 17:52
 */

@RestController
@Slf4j
@Api("测试服务接口")
public class TestApi {

    @Autowired
    private RedisStringUtil redisStringUtil;

    @Autowired
    private ITestFeign iTestFeign;


    @RequestMapping("/getinfo")
    public String getInfo(){
        return iTestFeign.getInfo();
    }

    @RequestMapping("/getinfoSleep")
    public  String getInfoSleep(){
        return iTestFeign.getInfoSleep();
    }


    @RequestMapping("/getinfoException")
    public  String getInfoException(){
        return iTestFeign.getInfoException();
    }


    @ApiOperation(value = "测试",notes="限流测试接口")
    @GetMapping("/test")
    @RateLimiterAnnotation(speed =0.2,timeout = 500)
    public String test(){
        return  "success";
    }


    @GetMapping("/getString")
    public String  getString(String key){
        return redisStringUtil.getString(key) ;
    }

    @GetMapping("/setString")
    public void  getString(String key,String value){
         redisStringUtil.setString(key,value);
    }

}
