package com.alex.universitymanagementsystem.config;

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

import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;
import com.alex.universitymanagementsystem.utils.StringToDegreeCourseConverter;


@Configuration
public class UmsMvcConfig implements WebMvcConfigurer, Serializable {

      // constants
      @Value("${spring.web.resources.static-locations}")
      private String staticCssResourcesPath;

      // instance variables
      private final transient DegreeCourseRepository degreeCourseRepository;

      // constructor
      public UmsMvcConfig(DegreeCourseRepository degreeCourseRepository) {
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
		registry.addViewController("/logout")
                  .setViewName("logout");
		registry.addViewController("/login")
                  .setViewName("login");
            registry.addViewController("/error")
                  .setViewName("error/error");
            //admin utilities
            registry.addViewController("/user_admin/admin-home")
                  .setViewName("/user_admin/admin-home");
            registry.addViewController("/user/user-menu")
                  .setViewName("/user/user-menu");
            registry.addViewController("/user/update/update")
                  .setViewName("/user/update/update");
            registry.addViewController("/user/delete/delete")
                  .setViewName("/user/delete/delete");
            // student utilities
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
            // professor utilities
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
            // examination utilities
            registry.addViewController("/examination/examination-menu")
                  .setViewName("examination/examination-menu");
            registry.addViewController("/examination/read/examination-course")
                  .setViewName("examination/read/examination-course");
            registry.addViewController("/examination/read/examination-student")
                  .setViewName("examination/read/examination-student");
            registry.addViewController("/examination/read/examination-professor")
                  .setViewName("examination/read/examination-professor");
            registry.addViewController("/examination/create/create")
                  .setViewName("examination/create/create");
            registry.addViewController("/examination/update/update")
                  .setViewName("examination/update/update");
            registry.addViewController("/examination/delete/delete")
                  .setViewName("examination/delete/delete");
            // course utilities
            registry.addViewController("/course/course-menu")
                  .setViewName("course/course-menu");
            registry.addViewController("/course/read/read")
                  .setViewName("course/read/read");
            registry.addViewController("/course/create/create")
                  .setViewName("course/create/create");
            registry.addViewController("/course/update/update")
                  .setViewName("course/update/update");
            registry.addViewController("/course/delete/delete")
                  .setViewName("course/delete/delete");
            // degree course utilities
            registry.addViewController("/degree_course/degree-course-menu")
                  .setViewName("degree_course/degree-course-menu");
            registry.addViewController("/degree_course/read/read-courses")
                  .setViewName("degree_course/read/read-courses");
            registry.addViewController("/degree_course/read/read-professors")
                  .setViewName("degree_course/read/read-professors");
            registry.addViewController("/degree_course/read/read-students")
                  .setViewName("degree_course/read/read-students");


            // user student
            registry.addViewController("/user_student/student-home")
                  .setViewName("/user_student/student-home");
            registry.addViewController("/role/create-student-from-user")
                  .setViewName("/role/create-student-from-user");
            registry.addViewController("/user_student/study_plan/study_plan_modify")
                  .setViewName("/user_student/study_plan/study_plan_modify");
            registry.addViewController("/user_student/examinations/examination-menu")
                  .setViewName("/user_student/examinations/examination-menu");
            registry.addViewController("/user_student/examinations/examination_appeal/calendar")
                  .setViewName("/user_student/examinations/examination_appeal/calendar");

            // user professor
            registry.addViewController("/user_professor/professor-home")
                  .setViewName("/user_professor/professor-home");
            registry.addViewController("/role/create-professor-from-user")
                  .setViewName("/role/create-professor-from-user");
            registry.addViewController("/user_professor/examinations/examination_appeal/examination-appeal-menu")
                  .setViewName("/user_professor/examinations/examination_appeal/examination-appeal-menu");
            registry.addViewController("/user_professor/examinations/examination_appeal/create-examination-appeal")
                  .setViewName("/user_professor/examinations/examination_appeal/create-examination-appeal");
            registry.addViewController("/user_professor/examinations/examination_appeal/delete-examination-appeal")
                  .setViewName("/user_professor/examinations/examination_appeal/delete-examination-appeal");
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
            registry.addResourceHandler("/css/admin-home.css")
                  .addResourceLocations(staticCssResourcesPath);
            registry.addResourceHandler("/css/student-home.css")
                  .addResourceLocations(staticCssResourcesPath);
            registry.addResourceHandler("/css/professor-home.css")
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


