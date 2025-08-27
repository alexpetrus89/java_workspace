package com.alex.universitymanagementsystem.enum_type;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public enum ModuleViews {

    // commons macros
    CREATE("/create/create"),
    READ("/read/read"),
    UPDATE("/update/update"),
    DELETE("/delete/delete"),
    EXAMINATION_APPEAL("/examinations/examination_appeal"),
    EXAMINATION_OUTCOME("/examinations/examination_outcome"),
    ACCESS_DENIED("/access-denied"),
    ALREADY_EXISTS("/already-exists"),
    DATA("/data"),
    ILLEGAL("/illegal"),
    NOT_FOUND("/not-found");

    private final String path;

    ModuleViews(String path) {
        this.path = path;
    }

    public String path() {
        return path;
    }




    // --- Concrete modules with concrete view + view list ---
    public enum Modules {

        EXAMINATION("/examination", List.of(
                "/examination-menu",
                "/read/examination-course",
                "/read/examination-student",
                "/read/examination-professor",
                CREATE.path(),
                UPDATE.path(),
                DELETE.path()
        )),

        COURSE("/course", List.of(
                "/course-menu",
                READ.path(),
                CREATE.path(),
                UPDATE.path(),
                DELETE.path()
        )),

        DEGREE_COURSE("/degree_course", List.of(
                "/degree-course-menu",
                "/read/read-courses",
                "/read/read-professors",
                "/read/read-students"
        )),

        USER_ADMIN("/user_admin", List.of(
                "/admin-home",
                "/user-menu",
                UPDATE.path(),
                DELETE.path()
        )),

        USER_STUDENT("/user_student", List.of(
                "/student-home",
                "/create/create-student-from-user",
                UPDATE.path(),
                "/study_plan/study_plan_modify",
                "/examinations/examination-menu",
                EXAMINATION_APPEAL.path() + "/available-calendar",
                EXAMINATION_APPEAL.path() + "/booked-calendar",
                EXAMINATION_OUTCOME.path() + "/outcome",
                EXAMINATION_OUTCOME.path() + "/outcome-result",
                EXAMINATION_OUTCOME.path() + "/confirm-refusal",
                EXAMINATION_OUTCOME.path() + "/refusal-confirmed",
                EXAMINATION_OUTCOME.path() + "/student-absent"
        )),

        USER_PROFESSOR("/user_professor", List.of(
                "/professor-home",
                "/create/create-professor-from-user",
                UPDATE.path(),
                EXAMINATION_APPEAL.path() + "/examination-appeal-menu",
                EXAMINATION_APPEAL.path() + "/create/create-examination-appeal",
                EXAMINATION_APPEAL.path() + "/delete/delete-examination-appeal"
        )),

        PROFESSOR("/professor", List.of(
                "/professor-menu",
                READ.path(),
                CREATE.path(),
                UPDATE.path(),
                DELETE.path()
        )),

        STUDENT("/student", List.of(
                "/student-menu",
                READ.path(),
                CREATE.path(),
                UPDATE.path(),
                DELETE.path()
        )),

        EXCEPTION("/exception", List.of(
                "/generic-exception",
                "/transient-object-exception",
                ACCESS_DENIED.path() + "/access-denied",
                ALREADY_EXISTS.path() + "/object-already-exists",
                ALREADY_EXISTS.path() + "/professor-already-exists",
                ALREADY_EXISTS.path() + "/student-already-exists",
                DATA.path() + "/data-access-exception",
                DATA.path() + "/json-processing-exception",
                ILLEGAL.path() + "/registration/username-already-taken",
                ILLEGAL.path() + "/registration/password-not-match",
                ILLEGAL.path() + "/registration/invalid-dob",
                ILLEGAL.path() + "/study_plan/invalid-choice",
                ILLEGAL.path() + "/illegal-parameter",
                ILLEGAL.path() + "/illegal-parameters",
                ILLEGAL.path() + "/illegal-username",
                NOT_FOUND.path() + "/degree-course-not-found",
                NOT_FOUND.path() + "/error-not-found",
                NOT_FOUND.path() + "/fiscal-code-not-found",
                NOT_FOUND.path() + "/object-not-found",
                NOT_FOUND.path() + "/username-not-found"
        ));

        private final String prefix;
        private final List<String> views;

        Modules(String prefix, List<String> views) {
            this.prefix = prefix;
            // Costruisco i percorsi completi concatenando il prefisso
            this.views = views
                .stream()
                .map(view -> prefix + view)
                .toList();
        }

        public String getPrefix() {
            return prefix;
        }

        public List<String> getViews() {
            return views;
        }


        public static Stream<String> streamAllViews() {
            return Arrays
                .stream(values())
                .flatMap(module -> module.getViews().stream());
        }



    }



}



