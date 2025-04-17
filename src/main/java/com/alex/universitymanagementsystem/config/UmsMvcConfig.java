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

import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;
import com.alex.universitymanagementsystem.utils.StringToDegreeCourseConverter;


@Configuration
public class UmsMvcConfig implements WebMvcConfigurer, Serializable {

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

            //admin utilities
            registry.addViewController("/user_admin/admin-home")
                  .setViewName("/user_admin/admin-home");
            registry.addViewController("/user/user-menu")
                  .setViewName("/user/user-menu");
            registry.addViewController("/user/update/update")
                  .setViewName("/user/update/update");
            registry.addViewController("/user/delete/delete")
                  .setViewName("/user/delete/delete");


            // user student
            registry
                  .addViewController("/user_student/student-home")
                  .setViewName("/user_student/student-home");
            registry
                  .addViewController("/user_student/create/create-student-from-user")
                  .setViewName("/user_student/create/create-student-from-user");
            registry
                  .addViewController("/user_student/study_plan/study_plan_modify")
                  .setViewName("/user_student/study_plan/study_plan_modify");
            registry
                  .addViewController("/user_student/examinations/examination-menu")
                  .setViewName("/user_student/examinations/examination-menu");
            registry
                  .addViewController("/user_student/examinations/examination_appeal/available-calendar")
                  .setViewName("/user_student/examinations/examination_appeal/available-calendar");

            registry
                  .addViewController("/user_student/examinations/examination_appeal/booked-calendar")
                  .setViewName("/user_student/examinations/examination_appeal/booked-calendar");

            // user professor
            registry
                  .addViewController("/user_professor/professor-home")
                  .setViewName("/user_professor/professor-home");
            registry
                  .addViewController("/user_professor/create/create-professor-from-user")
                  .setViewName("/user_professor/create/create-professor-from-user");
            registry
                  .addViewController("/user_professor/examinations/examination_appeal/examination-appeal-menu")
                  .setViewName("/user_professor/examinations/examination_appeal/examination-appeal-menu");
            registry
                  .addViewController("/user_professor/examinations/examination_appeal/create-examination-appeal")
                  .setViewName("/user_professor/examinations/examination_appeal/create-examination-appeal");
            registry
                  .addViewController("/user_professor/examinations/examination_appeal/delete-examination-appeal")
                  .setViewName("/user_professor/examinations/examination_appeal/delete-examination-appeal");

            //validation
            registry
                  .addViewController("/validation/registration/username-already-taken")
                  .setViewName("/validation/registration/username-already-taken");
            registry
                  .addViewController("/validation/registration/password-not-match")
                  .setViewName("/validation/registration/password-not-match");
            registry
                  .addViewController("/validation/registration/invalid-dob")
                  .setViewName("/validation/registration/invalid-dob");
            registry
                  .addViewController("/validation/study_plan/invalid-choice")
                  .setViewName("/validation/study_plan/invalid-choice");
      }

      @Override
      public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
            registry
                  .addResourceHandler("/static/**")
                  .addResourceLocations("classpath:/static/");
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


