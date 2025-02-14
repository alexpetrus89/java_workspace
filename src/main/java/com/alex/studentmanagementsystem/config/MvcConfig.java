package com.alex.studentmanagementsystem.config;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alex.studentmanagementsystem.repository.DegreeCourseRepository;
import com.alex.studentmanagementsystem.utility.StringToDegreeCourseConverter;

@Configuration
public class MvcConfig implements WebMvcConfigurer, Serializable {

      // constants
      @Value("${spring.web.resources.static-locations}")
      private String staticCssResourcesPath;

      // instance variables
      private final transient DegreeCourseRepository degreeCourseRepository;

      // constructor
      public MvcConfig(DegreeCourseRepository degreeCourseRepository) {
            this.degreeCourseRepository = degreeCourseRepository;
      }

      // methods
      @Override
      public void addViewControllers(@NonNull ViewControllerRegistry registry) {
            // home page
		registry.addViewController("/")
                  .setViewName("home");
            registry.addViewController("/home")
                  .setViewName("home");
		registry.addViewController("/hello")
                  .setViewName("hello");
		registry.addViewController("/login")
                  .setViewName("login");
            // user page
            registry.addViewController("/user/user-menu")
                  .setViewName("user/user-menu");
            registry.addViewController("/user/update/update")
                  .setViewName("user/update/update");
            // student page
            registry.addViewController("/student/student-menu")
                  .setViewName("student/student-menu");
            registry.addViewController("/student/read/read")
                  .setViewName("student/read/read");
            registry.addViewController("/student/create/create")
                  .setViewName("student/create/create");
            registry.addViewController("/student/update/update")
                  .setViewName("student/update/update");
            registry.addViewController("/student/delete/delete")
                  .setViewName("student/delete/delete");
            // professor page
            registry.addViewController("/professor/professor-menu")
                  .setViewName("professor/professor-menu");
            registry.addViewController("/professor/read/read")
                  .setViewName("professor/read/read");
            registry.addViewController("/professor/create/create")
                  .setViewName("professor/create/create");
            registry.addViewController("/professor/update/update")
                  .setViewName("professor/update/update");
            registry.addViewController("/professor/delete/delete")
                  .setViewName("professor/delete/delete");
            // examination page
            registry.addViewController("/examination/examination-menu")
                  .setViewName("examination/examination-menu");
            registry.addViewController("/examination/examination-course")
                  .setViewName("examination/examination-course");
            registry.addViewController("/examination/examination-student")
                  .setViewName("examination/examination-student");
            registry.addViewController("/examination/examination-professor")
                  .setViewName("examination/examination-professor");
            registry.addViewController("/examination/create/create")
                  .setViewName("examination/create/create");
            registry.addViewController("/examination/update/update")
                  .setViewName("examination/update/update");
            // course page
            registry.addViewController("/course/course-menu")
                  .setViewName("course/course-menu");
            registry.addViewController("/course/read/read")
                  .setViewName("course/read/read");
            // degree course page
            registry.addViewController("/degree_course/degree-course-menu")
                  .setViewName("degree_course/degree-course-menu");
            registry.addViewController("/degree_course/read-courses")
                  .setViewName("degree_course/read-courses");
            registry.addViewController("/degree_course/read-professors")
                  .setViewName("degree_course/read-professors");
            registry.addViewController("/degree_course/read-students")
                  .setViewName("degree_course/read-students");
	}

      @Override
      public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/static/**")
                  .addResourceLocations("classpath:/static/");
            registry.addResourceHandler("/api/v1/student/css/**")
                  .addResourceLocations(staticCssResourcesPath);
            registry.addResourceHandler("/api/v1/professor/css/**")
                  .addResourceLocations(staticCssResourcesPath);
            registry.addResourceHandler("/api/v1/course/css/**")
                  .addResourceLocations(staticCssResourcesPath);
            registry.addResourceHandler("/api/v1/degree-course/professors/css/**")
                  .addResourceLocations(staticCssResourcesPath);
            registry.addResourceHandler("/api/v1/examination/css/**")
                  .addResourceLocations(staticCssResourcesPath);
            registry.addResourceHandler("/api/v1/user/css/**")
                  .addResourceLocations(staticCssResourcesPath);
      }

      @Override
      public void addFormatters(@NonNull FormatterRegistry registry) {
            registry.addConverter(new StringToDegreeCourseConverter(degreeCourseRepository));
      }

      @Bean
      HiddenHttpMethodFilter hiddenHttpMethodFilter() {
            return new HiddenHttpMethodFilter();
      }

}


