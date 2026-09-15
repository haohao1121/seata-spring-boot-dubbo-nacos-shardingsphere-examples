package io.seata.samples.integration.call;


import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 业务服务启动类
 *
 * @author lli
 * @since 2026-09-01
 */
@SpringBootApplication(scanBasePackages = "io.seata.samples.integration.call",
        exclude = {DataSourceAutoConfiguration.class})
@EnableDubbo(scanBasePackages = "io.seata.samples.integration.call")
public class BusinessExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusinessExampleApplication.class, args);
    }

}

