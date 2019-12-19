package com.demo.distributed.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.caucho.hessian.io.HessianInputFactory.log;

/**
 * @author oulei
 * @Description
 * @date 2019/11/25 15:09
 */
@JobHandler(value = "demoJobHandler")
@Component
@Slf4j
public class DemoJobHandler extends IJobHandler {
    @Override
    public ReturnT<String> execute(String s) throws Exception {
           //执行器执行内容：
           log.info("日志记录"+new Date()+"参数"+s+" 任务正在执行");
        return ReturnT.SUCCESS;
    }
}
