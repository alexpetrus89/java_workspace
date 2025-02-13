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
import com.alex.studentmanagementsystem.utility.Builder;
import com.alex.studentmanagementsystem.utility.RegistrationForm;


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

    private static final String TRIENNALE = "triennale";
    private static final String MAGISTRALE = "magistrale";

    private static final String MATEMATICA = "matematica";
    private static final String INFORMATICA = "informatica";
    private static final String ECONOMIA = "economia";
    private static final String FISICA = "fisica";
    private static final String CHIMICA = "chimica";
    private static final String DISEGNO = "disegno";
    private static final String ING_MECCANICA = "ing. meccanica";
    private static final String ING_GESTIONALE = "ing. gestionale";
    private static final String ING_ELETTRICA = "ing. elettrica";
    private static final String LINGUA = "lingua straniera";

    private static final String UC_GIACINTO = "wer456er";
    private static final String UC_GENESIO = "ert456er";
    private static final String UC_FABIO = "bbb456er";
    private static final String UC_GIACOMO = "frt456er";
    private static final String UC_FELICE = "uba789lo";
    private static final String UC_VLADIMIRO = "rot111ad";

    private static final String STUDENT_NOT_FOUND = "Student not found";
    private static final String COURSE_NOT_FOUND = "Course not found";
    private static final String DEGREE_COURSE_NOT_FOUND = "Degree course not found";


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
            initializeExaminations(studentRepository, courseRepository, examinationRepository);

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
                TRIENNALE,
                3
            )
        );

        degreeCourses.add(
            new DegreeCourse(
                INGEGNERIA_MECCANICA,
                TRIENNALE,
                3
            )
        );

        degreeCourses.add(
            new DegreeCourse(
                INGEGNERIA_ELETTRICA,
                TRIENNALE,
                3
            )
        );

        degreeCourses.add(
            new DegreeCourse(
                INGEGNERIA_CIVILE,
                TRIENNALE,
                3
            )
        );

        degreeCourses.add(
            new DegreeCourse(
                INGEGNERIA_INFORMATICA,
                TRIENNALE,
                3
            )
        );

        degreeCourses.add(
            new DegreeCourse(
                INGEGNERIA_GESTIONALE_MAGISTRALE,
                MAGISTRALE,
                2
            )
        );

        degreeCourses.add(
            new DegreeCourse(
                INGEGNERIA_INFORMATICA_MAGISTRALE,
                MAGISTRALE,
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
                new Register("123459"),
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
                new Register("123460"),
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
                new Register("123461"),
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
                new Register("123462"),
                "alfonso",
                "alfonso.blood@gmail.com",
                LocalDate.of(1993, 11, 8),
                degreeCourseRepository
                    .findByName(INGEGNERIA_GESTIONALE_MAGISTRALE)
                    .orElseThrow(() -> new ObjectNotFoundException(DEGREE_COURSE_NOT_FOUND, INGEGNERIA_GESTIONALE_MAGISTRALE))
            )
        );

        students.add(
            new StudentDto(
                new Register("123463"),
                "fedayno",
                "fedayno.blood@gmail.com",
                LocalDate.of(1990, 11, 8),
                degreeCourseRepository
                    .findByName(INGEGNERIA_GESTIONALE_MAGISTRALE)
                    .orElseThrow(() -> new ObjectNotFoundException(DEGREE_COURSE_NOT_FOUND, INGEGNERIA_GESTIONALE_MAGISTRALE))
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
                new Register("123464"),
                "uccio",
                "uccio.blood@gmail.com",
                LocalDate.of(1990, 3, 29),
                degreeCourseRepository
                    .findByName(INGEGNERIA_INFORMATICA_MAGISTRALE)
                    .orElseThrow(() -> new ObjectNotFoundException(DEGREE_COURSE_NOT_FOUND, INGEGNERIA_INFORMATICA_MAGISTRALE))
            )
        );

        students.add(
            new StudentDto(
                new Register("123466"),
                "bomba",
                "bomba.blood@gmail.com",
                LocalDate.of(1990, 3, 2),
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
            .orElseThrow(null);

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


        // save students for ING. INFORMATICA MAGISTRALE degree course
        DegreeCourse ingInfMag = degreeCourseRepository
            .findByName(INGEGNERIA_INFORMATICA_MAGISTRALE)
            .orElseThrow(null);

        // set students of ING. INFORMATICA MAGISTRALE in degreeCourse object
        ingGestMag.setStudents(
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

        DegreeCourse ingGestMag = degreeCourseRepository
            .findByName(INGEGNERIA_GESTIONALE_MAGISTRALE)
            .orElseThrow();

        DegreeCourse ingInfMag = degreeCourseRepository
            .findByName(INGEGNERIA_INFORMATICA_MAGISTRALE)
            .orElseThrow();


        List<Course> courses = new ArrayList<>();

        // create courses
        courses.add(
            new Course(
                "analisi matematica",
                MATEMATICA,
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
                ECONOMIA,
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
                MATEMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GIACINTO))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "fisica generale",
                FISICA,
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
                INFORMATICA,
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
                CHIMICA,
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
                ING_MECCANICA,
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
                MATEMATICA,
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
                DISEGNO,
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
                ING_MECCANICA,
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
                FISICA,
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
                ECONOMIA,
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
                FISICA,
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
                ING_ELETTRICA,
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
                ING_MECCANICA,
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
                MATEMATICA,
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
                ING_GESTIONALE,
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
                ING_MECCANICA,
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
                LINGUA,
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
                ING_GESTIONALE,
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
                ING_GESTIONALE,
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
                ING_ELETTRICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GIACINTO))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "sistemi informativi",
                INFORMATICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_FABIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
                ingGestMag
            )
        );

        courses.add(
            new Course(
                "big data",
                INFORMATICA,
                6,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_VLADIMIRO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_VLADIMIRO))),
                ingGestMag
            )
        );

        courses.add(
            new Course(
                "compilatori",
                INFORMATICA,
                12,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_GENESIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GENESIO))),
                ingInfMag
            )
        );

        courses.add(
            new Course(
                "tirocinio",
                ING_GESTIONALE,
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
                ING_GESTIONALE,
                3,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_FABIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
                ingGest
            )
        );

        courses.add(
            new Course(
                "impianti meccanici",
                ING_MECCANICA,
                3,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_FABIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
                ingMecc
            )
        );

        courses.add(
            new Course(
                "sistemi energetici e macchine a fluido",
                ING_MECCANICA,
                3,
                professorRepository
                    .findByUniqueCode(new UniqueCode(UC_FABIO))
                    .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
                ingMecc
            )
        );

        // save courses
        courses.forEach(courseRepository::saveAndFlush);

        // associate the respective courses to each degree course
        courseRepository
            .findAll()
            .stream()
            .forEach(course ->
                Arrays.asList(ingGest, ingInfMag, ingGestMag)
                .stream()
                .filter(degreeCourse -> degreeCourse.equals(course.getDegreeCourse()))
                .forEach(degreeCourse -> degreeCourse.addCourse(course)));


        // set courses in degreeCourse object
        degreeCourseRepository.save(ingGest);
        degreeCourseRepository.save(ingGestMag);
        degreeCourseRepository.save(ingInfMag);

    }







    // initialize examinations
    private void initializeExaminations(
        StudentRepository studentRepository,
        CourseRepository courseRepository,
        ExaminationRepository examinationRepository
    ) {

        // Retrieve existing Student entity from the database
        Student anacleto = studentRepository
            .findByName("anacleto")
            .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND)
        );

        Student filippo = studentRepository
            .findByName("filippo")
            .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND)
        );

        Student angelo = studentRepository
            .findByName("angelo")
            .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_FOUND)
        );


        // Retrieve existing Course entity from the database
        Course analisiMatematica = courseRepository
            .findByName("analisi matematica")
            .orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND)
        );

        Course gp = courseRepository
            .findByName("gestione dei progetti")
            .orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND)
        );

        Course scienzaDelleCostruzioni = courseRepository
            .findByName("scienza delle costruzioni")
            .orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND)
        );

        Course chimica = courseRepository
            .findByName("chimica generale")
            .orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND)
        );

        Course fisicaGenerale = courseRepository
            .findByName("fisica generale")
            .orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND)
        );

        Course calcoloNumerico = courseRepository
            .findByName("calcolo numerico")
            .orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND)
        );

        Course matInnovativiIngElettrica = courseRepository
            .findByName("materiali innovativi per l'ingegneria elettrica")
            .orElseThrow(() -> new NoSuchElementException(COURSE_NOT_FOUND)
        );

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

        Builder formBuilderTwo = new Builder();
        formBuilderTwo.withUsername("fido");
        formBuilderTwo.withPassword("fido");
        formBuilderTwo.withFullname("enrico ruggieri");
        formBuilderTwo.withStreet("via del calvario");
        formBuilderTwo.withCity("pezze di greco");
        formBuilderTwo.withState("italia");
        formBuilderTwo.withZip("72015");
        formBuilderTwo.withPhone("3815674128");

        // create users
        List<RegistrationForm> users = List.of(
            new RegistrationForm(formBuilderOne),
            new RegistrationForm(formBuilderTwo)
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
