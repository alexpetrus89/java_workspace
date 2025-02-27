package com.alex.studentmanagementsystem.config;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.alex.studentmanagementsystem.domain.Course;
import com.alex.studentmanagementsystem.domain.DegreeCourse;
import com.alex.studentmanagementsystem.domain.Student;
import com.alex.studentmanagementsystem.domain.immutable.Register;
import com.alex.studentmanagementsystem.domain.immutable.UniqueCode;
import com.alex.studentmanagementsystem.dto.ExaminationDto;
import com.alex.studentmanagementsystem.dto.ProfessorDto;
import com.alex.studentmanagementsystem.dto.StudentDto;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;
import com.alex.studentmanagementsystem.mapper.ExaminationMapper;
import com.alex.studentmanagementsystem.mapper.ProfessorMapper;
import com.alex.studentmanagementsystem.mapper.StudentMapper;
import com.alex.studentmanagementsystem.repository.CourseRepository;
import com.alex.studentmanagementsystem.repository.DegreeCourseRepository;
import com.alex.studentmanagementsystem.repository.ExaminationRepository;
import com.alex.studentmanagementsystem.repository.ProfessorRepository;
import com.alex.studentmanagementsystem.repository.StudentRepository;
import com.alex.studentmanagementsystem.repository.UserRepository;
import com.alex.studentmanagementsystem.utils.Builder;
import com.alex.studentmanagementsystem.utils.CourseType;
import com.alex.studentmanagementsystem.utils.DegreeType;
import com.alex.studentmanagementsystem.utils.RegistrationForm;
import com.alex.studentmanagementsystem.utils.Role;


@Configuration
public class StudentDatabaseInit implements Serializable {

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
    private static final String UC_GENESIO = "ert456er";
    private static final String UC_FABIO = "bbb456er";
    private static final String UC_GIACOMO = "frt456er";
    private static final String UC_FELICE = "uba789lo";
    private static final String UC_VLADIMIRO = "rot111ad";
    private static final String UC_ORAZIO = "ret987ws";
    private static final String UC_GRIGNANI = "ner111tt";
    private static final String UC_SILVESTRI = "tyi389ua";

    private static final String STUDENT_NOT_FOUND = "Student not found";
    private static final String COURSE_NOT_FOUND = "Course not found";
    private static final String DEGREE_COURSE_NOT_FOUND = "Degree course not found";
    private static final String NO_STUDENTS_FOR_THIS_DEGREE = "No students found for this degree course";


    private final transient Logger logger =
        org.slf4j.LoggerFactory.getLogger(StudentDatabaseInit.class);

    /**
     * This method is called at the application startup and it populates the database
     * with the initial values.
     *
     * @param degreeCourseRepository the repository for the degree courses
     * @param studentRepository the repository for the students
     * @param courseRepository the repository for the courses
     * @param professorRepository the repository for the professors
     * @param examinationRepository the repository for the examinations
     * @param userRepository the repository for the users
     * @param passwordEncoder the password encoder
     * @return a CommandLineRunner
     */
    @Bean
    @SuppressWarnings("unused")
    CommandLineRunner commandLineRunner(
        @Autowired
        DegreeCourseRepository degreeCourseRepository,
        @Autowired
        StudentRepository studentRepository,
        @Autowired
        CourseRepository courseRepository,
        @Autowired
        ProfessorRepository professorRepository,
        @Autowired
        ExaminationRepository examinationRepository,
        @Autowired
        UserRepository userRepository,
        @Autowired
        PasswordEncoder passwordEncoder
    ) {
        return args -> {

            // degreeCourse initializer
            initializeDegreeCourse(degreeCourseRepository);

            // student initializer
            initializeStudents(studentRepository, degreeCourseRepository);

            // professor initializer
            initializeProfessors(professorRepository);

            // course initializer
            initializeCourses(courseRepository, professorRepository, degreeCourseRepository);

            // examination initializer
            initializeExaminations(studentRepository, courseRepository, degreeCourseRepository, examinationRepository);

            // user initializer
            initializeUsers(userRepository, passwordEncoder);

        };
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
        degreeCourses.forEach(degreeCourseRepository::save);

    }



    // INITIALIZE STUDENTS
    private void initializeStudents(
        StudentRepository studentRepository,
        DegreeCourseRepository degreeCourseRepository
    ) {

        List<StudentDto> students = new ArrayList<>();

        students.add(
            // create students
            new StudentDto(
                // assuming Register has a constructor that takes a String
                new Register("123456"),
                "anacleto",
                "anacleto@dominio.it",
                LocalDate.of(2000, 6, 23),
                degreeCourseRepository
                    .findByName(INGEGNERIA_GESTIONALE)
                    .orElseThrow(() -> new ObjectNotFoundException(DEGREE_COURSE_NOT_FOUND, INGEGNERIA_GESTIONALE))
            )
        );

        students.add(
            new StudentDto(
                new Register("123457"),
                "filippo",
                "elpueblounido@dominio.com",
                LocalDate.of(2001, 8, 23),
                degreeCourseRepository
                    .findByName(INGEGNERIA_GESTIONALE)
                    .orElseThrow(() -> new ObjectNotFoundException(DEGREE_COURSE_NOT_FOUND, INGEGNERIA_GESTIONALE))
            )
        );

        students.add(
            new StudentDto(
                new Register("123458"),
                "angelo",
                "ilmiodominio@dominio.com",
                LocalDate.of(2000, 2, 14),
                degreeCourseRepository
                    .findByName(INGEGNERIA_GESTIONALE)
                    .orElseThrow(() -> new ObjectNotFoundException(DEGREE_COURSE_NOT_FOUND, INGEGNERIA_GESTIONALE))
            )
        );

        students.add(
            new StudentDto(
                new Register("123741"),
                "graziano",
                "cofano.graziano@gmail.com",
                LocalDate.of(1990, 7, 15),
                degreeCourseRepository
                    .findByName(INGEGNERIA_GESTIONALE)
                    .orElseThrow(() -> new ObjectNotFoundException(DEGREE_COURSE_NOT_FOUND, INGEGNERIA_GESTIONALE))
            )
        );

        students.add(
            new StudentDto(
                new Register("123459"),
                "edmondo",
                "edmondo.albanas@gmail.com",
                LocalDate.of(1993, 8, 15),
                degreeCourseRepository
                    .findByName(INGEGNERIA_GESTIONALE)
                    .orElseThrow(() -> new ObjectNotFoundException(DEGREE_COURSE_NOT_FOUND, INGEGNERIA_GESTIONALE))
            )
        );

        students.add(
            new StudentDto(
                new Register("123460"),
                "ernesto",
                "ernesto.blood@gmail.com",
                LocalDate.of(1991, 12, 4),
                degreeCourseRepository
                    .findByName(INGEGNERIA_GESTIONALE)
                    .orElseThrow(() -> new ObjectNotFoundException(DEGREE_COURSE_NOT_FOUND, INGEGNERIA_GESTIONALE))
            )
        );

        students.add(
            new StudentDto(
                new Register("123461"),
                "alfredo",
                "alfredo.blood@gmail.com",
                LocalDate.of(1993, 11, 8),
                degreeCourseRepository
                    .findByName(INGEGNERIA_GESTIONALE)
                    .orElseThrow(() -> new ObjectNotFoundException(DEGREE_COURSE_NOT_FOUND, INGEGNERIA_GESTIONALE))
            )
        );

        students.add(
            new StudentDto(
                new Register("123462"),
                "jonspencer",
                "jonspencer.blues@gmail.com",
                LocalDate.of(1991, 02, 8),
                degreeCourseRepository
                    .findByName(INGEGNERIA_GESTIONALE_MAGISTRALE)
                    .orElseThrow(() -> new ObjectNotFoundException(DEGREE_COURSE_NOT_FOUND, INGEGNERIA_GESTIONALE_MAGISTRALE))
            )
        );

        students.add(
            new StudentDto(
                new Register("123463"),
                "feluccio",
                "feluccio.dart@gmail.com",
                LocalDate.of(1990, 11, 8),
                degreeCourseRepository
                    .findByName(INGEGNERIA_GESTIONALE_MAGISTRALE)
                    .orElseThrow(() -> new ObjectNotFoundException(DEGREE_COURSE_NOT_FOUND, INGEGNERIA_GESTIONALE_MAGISTRALE))
            )
        );

        students.add(
            new StudentDto(
                new Register("123470"),
                "marklanegan",
                "screeming.trees@gmail.com",
                LocalDate.of(1989, 12, 21),
                degreeCourseRepository
                    .findByName(INGEGNERIA_INFORMATICA)
                    .orElseThrow(() -> new ObjectNotFoundException(DEGREE_COURSE_NOT_FOUND, INGEGNERIA_INFORMATICA))
            )
        );

        students.add(
            new StudentDto(
                new Register("123464"),
                "uccio",
                "uccio.fasanboy@gmail.com",
                LocalDate.of(1990, 3, 29),
                degreeCourseRepository
                    .findByName(INGEGNERIA_INFORMATICA_MAGISTRALE)
                    .orElseThrow(() -> new ObjectNotFoundException(DEGREE_COURSE_NOT_FOUND, INGEGNERIA_INFORMATICA_MAGISTRALE))
            )
        );

        students.add(
            new StudentDto(
                new Register("123469"),
                "iggypop",
                "iggyandthe.stooges@gmail.com",
                LocalDate.of(1989, 12, 21),
                degreeCourseRepository
                    .findByName(INGEGNERIA_INFORMATICA_MAGISTRALE)
                    .orElseThrow(() -> new ObjectNotFoundException(DEGREE_COURSE_NOT_FOUND, INGEGNERIA_INFORMATICA_MAGISTRALE))
            )
        );

        students.add(
            new StudentDto(
                new Register("123465"),
                "renzi",
                "renzi.blood@gmail.com",
                LocalDate.of(1990, 11, 1),
                degreeCourseRepository
                    .findByName(INGEGNERIA_ELETTRICA)
                    .orElseThrow(() -> new ObjectNotFoundException(DEGREE_COURSE_NOT_FOUND, INGEGNERIA_ELETTRICA))
            )
        );

        students.add(
            new StudentDto(
                new Register("123466"),
                "santana",
                "santana.blood@gmail.com",
                LocalDate.of(1990, 3, 2),
                degreeCourseRepository
                    .findByName(INGEGNERIA_ELETTRICA_MAGISTRALE)
                    .orElseThrow(() -> new ObjectNotFoundException(DEGREE_COURSE_NOT_FOUND, INGEGNERIA_INFORMATICA_MAGISTRALE))
            )
        );

        students.add(
            new StudentDto(
                new Register("123528"),
                "anacleto",
                "ananana.tuo@gmail.com",
                LocalDate.of(1988, 4, 7),
                degreeCourseRepository
                    .findByName(INGEGNERIA_ELETTRICA_MAGISTRALE)
                    .orElseThrow(() -> new ObjectNotFoundException(DEGREE_COURSE_NOT_FOUND, INGEGNERIA_INFORMATICA_MAGISTRALE))
            )
        );


        students.add(
            new StudentDto(
                new Register("123468"),
                "iginio",
                "iginio.blood@gmail.com",
                LocalDate.of(1990, 6, 3),
                degreeCourseRepository
                    .findByName(INGEGNERIA_MECCANICA)
                    .orElseThrow(() -> new ObjectNotFoundException(DEGREE_COURSE_NOT_FOUND, INGEGNERIA_GESTIONALE))
            )
        );

        students.add(
            new StudentDto(
                new Register("123467"),
                "benny",
                "segafredo.caffe@gmail.com",
                LocalDate.of(1996, 3, 8),
                degreeCourseRepository
                    .findByName(INGEGNERIA_MECCANICA_MAGISTRALE)
                    .orElseThrow(() -> new ObjectNotFoundException(DEGREE_COURSE_NOT_FOUND, INGEGNERIA_INFORMATICA_MAGISTRALE))
            )
        );

        students.add(
            new StudentDto(
                new Register("120689"),
                "anacleto",
                "willy.wonka@gmail.com",
                LocalDate.of(1992, 8, 4),
                degreeCourseRepository
                    .findByName(INGEGNERIA_INFORMATICA_MAGISTRALE)
                    .orElseThrow(() -> new ObjectNotFoundException(DEGREE_COURSE_NOT_FOUND, INGEGNERIA_INFORMATICA_MAGISTRALE))
            )
        );


        // sanity check
        if(students.isEmpty())
            throw new IllegalArgumentException("students list is empty");

        // save students
        students
            .stream()
            .map(StudentMapper::mapToStudent)
            .forEach(studentRepository::saveAndFlush);


        // save students for GESTIONALE degree course
        DegreeCourse ingGest = degreeCourseRepository
            .findByName(INGEGNERIA_GESTIONALE)
            .orElseThrow(null);

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


        // save students for ING. INFORMATICA degree course
        DegreeCourse ingInf = degreeCourseRepository
            .findByName(INGEGNERIA_INFORMATICA)
            .orElseThrow(() -> new RuntimeException(NO_STUDENTS_FOR_THIS_DEGREE));

        // set students of ING. INFORMATICA in degreeCourse object
        ingInf.setStudents(
            studentRepository
                .findAll()
                .stream()
                .filter(student -> student.getDegreeCourse().getName().equals(INGEGNERIA_INFORMATICA))
                .toList()
        );

        // save degree course ING. INFORMATICA MAGISTRALE
        degreeCourseRepository.saveAndFlush(ingInf);


        // save students for ING. INFORMATICA MAGISTRALE degree course
        DegreeCourse ingInfMag = degreeCourseRepository
            .findByName(INGEGNERIA_INFORMATICA_MAGISTRALE)
            .orElseThrow(() -> new RuntimeException(NO_STUDENTS_FOR_THIS_DEGREE));

        // set students of ING. INFORMATICA MAGISTRALE in degreeCourse object
        ingInfMag.setStudents(
            studentRepository
                .findAll()
                .stream()
                .filter(student -> student.getDegreeCourse().getName().equals(INGEGNERIA_INFORMATICA_MAGISTRALE))
                .toList()
        );

        // save degree course ING. INFORMATICA MAGISTRALE
        degreeCourseRepository.saveAndFlush(ingInfMag);

    }






    // INITIALIZE PROFESSORS
    void initializeProfessors(ProfessorRepository professorRepository) {

        List<ProfessorDto> professors = new ArrayList<>();

        professors.add(
            new ProfessorDto(
                new UniqueCode(UC_GIACINTO),
                "abc678rde217we56",
                "giacinto",
                "professore.giacinto@dominio.it"
            )
        );

        professors.add(
            new ProfessorDto(
                new UniqueCode(UC_GENESIO),
                "duf677rde22werf3",
                "genesio",
                "professore.genesio@dominio.it"
            )
        );

        professors.add(
            new ProfessorDto(
                new UniqueCode(UC_FABIO),
                "ert678rde23123tr",
                "fabio",
                "professore.fabio@dominio.it"
            )
        );

        professors.add(
            new ProfessorDto(
                new UniqueCode(UC_GIACOMO),
                "but678rde237e34v",
                "giacomo",
                "professore.giacomo@dominio.it"
            )
        );

        professors.add(
            new ProfessorDto(
                new UniqueCode(UC_FELICE),
                "flt985aba741o34v",
                "felice",
                "professore.felice@dominio.it"
            )
        );

        professors.add(
            new ProfessorDto(
                new UniqueCode(UC_VLADIMIRO),
                "pol236uii632u15f",
                "vladimiro",
                "professore.vladimiro@dominio.it"
            )
        );

        professors.add(
            new ProfessorDto(
                new UniqueCode(UC_ORAZIO),
                "acd784tre415y48r",
                "orazio",
                "professore.orazio@dominio.it"
            )
        );

        professors.add(
            new ProfessorDto(
                new UniqueCode(UC_GRIGNANI),
                "tfr127qua639t11a",
                "grignani",
                "professore.grignani@dominio.it"
            )
        );

        professors.add(
            new ProfessorDto(
                new UniqueCode(UC_SILVESTRI),
                "rua712sas597u55r",
                "silvestri",
                "professore.silvestri@dominio.it"
            )
        );

        // sanity check
        if(professors.isEmpty())
            throw new IllegalArgumentException("professors list is empty");

        // save professors
        professors
            .stream()
            .map(ProfessorMapper::mapToProfessor)
            .forEach(professorRepository::saveAndFlush);

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

        DegreeCourse ingMecc = degreeCourseRepository
            .findByName(INGEGNERIA_MECCANICA)
            .orElseThrow();

        DegreeCourse ingInf = degreeCourseRepository
            .findByName(INGEGNERIA_INFORMATICA)
            .orElseThrow();

        DegreeCourse ingEle = degreeCourseRepository
            .findByName(INGEGNERIA_ELETTRICA)
            .orElseThrow();

        DegreeCourse ingGestMag = degreeCourseRepository
            .findByName(INGEGNERIA_GESTIONALE_MAGISTRALE)
            .orElseThrow();

        DegreeCourse ingInfMag = degreeCourseRepository
            .findByName(INGEGNERIA_INFORMATICA_MAGISTRALE)
            .orElseThrow();

        DegreeCourse ingMeccMag = degreeCourseRepository
            .findByName(INGEGNERIA_MECCANICA_MAGISTRALE)
            .orElseThrow();

        DegreeCourse ingEleMag = degreeCourseRepository
            .findByName(INGEGNERIA_ELETTRICA_MAGISTRALE)
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
                    .findByUniqueCode(new UniqueCode(UC_SILVESTRI))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_SILVESTRI))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "geometria e algebra",
                CourseType.MATEMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACOMO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GIACOMO))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "fisica generale",
                CourseType.FISICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GIACINTO))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "fondamenti di informatica",
                CourseType.INFORMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GENESIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GENESIO))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "chimica generale",
                CourseType.CHIMICA,
                9,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_FABIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "scienza delle costruzioni",
                CourseType.ING_MECCANICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_FABIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "metodi di ottimizzazione",
                CourseType.MATEMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_FABIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "metodi di rappresentazione tecnica",
                CourseType.DISEGNO,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_FABIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "elementi di meccanica delle macchine e progettazione meccanica",
                CourseType.ING_MECCANICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACOMO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GIACOMO))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "fisica tecnica e sistemi energetici",
                CourseType.FISICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACOMO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GIACOMO))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "gestione aziendale",
                CourseType.ECONOMIA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACOMO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GIACOMO))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "meccanica dei fluidi",
                CourseType.IDRAULICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_VLADIMIRO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_VLADIMIRO))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "principi di ingegneria elettrica",
                CourseType.ING_ELETTRICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_VLADIMIRO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_VLADIMIRO))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "tecnologia meccaniche e dei materiali",
                CourseType.ING_MECCANICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACOMO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GIACOMO))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "calcolo numerico",
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
                "gestione dei progetti",
                CourseType.ING_GESTIONALE,
                9,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_FABIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "impianti industriali",
                CourseType.ING_MECCANICA,
                9,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_FELICE))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FELICE))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "inglese",
                CourseType.LINGUA_STRANIERA,
                3,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_FELICE))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FELICE))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "progettazione dei processi produttivi e qualità dei processi produttivi",
                CourseType.ING_GESTIONALE,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_FELICE))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FELICE))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "sicurezza degli impianti industriali",
                CourseType.ING_GESTIONALE,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_FELICE))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FELICE))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "materiali innovativi per l'ingegneria elettrica",
                CourseType.ING_ELETTRICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GIACINTO))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "tirocinio",
                CourseType.ING_GESTIONALE,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_FABIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "prova finale",
                CourseType.ING_GESTIONALE,
                3,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_FABIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "sistemi informativi",
                CourseType.ING_INFORMATICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_FABIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
                ingGestMag
            )
        );

        courses.add(
            new Course(
                "big data analytics",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_VLADIMIRO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_VLADIMIRO))),
                ingGestMag
            )
        );

        courses.add(
            new Course(
                "internet of things",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_ORAZIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_ORAZIO))),
                ingGestMag
            )
        );

        courses.add(
            new Course(
                "fondamenti di cybersecurity",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_ORAZIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_ORAZIO))),
                ingGestMag
            )
        );

        courses.add(
            new Course(
                "basi di dati",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_VLADIMIRO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_VLADIMIRO))),
                ingGestMag
            )
        );

        courses.add(
            new Course(
                "produzione avanzata nella fabbrica digitale",
                CourseType.ING_MECCANICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GENESIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GENESIO))),
                ingGestMag
            )
        );

        courses.add(
            new Course(
                "analisi dei sistemi dinamici",
                CourseType.MATEMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_ORAZIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_ORAZIO))),
                ingGestMag
            )
        );

        courses.add(
            new Course(
                "algoritmi e strutture dati in java",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_ORAZIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_ORAZIO))),
                ingInf
            )
        );

        courses.add(
            new Course(
                "fondamenti di telecomunicazioni",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_ORAZIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_ORAZIO))),
                ingInf
            )
        );

        courses.add(
            new Course(
                "fondamenti di elettronica",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_ORAZIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_ORAZIO))),
                ingInf
            )
        );

        courses.add(
            new Course(
                "compilatori",
                CourseType.ING_INFORMATICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_ORAZIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_ORAZIO))),
                ingInfMag
            )
        );

        courses.add(
            new Course(
                "big data",
                CourseType.ING_INFORMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_ORAZIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_ORAZIO))),
                ingInfMag
            )
        );

        courses.add(
            new Course(
                "impianti meccanici",
                CourseType.ING_MECCANICA,
                3,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_FABIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
                ingMecc
            )
        );

        courses.add(
            new Course(
                "energetica e macchine a fluido",
                CourseType.ING_MECCANICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_FABIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
                ingMecc
            )
        );

        courses.add(
            new Course(
                "misure meccaniche e termiche",
                CourseType.ING_MECCANICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_FABIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
                ingMecc
            )
        );

        courses.add(
            new Course(
                "turbomacchine",
                CourseType.ING_MECCANICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_FABIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
                ingMeccMag
            )
        );

        courses.add(
            new Course(
                "gasdinamica e fluidodinamica",
                CourseType.ING_MECCANICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_FABIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
                ingMeccMag
            )
        );

        courses.add(
            new Course(
                "macchine elettriche",
                CourseType.ING_ELETTRICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_FABIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
                ingEle
            )
        );

        courses.add(
            new Course(
                "elettrica di potenza",
                CourseType.ING_ELETTRICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_FABIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
                ingEleMag
            )
        );

        courses.add(
            new Course(
                "impianti elettrici civili e industriali",
                CourseType.ING_MECCANICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_FABIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
                ingEleMag
            )
        );

        courses.add(
            new Course(
                "analisi matematica",
                CourseType.MATEMATICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GRIGNANI))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GIACINTO))),
                ingInf
            )
        );

        courses.add(
            new Course(
                "elementi di economia",
                CourseType.ECONOMIA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_SILVESTRI))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GIACINTO))),
                ingInf
            )
        );

        courses.add(
            new Course(
                "geometria e algebra",
                CourseType.MATEMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GRIGNANI))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GIACINTO))),
                ingInf
            )
        );

        courses.add(
            new Course(
                "fondamenti di informatica",
                CourseType.INFORMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_ORAZIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_ORAZIO))),
                ingInf
            )
        );

        // save courses
        courses.forEach(courseRepository::saveAndFlush);

        // associate the respective courses to each degree course
        courseRepository
            .findAll()
            .stream()
            .forEach(course ->
                Arrays.asList(ingGest, ingInf, ingMecc, ingEle, ingInfMag, ingGestMag, ingEleMag, ingMeccMag)
                .stream()
                .filter(degreeCourse -> degreeCourse.equals(course.getDegreeCourse()))
                .forEach(degreeCourse -> degreeCourse.addCourse(course)));


        // set courses in degreeCourse object
        degreeCourseRepository.save(ingGest);
        degreeCourseRepository.save(ingInf);
        degreeCourseRepository.save(ingEle);
        degreeCourseRepository.save(ingMecc);
        degreeCourseRepository.save(ingGestMag);
        degreeCourseRepository.save(ingInfMag);
        degreeCourseRepository.save(ingEleMag);
        degreeCourseRepository.save(ingMeccMag);

    }







    // initialize examinations
    private void initializeExaminations(
        StudentRepository studentRepository,
        CourseRepository courseRepository,
        DegreeCourseRepository degreeCourseRepository,
        ExaminationRepository examinationRepository
    ) {

        // Retrieve existing Student entity from the database
        Student anacleto = studentRepository
            .findByRegister(new Register("123456"))
            .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND)
        );

        Student feluccio = studentRepository
            .findByRegister(new Register("123463"))
            .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND)
        );

        Student filippo = studentRepository
            .findByRegister(new Register("123457"))
            .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND)
        );

        Student angelo = studentRepository
            .findByRegister(new Register("123458"))
            .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND)
        );

        Student jonspencer = studentRepository
            .findByRegister(new Register("123462"))
            .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND)
        );

        Student mark = studentRepository
            .findByRegister(new Register("123470"))
            .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND)
        );

        Student iggypop = studentRepository
            .findByRegister(new Register("123469"))
            .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND)
        );

        Student uccio = studentRepository
            .findByRegister(new Register("123464"))
            .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND)
        );

        Student iginio = studentRepository
            .findByRegister(new Register("123468"))
            .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND)
        );

        Student renzi = studentRepository
            .findByRegister(new Register("123465"))
            .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND)
        );

        Student santana = studentRepository
            .findByRegister(new Register("123466"))
            .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND)
        );

        Student benny = studentRepository
            .findByRegister(new Register("123467"))
            .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND)
        );

        Student anacletoTwo = studentRepository
            .findByRegister(new Register("120689"))
            .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND)
        );


        // Retrieve existing Course entity from the database
        Course analisiMatematica = courseRepository
            .findByNameAndDegreeCourse(
                "analisi matematica",
                degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND)).getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course analisiMatematicaInf = courseRepository
            .findByNameAndDegreeCourse(
                "analisi matematica",
                degreeCourseRepository.findByName(INGEGNERIA_INFORMATICA)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND)).getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course geometriaInf = courseRepository
            .findByNameAndDegreeCourse(
                "geometria e algebra",
                degreeCourseRepository.findByName(INGEGNERIA_INFORMATICA)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND)).getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course economiaInf = courseRepository
            .findByNameAndDegreeCourse(
                "elementi di economia",
                degreeCourseRepository.findByName(INGEGNERIA_INFORMATICA)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND)).getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course fondInfInf = courseRepository
            .findByNameAndDegreeCourse(
                "fondamenti di informatica",
                degreeCourseRepository.findByName(INGEGNERIA_INFORMATICA)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND)).getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course gp = courseRepository
            .findByNameAndDegreeCourse(
                "gestione dei progetti",
                degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course scienzaDelleCostruzioni = courseRepository
            .findByNameAndDegreeCourse(
                "scienza delle costruzioni",
                degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course chimica = courseRepository
            .findByNameAndDegreeCourse(
                "chimica generale",
                degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course eMMePM = courseRepository
            .findByNameAndDegreeCourse(
                "elementi di meccanica delle macchine e progettazione meccanica",
                degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course fisicaGenerale = courseRepository
            .findByNameAndDegreeCourse(
                "fisica generale",
                degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course calcoloNumerico = courseRepository
            .findByNameAndDegreeCourse(
                "calcolo numerico",
                degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course matInnovativiIngElettrica = courseRepository
            .findByNameAndDegreeCourse(
                "materiali innovativi per l'ingegneria elettrica",
                degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course sistemiInformativi = courseRepository
            .findByNameAndDegreeCourse(
                "sistemi informativi",
                degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE_MAGISTRALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course bigData = courseRepository
            .findByNameAndDegreeCourse(
                "big data",
                degreeCourseRepository
                    .findByName(INGEGNERIA_INFORMATICA_MAGISTRALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course bigDataAna = courseRepository
            .findByNameAndDegreeCourse(
                "big data analytics",
                degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE_MAGISTRALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course anSisDin = courseRepository
            .findByNameAndDegreeCourse(
                "analisi dei sistemi dinamici",
                degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE_MAGISTRALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course pAFD = courseRepository
            .findByNameAndDegreeCourse(
                "produzione avanzata nella fabbrica digitale",
                degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE_MAGISTRALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course iOT = courseRepository
            .findByNameAndDegreeCourse(
                "internet of things",
                degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE_MAGISTRALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course cyberSecurity = courseRepository
            .findByNameAndDegreeCourse(
                "fondamenti di cybersecurity",
                degreeCourseRepository
                    .findByName(INGEGNERIA_GESTIONALE_MAGISTRALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course telecomunicazioni = courseRepository
            .findByNameAndDegreeCourse(
                "fondamenti di telecomunicazioni",
                degreeCourseRepository.findByName(INGEGNERIA_INFORMATICA)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course basiDiDati = courseRepository
            .findByNameAndDegreeCourse(
                "basi di dati",
                degreeCourseRepository.findByName(INGEGNERIA_GESTIONALE_MAGISTRALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course compilatori = courseRepository
            .findByNameAndDegreeCourse(
                "compilatori",
                degreeCourseRepository.findByName(INGEGNERIA_INFORMATICA_MAGISTRALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course alInJava = courseRepository
            .findByNameAndDegreeCourse(
                "algoritmi e strutture dati in java",
                degreeCourseRepository.findByName(INGEGNERIA_INFORMATICA)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course fondElet = courseRepository
            .findByNameAndDegreeCourse(
                "fondamenti di elettronica",
                degreeCourseRepository.findByName(INGEGNERIA_INFORMATICA)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course sisEneMaFlu = courseRepository
            .findByNameAndDegreeCourse(
                "energetica e macchine a fluido",
                degreeCourseRepository.findByName(INGEGNERIA_MECCANICA)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course impiantiMecc = courseRepository
            .findByNameAndDegreeCourse(
                "impianti meccanici",
                degreeCourseRepository.findByName(INGEGNERIA_MECCANICA)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course misMeccTerm = courseRepository
            .findByNameAndDegreeCourse(
                "misure meccaniche e termiche",
                degreeCourseRepository
                    .findByName(INGEGNERIA_MECCANICA)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course gasDinFluDin = courseRepository
            .findByNameAndDegreeCourse(
                "gasdinamica e fluidodinamica",
                degreeCourseRepository.findByName(INGEGNERIA_MECCANICA_MAGISTRALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course turboMacchine = courseRepository
            .findByNameAndDegreeCourse(
                "turbomacchine",
                degreeCourseRepository.findByName(INGEGNERIA_MECCANICA_MAGISTRALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course maccEle = courseRepository
            .findByNameAndDegreeCourse(
                "macchine elettriche",
                degreeCourseRepository.findByName(INGEGNERIA_ELETTRICA)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course elePot = courseRepository
            .findByNameAndDegreeCourse(
                "elettrica di potenza",
                degreeCourseRepository.findByName(INGEGNERIA_ELETTRICA_MAGISTRALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));

        Course impEleCivInd = courseRepository
            .findByNameAndDegreeCourse(
                "impianti elettrici civili e industriali",
                degreeCourseRepository.findByName(INGEGNERIA_ELETTRICA_MAGISTRALE)
                    .orElseThrow(() -> new NoSuchElementException(DEGREE_COURSE_NOT_FOUND))
                    .getId()
            ).orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND));


        List<ExaminationDto> examinations = new ArrayList<>();

        // create examination
        examinations.add(
            new ExaminationDto(
                analisiMatematica,
                anacleto,
                30,
                true,
                LocalDate.of(2022, 6, 23)
            )
        );

        examinations.add(
            new ExaminationDto(
                eMMePM,
                anacleto,
                30,
                true,
                LocalDate.of(2018, 2, 18)
            )
        );

        examinations.add(
            new ExaminationDto(
                analisiMatematicaInf,
                mark,
                30,
                true,
                LocalDate.of(2018, 2, 18)
            )
        );

        examinations.add(
            new ExaminationDto(
                economiaInf,
                mark,
                30,
                true,
                LocalDate.of(2018, 2, 18)
            )
        );

        examinations.add(
            new ExaminationDto(
                geometriaInf,
                mark,
                30,
                true,
                LocalDate.of(2023, 9, 18)
            )
        );

        examinations.add(
            new ExaminationDto(
                fondInfInf,
                mark,
                30,
                true,
                LocalDate.of(2021, 10, 11)
            )
        );

        examinations.add(
            new ExaminationDto(
                calcoloNumerico,
                filippo,
                18,
                false,
                LocalDate.of(2024, 03, 14)
            )
        );

        examinations.add(
            new ExaminationDto(
                scienzaDelleCostruzioni,
                anacleto,
                30,
                true,
                LocalDate.of(2022, 01, 13)
            )
        );

        examinations.add(
            new ExaminationDto(
                chimica,
                anacleto,
                25,
                false,
                LocalDate.of(2023, 05, 15)
            )
        );

        examinations.add(
            new ExaminationDto(
                gp,
                anacleto,
                30,
                true,
                LocalDate.of(2022, 07, 30)
            )
        );

        examinations.add(
            new ExaminationDto(
                matInnovativiIngElettrica,
                anacleto,
                30,
                true,
                LocalDate.of(2024, 06, 06)
            )
        );

        examinations.add(
            new ExaminationDto(
                fisicaGenerale,
                filippo,
                18,
                false,
                LocalDate.of(2024, 07, 20)
            )
        );

        examinations.add(
            new ExaminationDto(
                gp,
                angelo,
                24,
                false,
                LocalDate.of(2021, 03, 11)
            )
        );

        examinations.add(
            new ExaminationDto(
                calcoloNumerico,
                angelo,
                30,
                false,
                LocalDate.of(2022, 01, 20)
            )
        );

        examinations.add(
            new ExaminationDto(
                chimica,
                angelo,
                22,
                false,
                LocalDate.of(2021, 05, 20)
            )
        );

        examinations.add(
            new ExaminationDto(
                scienzaDelleCostruzioni,
                angelo,
                30,
                true,
                LocalDate.of(2021, 07, 03)
            )
        );

        examinations.add(
            new ExaminationDto(
                bigDataAna,
                feluccio,
                30,
                true,
                LocalDate.of(2021, 06, 03)
            )
        );

        examinations.add(
            new ExaminationDto(
                cyberSecurity,
                feluccio,
                30,
                false,
                LocalDate.of(2021, 02, 03)
            )
        );

        examinations.add(
            new ExaminationDto(
                basiDiDati,
                feluccio,
                21,
                false,
                LocalDate.of(2024, 04, 12)
            )
        );

        examinations.add(
            new ExaminationDto(
                sistemiInformativi,
                feluccio,
                18,
                false,
                LocalDate.of(2023, 06, 8)
            )
        );


        examinations.add(
            new ExaminationDto(
                sistemiInformativi,
                jonspencer,
                25,
                false,
                LocalDate.of(2021, 04, 03)
            )
        );

        examinations.add(
            new ExaminationDto(
                pAFD,
                jonspencer,
                30,
                false,
                LocalDate.of(2025, 01, 21)
            )
        );

        examinations.add(
            new ExaminationDto(
                anSisDin,
                jonspencer,
                28,
                false,
                LocalDate.of(2024, 05, 18)
            )
        );

        examinations.add(
            new ExaminationDto(
                iOT,
                jonspencer,
                30,
                true,
                LocalDate.of(2021, 06, 03)
            )
        );

        examinations.add(
            new ExaminationDto(
                bigDataAna,
                jonspencer,
                30,
                true,
                LocalDate.of(2021, 06, 03)
            )
        );

        examinations.add(
            new ExaminationDto(
                cyberSecurity,
                jonspencer,
                30,
                true,
                LocalDate.of(2021, 02, 03)
            )
        );

        examinations.add(
            new ExaminationDto(
                basiDiDati,
                jonspencer,
                30,
                true,
                LocalDate.of(2024, 04, 12)
            )
        );

        examinations.add(
            new ExaminationDto(
                bigData,
                iggypop,
                30,
                true,
                LocalDate.of(2021, 06, 03)
            )
        );

        examinations.add(
            new ExaminationDto(
                compilatori,
                iggypop,
                30,
                true,
                LocalDate.of(2021, 06, 03)
            )
        );

        examinations.add(
            new ExaminationDto(
                bigData,
                uccio,
                27,
                true,
                LocalDate.of(2021, 06, 03)
            )
        );

        examinations.add(
            new ExaminationDto(
                compilatori,
                uccio,
                18,
                false,
                LocalDate.of(2021, 06, 03)
            )
        );

        examinations.add(
            new ExaminationDto(
                alInJava,
                mark,
                21,
                false,
                LocalDate.of(2021, 06, 8)
            )
        );

        examinations.add(
            new ExaminationDto(
                fondElet,
                mark,
                28,
                false,
                LocalDate.of(2020, 12, 16)
            )
        );

        examinations.add(
            new ExaminationDto(
                telecomunicazioni,
                mark,
                26,
                false,
                LocalDate.of(2024, 10, 4)
            )
        );

        examinations.add(
            new ExaminationDto(
                sisEneMaFlu,
                iginio,
                30,
                true,
                LocalDate.of(2021, 06, 03)
            )
        );

        examinations.add(
            new ExaminationDto(
                impiantiMecc,
                iginio,
                30,
                true,
                LocalDate.of(2024, 7, 01)
            )
        );

        examinations.add(
            new ExaminationDto(
                misMeccTerm,
                iginio,
                30,
                true,
                LocalDate.of(2021, 01, 22)
            )
        );


        examinations.add(
            new ExaminationDto(
                gasDinFluDin,
                benny,
                26,
                false,
                LocalDate.of(2021, 05, 03)
            )
        );

        examinations.add(
            new ExaminationDto(
                turboMacchine,
                benny,
                22,
                false,
                LocalDate.of(2022, 07, 7)
            )
        );

        examinations.add(
            new ExaminationDto(
                maccEle,
                renzi,
                18,
                false,
                LocalDate.of(2021, 03, 12)
            )
        );

        examinations.add(
            new ExaminationDto(
                elePot,
                santana,
                30,
                false,
                LocalDate.of(2024, 5, 12)
            )
        );

        examinations.add(
            new ExaminationDto(
                impEleCivInd,
                santana,
                30,
                false,
                LocalDate.of(2023, 11, 12)
            )
        );

        examinations.add(
            new ExaminationDto(
                sistemiInformativi,
                anacletoTwo,
                25,
                false,
                LocalDate.of(2021, 04, 03)
            )
        );

        examinations.add(
            new ExaminationDto(
                pAFD,
                anacletoTwo,
                30,
                false,
                LocalDate.of(2025, 01, 21)
            )
        );

        examinations.add(
            new ExaminationDto(
                anSisDin,
                anacletoTwo,
                28,
                false,
                LocalDate.of(2024, 05, 18)
            )
        );

        examinations.add(
            new ExaminationDto(
                iOT,
                anacletoTwo,
                30,
                true,
                LocalDate.of(2021, 06, 03)
            )
        );

        examinations.add(
            new ExaminationDto(
                bigDataAna,
                anacletoTwo,
                30,
                true,
                LocalDate.of(2021, 06, 03)
            )
        );

        examinations.add(
            new ExaminationDto(
                cyberSecurity,
                anacletoTwo,
                30,
                true,
                LocalDate.of(2021, 02, 03)
            )
        );

        examinations.add(
            new ExaminationDto(
                basiDiDati,
                anacletoTwo,
                30,
                true,
                LocalDate.of(2024, 04, 12)
            )
        );

        examinations.add(
            new ExaminationDto(
                compilatori,
                anacletoTwo,
                30,
                true,
                LocalDate.of(2021, 06, 03)
            )
        );

        examinations.add(
            new ExaminationDto(
                bigData,
                anacletoTwo,
                27,
                true,
                LocalDate.of(2021, 06, 03)
            )
        );

        examinations.add(
            new ExaminationDto(
                alInJava,
                anacletoTwo,
                29,
                false,
                LocalDate.of(2021, 06, 8)
            )
        );

        examinations.add(
            new ExaminationDto(
                fondElet,
                anacletoTwo,
                24,
                false,
                LocalDate.of(2022, 12, 16)
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



    // initialize user
    private void initializeUsers(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {

        // create user
        Builder formBuilderOne = new Builder();
        formBuilderOne.withUsername("rico");
        formBuilderOne.withPassword("rico");
        formBuilderOne.withFullname("damiano ruggieri");
        formBuilderOne.withStreet("via della nazione");
        formBuilderOne.withCity("fasano");
        formBuilderOne.withState("italia");
        formBuilderOne.withZip("72015");
        formBuilderOne.withPhone("3815674128");
        formBuilderOne.withRole(Role.ADMIN);

        Builder formBuilderTwo = new Builder();
        formBuilderTwo.withUsername("fido");
        formBuilderTwo.withPassword("fido");
        formBuilderTwo.withFullname("enrico ruggieri");
        formBuilderTwo.withStreet("via del calvario");
        formBuilderTwo.withCity("pezze di greco");
        formBuilderTwo.withState("italia");
        formBuilderTwo.withZip("72015");
        formBuilderTwo.withPhone("3815674128");
        formBuilderTwo.withRole(Role.ADMIN);

        Builder formBuilderThree = new Builder();
        formBuilderThree.withUsername("nino");
        formBuilderThree.withPassword("nino");
        formBuilderThree.withFullname("bob dylamie");
        formBuilderThree.withStreet("via delle lamie di olimpia");
        formBuilderThree.withCity("laureto");
        formBuilderThree.withState("italia");
        formBuilderThree.withZip("72015");
        formBuilderThree.withPhone("3619647852");
        formBuilderThree.withRole(Role.STUDENT);

        Builder formBuilderFour = new Builder();
        formBuilderFour.withUsername("dino");
        formBuilderFour.withPassword("dino");
        formBuilderFour.withFullname("gilles villeneuve");
        formBuilderFour.withStreet("via di vancouver");
        formBuilderFour.withCity("vancouver");
        formBuilderFour.withState("canada");
        formBuilderFour.withZip("48759");
        formBuilderFour.withPhone("8749652314");
        formBuilderFour.withRole(Role.PROFESSOR);

        // create users
        List<RegistrationForm> users = List.of(
            new RegistrationForm(formBuilderOne),
            new RegistrationForm(formBuilderTwo),
            new RegistrationForm(formBuilderThree),
            new RegistrationForm(formBuilderFour)
        );

        // save user
        users.forEach(form -> {
            try {
                userRepository.saveAndFlush(form.toUser(passwordEncoder));
            } catch (Exception e) {
                logger.info("Error: {}", e.getMessage());
            }
        });

    }

}









