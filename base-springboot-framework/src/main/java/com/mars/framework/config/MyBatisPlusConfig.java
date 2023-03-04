//package com.mars.framework.config;
//
//
//import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
//import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
//import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//@EnableTransactionManagement(proxyTargetClass = true)
//@Configuration
//@MapperScan("com.mars.**.mapper*")
//public class MyBatisPlusConfig {
//
//    /*
//     * 分页插件，自动识别数据库类型
//     */
//    @Bean
//    public PaginationInterceptor paginationInterceptor() {
//        return new PaginationInterceptor();
//    }
//
//    @Bean
//    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
//        return new OptimisticLockerInterceptor();
//    }
//
//    @Bean
//    public MetaObjectHandler metaObjectHandler() {
//        return new CustomMetaObjectHandler();
//    }
//}
