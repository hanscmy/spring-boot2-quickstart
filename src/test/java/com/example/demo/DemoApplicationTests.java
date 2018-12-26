package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.util.Assert;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    private Map<String, TransactionInterceptor> transactionInterceptorMap;

    @Test
    public void contextLoads()  {
        Assert.notEmpty(transactionInterceptorMap, "事务管理器初始化失败");
        transactionInterceptorMap.forEach((s, transactionInterceptor) -> {
            System.out.println(s + " ------ " + transactionInterceptor);
        });
    }

}

