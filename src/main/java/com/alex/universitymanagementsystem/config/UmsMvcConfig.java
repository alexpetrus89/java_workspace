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
      private final transient DegreeCourseRepository degreeCourseRepository;

      // constructor
      public UmsMvcConfig(DegreeCourseRepository degreeCourseRepository) {
            this.degreeCourseRepository = degreeCourseRepository;
      }

      // methods
      @Override
      public void addViewControllers(@NonNull ViewControllerRegistry registry) {
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

            // examination utilities
            registry
                  .addViewController("/examination/examination-menu")
                  .setViewName("examination/examination-menu");
            registry
                  .addViewController("/examination/read/examination-course")
                  .setViewName("examination/read/examination-course");
            registry
                  .addViewController("/examination/read/examination-student")
                  .setViewName("examination/read/examination-student");
            registry
                  .addViewController("/examination/read/examination-professor")
                  .setViewName("examination/read/examination-professor");
            registry
                  .addViewController("/examination/create/create")
                  .setViewName("examination/create/create");
            registry
                  .addViewController("/examination/update/update")
                  .setViewName("examination/update/update");
            registry
                  .addViewController("/examination/delete/delete")
                  .setViewName("examination/delete/delete");

            // course utilities
            registry
                  .addViewController("/course/course-menu")
                  .setViewName("course/course-menu");
            registry
                  .addViewController("/course/read/read")
                  .setViewName("course/read/read");
            registry
                  .addViewController("/course/create/create")
                  .setViewName("course/create/create");
            registry
                  .addViewController("/course/update/update")
                  .setViewName("course/update/update");
            registry
                  .addViewController("/course/delete/delete")
                  .setViewName("course/delete/delete");

            // degree course utilities
            registry
                  .addViewController("/degree_course/degree-course-menu")
                  .setViewName("degree_course/degree-course-menu");
            registry
                  .addViewController("/degree_course/read/read-courses")
                  .setViewName("degree_course/read/read-courses");
            registry
                  .addViewController("/degree_course/read/read-professors")
                  .setViewName("degree_course/read/read-professors");
            registry
                  .addViewController("/degree_course/read/read-students")
                  .setViewName("degree_course/read/read-students");

            // admin utilities
            registry
                  .addViewController("/user_admin/admin-home")
                  .setViewName("/user_admin/admin-home");
            registry
                  .addViewController("/user_admin/user-menu")
                  .setViewName("/user_admin/user-menu");
            registry
                  .addViewController("/user_admin/update/update")
                  .setViewName("/user_admin/update/update");
            registry
                  .addViewController("/user_admin/delete/delete")
                  .setViewName("/user_admin/delete/delete");


            // user student
            registry
                  .addViewController("/user_student/student-home")
                  .setViewName("/user_student/student-home");
            registry
                  .addViewController("/user_student/create/create-student-from-user")
                  .setViewName("/user_student/create/create-student-from-user");
            registry
                  .addViewController("/user_student/update/update")
                  .setViewName("/user_student/update/update");
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

            registry
                  .addViewController("/user_student/examinations/examination_outcome/outcome")
                  .setViewName("/user_student/examinations/examination_outcome/outcome");

            registry
                  .addViewController("/user_student/examinations/examination_outcome/outcome-result")
                  .setViewName("/user_student/examinations/examination_outcome/outcome-result");

            registry
                  .addViewController("/user_student/examinations/examination_outcome/confirm-refusal")
                  .setViewName("/user_student/examinations/examination_outcome/confirm-refusal");

            registry
                  .addViewController("/user_student/examinations/examination_outcome/refusal-confirmed")
                  .setViewName("/user_student/examinations/examination_outcome/refusal-confirmed");

            registry
                  .addViewController("/user_student/examinations/examination_outcome/student-absent")
                  .setViewName("/user_student/examinations/examination_outcome/student-absent");

            // admin student utilities
            registry
                  .addViewController("/student/student-menu")
                  .setViewName("/student/student-menu");
            registry
                  .addViewController("/student/read/read")
                  .setViewName("student/read/read");
            registry
                  .addViewController("/student/create/create")
                  .setViewName("/student/create/create");
            registry
                  .addViewController("/student/update/update")
                  .setViewName("/student/update/update");
            registry
                  .addViewController("/student/delete/delete")
                  .setViewName("/student/delete/delete");

            // user professor
            registry
                  .addViewController("/user_professor/professor-home")
                  .setViewName("/user_professor/professor-home");
            registry
                  .addViewController("/user_professor/create/create-professor-from-user")
                  .setViewName("/user_professor/create/create-professor-from-user");
            registry
                  .addViewController("/user_professor/update/update")
                  .setViewName("/user_professor/update/update");
            registry
                  .addViewController("/user_professor/examinations/examination_appeal/examination-appeal-menu")
                  .setViewName("/user_professor/examinations/examination_appeal/examination-appeal-menu");
            registry
                  .addViewController("/user_professor/examinations/examination_appeal/create/create-examination-appeal")
                  .setViewName("/user_professor/examinations/examination_appeal/create/create-examination-appeal");
            registry
                  .addViewController("/user_professor/examinations/examination_appeal/delete/delete-examination-appeal")
                  .setViewName("/user_professor/examinations/examination_appeal/delete/delete-examination-appeal");

            // admin professor utilities
            registry
                  .addViewController("/professor/professor-menu")
                  .setViewName("/professor/professor-menu");
            registry
                  .addViewController("/professor/read/read")
                  .setViewName("/professor/read/read");
            registry
                  .addViewController("/professor/create/create")
                  .setViewName("/professor/create/create");
            registry
                  .addViewController("/professor/update/update")
                  .setViewName("/professor/update/update");
            registry
                  .addViewController("/professor/delete/delete")
                  .setViewName("/professor/delete/delete");

            // exception
            registry
                  .addViewController("/exception/illegal/registration/username-already-taken")
                  .setViewName("/exception/illegal/registration/username-already-taken");
            registry
                  .addViewController("/exception/illegal/registration/username-already-taken/password-not-match")
                  .setViewName("/exception/illegal/registration/username-already-taken/password-not-match");
            registry
                  .addViewController("/exception/illegal/registration/invalid-dob")
                  .setViewName("/exception/illegal/registration/invalid-dob");
            registry
                  .addViewController("/exception/illegal/study_plan/invalid-choice")
                  .setViewName("/exception/illegal/study_plan/invalid-choice");
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




