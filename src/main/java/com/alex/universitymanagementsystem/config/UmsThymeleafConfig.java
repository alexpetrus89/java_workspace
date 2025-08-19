package com.alex.universitymanagementsystem.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;


@Configuration
public class UmsThymeleafConfig {

    @Value("${spring.thymeleaf.enabled}")
    private boolean thymeleafEnabled;

    private static final Logger logger = LoggerFactory.getLogger(UmsThymeleafConfig.class);

    private final ApplicationContext applicationContext;

    public UmsThymeleafConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    SpringResourceTemplateResolver templateResolver(){

        if (thymeleafEnabled) {
            SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
            templateResolver.setApplicationContext(this.applicationContext);
            templateResolver.setPrefix("classpath:/templates/");
            templateResolver.setSuffix(".html");
            templateResolver.setTemplateMode(TemplateMode.HTML);
            templateResolver.setCacheable(false);
            logger.info("method templateResolver created: {}", templateResolver);
            return templateResolver;
        } else {
            return null;
        }
    }


    @Bean
    SpringTemplateEngine templateEngine(){

        if (thymeleafEnabled) {
            // SpringTemplateEngine automatically applies SpringStandardDialect and
            // enables Spring's own MessageSource message resolution mechanisms.
            SpringTemplateEngine templateEngine = new SpringTemplateEngine();
            templateEngine.setTemplateResolver(templateResolver());
            templateEngine.addDialect(securityDialect());
            // SpringSecurityDialect is not automatically added to the template engine.
            // Enabling the SpringEL compiler with Spring 4.2.4 or newer can
            // speed up execution in most scenarios, but might be incompatible
            // with specific cases when expressions in one template are reused
            // across different data types, so this flag is "false" by default
            // for safer backwards compatibility.
            templateEngine.setEnableSpringELCompiler(true);
            return templateEngine;
        } else {
            return null;
        }
    }

    @Bean
    SpringSecurityDialect securityDialect() {
        return new SpringSecurityDialect();
    }

}
