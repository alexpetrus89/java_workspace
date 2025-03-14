package com.alex.universitymanagementsystem.config;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.User;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.dto.ExaminationDto;
import com.alex.universitymanagementsystem.dto.StudyPlanDto;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.ExaminationMapper;
import com.alex.universitymanagementsystem.mapper.StudyPlanMapper;
import com.alex.universitymanagementsystem.repository.CourseRepository;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;
import com.alex.universitymanagementsystem.repository.ExaminationRepository;
import com.alex.universitymanagementsystem.repository.ProfessorRepository;
import com.alex.universitymanagementsystem.repository.StudentRepository;
import com.alex.universitymanagementsystem.repository.StudyPlanRepository;
import com.alex.universitymanagementsystem.repository.UserRepository;
import com.alex.universitymanagementsystem.utils.Builder;
import com.alex.universitymanagementsystem.utils.CourseType;
import com.alex.universitymanagementsystem.utils.DegreeType;
import com.alex.universitymanagementsystem.utils.RegistrationForm;
import com.alex.universitymanagementsystem.utils.Role;

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

    private static final String STUDENT_NOT_FOUND = "Student not found";
    private static final String COURSE_NOT_FOUND = "Course not found";
    private static final String DEGREE_COURSE_NOT_FOUND = "Degree course not found";
    private static final String NO_STUDENTS_FOR_THIS_DEGREE = "No students found for this degree course";

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
        ExaminationRepository examinationRepository
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
        formBuilderOne.withRole(Role.ADMIN);

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
        formBuilderTwo.withRole(Role.ADMIN);

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
        formBuilderFour.withRole(Role.STUDENT);

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
        formBuilderFive.withRole(Role.STUDENT);

        List<Student> students = new ArrayList<>();

        // 1
        students.add(
            new Student(
                formBuilderFour,
                passwordEncoder,
                new Register("123456"),
                degreeCourseRepository
                    .findByName(INGEGNERIA_GESTIONALE)
                    .orElseThrow(() -> new ObjectNotFoundException(DEGREE_COURSE_NOT_FOUND, INGEGNERIA_GESTIONALE))
            )
        );

        // 2
        students.add(
            new Student(
                formBuilderFive,
                passwordEncoder,
                new Register("123457"),
                degreeCourseRepository
                    .findByName(INGEGNERIA_GESTIONALE_MAGISTRALE)
                    .orElseThrow(() -> new ObjectNotFoundException(DEGREE_COURSE_NOT_FOUND, INGEGNERIA_GESTIONALE))
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
            .orElseThrow(() -> new RuntimeException(NO_STUDENTS_FOR_THIS_DEGREE));

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
            .orElseThrow(() -> new RuntimeException(NO_STUDENTS_FOR_THIS_DEGREE));

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

    }










    // INITIALIZE PROFESSORS
    void initializeProfessors(ProfessorRepository professorRepository, PasswordEncoder passwordEncoder) {

        Builder formBuilderThree = new Builder();
        formBuilderThree.withUsername("professore.giacinto@dominio.it");
        formBuilderThree.withPassword("dino");
        formBuilderThree.withFullname("gilles villeneuve");
        formBuilderThree.withDob(LocalDate.of(1993, 4, 6));
        formBuilderThree.withStreet("via di vancouver");
        formBuilderThree.withCity("vancouver");
        formBuilderThree.withState("canada");
        formBuilderThree.withZip("48759");
        formBuilderThree.withPhone("8749652314");
        formBuilderThree.withRole(Role.PROFESSOR);
        List<Professor> professors = new ArrayList<>();

        // 1
        professors.add(
            new Professor(
                formBuilderThree,
                passwordEncoder,
                new UniqueCode(UC_GIACINTO),
                "abc678rde217we56"
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
        DegreeCourse ingGest = degreeCourseRepository
            .findByName(INGEGNERIA_GESTIONALE)
            .orElseThrow();

        DegreeCourse ingGestMag = degreeCourseRepository
            .findByName(INGEGNERIA_GESTIONALE_MAGISTRALE)
            .orElseThrow();

        List<Course> courses = new ArrayList<>();

        // create courses
        courses.add(
            new Course(
                "analisi matematica",
                CourseType.MATEMATICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GIACINTO))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "elementi di economia",
                CourseType.ECONOMIA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GIACINTO))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "geometria e algebra",
                CourseType.MATEMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GIACINTO))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "analisi dei sistemi dinamici",
                CourseType.MATEMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GIACINTO))),
                ingGestMag
            )
        );


         // save courses
        courses.forEach(courseRepository::saveAndFlush);

        // associate the respective courses to each degree course
        courseRepository
            .findAll()
            .stream()
            .forEach(course -> Arrays.asList(ingGest,ingGestMag)
                .stream()
                .filter(degreeCourse -> degreeCourse.equals(course.getDegreeCourse()))
                .forEach(degreeCourse -> degreeCourse.addCourse(course))
            );


        // set courses in degreeCourse object
        degreeCourseRepository.save(ingGest);
        degreeCourseRepository.save(ingGestMag);

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
        Student nino = studentRepository
            .findByRegister(new Register("123456"))
            .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND)
        );

        Student luca = studentRepository
            .findByRegister(new Register("123457"))
            .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND)
        );


        // Retrieve existing Course entity from the database
        Course analisiMatematica = courseRepository
            .findByNameAndDegreeCourse(
                "analisi matematica",
                degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND)).getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));


        Course geometria = courseRepository
            .findByNameAndDegreeCourse(
                "geometria e algebra",
                degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND)).getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course anSisDin = courseRepository
            .findByNameAndDegreeCourse(
                "analisi dei sistemi dinamici",
                degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE_MAGISTRALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));


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
    public void initializeStudyPlan(
        StudyPlanRepository studyPlanRepository,
        DegreeCourseRepository degreeCourseRepository,
        StudentRepository studentRepository
    ) {

        DegreeCourse ingGest = degreeCourseRepository
            .findByName(INGEGNERIA_GESTIONALE)
            .orElseThrow(() -> new ObjectNotFoundException(DEGREE_COURSE_NOT_FOUND, INGEGNERIA_GESTIONALE));

        DegreeCourse ingGestMag = degreeCourseRepository
            .findByName(INGEGNERIA_GESTIONALE_MAGISTRALE)
            .orElseThrow(() -> new ObjectNotFoundException(DEGREE_COURSE_NOT_FOUND, INGEGNERIA_GESTIONALE_MAGISTRALE));

        List<StudyPlanDto> studyPlans = new ArrayList<>();

        studyPlans.add(
            new StudyPlanDto(
                studentRepository
                    .findByRegister(new Register("123456"))
                    .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND)),
                "ORD509",
                new HashSet<>(ingGest.getCourses())
            )
        );

        studyPlans.add(
            new StudyPlanDto(
                studentRepository
                    .findByRegister(new Register("123457"))
                    .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND)),
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


}
