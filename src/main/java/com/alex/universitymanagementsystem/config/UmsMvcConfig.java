package com.alex.universitymanagementsystem.config;


import java.io.Serializable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alex.universitymanagementsystem.component.StringToDegreeCourseConverter;
import com.alex.universitymanagementsystem.component.UmsViewRegistry;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;

@Configuration
public class UmsMvcConfig implements WebMvcConfigurer, Serializable {

    // instance variables
    private final transient UmsViewRegistry umsViewRegistry;
    private final transient DegreeCourseRepository degreeCourseRepository;

    // constructor
    public UmsMvcConfig(UmsViewRegistry umsViewRegistry, DegreeCourseRepository degreeCourseRepository) {
        this.umsViewRegistry = umsViewRegistry;
        this.degreeCourseRepository = degreeCourseRepository;
    }


    /**
     * Configure view controllers
     * @param registry
     */
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

        umsViewRegistry
            .getViewPathsByModuleMappings()
            .streamAllViewPaths()
            .forEach(view -> registry.addViewController(view).setViewName(view));
    }


    /**
     * Configure formatters
     * @param registry
     */
    @Override
    public void addFormatters(@NonNull FormatterRegistry registry) {
        registry.addConverter(new StringToDegreeCourseConverter(degreeCourseRepository));
    }


    // --- Beans ---
    /**
     * Enables support for HTTP PUT and DELETE methods in HTML forms.
     * @return HiddenHttpMethodFilter
     */
    @Bean
    HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }


}

