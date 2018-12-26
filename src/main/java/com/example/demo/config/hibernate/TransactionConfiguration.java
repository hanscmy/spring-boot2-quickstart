package com.example.demo.config.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * @author: hanshichao
 * @create: 2018-12-17 15:27
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true, order = 2)
class TransactionConfiguration implements TransactionManagementConfigurer {

    private static final String AOP_POINTCUT_EXPRESSION = "execution(* *..service..*.*(..))";

    @Autowired
    private PlatformTransactionManager hibernateTxManager;

    @Bean
    public PlatformTransactionManager hibernateTxManager(SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return this.hibernateTxManager;
    }

    @Bean
    public TransactionInterceptor txAdvice(PlatformTransactionManager hibernateTxManager) {
        DefaultTransactionAttribute txAttr_REQUIRED = new DefaultTransactionAttribute();
        txAttr_REQUIRED.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        txAttr_REQUIRED.rollbackOn(new Exception());

        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        source.addTransactionalMethod("create*", txAttr_REQUIRED);
        source.addTransactionalMethod("add*", txAttr_REQUIRED);
        source.addTransactionalMethod("save*", txAttr_REQUIRED);
        source.addTransactionalMethod("update*", txAttr_REQUIRED);
        source.addTransactionalMethod("del*", txAttr_REQUIRED);
        source.addTransactionalMethod("remove*", txAttr_REQUIRED);
        source.addTransactionalMethod("do*", txAttr_REQUIRED);
        return new TransactionInterceptor(hibernateTxManager, source);
    }

    @Bean
    public Advisor txAdviceAdvisor(PlatformTransactionManager hibernateTxManager) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        DefaultPointcutAdvisor pointcutAdvisor = new DefaultPointcutAdvisor(pointcut, txAdvice(hibernateTxManager));
        // 设置切面式事务优先级低于注解式事务
        pointcutAdvisor.setOrder(3);
        return pointcutAdvisor;
    }

}