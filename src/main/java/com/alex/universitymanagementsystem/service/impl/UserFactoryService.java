package com.alex.universitymanagementsystem.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.User;
import com.alex.universitymanagementsystem.utils.Builder;
import com.alex.universitymanagementsystem.utils.RegistrationForm;

import jakarta.transaction.Transactional;

@Service
public class UserFactoryService {


    // instance variables
    private final UserServiceImpl userServiceImpl;
    private final StudentServiceImpl studentServiceImpl;
    private final ProfessorServiceImpl professorServiceImpl;
    private final PasswordEncoder passwordEncoder;

    public UserFactoryService(
        UserServiceImpl userServiceImpl,
        StudentServiceImpl studentServiceImpl,
        ProfessorServiceImpl professorServiceImpl,
        PasswordEncoder passwordEncoder
    ) {
        this.userServiceImpl = userServiceImpl;
        this.studentServiceImpl = studentServiceImpl;
        this.professorServiceImpl = professorServiceImpl;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional // con l'annotazione transactional effettua una gestione propria degli errori
    public User createUser(Builder builder) {
        return switch (builder.getRole()) {
            case STUDENT -> {
                Student student = new Student(builder, passwordEncoder);
                studentServiceImpl.addNewStudent(student);
                yield student;
            }
            case PROFESSOR -> {
                Professor professor = new Professor(builder, passwordEncoder);
                professorServiceImpl.addNewProfessor(professor);
                yield professor;
            }
            case ADMIN -> {
                User user = new RegistrationForm(builder).toUser(passwordEncoder);
                userServiceImpl.addNewUser(user);
                yield user;
            }
            default -> throw new UnsupportedOperationException("Role not supported");
        };
    }
}
