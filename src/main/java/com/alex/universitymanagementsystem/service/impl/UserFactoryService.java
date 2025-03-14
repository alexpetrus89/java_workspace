package com.alex.universitymanagementsystem.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.User;
import com.alex.universitymanagementsystem.repository.ProfessorRepository;
import com.alex.universitymanagementsystem.repository.UserRepository;
import com.alex.universitymanagementsystem.utils.Builder;

import jakarta.transaction.Transactional;

@Service
public class UserFactoryService {

    // instance variables
    private final UserRepository userRepository;
    private final StudentServiceImpl studentServiceImpl;
    private final ProfessorRepository professorRepository;

    public UserFactoryService(
        UserRepository userRepository,
        StudentServiceImpl studentServiceImpl,
        ProfessorRepository professorRepository
    ) {
        this.userRepository = userRepository;
        this.studentServiceImpl = studentServiceImpl;
        this.professorRepository = professorRepository;
    }

    @Transactional // con l'annotazione transactional effettua una gestione propria degli errori
    public User createUser(Builder builder, PasswordEncoder passwordEncoder) {
        return switch (builder.getRole()) {
            case STUDENT -> {
                Student student = new Student(builder, passwordEncoder);
                studentServiceImpl.addNewStudent(student);
                yield student;
            }
            case PROFESSOR -> {
                Professor professor = new Professor(builder, passwordEncoder);
                professorRepository.saveAndFlush(professor);
                yield professor;
            }
            case ADMIN -> {
                User user = new User(builder, passwordEncoder);
                userRepository.saveAndFlush(user);
                yield user;
            }
            default -> throw new UnsupportedOperationException("Role not supported");
        };
    }
}
