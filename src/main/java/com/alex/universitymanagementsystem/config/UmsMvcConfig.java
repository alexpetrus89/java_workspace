package com.alex.universitymanagementsystem.config;


import java.io.Serializable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alex.universitymanagementsystem.component.StringToDegreeCourseConverter;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;

@Configuration
public class UmsMvcConfig implements WebMvcConfigurer, Serializable {

    // instance variables
    private final transient UmsConfig umsConfig;
    private final transient DegreeCourseRepository degreeCourseRepository;

    // constructor
    public UmsMvcConfig(UmsConfig umsConfig, DegreeCourseRepository degreeCourseRepository) {
        this.umsConfig = umsConfig;
        this.degreeCourseRepository = degreeCourseRepository;
    }


    @Override
    public void addViewControllers(@NonNull ViewControllerRegistry registry) {

        // Root e home
        // home page
		registry
            .addViewController("/")
            .setViewName("home");
        registry
            .addViewController("/home")
            .setViewName("home");
		registry
            .addViewController("/logout")
            .setViewName("logout");
		registry
            .addViewController("/login")
            .setViewName("login");
        registry
            .addViewController("/forgot-password")
            .setViewName("forgot-password");
        registry
            .addViewController("/reset-password")
            .setViewName("reset-password");


        umsConfig.streamAllViews().forEach(view -> registry.addViewController(view).setViewName(view));
    }


    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    @Override
    public void addFormatters(@NonNull FormatterRegistry registry) {
        registry.addConverter(new StringToDegreeCourseConverter(degreeCourseRepository));
    }

    // --- Beans ---

    /**
     * Abilita il supporto per i metodi HTTP PUT e DELETE nei form HTML.
     * @return HiddenHttpMethodFilter
     */
    @Bean
    HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }


}

