package com.alex.universitymanagementsystem.config;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.domain.ExaminationAppeal;
import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.User;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.dto.ExaminationDto;
import com.alex.universitymanagementsystem.dto.StudyPlanDto;
import com.alex.universitymanagementsystem.enum_type.CourseType;
import com.alex.universitymanagementsystem.enum_type.DegreeType;
import com.alex.universitymanagementsystem.enum_type.RoleType;
import com.alex.universitymanagementsystem.mapper.ExaminationMapper;
import com.alex.universitymanagementsystem.mapper.StudyPlanMapper;
import com.alex.universitymanagementsystem.repository.CourseRepository;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;
import com.alex.universitymanagementsystem.repository.ExaminationAppealRepository;
import com.alex.universitymanagementsystem.repository.ExaminationRepository;
import com.alex.universitymanagementsystem.repository.ProfessorRepository;
import com.alex.universitymanagementsystem.repository.StudentRepository;
import com.alex.universitymanagementsystem.repository.StudyPlanRepository;
import com.alex.universitymanagementsystem.repository.UserRepository;
import com.alex.universitymanagementsystem.utils.Builder;
import com.alex.universitymanagementsystem.utils.RegistrationForm;

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

    private final transient Logger logger =
        org.slf4j.LoggerFactory.getLogger(UmsDBInitConfig.class);


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
            initializeExaminations(studentRepository, courseRepository, degreeCourseRepository, examinationRepository);

            // examination appeal initializer
            initializeExaminationAppeals(examinationAppealRepository, courseRepository, degreeCourseRepository, studentRepository);

        };
    }










    // initialize user
    private void initializeUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        // create user  - 2 admin + 18 students + 9 professors
        Builder builderAdminOne = new Builder();
        builderAdminOne.withUsername("rico@gmail.com");
        builderAdminOne.withPassword("rico");
        builderAdminOne.withFullname("damiano ruggieri");
        builderAdminOne.withDob(LocalDate.of(1993, 1, 1));
        builderAdminOne.withStreet("via della nazione");
        builderAdminOne.withCity("fasano");
        builderAdminOne.withState("italia");
        builderAdminOne.withZip("72015");
        builderAdminOne.withPhone("3815674128");
        builderAdminOne.withRole(RoleType.ADMIN);

        Builder builderAdminTwo = new Builder();
        builderAdminTwo.withUsername("fido@gmail.com");
        builderAdminTwo.withPassword("fido");
        builderAdminTwo.withFullname("enrico ruggieri");
        builderAdminTwo.withDob(LocalDate.of(1993, 4, 1));
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

        // 1
        Builder builderStudentOne = new Builder();
        builderStudentOne.withUsername("nino@gmail.com");
        builderStudentOne.withPassword("nino");
        builderStudentOne.withFullname("bob dylamie");
        builderStudentOne.withDob(LocalDate.of(1991, 4, 6));
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
        builderStudentTwo.withFullname("pelaccio");
        builderStudentTwo.withDob(LocalDate.of(1991, 11, 12));
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
        builderStudentThree.withFullname("ennio");
        builderStudentThree.withDob(LocalDate.of(1991, 11, 12));
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
        builderStudentFour.withFullname("gino");
        builderStudentFour.withDob(LocalDate.of(1991, 11, 12));
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
        builderStudentFive.withFullname("pino");
        builderStudentFive.withDob(LocalDate.of(1991, 11, 12));
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
        builderStudentSix.withFullname("tino");
        builderStudentSix.withDob(LocalDate.of(1991, 11, 12));
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
        builderStudentSeven.withFullname("tino");
        builderStudentSeven.withDob(LocalDate.of(1991, 11, 12));
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
        builderStudentEight.withFullname("otto");
        builderStudentEight.withDob(LocalDate.of(1991, 11, 12));
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
        builderStudentNine.withFullname("raffaele macina leone");
        builderStudentNine.withDob(LocalDate.of(1991, 11, 12));
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
                degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE)
            )
        );

        // 2
        students.add(
            new Student(
                builderStudentTwo,
                passwordEncoder,
                new Register("123457"),
                degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE_MAGISTRALE)
            )
        );

        // 3
        students.add(
            new Student(
                builderStudentThree,
                passwordEncoder,
                new Register("123458"),
                degreeCourseRepository.findByName(INGEGNERIA_INFORMATICA)
            )
        );

        // 4
        students.add(
            new Student(
                builderStudentFour,
                passwordEncoder,
                new Register("123459"),
                degreeCourseRepository.findByName(INGEGNERIA_INFORMATICA_MAGISTRALE)
            )
        );

        // 5
        students.add(
            new Student(
                builderStudentFive,
                passwordEncoder,
                new Register("123460"),
                degreeCourseRepository.findByName(INGEGNERIA_MECCANICA)
            )
        );

        // 6
        students.add(
            new Student(
                builderStudentSix,
                passwordEncoder,
                new Register("123461"),
                degreeCourseRepository.findByName(INGEGNERIA_MECCANICA_MAGISTRALE)
            )
        );

        // 7
        students.add(
            new Student(
                builderStudentSeven,
                passwordEncoder,
                new Register("123462"),
                degreeCourseRepository.findByName(INGEGNERIA_ELETTRICA)
            )
        );

        // 7
        students.add(
            new Student(
                builderStudentEight,
                passwordEncoder,
                new Register("169841"),
                degreeCourseRepository.findByName(INGEGNERIA_MECCANICA)
            )
        );

        // 8
        students.add(
            new Student(
                builderStudentNine,
                passwordEncoder,
                new Register("555555"),
                degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE)
            )
        );


        // sanity check
        if(students.isEmpty())
            throw new IllegalArgumentException("students list is empty");

        // save students
        students.stream().forEach(studentRepository::saveAndFlush);

        // save students for GESTIONALE degree course
        DegreeCourse ingGest = degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE);

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
        DegreeCourse ingGestMag = degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE_MAGISTRALE);

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
        DegreeCourse ingInf = degreeCourseRepository.findByName(INGEGNERIA_INFORMATICA);

        ingInf.setStudents(
            studentRepository
                .findAll()
                .stream()
                .filter(student -> student.getDegreeCourse().getName().equals(INGEGNERIA_INFORMATICA))
                .toList()
        );

        degreeCourseRepository.saveAndFlush(ingInf);


        DegreeCourse ingInfMag = degreeCourseRepository.findByName(INGEGNERIA_INFORMATICA_MAGISTRALE);

        ingInf.setStudents(
            studentRepository
                .findAll()
                .stream()
                .filter(student -> student.getDegreeCourse().getName().equals(INGEGNERIA_INFORMATICA_MAGISTRALE))
                .toList()
        );

        degreeCourseRepository.saveAndFlush(ingInfMag);


        DegreeCourse ingMecc = degreeCourseRepository.findByName(INGEGNERIA_MECCANICA);

        ingMecc.setStudents(
            studentRepository
                .findAll()
                .stream()
                .filter(student -> student.getDegreeCourse().getName().equals(INGEGNERIA_MECCANICA))
                .toList()
        );

        degreeCourseRepository.saveAndFlush(ingMecc);

        DegreeCourse ingMeccMag = degreeCourseRepository.findByName(INGEGNERIA_MECCANICA_MAGISTRALE);

        ingMeccMag.setStudents(
            studentRepository
                .findAll()
                .stream()
                .filter(student -> student.getDegreeCourse().getName().equals(INGEGNERIA_MECCANICA_MAGISTRALE))
                .toList()
        );

        degreeCourseRepository.saveAndFlush(ingMeccMag);

        DegreeCourse ingEle = degreeCourseRepository.findByName(INGEGNERIA_ELETTRICA);

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

        Builder fbProfOne = new Builder();
        fbProfOne.withUsername("professore.giacinto@dominio.it");
        fbProfOne.withPassword("dino");
        fbProfOne.withFullname("gilles villeneuve");
        fbProfOne.withDob(LocalDate.of(1993, 4, 6));
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
                new UniqueCode(UC_GIACINTO),
                "abc678rde217we56"
            )
        );


        // 2
        Builder fbProfTwo = new Builder();
        fbProfTwo.withUsername("professore.genesio@dominio.it");
        fbProfTwo.withPassword("gene");
        fbProfTwo.withFullname("tazio nuvolari");
        fbProfTwo.withDob(LocalDate.of(1968, 4, 6));
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
                new UniqueCode(UC_GENESIO),
                "abc784rde217we56"
            )
        );


        // 3
        Builder fbProfThree = new Builder();
        fbProfThree.withUsername("professore.giacomo@dominio.it");
        fbProfThree.withPassword("giaco");
        fbProfThree.withFullname("giacomo agostini");
        fbProfThree.withDob(LocalDate.of(1984, 8, 7));
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
                new UniqueCode(UC_GIACOMO),
                "xxx965rde217we56"
            )
        );


        // 3
        Builder fbProfFour = new Builder();
        fbProfFour.withUsername("professore.gioele@dominio.it");
        fbProfFour.withPassword("gioele");
        fbProfFour.withFullname("john surtees");
        fbProfFour.withDob(LocalDate.of(1993, 4, 6));
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
                new UniqueCode(UC_GIOELE),
                "zzz665rde217we56"
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

        // retrieve degreeCourses
        DegreeCourse ingGest = degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE);

        DegreeCourse ingGestMag = degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE_MAGISTRALE);

        DegreeCourse ingInf = degreeCourseRepository.findByName(INGEGNERIA_INFORMATICA);

        DegreeCourse ingInfMag = degreeCourseRepository.findByName(INGEGNERIA_INFORMATICA_MAGISTRALE);

        DegreeCourse ingMecc = degreeCourseRepository.findByName(INGEGNERIA_MECCANICA);

        DegreeCourse ingMeccMag = degreeCourseRepository.findByName(INGEGNERIA_MECCANICA_MAGISTRALE);

        DegreeCourse ingEle = degreeCourseRepository.findByName(INGEGNERIA_ELETTRICA);

        List<Course> courses = new ArrayList<>();

        // create courses
        courses.add(
            new Course(
                "analisi matematica",
                CourseType.MATEMATICA,
                12,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACINTO)),
                ingGest
            )
        );

        courses.add(
            new Course(
                "analisi matematica",
                CourseType.MATEMATICA,
                12,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GENESIO)),
                ingInf
            )
        );

        courses.add(
            new Course(
                "analisi matematica",
                CourseType.MATEMATICA,
                12,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GENESIO)),
                ingMecc
            )
        );

        courses.add(
            new Course(
                "analisi matematica",
                CourseType.MATEMATICA,
                12,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GENESIO)),
                ingEle
            )
        );

        courses.add(
            new Course(
                "elementi di economia",
                CourseType.ECONOMIA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACINTO)),
                ingGest
            )
        );

        courses.add(
            new Course(
                "elementi di economia",
                CourseType.ECONOMIA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GENESIO)),
                ingInf
            )
        );

        courses.add(
            new Course(
                "elementi di economia",
                CourseType.ECONOMIA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GENESIO)),
                ingMecc
            )
        );

        courses.add(
            new Course(
                "geometria e algebra",
                CourseType.MATEMATICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACINTO)),
                ingGest
            )
        );

        courses.add(
            new Course(
                "geometria e algebra",
                CourseType.MATEMATICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACINTO)),
                ingInf
            )
        );

        courses.add(
            new Course(
                "geometria e algebra",
                CourseType.MATEMATICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACINTO)),
                ingMecc
            )
        );

        courses.add(
            new Course(
                "analisi dei sistemi dinamici",
                CourseType.MATEMATICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACINTO)),
                ingGestMag
            )
        );

        courses.add(
            new Course(
                "analisi dei sistemi dinamici",
                CourseType.MATEMATICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIOELE)),
                ingInfMag
            )
        );

        courses.add(
            new Course(
                "fisica generale",
                CourseType.FISICA,
                12,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACOMO)),
                ingGest
            )
        );

        courses.add(
            new Course(
                "fisica generale",
                CourseType.FISICA,
                12,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACOMO)),
                ingInf
            )
        );

        courses.add(
            new Course(
                "fisica generale",
                CourseType.FISICA,
                12,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACOMO)),
                ingMecc
            )
        );

        courses.add(
            new Course(
                "fisica generale",
                CourseType.FISICA,
                12,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACOMO)),
                ingEle
            )
        );

        courses.add(
            new Course(
                "fondamenti di informatica",
                CourseType.INFORMATICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACINTO)),
                ingGest
            )
        );

        courses.add(
            new Course(
                "fondamenti di informatica",
                CourseType.INFORMATICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GENESIO)),
                ingInf
            )
        );

        courses.add(
            new Course(
                "fondamenti di informatica",
                CourseType.INFORMATICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GENESIO)),
                ingMecc
            )
        );

        courses.add(
            new Course(
                "fondamenti di informatica",
                CourseType.INFORMATICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GENESIO)),
                ingEle
            )
        );

        courses.add(
            new Course(
                "chimica generale",
                CourseType.CHIMICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIOELE)),
                ingGest
            )
        );

        courses.add(
            new Course(
                "chimica generale",
                CourseType.CHIMICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIOELE)),
                ingInf
            )
        );

        courses.add(
            new Course(
                "chimica generale",
                CourseType.CHIMICA,
                9,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIOELE)),
                ingMecc
            )
        );

        courses.add(
            new Course(
                "chimica generale",
                CourseType.CHIMICA,
                9,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIOELE)),
                ingEle
            )
        );

        courses.add(
            new Course(
                "scienza delle costruzioni",
                CourseType.ING_MECCANICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACOMO)),
                ingGest
            )
        );

        courses.add(
            new Course(
                "scienza delle costruzioni",
                CourseType.ING_MECCANICA,
                12,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACOMO)),
                ingMecc
            )
        );

        courses.add(
            new Course(
                "metodi di ottimizzazione",
                CourseType.MATEMATICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACINTO)),
                ingGest
            )
        );

        courses.add(
            new Course(
                "metodi di rappresentazione tecnica",
                CourseType.DISEGNO,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACINTO)),
                ingGest
            )
        );

        courses.add(
            new Course(
                "elementi di meccanica delle macchine e progettazione meccanica",
                CourseType.ING_MECCANICA,
                12,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACOMO)),
                ingGest
            )
        );

        courses.add(
            new Course(
                "fisica tecnica e sistemi energetici",
                CourseType.FISICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACOMO)),
                ingGest
            )
        );

        courses.add(
            new Course(
                "gestione aziendale",
                CourseType.ECONOMIA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACOMO)),
                ingGest
            )
        );

        courses.add(
            new Course(
                "meccanica dei fluidi",
                CourseType.IDRAULICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIOELE)),
                ingGest
            )
        );

        courses.add(
            new Course(
                "principi di ingegneria elettrica",
                CourseType.ING_ELETTRICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACOMO)),
                ingGest
            )
        );

        courses.add(
            new Course(
                "tecnologia meccaniche e dei materiali",
                CourseType.ING_MECCANICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACOMO)),
                ingGest
            )
        );

        courses.add(
            new Course(
                "calcolo numerico",
                CourseType.MATEMATICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACINTO)),
                ingGest
            )
        );

        courses.add(
            new Course(
                "gestione dei progetti",
                CourseType.ING_GESTIONALE,
                9,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACINTO)),
                ingGest
            )
        );

        courses.add(
            new Course(
                "impianti industriali",
                CourseType.ING_MECCANICA,
                9,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACOMO)),
                ingGest
            )
        );

        courses.add(
            new Course(
                "inglese",
                CourseType.LINGUA_STRANIERA,
                3,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIOELE)),
                ingGest
            )
        );

        courses.add(
            new Course(
                "progettazione dei processi produttivi e qualità dei processi produttivi",
                CourseType.ING_GESTIONALE,
                12,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GENESIO)),
                ingGest
            )
        );

        courses.add(
            new Course(
                "sicurezza degli impianti industriali",
                CourseType.ING_GESTIONALE,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACINTO)),
                ingGest
            )
        );

        courses.add(
            new Course(
                "materiali innovativi per l'ingegneria elettrica",
                CourseType.ING_ELETTRICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACINTO)),
                ingGest
            )
        );

        courses.add(
            new Course(
                "tirocinio",
                CourseType.ING_GESTIONALE,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIOELE)),
                ingGest
            )
        );

        courses.add(
            new Course(
                "prova finale",
                CourseType.ING_GESTIONALE,
                3,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACINTO)),
                ingGest
            )
        );

        courses.add(
            new Course(
                "sistemi informativi",
                CourseType.ING_INFORMATICA,
                12,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GENESIO)),
                ingGestMag
            )
        );

        courses.add(
            new Course(
                "sistemi informativi",
                CourseType.ING_INFORMATICA,
                12,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GENESIO)),
                ingInfMag
            )
        );

        courses.add(
            new Course(
                "big data analytics",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACINTO)),
                ingGestMag
            )
        );

        courses.add(
            new Course(
                "internet of things",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIOELE)),
                ingGestMag
            )
        );

        courses.add(
            new Course(
                "internet of things",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIOELE)),
                ingInfMag
            )
        );

        courses.add(
            new Course(
                "fondamenti di cybersecurity",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIOELE)),
                ingGestMag
            )
        );

        courses.add(
            new Course(
                "basi di dati",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACINTO)),
                ingGestMag
            )
        );

        courses.add(
            new Course(
                "basi di dati",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACINTO)),
                ingInfMag
            )
        );

        courses.add(
            new Course(
                "produzione avanzata nella fabbrica digitale",
                CourseType.ING_MECCANICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GENESIO)),
                ingGestMag
            )
        );

        courses.add(
            new Course(
                "analisi dei sistemi dinamici",
                CourseType.MATEMATICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GENESIO)),
                ingMeccMag
            )
        );

        courses.add(
            new Course(
                "algoritmi e strutture dati in java",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GENESIO)),
                ingInf
            )
        );

        courses.add(
            new Course(
                "fondamenti di telecomunicazioni",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACOMO)),
                ingInf
            )
        );

        courses.add(
            new Course(
                "fondamenti di elettronica",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACOMO)),
                ingInf
            )
        );

        courses.add(
            new Course(
                "compilatori",
                CourseType.ING_INFORMATICA,
                12,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACOMO)),
                ingInfMag
            )
        );

        courses.add(
            new Course(
                "big data",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIOELE)),
                ingInfMag
            )
        );

        courses.add(
            new Course(
                "impianti meccanici",
                CourseType.ING_MECCANICA,
                3,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GENESIO)),
                ingMecc
            )
        );

        courses.add(
            new Course(
                "energetica e macchine a fluido",
                CourseType.ING_MECCANICA,
                6,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACINTO)),
                ingMecc
            )
        );

        courses.add(
            new Course(
                "misure meccaniche e termiche",
                CourseType.ING_MECCANICA,
                12,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACINTO)),
                ingMecc
            )
        );

        courses.add(
            new Course(
                "turbomacchine",
                CourseType.ING_MECCANICA,
                12,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACINTO)),
                ingMeccMag
            )
        );

        courses.add(
            new Course(
                "gasdinamica e fluidodinamica",
                CourseType.ING_MECCANICA,
                12,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACINTO)),
                ingMeccMag
            )
        );

        courses.add(
            new Course(
                "macchine elettriche",
                CourseType.ING_ELETTRICA,
                12,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACINTO)),
                ingEle
            )
        );

        courses.add(
            new Course(
                "elettrica di potenza",
                CourseType.ING_ELETTRICA,
                12,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIOELE)),
                ingEle
            )
        );

        courses.add(
            new Course(
                "impianti elettrici civili e industriali",
                CourseType.ING_MECCANICA,
                12,
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIOELE)),
                ingEle
            )
        );


         // save courses
        courses.forEach(courseRepository::saveAndFlush);

        // associate the respective courses to each degree course
        courseRepository
            .findAll()
            .stream()
            .forEach(course -> Arrays.asList(ingGest,ingGestMag, ingInf, ingInfMag, ingMecc, ingMeccMag, ingEle)
                .stream()
                .filter(degreeCourse -> degreeCourse.equals(course.getDegreeCourse()))
                .forEach(degreeCourse -> degreeCourse.addCourse(course))
            );


        // set courses in degreeCourse object
        degreeCourseRepository.saveAndFlush(ingGest);
        degreeCourseRepository.saveAndFlush(ingGestMag);
        degreeCourseRepository.saveAndFlush(ingInf);
        degreeCourseRepository.saveAndFlush(ingInfMag);
        degreeCourseRepository.saveAndFlush(ingMecc);
        degreeCourseRepository.saveAndFlush(ingMeccMag);

    }










    // initialize examinations
    private void initializeExaminations(
        StudentRepository studentRepository,
        CourseRepository courseRepository,
        DegreeCourseRepository degreeCourseRepository,
        ExaminationRepository examinationRepository
    ) {

        // Retrieve existing Student entity from the database
        Student nino = studentRepository.findByRegister(new Register("123456"));
        Student raffo = studentRepository.findByRegister(new Register("555555"));
        Student luca = studentRepository.findByRegister(new Register("123457"));


        // Retrieve existing Course entity from the database
        Course analisiMatematica = courseRepository
            .findByNameAndDegreeCourse("analisi matematica", degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE));

        Course geometria = courseRepository
            .findByNameAndDegreeCourse("geometria e algebra", degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE));

        Course scienzaCostr = courseRepository
            .findByNameAndDegreeCourse("scienza delle costruzioni", degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE));

        Course impInd = courseRepository
            .findByNameAndDegreeCourse("impianti industriali", degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE));

        Course metodiOttimizzazione = courseRepository
            .findByNameAndDegreeCourse("metodi di ottimizzazione", degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE));

        Course anSisDin = courseRepository
            .findByNameAndDegreeCourse("analisi dei sistemi dinamici", degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE_MAGISTRALE));

        Course basiDiDati = courseRepository
            .findByNameAndDegreeCourse("basi di dati", degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE_MAGISTRALE));

        List<ExaminationDto> examinations = new ArrayList<>();

        // create examinations
        examinations.add(
            new ExaminationDto(
                analisiMatematica,
                nino,
                30,
                true,
                LocalDate.of(2022, 6, 23)
            )
        );

        examinations.add(
            new ExaminationDto(
                geometria,
                nino,
                30,
                true,
                LocalDate.of(2018, 2, 18)
            )
        );

        examinations.add(
            new ExaminationDto(
                scienzaCostr,
                nino,
                18,
                false,
                LocalDate.of(2019, 2, 18)
            )
        );

        examinations.add(
            new ExaminationDto(
                impInd,
                nino,
                24,
                false,
                LocalDate.of(2020, 2, 18)
            )
        );

        examinations.add(
            new ExaminationDto(
                metodiOttimizzazione,
                nino,
                27,
                false,
                LocalDate.of(2021, 2, 18)
            )
        );

        examinations.add(
            new ExaminationDto(
                analisiMatematica,
                raffo,
                30,
                true,
                LocalDate.of(2022, 6, 23)
            )
        );

        examinations.add(
            new ExaminationDto(
                geometria,
                raffo,
                30,
                true,
                LocalDate.of(2018, 2, 18)
            )
        );

        examinations.add(
            new ExaminationDto(
                scienzaCostr,
                raffo,
                30,
                true,
                LocalDate.of(2019, 2, 18)
            )
        );

        examinations.add(
            new ExaminationDto(
                impInd,
                raffo,
                30,
                false,
                LocalDate.of(2020, 2, 18)
            )
        );

        examinations.add(
            new ExaminationDto(
                metodiOttimizzazione,
                raffo,
                21,
                false,
                LocalDate.of(2021, 2, 18)
            )
        );

        examinations.add(
            new ExaminationDto(
                anSisDin,
                luca,
                28,
                false,
                LocalDate.of(2024, 05, 18)
            )
        );

        examinations.add(
            new ExaminationDto(
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
            .map(ExaminationMapper::mapToExamination)
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

        DegreeCourse ingGest = degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE);

        DegreeCourse ingGestMag = degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE_MAGISTRALE);

        List<StudyPlanDto> studyPlans = new ArrayList<>();

        studyPlans.add(
            new StudyPlanDto(
                studentRepository.findByRegister(new Register("123456")),
                "ORD509",
                new HashSet<>(ingGest.getCourses())
            )
        );

        studyPlans.add(
            new StudyPlanDto(
                studentRepository.findByRegister(new Register("123457")),
                "ORD270",
                new HashSet<>(ingGestMag.getCourses())
            )
        );

        studyPlans.add(
            new StudyPlanDto(
                studentRepository.findByRegister(new Register("555555")),
                "ORD509",
                new HashSet<>(ingGest.getCourses())
            )
        );

        // sanity check
        if(studyPlans.isEmpty())
            throw new IllegalArgumentException("study plan list is empty");

        // save students
        studyPlans
            .stream()
            .map(StudyPlanMapper::mapToStudyPlan)
            .forEach(studyPlanRepository::saveAndFlush);

    }










    // initialize examination appeal
    private void initializeExaminationAppeals(
        ExaminationAppealRepository examinationAppealRepository,
        CourseRepository courseRepository,
        DegreeCourseRepository degreeCourseRepository,
        StudentRepository studentRepository
    ) {

        Course analisiMat = courseRepository
            .findByNameAndDegreeCourse("analisi matematica", degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE));

        Course geometria = courseRepository
            .findByNameAndDegreeCourse("geometria e algebra", degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE));

        Course fisicaGen = courseRepository
            .findByNameAndDegreeCourse("fisica generale", degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE));

        Course informatica = courseRepository
            .findByNameAndDegreeCourse("fondamenti di informatica", degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE));

        Course gp = courseRepository
            .findByNameAndDegreeCourse("gestione dei progetti", degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE));

        Course scienzaDelleCostr = courseRepository
            .findByNameAndDegreeCourse("scienza delle costruzioni", degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE));

        Course chimicaGen = courseRepository
            .findByNameAndDegreeCourse("chimica generale", degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE));

        Course calcoloNum = courseRepository
            .findByNameAndDegreeCourse("calcolo numerico", degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE));

        Course economia = courseRepository
            .findByNameAndDegreeCourse("elementi di economia", degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE));

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

        // retrieve students
        Student nino = studentRepository.findByRegister(new Register("123456"));
        Student raffo = studentRepository.findByRegister(new Register("555555"));
        ExaminationAppeal gpAppeal = new ExaminationAppeal(gp, "crm, pert", LocalDate.of(2025, 04, 30));
        gpAppeal.addStudent(nino.getRegister());
        gpAppeal.addStudent(raffo.getRegister());
        examinationAppeals.add(gpAppeal);

        ExaminationAppeal infAppeal = new ExaminationAppeal(informatica, "programmazione", LocalDate.of(2025, 10, 19));
        infAppeal.addStudent(nino.getRegister());
        infAppeal.addStudent(raffo.getRegister());
        examinationAppeals.add(infAppeal);

        if(examinationAppeals.isEmpty())
            throw new IllegalArgumentException("examination appeal list is empty");

        examinationAppeals
            .stream()
            .forEach(examinationAppealRepository::saveAndFlush);

    }

}

