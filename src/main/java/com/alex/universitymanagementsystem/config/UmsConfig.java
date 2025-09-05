package com.alex.universitymanagementsystem.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.alex.universitymanagementsystem.UniversityManagementSystemApplication;
import com.alex.universitymanagementsystem.component.UmsExitCodeGenerator;


@Configuration
public class UmsConfig {

    // logger
    private final Logger logger = LoggerFactory.getLogger(UmsConfig.class);

    // instance variables
    private final ConfigurableApplicationContext applicationContext;
    private final Map<String, List<String>> moduleViews = new HashMap<>();
    private final Map<String, String> fieldToView = new HashMap<>();
    private static String[] mainArgs;

    public UmsConfig(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;

        // --- MODULE VIEWS for MvcConfig ---
        moduleViews.put("user_admin", List.of(
            "/user_admin/admin-home",
            "/user_admin/admin-menu",
            "/user_admin/update/update",
            "/user_admin/delete/delete",
            "/user_admin/student/student-menu",
            "/user_admin/professor/professor-menu",
            "/user_admin/course/course-menu",
            "/user_admin/course/read/read",
            "/user_admin/course/create/create",
            "/user_admin/course/update/update",
            "/user_admin/course/delete/delete",
            "/user_admin/degree_course/degree-course-menu",
            "/user_admin/degree_course/read/read-courses",
            "/user_admin/degree_course/read/read-professors",
            "/user_admin/degree_course/read/read-students",
            "/user_admin/examination/examination-menu",
            "/user_admin/examination/read/examination-course",
            "/user_admin/examination/read/examination-student",
            "/user_admin/examination/read/examination-professor",
            "/user_admin/examination/create/create",
            "/user_admin/examination/update/update",
            "/user_admin/examination/delete/delete"
        ));

        moduleViews.put("user_student", List.of(
            "/user_student/student-home",
            "/user_student/create/select-degree-course",
            "/user_student/read/read",
            "/user_student/create/create",
            "/user_student/update/update",
            "/user_student/delete/delete",
            "/user_student/study_plan/study_plan_modify",
            "/user_student/examinations/examination-menu",
            "/user_student/examinations/examination_appeal/available-calendar",
            "/user_student/examinations/examination_appeal/booked-calendar",
            "/user_student/examinations/examination_outcome/outcome",
            "/user_student/examinations/examination_outcome/outcome-result",
            "/user_student/examinations/examination_outcome/confirm-refusal",
            "/user_student/examinations/examination_outcome/refusal-confirmed",
            "/user_student/examinations/examination_outcome/student-absent"
        ));

        moduleViews.put("user_professor", List.of(
            "/user_professor/professor-home",
            "/user_professor/read/read",
            "/user_professor/delete/delete",
            "/user_professor/examinations/examination_appeal/examination-appeal-menu",
            "/user_professor/examinations/examination_appeal/create/create-examination-appeal",
            "/user_professor/examinations/examination_appeal/delete/delete-examination-appeal"
        ));

        moduleViews.put("exception", List.of(
            "/exception/generic-exception",
            "/exception/transient-object-exception",
            "/exception/access-denied/access-denied-exception",
            "/exception/already-exists/object-already-exists",
            "/exception/already-exists/professor-already-exists",
            "/exception/already-exists/student-already-exists",
            "/exception/data/data-access-exception",
            "/exception/data/json-processing-exception",
            "/exception/illegal/registration/username-already-taken",
            "/exception/illegal/registration/password-not-match",
            "/exception/illegal/registration/invalid-dob",
            "/exception/illegal/study_plan/invalid-choice",
            "/exception/illegal/illegal-parameter",
            "/exception/illegal/illegal-parameters",
            "/exception/not-found/degree-course-not-found",
            "/exception/not-found/not-found-exception",
            "/exception/not-found/fiscal-code-not-found",
            "/exception/not-found/object-not-found",
            "/exception/not-found/username-not-found"
        ));


        // --- FIELD VIEWS for exception management ---
        fieldToView.put("username", "exception/illegal/invalid/invalid-username");
        fieldToView.put("usernameAlreadyTaken", "exception/illegal/invalid/username-already-taken");
        fieldToView.put("password", "exception/illegal/invalid/invalid-password");
        fieldToView.put("firstName", "exception/illegal/invalid/invalid-first-name");
        fieldToView.put("lastName", "exception/illegal/invalid/invalid-last-name");
        fieldToView.put("dob", "exception/illegal/invalid/invalid-dob");
        fieldToView.put("fiscalCode", "exception/illegal/invalid/invalid-fiscal-code");
        fieldToView.put("fiscalCodeAlreadyTaken", "exception/illegal/invalid/fiscal-code-already-taken");
        fieldToView.put("register", "exception/illegal/invalid-register");
        fieldToView.put("phone", "exception/illegal/invalid/invalid-phone");
        fieldToView.put("role", "exception/illegal/invalid/invalid-role");

        final String invalidAddressView = "exception/illegal/invalid/invalid-address";
        fieldToView.put("street", invalidAddressView);
        fieldToView.put("city", invalidAddressView);
        fieldToView.put("state", invalidAddressView);
        fieldToView.put("zip", invalidAddressView);

        final String invalidChoiceView = "exception/illegal/invalid/invalid-choice";
        fieldToView.put("courseToAdd", invalidChoiceView);
        fieldToView.put("courseToRemove", invalidChoiceView);
        fieldToView.put("degreeCourseOfNewCourse", invalidChoiceView);
        fieldToView.put("degreeCourseOfOldCourse", invalidChoiceView);

        // object errors
        fieldToView.put("invalidChoice", "exception/illegal/invalid/invalid-choice");
        fieldToView.put("passwordsDoNotMatch", "exception/illegal/invalid/password-not-match");


        // fallback generico
        fieldToView.put("register", "exception/generic-exception");

    }


    // --- Security filter chain URL's ---
    protected static final String[] PUBLIC_URLS = {
        "/",
		"/shutdown",
        "/restart",
        "/login",
		"/logout",
		"/home",
		"/registration",
        "/forgot-password",
        "/reset-password",
		"/static/css/**",
		"/static/js/ums-degree-courses.js",
		"/static/js/ums-password-rules.js",
        "/static/images/**",
		"/favicon.ico",
		"/exception/**",
        "/user_student/create/select-degree-course",
		"/user/update/update-result",
		"/api/v1/user/create-admin",
		"/api/v1/user/create-student",
		"/api/v1/user/create-professor",
		"/api/v1/degree-course/ajax",
        "/ws/**"
    };


    protected static final String[] ADMIN_URLS = {
        // user
		"/user_admin/**",
		"/api/v1/user",
		"/api/v1/user/delete",

		// student
        "/api/v1/student/view",
		"/api/v1/student/read/read",
		"/api/v1/student/update/update",

        // professor
		"/api/v1/professor/view",
		"/api/v1/professor/read/read",
		"/api/v1/professor/update/update",

		// course
		"/api/v1/course/view",
		"/api/v1/course/read/read",
		"/api/v1/course/create/create",
		"/api/v1/course/update/update",
		"/api/v1/course/delete/delete",

        // degree course
		"/api/v1/degree-course/view",
		"/api/v1/degree-course/professors/view",
		"/api/v1/degree-course/students/view",

        // examination
		"/api/v1/examination/view",
		"/api/v1/examination/update/update",
		"/api/v1/examination/delete/delete",
		"/api/v1/examination/read/course-name",
		"/api/v1/examination/read/student-register",
		"/api/v1/examination/read/professor-unique-code"
    };


    protected static final String[] STUDENT_URLS = {
        // URL accessibili solo agli utenti con ruolo STUDENT o ADMIN
		"/user_student/**",
		"/api/v1/user/create/student",
		"/api/v1/user/update/student",
		"/api/v1/study_plan/view",
		"/api/v1/study_plan/modify",
		"/api/v1/study_plan/swap",
		"/api/v1/degree-course/courses/view",
		"/api/v1/degree-course/courses/ajax",
		"/api/v1/examination/create/create",
		"/api/v1/examination-appeal/available/student",
		"/api/v1/examination-appeal/booked/student",
		"/api/v1/examination-appeal/booked/{id}",
		"/api/v1/examination-appeal/delete-booked/{id}",
		"/api/v1/examination-outcome/view",
		"/api/v1/examination-outcome/outcome",
		"/api/v1/examination-outcome/confirm-refusal",
		"/api/v1/outcome-notifications"
    };


    protected static final String[] PROFESSOR_URLS = {
        // URL accessibili solo agli utenti con ruolo PROFESSOR o ADMIN
        "/user_professor/**",
        "/api/v1/user/create/professor",
        "/api/v1/user/update/professor",
        "/api/v1/course/view/professor",
        "/api/v1/course/read/{courseId}",
		"/api/v1/examination-appeal/make",
		"/api/v1/examination-appeal/create",
		"/api/v1/examination-appeal/delete",
		"/api/v1/examination-outcome/make/{register}/{id}",
		"/api/v1/examination-outcome/notify"
    };


    /**
     * Return the stream of all views.
     * @return the stream
     */
    public Stream<String> streamAllViews() {
        return moduleViews.values().stream().flatMap(List::stream);
    }


    /**
     * Return a map of all views.
     * @return the map
     */
    public Map<String, List<String>> getModuleViews() {
        return moduleViews;
    }


    /**
     * Return the view corresponding to the field.
     * If no mapping exists, returns the default view.
     * @param fieldName the field name
     * @return the view
     */
    public String resolveView(String fieldName) {
        return fieldToView.getOrDefault(fieldName, "exception/illegal/illegal-parameter");
    }


    /**
     * Shut down the application.
     * @param exitCodeGenerator the exit code generator
     * @see UmsExitCodeGenerator
     */
    public void shutDown(ExitCodeGenerator exitCodeGenerator) {
        logger.info("Shutting down the application...");
        SpringApplication.exit(applicationContext, exitCodeGenerator);
    }


    /**
     * Save the main args.
     * @param args the main args
     */
    public static void setMainArgs(String[] args) {
        mainArgs = args;
    }


    /**
     * Restart the application.
     * @see UmsExitCodeGenerator
     */
    public void restart() {
        Thread thread = new Thread(() -> {
            try {
                try (applicationContext) {
                    logger.info("Closing application context for restart...");
                }
            } catch (Exception e) {
                logger.error("Failed to close application context during restart", e);
                return; // non ha senso riavviare se la chiusura fallisce
            }

            try {
                logger.info("Restarting the application...");
                SpringApplication.run(UniversityManagementSystemApplication.class, mainArgs);
                logger.info("Application restarted successfully.");
            } catch (Exception e) {
                logger.error("Application failed to restart", e);
            }
        });

        thread.setDaemon(false);
        thread.start();
    }


    // beans
    @Bean
    ExitCodeGenerator exitCodeGenerator() {
        return new UmsExitCodeGenerator();
    }


    @Bean
	LocalValidatorFactoryBean validator() {
		return new LocalValidatorFactoryBean();
	}


    @Bean
    String genericExceptionUri() {
        return "/exception/generic-exception";
    }


    // commons uri
    @Bean
    String dataAccessExceptionUri() {
        return "/exception/data/data-access-exception";
    }

    @Bean
    String accessDeniedExceptionUri() {
        return "/exception/access_denied";
    }

    @Bean
    String illegalArgumentExceptionUri() {
        return "/exception/illegal";
    }

    @Bean
    String notFoundExceptionUri() {
        return "/exception/not_found";
    }

    @Bean
    String alreadyExistsExceptionUri() {
        return "/exception/already_exists";
    }

    @Bean
    String jsonProcessingExceptionUri() {
        return "/exception/data/json-processing-exception";
    }

    @Bean
    String duplicateUsernameUri() {
        return "/exception/illegal/invalid/duplicate-username";
    }

    @Bean
    String duplicateFiscalCodeUri() {
        return "/exception/illegal/invalid/duplicate-fiscal-code";
    }





}



