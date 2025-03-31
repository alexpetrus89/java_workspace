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
public class UmsDBInit implements Serializable {

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
        org.slf4j.LoggerFactory.getLogger(UmsDBInit.class);


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
            initializeExaminationAppeals(examinationAppealRepository, courseRepository, degreeCourseRepository);

        };
    }










    // initialize user
    private void initializeUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        // create user  - 2 admin + 18 students + 9 professors
        Builder formBuilderOne = new Builder();
        formBuilderOne.withUsername("rico@gmail.com");
        formBuilderOne.withPassword("rico");
        formBuilderOne.withFullname("damiano ruggieri");
        formBuilderOne.withDob(LocalDate.of(1993, 1, 1));
        formBuilderOne.withStreet("via della nazione");
        formBuilderOne.withCity("fasano");
        formBuilderOne.withState("italia");
        formBuilderOne.withZip("72015");
        formBuilderOne.withPhone("3815674128");
        formBuilderOne.withRole(RoleType.ADMIN);

        Builder formBuilderTwo = new Builder();
        formBuilderTwo.withUsername("fido@gmail.com");
        formBuilderTwo.withPassword("fido");
        formBuilderTwo.withFullname("enrico ruggieri");
        formBuilderTwo.withDob(LocalDate.of(1993, 4, 1));
        formBuilderTwo.withStreet("via del calvario");
        formBuilderTwo.withCity("pezze di greco");
        formBuilderTwo.withState("italia");
        formBuilderTwo.withZip("72015");
        formBuilderTwo.withPhone("3815674128");
        formBuilderTwo.withRole(RoleType.ADMIN);

        // create users
        List<User> users = List.of(
            new RegistrationForm(formBuilderOne).toUser(passwordEncoder),
            new RegistrationForm(formBuilderTwo).toUser(passwordEncoder)
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
        Builder formBuilderFour = new Builder();
        formBuilderFour.withUsername("nino@gmail.com");
        formBuilderFour.withPassword("nino");
        formBuilderFour.withFullname("bob dylamie");
        formBuilderFour.withDob(LocalDate.of(1991, 4, 6));
        formBuilderFour.withStreet("via delle lamie di olimpia");
        formBuilderFour.withCity("laureto");
        formBuilderFour.withState("italia");
        formBuilderFour.withZip("72015");
        formBuilderFour.withPhone("3619647852");
        formBuilderFour.withRole(RoleType.STUDENT);

        // 2
        Builder formBuilderFive = new Builder();
        formBuilderFive.withUsername("luca@gmail.com");
        formBuilderFive.withPassword("luca");
        formBuilderFive.withFullname("pelaccio");
        formBuilderFive.withDob(LocalDate.of(1991, 11, 12));
        formBuilderFive.withStreet("via delle lamie di olimpia");
        formBuilderFive.withCity("laureto");
        formBuilderFive.withState("italia");
        formBuilderFive.withZip("72015");
        formBuilderFive.withPhone("38412369547");
        formBuilderFive.withRole(RoleType.STUDENT);

        // 3
        Builder formBuilderSix = new Builder();
        formBuilderSix.withUsername("ennio@gmail.com");
        formBuilderSix.withPassword("ennio");
        formBuilderSix.withFullname("ennio");
        formBuilderSix.withDob(LocalDate.of(1991, 11, 12));
        formBuilderSix.withStreet("via delle lamie di olimpia");
        formBuilderSix.withCity("laureto");
        formBuilderSix.withState("italia");
        formBuilderSix.withZip("72015");
        formBuilderSix.withPhone("38412369547");
        formBuilderSix.withRole(RoleType.STUDENT);

        // 4
        Builder formBuilderSeven = new Builder();
        formBuilderSeven.withUsername("gino@gmail.com");
        formBuilderSeven.withPassword("gino");
        formBuilderSeven.withFullname("gino");
        formBuilderSeven.withDob(LocalDate.of(1991, 11, 12));
        formBuilderSeven.withStreet("via delle lamie di olimpia");
        formBuilderSeven.withCity("laureto");
        formBuilderSeven.withState("italia");
        formBuilderSeven.withZip("72015");
        formBuilderSix.withPhone("38412369547");
        formBuilderSix.withRole(RoleType.STUDENT);

        // 5
        Builder formBuilderEight = new Builder();
        formBuilderEight.withUsername("pino@gmail.com");
        formBuilderEight.withPassword("pino");
        formBuilderEight.withFullname("pino");
        formBuilderEight.withDob(LocalDate.of(1991, 11, 12));
        formBuilderEight.withStreet("via delle lamie di olimpia");
        formBuilderEight.withCity("laureto");
        formBuilderEight.withState("italia");
        formBuilderEight.withZip("72015");
        formBuilderEight.withPhone("38412369547");
        formBuilderEight.withRole(RoleType.STUDENT);

        // 6
        Builder formBuilderNine = new Builder();
        formBuilderNine.withUsername("tino@gmail.com");
        formBuilderNine.withPassword("tino");
        formBuilderNine.withFullname("tino");
        formBuilderNine.withDob(LocalDate.of(1991, 11, 12));
        formBuilderNine.withStreet("via delle lamie di olimpia");
        formBuilderNine.withCity("laureto");
        formBuilderNine.withState("italia");
        formBuilderNine.withZip("72015");
        formBuilderNine.withPhone("38412369547");
        formBuilderNine.withRole(RoleType.STUDENT);

        // 7
        Builder formBuilderTen = new Builder();
        formBuilderTen.withUsername("solo@gmail.com");
        formBuilderTen.withPassword("tino");
        formBuilderTen.withFullname("tino");
        formBuilderTen.withDob(LocalDate.of(1991, 11, 12));
        formBuilderTen.withStreet("via delle lamie di olimpia");
        formBuilderTen.withCity("laureto");
        formBuilderTen.withState("italia");
        formBuilderTen.withZip("72015");
        formBuilderTen.withPhone("38412369547");
        formBuilderTen.withRole(RoleType.STUDENT);

        List<Student> students = new ArrayList<>();

        // 1
        students.add(
            new Student(
                formBuilderFour,
                passwordEncoder,
                new Register("123456"),
                degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE)
            )
        );

        // 2
        students.add(
            new Student(
                formBuilderFive,
                passwordEncoder,
                new Register("123457"),
                degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE_MAGISTRALE)
            )
        );

        // 3
        students.add(
            new Student(
                formBuilderSix,
                passwordEncoder,
                new Register("123458"),
                degreeCourseRepository.findByName(INGEGNERIA_INFORMATICA)
            )
        );

        // 4
        students.add(
            new Student(
                formBuilderSeven,
                passwordEncoder,
                new Register("123459"),
                degreeCourseRepository.findByName(INGEGNERIA_INFORMATICA_MAGISTRALE)
            )
        );

        // 5
        students.add(
            new Student(
                formBuilderEight,
                passwordEncoder,
                new Register("123460"),
                degreeCourseRepository.findByName(INGEGNERIA_MECCANICA)
            )
        );

        // 6
        students.add(
            new Student(
                formBuilderNine,
                passwordEncoder,
                new Register("123461"),
                degreeCourseRepository.findByName(INGEGNERIA_MECCANICA_MAGISTRALE)
            )
        );

        // 7
        students.add(
            new Student(
                formBuilderTen,
                passwordEncoder,
                new Register("123462"),
                degreeCourseRepository.findByName(INGEGNERIA_ELETTRICA)
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
        fbProfTwo.withFullname("gilles villeneuve");
        fbProfTwo.withDob(LocalDate.of(1993, 4, 6));
        fbProfTwo.withStreet("via di vancouver");
        fbProfTwo.withCity("vancouver");
        fbProfTwo.withState("canada");
        fbProfTwo.withZip("48759");
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
        fbProfThree.withFullname("gilles villeneuve");
        fbProfThree.withDob(LocalDate.of(1993, 4, 6));
        fbProfThree.withStreet("via di vancouver");
        fbProfThree.withCity("vancouver");
        fbProfThree.withState("canada");
        fbProfThree.withZip("48759");
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
        fbProfFour.withFullname("gilles villeneuve");
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
                professorRepository.findByUniqueCode(new UniqueCode(UC_GENESIO)),
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
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACOMO)),
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
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIOELE)),
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
                professorRepository.findByUniqueCode(new UniqueCode(UC_GIACOMO)),
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

        // retrieve student
        // Retrieve existing Student entity from the database
        Student nino = studentRepository.findByRegister(new Register("123456"));

        Student luca = studentRepository.findByRegister(new Register("123457"));


        // Retrieve existing Course entity from the database
        Course analisiMatematica = courseRepository
            .findByNameAndDegreeCourse("analisi matematica", degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE).getId());


        Course geometria = courseRepository
            .findByNameAndDegreeCourse("geometria e algebra", degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE).getId());

        Course anSisDin = courseRepository
            .findByNameAndDegreeCourse("analisi dei sistemi dinamici", degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE_MAGISTRALE).getId());


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
                anSisDin,
                luca,
                28,
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
        DegreeCourseRepository degreeCourseRepository
    ) {

        Course analisiMat = courseRepository
            .findByNameAndDegreeCourse("analisi matematica", degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE).getId());

        Course geometria = courseRepository
            .findByNameAndDegreeCourse("geometria e algebra", degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE).getId());

        Course fisicaGen = courseRepository
            .findByNameAndDegreeCourse("fisica generale", degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE).getId());

        List<ExaminationAppeal> examinationAppeals = new ArrayList<>();

        examinationAppeals.add(
            new ExaminationAppeal(
                analisiMat,
                "am mort",
                LocalDate.of(2026, 6, 23)
            )
        );

        examinationAppeals.add(
            new ExaminationAppeal(
                geometria,
                "am mort",
                LocalDate.of(2025, 6, 23)
            )
        );

        examinationAppeals.add(
            new ExaminationAppeal(
                fisicaGen,
                "am stramort",
                LocalDate.of(2025, 4, 23)
            )
        );

        if(examinationAppeals.isEmpty())
            throw new IllegalArgumentException("examination appeal list is empty");

        examinationAppeals
            .stream()
            .forEach(examinationAppealRepository::saveAndFlush);

    }

}

