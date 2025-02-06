package com.alex.studentmanagementsystem.config;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
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


    private final transient Logger logger =
        org.slf4j.LoggerFactory.getLogger(StudentDatabaseInit.class);

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
                    .orElseThrow(null)
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
                    .orElseThrow(null)
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
                    .orElseThrow(null)
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
                    .orElseThrow(null)
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
                    .orElseThrow(null)
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
                    .orElseThrow(null)
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
                    .orElseThrow(null)
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
                    .orElseThrow(null)
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
                    .orElseThrow(null)
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
                    .orElseThrow(null)
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
                    .orElseThrow(null)
            )
        );

        // sanity check
        if(students.isEmpty())
            throw new IllegalArgumentException("students list is empty");

        // save students
        students.stream()
            .map(StudentMapper::mapToStudent)
            .forEach(studentRepository::saveAndFlush);


        // save students for GESTIONALE degree course
        DegreeCourse ingGestionale = degreeCourseRepository
            .findByName(INGEGNERIA_GESTIONALE)
            .orElseThrow(null);

        // set students of ING. GESTIONALE in degreeCourse object
        ingGestionale.setStudents(
            studentRepository
                .findAll()
                .stream()
                .filter(student -> student.getDegreeCourse().getName().equals(INGEGNERIA_GESTIONALE))
                .toList()
        );

        // save degree course ING. GESTIONALE
        degreeCourseRepository.save(ingGestionale);


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
        degreeCourseRepository.save(ingGestMag);


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
        degreeCourseRepository.save(ingInfMag);

    }






    // INITIALIZE PROFESSORS
    void initializeProfessors(ProfessorRepository professorRepository) {

        ProfessorDto giacinto = new ProfessorDto(
            new UniqueCode(UC_GIACINTO),
            "abc678rde217we56",
            "giacinto",
            "professore.giacinto@dominio.it"
        );

        ProfessorDto genesio = new ProfessorDto(
            new UniqueCode(UC_GENESIO),
            "duf677rde22werf3",
            "genesio",
            "professore.genesio@dominio.it"
        );

        ProfessorDto fabio = new ProfessorDto(
            new UniqueCode(UC_FABIO),
            "ert678rde23123tr",
            "fabio",
            "professore.fabio@dominio.it"
        );

        ProfessorDto giacomo = new ProfessorDto(
            new UniqueCode(UC_GIACOMO),
            "but678rde237e34v",
            "giacomo",
            "professore.giacomo@dominio.it"
        );

        ProfessorDto felice = new ProfessorDto(
            new UniqueCode(UC_FELICE),
            "flt985aba741o34v",
            "felice",
            "professore.felice@dominio.it"
        );

        ProfessorDto vladimiro = new ProfessorDto(
            new UniqueCode(UC_VLADIMIRO),
            "pol236uii632u15f",
            "vladimiro",
            "professore.vladimiro@dominio.it"
        );

        // create professors list
        List<ProfessorDto> professors = List.of(giacinto, genesio, fabio, giacomo, felice, vladimiro);

        // save professors
        professors.stream()
            .map(ProfessorMapper::mapToProfessor)
            .forEach(professorRepository::save);

    }



    // INITIALIZE COURSES
    private void initializeCourses(
        CourseRepository courseRepository,
        ProfessorRepository professorRepository,
        DegreeCourseRepository degreeCourseRepository
    ) {

        // retrieve degreeCourses
        DegreeCourse ingGestionale = degreeCourseRepository
            .findByName(INGEGNERIA_GESTIONALE)
            .orElseThrow();

        DegreeCourse ingGestionaleMagistrale = degreeCourseRepository
            .findByName(INGEGNERIA_GESTIONALE_MAGISTRALE)
            .orElseThrow();

        DegreeCourse ingInformaticaMagistrale = degreeCourseRepository
            .findByName(INGEGNERIA_INFORMATICA_MAGISTRALE)
            .orElseThrow();


        // create courses
        Course analisiMatematica = new Course(
            "analisi matematica",
            MATEMATICA,
            12,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GIACINTO))),
            ingGestionale
        );

        Course economia = new Course(
            "elementi di economia",
            ECONOMIA,
            6,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GIACINTO))),
            ingGestionale
        );

        Course geometria = new Course(
            "geometria e algebra",
            MATEMATICA,
            6,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GIACINTO))),
            ingGestionale
        );

        Course fisicaGenerale = new Course(
            "fisica generale",
            FISICA,
            12,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GIACINTO))),
            ingGestionale
        );

        Course informatica = new Course(
            "fondamenti di informatica",
            INFORMATICA,
            6,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_GENESIO))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GENESIO))),
            ingGestionale
        );

        Course chimica = new Course(
            "chimica generale",
            CHIMICA,
            9,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_FABIO))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
            ingGestionale
        );

        Course scienzaDelleCostruzioni = new Course(
            "scienza delle costruzioni",
            ING_MECCANICA,
            6,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_FABIO))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
            ingGestionale
        );

        Course ricercaOperativa = new Course(
            "metodi di ottimizzazione",
            MATEMATICA,
            6,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_FABIO))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
            ingGestionale
        );

        Course disegno = new Course(
            "metodi di rappresentazione tecnica",
            DISEGNO,
            6,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_FABIO))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
            ingGestionale
        );

        Course eMMEPM = new Course(
            "elementi di meccanica delle macchine e progettazione meccanica",
            ING_MECCANICA,
            12,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_GIACOMO))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GIACOMO))),
            ingGestionale
        );

        Course fisicaTecESE = new Course(
            "fisica tecnica e sistemi energetici",
            FISICA,
            12,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_GIACOMO))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GIACOMO))),
            ingGestionale
        );

        Course gestioneAziendale = new Course(
            "gestione aziendale",
            ECONOMIA,
            6,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_GIACOMO))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GIACOMO))),
            ingGestionale
        );

        Course mecDeiFluidi = new Course(
            "meccanica dei fluidi",
            FISICA,
            6,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_VLADIMIRO))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_VLADIMIRO))),
            ingGestionale
        );

        Course principiDiIngElettrica = new Course(
            "principi di ingegneria elettrica",
            ING_ELETTRICA,
            6,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_VLADIMIRO))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_VLADIMIRO))),
            ingGestionale
        );

        Course tecMeccTecMat = new Course(
            "tecnologia meccaniche e dei materiali",
            ING_MECCANICA,
            12,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_GIACOMO))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GIACOMO))),
            ingGestionale
        );

        Course calcoloNumerico = new Course(
            "calcolo numerico",
            MATEMATICA,
            6,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GIACINTO))),
            ingGestionale
        );

        Course gp = new Course(
            "gestione dei progetti",
            ING_GESTIONALE,
            9,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_FABIO))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
            ingGestionale
        );

        Course impiantiIndustriali = new Course(
            "impianti industriali",
            ING_MECCANICA,
            9,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_FELICE))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FELICE))),
            ingGestionale
        );

        Course inglese = new Course(
            "inglese",
            LINGUA,
            3,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_FELICE))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FELICE))),
            ingGestionale
        );

        Course pPPqPP = new Course(
            "progettazione dei processi produttivi e qualità dei processi produttivi",
            ING_GESTIONALE,
            12,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_FELICE))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FELICE))),
            ingGestionale
        );

        Course sicurezza = new Course(
            "sicurezza degli impianti industriali",
            ING_GESTIONALE,
            6,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_FELICE))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FELICE))),
            ingGestionale
        );

        Course matInnovativiIngElettrica = new Course(
            "materiali innovativi per l'ingegneria elettrica",
            ING_ELETTRICA,
            6,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_GIACINTO))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GIACINTO))),
            ingGestionale
        );

        Course sistemiInformativi = new Course(
            "sistemi informativi",
            INFORMATICA,
            12,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_FABIO))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
            ingGestionaleMagistrale
        );

        Course bigData = new Course(
            "big data",
            INFORMATICA,
            6,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_VLADIMIRO))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_VLADIMIRO))),
            ingGestionaleMagistrale
        );

        Course compilatori = new Course(
            "compilatori",
            INFORMATICA,
            12,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_GENESIO))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_GENESIO))),
            ingInformaticaMagistrale
        );

        Course tirocinio = new Course(
            "tirocinio",
            ING_GESTIONALE,
            6,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_FABIO))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
            ingGestionale
        );

        Course provaFinale = new Course(
            "prova finale",
            ING_GESTIONALE,
            3,
            professorRepository
                .findByUniqueCode(new UniqueCode(UC_FABIO))
                .orElseThrow(() -> new ObjectNotFoundException(new UniqueCode(UC_FABIO))),
            ingGestionale
        );

        // create list of courses
        List<Course> courses = List.of(
            analisiMatematica,
            chimica,
            economia,
            fisicaGenerale,
            geometria,
            economia,
            informatica,
            scienzaDelleCostruzioni,
            ricercaOperativa,
            disegno,
            eMMEPM,
            fisicaTecESE,
            gestioneAziendale,
            mecDeiFluidi,
            principiDiIngElettrica,
            tecMeccTecMat,
            calcoloNumerico,
            gp,
            impiantiIndustriali,
            inglese,
            pPPqPP,
            sicurezza,
            matInnovativiIngElettrica,
            sistemiInformativi,
            bigData,
            compilatori,
            tirocinio,
            provaFinale
        );

        // save courses
        courses.forEach(courseRepository::save);

        // find courses for the degree course
        List<Course> allCourses = courseRepository.findAll();
        for(Course course : allCourses) {
            if(course.getDegreeCourse().equals(ingGestionale))
                ingGestionale.addCourse(course);
            if(course.getDegreeCourse().equals(ingInformaticaMagistrale))
                ingInformaticaMagistrale.addCourse(course);
            if(course.getDegreeCourse().equals(ingGestionaleMagistrale))
                ingGestionaleMagistrale.addCourse(course);
        }


        // set courses in degreeCourse object
        degreeCourseRepository.save(ingGestionale);
        degreeCourseRepository.save(ingInformaticaMagistrale);
        degreeCourseRepository.save(ingGestionaleMagistrale);

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



        // create examination
        ExaminationDto exam1 = new ExaminationDto(
            analisiMatematica,
            anacleto,
            30,
            true,
            LocalDate.of(2022, 6, 23)
        );

        ExaminationDto exam2 = new ExaminationDto(
            calcoloNumerico,
            filippo,
            18,
            false,
            LocalDate.of(2024, 03, 14)
        );

        ExaminationDto exam3 = new ExaminationDto(
            scienzaDelleCostruzioni,
            anacleto,
            30,
            true,
            LocalDate.of(2022, 01, 13)
        );

        ExaminationDto exam4 = new ExaminationDto(
            chimica,
            anacleto,
            25,
            false,
            LocalDate.of(2023, 05, 15)
        );

        ExaminationDto exam5 = new ExaminationDto(
            gp,
            anacleto,
            30,
            true,
            LocalDate.of(2022, 07, 30)
        );

        ExaminationDto exam6 = new ExaminationDto(
            matInnovativiIngElettrica,
            anacleto,
            30,
            true,
            LocalDate.of(2024, 06, 06)
        );

        ExaminationDto exam7 = new ExaminationDto(
            fisicaGenerale,
            filippo,
            18,
            false,
            LocalDate.of(2024, 07, 20)
        );

        ExaminationDto exam8 = new ExaminationDto(
            gp,
            angelo,
            24,
            false,
            LocalDate.of(2021, 03, 11)
        );

        ExaminationDto exam9 = new ExaminationDto(
            calcoloNumerico,
            angelo,
            30,
            false,
            LocalDate.of(2022, 01, 20)
        );

        ExaminationDto exam10 = new ExaminationDto(
            chimica,
            angelo,
            22,
            false,
            LocalDate.of(2021, 05, 20)
        );

        ExaminationDto exam11 = new ExaminationDto(
            scienzaDelleCostruzioni,
            angelo,
            30,
            true,
            LocalDate.of(2021, 07, 03)
        );


        // create examinations list
        List<ExaminationDto> examinations = List.of(
            exam1,
            exam2,
            exam3,
            exam4,
            exam5,
            exam6,
            exam7,
            exam8,
            exam9,
            exam10,
            exam11
        );

        // save examination
        examinations.stream()
            .map(ExaminationMapper::mapToExamination)
            .forEach(examination -> {
                try {
                    examinationRepository.save(examination);
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
        List<RegistrationForm> users = List.of(
            new RegistrationForm(
            "rico",
            "rico",
            "damiano ruggieri",
            "via della nazione",
            "fasano",
            "italia",
            "72015",
            "3815674128"
        ),
            new RegistrationForm(
                "fido",
                "fido",
                "enrico ruggieri",
                "via del calvario",
                "pezze di greco",
                "italia",
                "72015",
                "3815674128"
            )
        );

        // save user
        users.forEach(form -> {
            try {
                userRepository.save(form.toUser(passwordEncoder));
            } catch (Exception e) {
                logger.info("Error: {}", e.getMessage());
            }
        });

    }


}
