package com.alex.universitymanagementsystem.config;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.domain.Examination;
import com.alex.universitymanagementsystem.domain.ExaminationAppeal;
import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.StudyPlan;
import com.alex.universitymanagementsystem.domain.User;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.dto.Builder;
import com.alex.universitymanagementsystem.dto.RegistrationForm;
import com.alex.universitymanagementsystem.enum_type.CourseType;
import com.alex.universitymanagementsystem.enum_type.DegreeType;
import com.alex.universitymanagementsystem.enum_type.MiurAcronymType;
import com.alex.universitymanagementsystem.enum_type.RoleType;
import com.alex.universitymanagementsystem.repository.CourseRepository;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;
import com.alex.universitymanagementsystem.repository.ExaminationAppealRepository;
import com.alex.universitymanagementsystem.repository.ExaminationRepository;
import com.alex.universitymanagementsystem.repository.ProfessorRepository;
import com.alex.universitymanagementsystem.repository.StudentRepository;
import com.alex.universitymanagementsystem.repository.StudyPlanRepository;
import com.alex.universitymanagementsystem.repository.UserRepository;


@Configuration
public class UmsDBInitConfig implements Serializable {

    // constants
    private static final String INGEGNERIA_GESTIONALE = "INGEGNERIA GESTIONALE";
    private static final String INGEGNERIA_MECCANICA = "INGEGNERIA MECCANICA";
    private static final String INGEGNERIA_ELETTRICA = "INGEGNERIA ELETTRICA";
    private static final String INGEGNERIA_CIVILE = "INGEGNERIA CIVILE";
    private static final String INGEGNERIA_INFORMATICA = "INGEGNERIA INFORMATICA";
    private static final String INGEGNERIA_GESTIONALE_MAGISTRALE = "INGEGNERIA GESTIONALE MAGISTRALE";
    private static final String INGEGNERIA_INFORMATICA_MAGISTRALE = "INGEGNERIA INFORMATICA MAGISTRALE";
    private static final String INGEGNERIA_MECCANICA_MAGISTRALE = "INGEGNERIA MECCANICA MAGISTRALE";
    private static final String INGEGNERIA_ELETTRICA_MAGISTRALE = "INGEGNERIA ELETTRICA MAGISTRALE";

    private static final String UC_GIACINTO = "wer456er";
    private static final String UC_GENESIO = "wer123er";
    private static final String UC_GIACOMO = "wer321er";
    private static final String UC_GIOELE = "wer111er";

    private static final String DEGREE_COURSE_NOT_FOUND_ERROR = "Degree course not found";
    private static final String COURSE_NOT_FOUND_ERROR = "Course not found";
    private static final String PROFESSOR_NOT_FOUND_ERROR = "Professor not found";
    private static final String STUDENT_NOT_FOUND_ERROR = "Student not found";

    private final transient Logger logger =
        LoggerFactory.getLogger(UmsDBInitConfig.class);

    @Bean
    @SuppressWarnings("unused")
    CommandLineRunner commandLineRunner(
        @Autowired
        UserRepository userRepository,
        @Autowired
        PasswordEncoder passwordEncoder,
        @Autowired
        DegreeCourseRepository degreeCourseRepository,
        @Autowired
        StudyPlanRepository studyPlanRepository,
        @Autowired
        StudentRepository studentRepository,
        @Autowired
        CourseRepository courseRepository,
        @Autowired
        ProfessorRepository professorRepository,
        @Autowired
        ExaminationRepository examinationRepository,
        @Autowired
        ExaminationAppealRepository examinationAppealRepository
    ) {
        return args -> {

            // user initializer
            initializeUsers(userRepository, passwordEncoder);

            // degree course initializer
            initializeDegreeCourse(degreeCourseRepository);

            // student initializer
            initializeStudents(studentRepository, degreeCourseRepository, passwordEncoder);

            // professor initializer
            initializeProfessors(professorRepository, passwordEncoder);

            // course initializer
            initializeCourses(courseRepository, professorRepository, degreeCourseRepository);

            // study plan initializer
            initializeStudyPlan(studyPlanRepository, degreeCourseRepository, studentRepository);

            // examination initializer
            initializeExaminations(studentRepository, courseRepository, examinationRepository);

            // examination appeal initializer
            initializeExaminationAppeals(examinationAppealRepository, courseRepository, studentRepository);

        };
    }










    // initialize user
    private void initializeUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        logger.info("\n\n\n--- INITIALIZE ADMIN ---");

        // create user  - 2 admin + 18 students + 9 professors
        Builder builderAdminOne = new Builder();
        builderAdminOne.withUsername("rico@gmail.com");
        builderAdminOne.withPassword("rico");
        builderAdminOne.withFirstName("damiano");
        builderAdminOne.withLastName("ruggieri");
        builderAdminOne.withDob(LocalDate.of(1993, 1, 1));
        builderAdminOne.withFiscalCode("abc678rde217we56");
        builderAdminOne.withStreet("via della nazione");
        builderAdminOne.withCity("fasano");
        builderAdminOne.withState("italia");
        builderAdminOne.withZip("72015");
        builderAdminOne.withPhone("3815674128");
        builderAdminOne.withRole(RoleType.ADMIN);

        Builder builderAdminTwo = new Builder();
        builderAdminTwo.withUsername("fido@gmail.com");
        builderAdminTwo.withPassword("fido");
        builderAdminTwo.withFirstName("enrico");
        builderAdminTwo.withLastName("ruggieri");
        builderAdminTwo.withDob(LocalDate.of(1993, 4, 1));
        builderAdminTwo.withFiscalCode("abc678rde217we47");
        builderAdminTwo.withStreet("via del calvario");
        builderAdminTwo.withCity("pezze di greco");
        builderAdminTwo.withState("italia");
        builderAdminTwo.withZip("72015");
        builderAdminTwo.withPhone("3815674128");
        builderAdminTwo.withRole(RoleType.ADMIN);

        // create users
        List<User> users = List.of(
            new RegistrationForm(builderAdminOne).toUser(passwordEncoder),
            new RegistrationForm(builderAdminTwo).toUser(passwordEncoder)
        );

        // save user
        users.forEach(user -> {
            try {
                userRepository.saveAndFlush(user);
            } catch (Exception e) {
                logger.error("Error: {}", e.getMessage());
            }
        });

    }










    // INITIALIZE DEGREE COURSE
    private void initializeDegreeCourse(DegreeCourseRepository degreeCourseRepository) {
        logger.info("\n\n\n--- INITIALIZE DEGREE COURSES ---");
        // degree courses list
        List<DegreeCourse> degreeCourses = new ArrayList<>();

        // create DegreeCourse
        degreeCourses.add(
            new DegreeCourse(
                INGEGNERIA_GESTIONALE,
                DegreeType.BACHELOR,
                3
            )
        );

        degreeCourses.add(
            new DegreeCourse(
                INGEGNERIA_INFORMATICA,
                DegreeType.BACHELOR,
                3
            )
        );

        degreeCourses.add(
            new DegreeCourse(
                INGEGNERIA_ELETTRICA,
                DegreeType.BACHELOR,
                3
            )
        );

        degreeCourses.add(
            new DegreeCourse(
                INGEGNERIA_CIVILE,
                DegreeType.BACHELOR,
                3
            )
        );

        degreeCourses.add(
            new DegreeCourse(
                INGEGNERIA_MECCANICA,
                DegreeType.BACHELOR,
                3
            )
        );

        degreeCourses.add(
            new DegreeCourse(
                INGEGNERIA_GESTIONALE_MAGISTRALE,
                DegreeType.MASTER,
                2
            )
        );

        degreeCourses.add(
            new DegreeCourse(
                INGEGNERIA_INFORMATICA_MAGISTRALE,
                DegreeType.MASTER,
                2
            )
        );

        degreeCourses.add(
            new DegreeCourse(
                INGEGNERIA_ELETTRICA_MAGISTRALE,
                DegreeType.MASTER,
                2
            )
        );

        degreeCourses.add(
            new DegreeCourse(
                INGEGNERIA_MECCANICA_MAGISTRALE,
                DegreeType.MASTER,
                2
            )
        );

        // sanity check
        if(degreeCourses.isEmpty())
            throw new IllegalStateException("Could not load degree course data");

        // save DegreeCourse
        degreeCourses.forEach(degreeCourseRepository::saveAndFlush);

    }










    // INITIALIZE STUDENTS
    private void initializeStudents(
        StudentRepository studentRepository,
        DegreeCourseRepository degreeCourseRepository,
        PasswordEncoder passwordEncoder
    ) {
        logger.info("\n\n\n--- INITIALIZE STUDENTS ---");
        // 1
        Builder builderStudentOne = new Builder();
        builderStudentOne.withUsername("nino@gmail.com");
        builderStudentOne.withPassword("nino");
        builderStudentOne.withFirstName("bob");
        builderStudentOne.withLastName("dylamie");
        builderStudentOne.withDob(LocalDate.of(1991, 4, 6));
        builderStudentOne.withFiscalCode("abc678rde217we12");
        builderStudentOne.withStreet("via delle lamie di olimpia");
        builderStudentOne.withCity("laureto");
        builderStudentOne.withState("italia");
        builderStudentOne.withZip("72015");
        builderStudentOne.withPhone("3619647852");
        builderStudentOne.withRole(RoleType.STUDENT);

        // 2
        Builder builderStudentTwo = new Builder();
        builderStudentTwo.withUsername("luca@gmail.com");
        builderStudentTwo.withPassword("luca");
        builderStudentTwo.withFirstName("pelaccio");
        builderStudentTwo.withLastName("pelaccio");
        builderStudentTwo.withDob(LocalDate.of(1991, 11, 12));
        builderStudentTwo.withFiscalCode("abc678rde217we71");
        builderStudentTwo.withStreet("via delle lamie di olimpia");
        builderStudentTwo.withCity("laureto");
        builderStudentTwo.withState("italia");
        builderStudentTwo.withZip("72015");
        builderStudentTwo.withPhone("38412369547");
        builderStudentTwo.withRole(RoleType.STUDENT);

        // 3
        Builder builderStudentThree = new Builder();
        builderStudentThree.withUsername("ennio@gmail.com");
        builderStudentThree.withPassword("ennio");
        builderStudentThree.withFirstName("ennio");
        builderStudentThree.withLastName("morricone");
        builderStudentThree.withDob(LocalDate.of(1991, 11, 12));
        builderStudentThree.withFiscalCode("abc678rde217we43");
        builderStudentThree.withStreet("via delle lamie di olimpia");
        builderStudentThree.withCity("laureto");
        builderStudentThree.withState("italia");
        builderStudentThree.withZip("72015");
        builderStudentThree.withPhone("38412369547");
        builderStudentThree.withRole(RoleType.STUDENT);

        // 4
        Builder builderStudentFour = new Builder();
        builderStudentFour.withUsername("gino@gmail.com");
        builderStudentFour.withPassword("gino");
        builderStudentFour.withFirstName("gino");
        builderStudentFour.withLastName("bramieri");
        builderStudentFour.withDob(LocalDate.of(1991, 11, 12));
        builderStudentFour.withFiscalCode("abc678rde217we34");
        builderStudentFour.withStreet("via delle lamie di olimpia");
        builderStudentFour.withCity("laureto");
        builderStudentFour.withState("italia");
        builderStudentFour.withZip("72015");
        builderStudentFour.withPhone("38412369547");
        builderStudentFour.withRole(RoleType.STUDENT);

        // 5
        Builder builderStudentFive = new Builder();
        builderStudentFive.withUsername("pino@gmail.com");
        builderStudentFive.withPassword("pino");
        builderStudentFive.withFirstName("pino");
        builderStudentFive.withLastName("pino");
        builderStudentFive.withDob(LocalDate.of(1991, 11, 12));
        builderStudentFive.withFiscalCode("abc678rde217we18");
        builderStudentFive.withStreet("via delle lamie di olimpia");
        builderStudentFive.withCity("laureto");
        builderStudentFive.withState("italia");
        builderStudentFive.withZip("72015");
        builderStudentFive.withPhone("38412369547");
        builderStudentFive.withRole(RoleType.STUDENT);

        // 6
        Builder builderStudentSix = new Builder();
        builderStudentSix.withUsername("tino@gmail.com");
        builderStudentSix.withPassword("tino");
        builderStudentSix.withFirstName("tino");
        builderStudentSix.withLastName("filipp");
        builderStudentSix.withDob(LocalDate.of(1991, 11, 12));
        builderStudentSix.withFiscalCode("abc678rde217we19");
        builderStudentSix.withStreet("via delle lamie di olimpia");
        builderStudentSix.withCity("laureto");
        builderStudentSix.withState("italia");
        builderStudentSix.withZip("72015");
        builderStudentSix.withPhone("38412369547");
        builderStudentSix.withRole(RoleType.STUDENT);

        // 7
        Builder builderStudentSeven = new Builder();
        builderStudentSeven.withUsername("solo@gmail.com");
        builderStudentSeven.withPassword("tino");
        builderStudentSeven.withFirstName("tino");
        builderStudentSeven.withLastName("laghezza");
        builderStudentSeven.withDob(LocalDate.of(1991, 11, 12));
        builderStudentSeven.withFiscalCode("abc678rde217we20");
        builderStudentSeven.withStreet("via delle lamie di olimpia");
        builderStudentSeven.withCity("laureto");
        builderStudentSeven.withState("italia");
        builderStudentSeven.withZip("72015");
        builderStudentSeven.withPhone("38412369547");
        builderStudentSeven.withRole(RoleType.STUDENT);

        // 8
        Builder builderStudentEight = new Builder();
        builderStudentEight.withUsername("otto@gmail.com");
        builderStudentEight.withPassword("otto");
        builderStudentEight.withFirstName("otto");
        builderStudentEight.withLastName("von bismarck");
        builderStudentEight.withDob(LocalDate.of(1991, 11, 12));
        builderStudentEight.withFiscalCode("abc678rde217we21");
        builderStudentEight.withStreet("via delle lamie di olimpia");
        builderStudentEight.withCity("laureto");
        builderStudentEight.withState("italia");
        builderStudentEight.withZip("72015");
        builderStudentEight.withPhone("38412369547");
        builderStudentEight.withRole(RoleType.STUDENT);

        // 9
        Builder builderStudentNine = new Builder();
        builderStudentNine.withUsername("raffo@gmail.com");
        builderStudentNine.withPassword("raffo");
        builderStudentNine.withFirstName("raffaele");
        builderStudentNine.withLastName("macina leone");
        builderStudentNine.withDob(LocalDate.of(1991, 11, 12));
        builderStudentNine.withFiscalCode("abc678rde217we22");
        builderStudentNine.withStreet("via les claypool 71");
        builderStudentNine.withCity("Bari");
        builderStudentNine.withState("italia");
        builderStudentNine.withZip("72100");
        builderStudentNine.withPhone("38412369547");
        builderStudentNine.withRole(RoleType.STUDENT);


        List<Student> students = new ArrayList<>();

        // 1
        students.add(
            new Student(
                builderStudentOne,
                passwordEncoder,
                new Register("123456"),
                degreeCourseRepository
                    .findByName(INGEGNERIA_GESTIONALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND_ERROR))
            )
        );

        // 2
        students.add(
            new Student(
                builderStudentTwo,
                passwordEncoder,
                new Register("123457"),
                degreeCourseRepository
                    .findByName(INGEGNERIA_GESTIONALE_MAGISTRALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND_ERROR))
            )
        );

        // 3
        students.add(
            new Student(
                builderStudentThree,
                passwordEncoder,
                new Register("123458"),
                degreeCourseRepository
                    .findByName(INGEGNERIA_INFORMATICA)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND_ERROR))
            )
        );

        // 4
        students.add(
            new Student(
                builderStudentFour,
                passwordEncoder,
                new Register("123459"),
                degreeCourseRepository
                    .findByName(INGEGNERIA_INFORMATICA_MAGISTRALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND_ERROR))
            )
        );

        // 5
        students.add(
            new Student(
                builderStudentFive,
                passwordEncoder,
                new Register("123460"),
                degreeCourseRepository
                    .findByName(INGEGNERIA_MECCANICA)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND_ERROR))
            )
        );

        // 6
        students.add(
            new Student(
                builderStudentSix,
                passwordEncoder,
                new Register("123461"),
                degreeCourseRepository
                    .findByName(INGEGNERIA_MECCANICA_MAGISTRALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND_ERROR))
            )
        );

        // 7
        students.add(
            new Student(
                builderStudentSeven,
                passwordEncoder,
                new Register("123462"),
                degreeCourseRepository
                    .findByName(INGEGNERIA_ELETTRICA)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND_ERROR))
            )
        );

        // 7
        students.add(
            new Student(
                builderStudentEight,
                passwordEncoder,
                new Register("169841"),
                degreeCourseRepository
                    .findByName(INGEGNERIA_MECCANICA)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND_ERROR))
            )
        );

        // 8
        students.add(
            new Student(
                builderStudentNine,
                passwordEncoder,
                new Register("555555"),
                degreeCourseRepository
                    .findByName(INGEGNERIA_GESTIONALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND_ERROR))
            )
        );


        // sanity check
        if(students.isEmpty())
            throw new IllegalArgumentException("students list is empty");

        // save students
        students.stream().forEach(studentRepository::saveAndFlush);

        // save students for GESTIONALE degree course
        DegreeCourse ingGest = degreeCourseRepository
            .findByName(INGEGNERIA_GESTIONALE)
            .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND_ERROR));

        // set students of ING. GESTIONALE in degreeCourse object
        ingGest.setStudents(
            studentRepository
                .findAll()
                .stream()
                .filter(student -> student.getDegreeCourse().getName().equals(INGEGNERIA_GESTIONALE))
                .toList()
        );

        // save degree course ING. GESTIONALE
        degreeCourseRepository.saveAndFlush(ingGest);


        // save students for GESTIONALE MAGISTRALE degree course
        DegreeCourse ingGestMag = degreeCourseRepository
            .findByName(INGEGNERIA_GESTIONALE_MAGISTRALE)
            .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND_ERROR));

        // set students of ING. GESTIONALE MAGISTRALE in degreeCourse object
        ingGestMag.setStudents(
            studentRepository
                .findAll()
                .stream()
                .filter(student -> student.getDegreeCourse().getName().equals(INGEGNERIA_GESTIONALE_MAGISTRALE))
                .toList()
        );

        // save degree course ING. GESTIONALE MAGISTRALE
        degreeCourseRepository.saveAndFlush(ingGestMag);


        // save students for INFORMATICA degree course
        DegreeCourse ingInf = degreeCourseRepository
            .findByName(INGEGNERIA_INFORMATICA)
            .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND_ERROR));

        ingInf.setStudents(
            studentRepository
                .findAll()
                .stream()
                .filter(student -> student.getDegreeCourse().getName().equals(INGEGNERIA_INFORMATICA))
                .toList()
        );

        degreeCourseRepository.saveAndFlush(ingInf);


        DegreeCourse ingInfMag = degreeCourseRepository
            .findByName(INGEGNERIA_INFORMATICA_MAGISTRALE)
            .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND_ERROR));

        ingInf.setStudents(
            studentRepository
                .findAll()
                .stream()
                .filter(student -> student.getDegreeCourse().getName().equals(INGEGNERIA_INFORMATICA_MAGISTRALE))
                .toList()
        );

        degreeCourseRepository.saveAndFlush(ingInfMag);


        DegreeCourse ingMecc = degreeCourseRepository
            .findByName(INGEGNERIA_MECCANICA)
            .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND_ERROR));

        ingMecc.setStudents(
            studentRepository
                .findAll()
                .stream()
                .filter(student -> student.getDegreeCourse().getName().equals(INGEGNERIA_MECCANICA))
                .toList()
        );

        degreeCourseRepository.saveAndFlush(ingMecc);

        DegreeCourse ingMeccMag = degreeCourseRepository
            .findByName(INGEGNERIA_MECCANICA_MAGISTRALE)
            .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND_ERROR));

        ingMeccMag.setStudents(
            studentRepository
                .findAll()
                .stream()
                .filter(student -> student.getDegreeCourse().getName().equals(INGEGNERIA_MECCANICA_MAGISTRALE))
                .toList()
        );

        degreeCourseRepository.saveAndFlush(ingMeccMag);

        DegreeCourse ingEle = degreeCourseRepository
            .findByName(INGEGNERIA_ELETTRICA)
            .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND_ERROR));

        ingEle.setStudents(
            studentRepository
                .findAll()
                .stream()
                .filter(student -> student.getDegreeCourse().getName().equals(INGEGNERIA_ELETTRICA))
                .toList()
        );

        degreeCourseRepository.saveAndFlush(ingEle);

    }










    // INITIALIZE PROFESSORS
    void initializeProfessors(ProfessorRepository professorRepository, PasswordEncoder passwordEncoder) {
        logger.info("\n\n\n--- INITIALIZE PROFESSORS ---");

        Builder fbProfOne = new Builder();
        fbProfOne.withUsername("professore.giacinto@dominio.it");
        fbProfOne.withPassword("dino");
        fbProfOne.withFirstName("gilles");
        fbProfOne.withLastName("villeneuve");
        fbProfOne.withDob(LocalDate.of(1993, 4, 6));
        fbProfOne.withFiscalCode("abc678rde217we11");
        fbProfOne.withStreet("via di vancouver");
        fbProfOne.withCity("vancouver");
        fbProfOne.withState("canada");
        fbProfOne.withZip("48759");
        fbProfOne.withPhone("8749652314");
        fbProfOne.withRole(RoleType.PROFESSOR);
        List<Professor> professors = new ArrayList<>();

        // 1
        professors.add(
            new Professor(
                fbProfOne,
                passwordEncoder,
                new UniqueCode(UC_GIACINTO)
            )
        );


        // 2
        Builder fbProfTwo = new Builder();
        fbProfTwo.withUsername("professore.genesio@dominio.it");
        fbProfTwo.withPassword("gene");
        fbProfTwo.withFirstName("tazio");
        fbProfTwo.withLastName("nuvolari");
        fbProfTwo.withDob(LocalDate.of(1968, 4, 6));
        fbProfTwo.withFiscalCode("abc999rde217we48");
        fbProfTwo.withStreet("via di babel");
        fbProfTwo.withCity("firenze");
        fbProfTwo.withState("italia");
        fbProfTwo.withZip("41695");
        fbProfTwo.withPhone("8749652314");
        fbProfTwo.withRole(RoleType.PROFESSOR);

        // 2
        professors.add(
            new Professor(
                fbProfTwo,
                passwordEncoder,
                new UniqueCode(UC_GENESIO)
            )
        );


        // 3
        Builder fbProfThree = new Builder();
        fbProfThree.withUsername("professore.giacomo@dominio.it");
        fbProfThree.withPassword("giaco");
        fbProfThree.withFirstName("giacomo");
        fbProfThree.withLastName("agostini");
        fbProfThree.withDob(LocalDate.of(1984, 8, 7));
        fbProfThree.withFiscalCode("abc568rde217we76");
        fbProfThree.withStreet("via di florio");
        fbProfThree.withCity("palermo");
        fbProfThree.withState("italia");
        fbProfThree.withZip("91000");
        fbProfThree.withPhone("8749652314");
        fbProfThree.withRole(RoleType.PROFESSOR);

        // 3
        professors.add(
            new Professor(
                fbProfThree,
                passwordEncoder,
                new UniqueCode(UC_GIACOMO)
            )
        );


        // 3
        Builder fbProfFour = new Builder();
        fbProfFour.withUsername("professore.gioele@dominio.it");
        fbProfFour.withPassword("gioele");
        fbProfFour.withFirstName("john");
        fbProfFour.withLastName("surtees");
        fbProfFour.withDob(LocalDate.of(1993, 4, 6));
        fbProfFour.withFiscalCode("zzz665rde217we56");
        fbProfFour.withStreet("via di vancouver");
        fbProfFour.withCity("vancouver");
        fbProfFour.withState("canada");
        fbProfFour.withZip("48759");
        fbProfFour.withPhone("8749652314");
        fbProfFour.withRole(RoleType.PROFESSOR);

        // 3
        professors.add(
            new Professor(
                fbProfFour,
                passwordEncoder,
                new UniqueCode(UC_GIOELE)
            )
        );


        // sanity check
        if(professors.isEmpty())
            throw new IllegalArgumentException("professors list is empty");

        // save professors
        professors.stream().forEach(professorRepository::saveAndFlush);

    }










    // INITIALIZE COURSES
    private void initializeCourses(
        CourseRepository courseRepository,
        ProfessorRepository professorRepository,
        DegreeCourseRepository degreeCourseRepository
    ) {
        logger.info("\n\n\n--- INITIALIZE COURSES ---");
        // retrieve degreeCourses
        DegreeCourse ingGest = degreeCourseRepository
            .findByName(INGEGNERIA_GESTIONALE)
            .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND_ERROR));

        DegreeCourse ingGestMag = degreeCourseRepository
            .findByName(INGEGNERIA_GESTIONALE_MAGISTRALE)
            .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND_ERROR));

        DegreeCourse ingInf = degreeCourseRepository
            .findByName(INGEGNERIA_INFORMATICA)
            .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND_ERROR));

        DegreeCourse ingInfMag = degreeCourseRepository
            .findByName(INGEGNERIA_INFORMATICA_MAGISTRALE)
            .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND_ERROR));

        DegreeCourse ingMecc = degreeCourseRepository
            .findByName(INGEGNERIA_MECCANICA)
            .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND_ERROR));

        DegreeCourse ingMeccMag = degreeCourseRepository
            .findByName(INGEGNERIA_MECCANICA_MAGISTRALE)
            .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND_ERROR));

        DegreeCourse ingEle = degreeCourseRepository
            .findByName(INGEGNERIA_ELETTRICA)
            .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND_ERROR));

        List<DegreeCourse> degreeCourses = Arrays.asList(ingGest,ingGestMag, ingInf, ingInfMag, ingMecc, ingMeccMag, ingEle);
        List<Course> courses = new ArrayList<>();

        // create courses
        courses.add(
            new Course(
                MiurAcronymType.MAT,
                "analisi matematica",
                CourseType.MATEMATICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGest
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.MAT,
                "analisi matematica",
                CourseType.MATEMATICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GENESIO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingInf
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.MAT,
                "analisi matematica",
                CourseType.MATEMATICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GENESIO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingMecc
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.MAT,
                "analisi matematica",
                CourseType.MATEMATICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GENESIO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingEle
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.ECO,
                "elementi di economia",
                CourseType.ECONOMIA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGest
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.ECO,
                "elementi di economia",
                CourseType.ECONOMIA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GENESIO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingInf
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.ECO,
                "elementi di economia",
                CourseType.ECONOMIA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GENESIO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingMecc
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.MAT,
                "geometria e algebra",
                CourseType.MATEMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGest
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.MAT,
                "geometria e algebra",
                CourseType.MATEMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingInf
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.MAT,
                "geometria e algebra",
                CourseType.MATEMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingMecc
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.MAT,
                "analisi dei sistemi dinamici",
                CourseType.MATEMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGestMag
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.MAT,
                "analisi dei sistemi dinamici",
                CourseType.MATEMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIOELE))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingInfMag
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.FIS,
                "fisica generale",
                CourseType.FISICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACOMO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGest
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.FIS,
                "fisica generale",
                CourseType.FISICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACOMO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingInf
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.FIS,
                "fisica generale",
                CourseType.FISICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACOMO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingMecc
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.FIS,
                "fisica generale",
                CourseType.FISICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACOMO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingEle
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.INF,
                "fondamenti di informatica",
                CourseType.INFORMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGest
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.INF,
                "fondamenti di informatica",
                CourseType.INFORMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GENESIO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingInf
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.INF,
                "fondamenti di informatica",
                CourseType.INFORMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GENESIO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingMecc
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.INF,
                "fondamenti di informatica",
                CourseType.INFORMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GENESIO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingEle
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.CHI,
                "chimica generale",
                CourseType.CHIMICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIOELE))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGest
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.CHI,
                "chimica generale",
                CourseType.CHIMICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIOELE))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingInf
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.CHI,
                "chimica generale",
                CourseType.CHIMICA,
                9,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIOELE))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingMecc
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.CHI,
                "chimica generale",
                CourseType.CHIMICA,
                9,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIOELE))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingEle
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.ING,
                "scienza delle costruzioni",
                CourseType.ING_MECCANICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACOMO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGest
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.ING,
                "scienza delle costruzioni",
                CourseType.ING_MECCANICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACOMO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingMecc
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.MAT,
                "metodi di ottimizzazione",
                CourseType.MATEMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGest
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.ING,
                "metodi di rappresentazione tecnica",
                CourseType.DISEGNO,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGest
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.ING,
                "elementi di meccanica delle macchine e progettazione meccanica",
                CourseType.ING_MECCANICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACOMO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGest
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.ING,
                "fisica tecnica e sistemi energetici",
                CourseType.FISICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACOMO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGest
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.ECO,
                "gestione aziendale",
                CourseType.ECONOMIA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACOMO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGest
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.FIS,
                "meccanica dei fluidi",
                CourseType.IDRAULICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIOELE))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGest
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.ING,
                "principi di ingegneria elettrica",
                CourseType.ING_ELETTRICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACOMO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGest
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.ING,
                "tecnologia meccaniche e dei materiali",
                CourseType.ING_MECCANICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACOMO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGest
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.MAT,
                "calcolo numerico",
                CourseType.MATEMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGest
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.ING,
                "gestione dei progetti",
                CourseType.ING_GESTIONALE,
                9,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGest
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.ING,
                "impianti industriali",
                CourseType.ING_MECCANICA,
                9,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACOMO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGest
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.LET,
                "inglese",
                CourseType.LINGUA_STRANIERA,
                3,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIOELE))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGest
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.ING,
                "progettazione dei processi produttivi e qualità dei processi produttivi",
                CourseType.ING_GESTIONALE,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GENESIO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGest
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.ING,
                "sicurezza degli impianti industriali",
                CourseType.ING_GESTIONALE,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGest
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.ING,
                "materiali innovativi per l'ingegneria elettrica",
                CourseType.ING_ELETTRICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGest
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.ING,
                "tirocinio",
                CourseType.ING_GESTIONALE,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIOELE))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGest
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.ING,
                "prova finale",
                CourseType.ING_GESTIONALE,
                3,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGest
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.INF,
                "sistemi informativi",
                CourseType.ING_INFORMATICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GENESIO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGestMag
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.INF,
                "sistemi informativi",
                CourseType.ING_INFORMATICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GENESIO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingInfMag
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.INF,
                "big data analytics",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGestMag
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.INF,
                "internet of things",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIOELE))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGestMag
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.INF,
                "internet of things",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIOELE))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingInfMag
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.INF,
                "fondamenti di cybersecurity",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIOELE))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGestMag
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.INF,
                "basi di dati",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGestMag
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.INF,
                "basi di dati",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingInfMag
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.ING,
                "produzione avanzata nella fabbrica digitale",
                CourseType.ING_MECCANICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GENESIO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingGestMag
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.MAT,
                "analisi dei sistemi dinamici",
                CourseType.MATEMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GENESIO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingMeccMag
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.INF,
                "algoritmi e strutture dati in java",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GENESIO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingInf
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.INF,
                "fondamenti di telecomunicazioni",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACOMO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingInf
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.FIS,
                "fondamenti di elettronica",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACOMO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingInf
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.INF,
                "compilatori",
                CourseType.ING_INFORMATICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACOMO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingInfMag
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.INF,
                "big data",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIOELE))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingInfMag
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.ING,
                "impianti meccanici",
                CourseType.ING_MECCANICA,
                3,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GENESIO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingMecc
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.ING,
                "energetica e macchine a fluido",
                CourseType.ING_MECCANICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingMecc
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.ING,
                "misure meccaniche e termiche",
                CourseType.ING_MECCANICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingMecc
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.ING,
                "turbomacchine",
                CourseType.ING_MECCANICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingMeccMag
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.FIS,
                "gasdinamica e fluidodinamica",
                CourseType.ING_MECCANICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingMeccMag
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.ING,
                "macchine elettriche",
                CourseType.ING_ELETTRICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingEle
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.ING,
                "elettrica di potenza",
                CourseType.ING_ELETTRICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIOELE))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingEle
            )
        );

        courses.add(
            new Course(
                MiurAcronymType.ING,
                "impianti elettrici civili e industriali",
                CourseType.ING_MECCANICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIOELE))
                    .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_FOUND_ERROR)),
                ingEle
            )
        );


        // save courses
        courses.forEach(courseRepository::saveAndFlush);

        // associate the respective courses to each degree course
        courses
            .stream()
            .forEach(course -> degreeCourses
                .stream()
                .filter(degreeCourse -> degreeCourse.equals(course.getDegreeCourse()))
                .forEach(degreeCourse -> degreeCourse.addCourse(course))
            );


        // save updated degree courses
        degreeCourses.forEach(degreeCourseRepository::saveAndFlush);

    }










    // initialize examinations
    private void initializeExaminations(
        StudentRepository studentRepository,
        CourseRepository courseRepository,
        ExaminationRepository examinationRepository
    ) {

        logger.info("\n\n\n--- INITIALIZE EXAMINATIONS ---");

        // Retrieve existing Student entity from the database
        Student nino = studentRepository
            .findByRegister(new Register("123456"))
            .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND_ERROR));
        Student raffo = studentRepository
            .findByRegister(new Register("555555"))
            .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND_ERROR));
        Student luca = studentRepository
            .findByRegister(new Register("123457"))
            .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND_ERROR));


        // Retrieve existing Course entity from the database

        Course analisiMatematica = courseRepository
            .findByNameAndDegreeCourseName("analisi matematica", INGEGNERIA_GESTIONALE)
            .orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND_ERROR));

        Course geometria = courseRepository
            .findByNameAndDegreeCourseName("geometria e algebra", INGEGNERIA_GESTIONALE)
            .orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND_ERROR));

        Course scienzaCostr = courseRepository
            .findByNameAndDegreeCourseName("scienza delle costruzioni", INGEGNERIA_GESTIONALE)
            .orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND_ERROR));

        Course impInd = courseRepository
            .findByNameAndDegreeCourseName("impianti industriali", INGEGNERIA_GESTIONALE)
            .orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND_ERROR));

        Course metodiOttimizzazione = courseRepository
            .findByNameAndDegreeCourseName("metodi di ottimizzazione", INGEGNERIA_GESTIONALE)
            .orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND_ERROR));

        Course anSisDin = courseRepository
            .findByNameAndDegreeCourseName("analisi dei sistemi dinamici", INGEGNERIA_GESTIONALE_MAGISTRALE)
            .orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND_ERROR));

        Course basiDiDati = courseRepository
            .findByNameAndDegreeCourseName("basi di dati", INGEGNERIA_GESTIONALE_MAGISTRALE)
            .orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND_ERROR));

        List<Examination> examinations = new ArrayList<>();

        // create examinations
        examinations.add(
            new Examination(
                analisiMatematica,
                nino,
                30,
                true,
                LocalDate.of(2022, 6, 23)
            )
        );

        examinations.add(
            new Examination(
                geometria,
                nino,
                30,
                true,
                LocalDate.of(2018, 2, 18)
            )
        );

        examinations.add(
            new Examination(
                scienzaCostr,
                nino,
                18,
                false,
                LocalDate.of(2019, 2, 18)
            )
        );

        examinations.add(
            new Examination(
                impInd,
                nino,
                24,
                false,
                LocalDate.of(2020, 2, 18)
            )
        );

        examinations.add(
            new Examination(
                metodiOttimizzazione,
                nino,
                27,
                false,
                LocalDate.of(2021, 2, 18)
            )
        );

        examinations.add(
            new Examination(
                analisiMatematica,
                raffo,
                30,
                true,
                LocalDate.of(2022, 6, 23)
            )
        );

        examinations.add(
            new Examination(
                geometria,
                raffo,
                30,
                true,
                LocalDate.of(2018, 2, 18)
            )
        );

        examinations.add(
            new Examination(
                scienzaCostr,
                raffo,
                30,
                true,
                LocalDate.of(2019, 2, 18)
            )
        );

        examinations.add(
            new Examination(
                impInd,
                raffo,
                30,
                false,
                LocalDate.of(2020, 2, 18)
            )
        );

        examinations.add(
            new Examination(
                metodiOttimizzazione,
                raffo,
                21,
                false,
                LocalDate.of(2021, 2, 18)
            )
        );

        examinations.add(
            new Examination(
                anSisDin,
                luca,
                28,
                false,
                LocalDate.of(2024, 05, 18)
            )
        );

        examinations.add(
            new Examination(
                basiDiDati,
                luca,
                25,
                false,
                LocalDate.of(2024, 05, 18)
            )
        );




        // save examination
        examinations
            .stream()
            .forEach(examination -> {
                try {
                    examinationRepository.saveAndFlush(examination);
                } catch (Exception e) {
                    logger.info("Error: {}", e.getMessage());
                }
            });

    }


    // INITIALIZE STUDY PLANS
    private void initializeStudyPlan(
        StudyPlanRepository studyPlanRepository,
        DegreeCourseRepository degreeCourseRepository,
        StudentRepository studentRepository
    ) {
        logger.info("\n\n\n--- INITIALIZE STUDY PLANS ---");

        DegreeCourse ingGest = degreeCourseRepository
            .findByName(INGEGNERIA_GESTIONALE)
            .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND_ERROR));


        DegreeCourse ingGestMag = degreeCourseRepository
            .findByName(INGEGNERIA_GESTIONALE_MAGISTRALE)
            .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND_ERROR));


        List<StudyPlan> studyPlans = new ArrayList<>();

        studyPlans.add(
            new StudyPlan(
                studentRepository
                    .findByRegister(new Register("123456"))
                    .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND_ERROR)),
                "ORD509",
                new HashSet<>(ingGest.getCourses()
                    .stream()
                    .collect(Collectors.toSet())
                )
            )
        );

        studyPlans.add(
            new StudyPlan(
                studentRepository
                    .findByRegister(new Register("123457"))
                    .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND_ERROR)),
                "ORD270",
                new HashSet<>(ingGestMag.getCourses()
                    .stream()
                    .collect(Collectors.toSet())
                )
            )
        );

        studyPlans.add(
            new StudyPlan(
                studentRepository
                    .findByRegister(new Register("555555"))
                    .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND_ERROR)),
                "ORD509",
                new HashSet<>(ingGest.getCourses()
                    .stream()
                    .collect(Collectors.toSet())
                )
            )
        );

        // sanity check
        if(studyPlans.isEmpty())
            throw new IllegalArgumentException("study plan list is empty");

        // save students
        studyPlans
            .stream()
            .forEach(studyPlanRepository::saveAndFlush);

    }










    // initialize examination appeal
    private void initializeExaminationAppeals(
        ExaminationAppealRepository examinationAppealRepository,
        CourseRepository courseRepository,
        StudentRepository studentRepository
    ) {
        logger.info("\n\n\n--- INITIALIZE EXAMINATION APPEALS ---");

        Course analisiMat = courseRepository
            .findByNameAndDegreeCourseName("analisi matematica", INGEGNERIA_GESTIONALE)
            .orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND_ERROR));

        Course geometria = courseRepository
            .findByNameAndDegreeCourseName("geometria e algebra", INGEGNERIA_GESTIONALE)
            .orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND_ERROR));

        Course fisicaGen = courseRepository
            .findByNameAndDegreeCourseName("fisica generale", INGEGNERIA_GESTIONALE)
            .orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND_ERROR));

        Course informatica = courseRepository
            .findByNameAndDegreeCourseName("fondamenti di informatica", INGEGNERIA_GESTIONALE)
            .orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND_ERROR));

        Course gp = courseRepository
            .findByNameAndDegreeCourseName("gestione dei progetti", INGEGNERIA_GESTIONALE)
            .orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND_ERROR));

        Course scienzaDelleCostr = courseRepository
            .findByNameAndDegreeCourseName("scienza delle costruzioni", INGEGNERIA_GESTIONALE)
            .orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND_ERROR));

        Course chimicaGen = courseRepository
            .findByNameAndDegreeCourseName("chimica generale", INGEGNERIA_GESTIONALE)
            .orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND_ERROR));

        Course calcoloNum = courseRepository
            .findByNameAndDegreeCourseName("calcolo numerico", INGEGNERIA_GESTIONALE)
            .orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND_ERROR));

        Course economia = courseRepository
            .findByNameAndDegreeCourseName("elementi di economia", INGEGNERIA_GESTIONALE)
            .orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND_ERROR));

        Course sicurezza = courseRepository
            .findByNameAndDegreeCourseName("sicurezza degli impianti industriali", INGEGNERIA_GESTIONALE)
            .orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND_ERROR));

        Course materialiInnovativi = courseRepository
            .findByNameAndDegreeCourseName("materiali innovativi per l'ingegneria elettrica", INGEGNERIA_GESTIONALE)
            .orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND_ERROR));

        List<ExaminationAppeal> examinationAppeals = new ArrayList<>();

        examinationAppeals.add(
            new ExaminationAppeal(
                analisiMat,
                "modulo 1 + modulo 2",
                LocalDate.of(2026, 6, 23)
            )
        );

        examinationAppeals.add(
            new ExaminationAppeal(
                geometria,
                "matrici",
                LocalDate.of(2025, 6, 23)
            )
        );

        examinationAppeals.add(
            new ExaminationAppeal(
                fisicaGen,
                "meccanica, termodinamica, elettromagnetismo",
                LocalDate.of(2025, 4, 23)
            )
        );

        examinationAppeals.add(
            new ExaminationAppeal(
                scienzaDelleCostr,
                "tensori, travi, sollecitazioni",
                LocalDate.of(2025, 7, 26)
            )
        );

        examinationAppeals.add(
            new ExaminationAppeal(
                chimicaGen,
                "chimica inorganica",
                LocalDate.of(2025, 5, 16)
            )
        );

        examinationAppeals.add(
            new ExaminationAppeal(
                calcoloNum,
                "fondamenti del calcolo numerico",
                LocalDate.of(2025, 5, 18)
            )
        );

        examinationAppeals.add(
            new ExaminationAppeal(
                economia,
                "microeconomia + macroeconomia",
                LocalDate.of(2025, 11, 18)
            )
        );

        examinationAppeals.add(
            new ExaminationAppeal(
                sicurezza,
                "sicurezza degli impianti industriali",
                LocalDate.of(2025, 05, 03)
            )
        );

        examinationAppeals.add(
            new ExaminationAppeal(
                materialiInnovativi,
                "materiali innovativi per l'ingegneria elettrica",
                LocalDate.of(2025, 8, 18)
            )
        );

        examinationAppeals.add(
            new ExaminationAppeal(
                informatica,
                "fondamenti di informatica",
                LocalDate.of(2025, 7, 15)
            )
        );


        // retrieve students
        Student nino = studentRepository
            .findByRegister(new Register("123456"))
            .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND_ERROR));
        Student raffo = studentRepository
            .findByRegister(new Register("555555"))
            .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND_ERROR));

        // add students
        ExaminationAppeal gpAppeal = new ExaminationAppeal(gp, "crm, pert", LocalDate.of(2025, 04, 30));
        gpAppeal.addRegister(nino.getRegister());
        gpAppeal.addRegister(raffo.getRegister());
        examinationAppeals.add(gpAppeal);

        ExaminationAppeal infAppeal = new ExaminationAppeal(informatica, "programmazione", LocalDate.of(2025, 10, 19));
        infAppeal.addRegister(nino.getRegister());
        infAppeal.addRegister(raffo.getRegister());
        examinationAppeals.add(infAppeal);

        ExaminationAppeal sicurezzaAppeal = new ExaminationAppeal(sicurezza, "sicurezza degli impianti industriali", LocalDate.of(2025, 05, 07));
        sicurezzaAppeal.addRegister(nino.getRegister());
        sicurezzaAppeal.addRegister(raffo.getRegister());
        examinationAppeals.add(sicurezzaAppeal);

        ExaminationAppeal materialiInnovativiAppeal = new ExaminationAppeal(materialiInnovativi, "materiali innovativi per l'ingegneria elettrica", LocalDate.of(2025, 8, 18));
        materialiInnovativiAppeal.addRegister(nino.getRegister());
        materialiInnovativiAppeal.addRegister(raffo.getRegister());
        examinationAppeals.add(materialiInnovativiAppeal);

        ExaminationAppeal inf2Appeal = new ExaminationAppeal(informatica, "fondamenti di informatica", LocalDate.of(2025, 7, 15));
        inf2Appeal.addRegister(nino.getRegister());
        inf2Appeal.addRegister(raffo.getRegister());
        examinationAppeals.add(inf2Appeal);

        if(examinationAppeals.isEmpty())
            throw new IllegalArgumentException("examination appeal list is empty");

        examinationAppeals
            .stream()
            .forEach(examinationAppealRepository::saveAndFlush);

    }

}

