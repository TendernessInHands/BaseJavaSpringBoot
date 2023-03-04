package com.mars;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * 启动程序
 * ional text can depend on base direction (set in view menu)
 *
 * @author mars
 */
@EnableScheduling
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan("com.mars.**.mapper")
@Slf4j
public class BreedTraceabilityApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BreedTraceabilityApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(BreedTraceabilityApplication.class, args);
        log.info("\n(♥◠‿◠)ﾉﾞ  Mars启动成功   ლ(´ڡ`ლ)ﾞ   \n" +
                " .-------.       ____     __                       \n" +
                " |  _ _   \\      \\   \\   /  /                     \n" +
                " | ( ' )  |       \\  _. /  '                          \n" +
                " |(_ o _) /        _( )_ .'                         \n" +
                " | (_,_).' __  ___(_ o _)'                       \n" +
                " |  |\\ \\  |  ||   |(_,_)'                          \n" +
                " |  | \\ `'   /|   `-'  /                             \n" +
                " |  |  \\    /  \\      /                              \n" +
                " ''-'   `'-'    `-..-'                                   ");
    }
}
