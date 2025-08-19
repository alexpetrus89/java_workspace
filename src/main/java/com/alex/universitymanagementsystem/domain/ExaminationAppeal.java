package com.alex.universitymanagementsystem.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "EXAMINATION_APPEALS")
@Access(AccessType.PROPERTY)
public class ExaminationAppeal implements Serializable {

    // instance variables
    private Long id;
    private Course course;
    private UniqueCode professor;
    private String description;
    private LocalDate date;
    private Set<Register> registers = new HashSet<>();

    // constructor
    public ExaminationAppeal() {}

    public ExaminationAppeal(Course course, String description, LocalDate date) {
        this.course = course;
        this.professor = course.getProfessor().getUniqueCode();
        this.description = description;
        this.date = date;
    }

    public ExaminationAppeal(Course course, String description, LocalDate date, Set<Register> registers) {
        this.course = course;
        this.professor = course.getProfessor().getUniqueCode();
        this.description = description;
        this.date = date;
        this.registers = registers;
    }

    // getters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "examination_appeal_id")
    public Long getId() { return id; }

    @ManyToOne
    @JoinColumn(name = "course_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Course getCourse() { return course; }

    @Column(name = "professor_unique_code")
    public UniqueCode getProfessor() { return professor; }

    @Column(name = "description")
    public String getDescription() { return description; }

    @Column(name = "date")
    public LocalDate getDate() { return date; }

    @Column(name = "registers")
    @ElementCollection(targetClass = Register.class)
    public Set<Register> getRegisters() { return registers; }

    // setters
    public void setId(Long id) { this.id = id; }
    public void setCourse(Course course) { this.course = course; }
    public void setProfessor(UniqueCode professor) { this.professor = professor; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setRegisters(Set<Register> registers) { this.registers = registers; }

    @Override
    public String toString() {
        return "ExaminationAppeal [course=" + course.getName() + ", professor=" + professor.toString() + ", description=" + description + ", date=" + date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "]";
    }

    @Override
    public int hashCode() {
            return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExaminationAppeal)) return false;
        ExaminationAppeal other = (ExaminationAppeal) o;
        return Objects.equals(id, other.id);
    }

    // other methods
    public boolean addRegister(Register register) {
        // check if register is null
        if (register == null)
            throw new IllegalArgumentException("Register cannot be null");

        return this.registers.add(register);
    }

    public boolean removeRegister(Register register) {
        if (register == null)
            throw new IllegalArgumentException("Register cannot be null");

        return this.registers.remove(register);
    }

    public boolean deleteIfExpiredAndNoRegisters() {
        LocalDate today = LocalDate.now();
        return today.isAfter(getDate()) && getRegisters().isEmpty();
    }



}
