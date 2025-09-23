package com.alex.universitymanagementsystem.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.alex.universitymanagementsystem.entity.immutable.Register;
import com.alex.universitymanagementsystem.entity.immutable.UniqueCode;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

    // constructors
    protected ExaminationAppeal() {}

    private ExaminationAppeal(Course course, String description, LocalDate date, Set<Register> registers) {
        this.course = course;
        this.professor = course.getProfessor().getUniqueCode();
        this.description = description;
        this.date = date;
        initializeRegisters(registers);
    }

    public static ExaminationAppeal of(Course course, String description, LocalDate date) {
        return new ExaminationAppeal(course, description, date, null);
    }

    public static ExaminationAppeal of(Course course, String description, LocalDate date, Set<Register> registers) {
        return new ExaminationAppeal(course, description, date, registers);
    }

    // getters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "examination_appeal_id")
    public Long getId() { return id; }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Course getCourse() { return course; }

    @Column(name = "professor_unique_code", nullable = false)
    public UniqueCode getProfessor() { return professor; }

    @Column(name = "description", length = 255)
    public String getDescription() { return description; }

    @Column(name = "date", nullable = false )
    public LocalDate getDate() { return date; }

    @ElementCollection(targetClass = Register.class)
    @CollectionTable(name = "EXAMINATION_APPEAL_REGISTERS", joinColumns = @JoinColumn(name = "examination_appeal_id"))
    @Column(name = "register", nullable = false, length = 6)
    public Set<Register> getRegisters() { return registers; }

    // setters
    public void setId(Long id) { this.id = id; }
    public void setCourse(Course course) { this.course = course; }
    public void setProfessor(UniqueCode professor) { this.professor = professor; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setRegisters(Set<Register> registers) {
        this.registers = (registers != null) ? new HashSet<>(registers) : new HashSet<>();
    }

    // Bi-directional helpers
    public boolean addRegister(Register register) {
        if (register == null) throw new IllegalArgumentException("Register cannot be null");
        return this.registers.add(register);
    }

    public boolean removeRegister(Register register) {
        if (register == null) throw new IllegalArgumentException("Register cannot be null");
        return this.registers.remove(register);
    }

    public boolean deleteIfExpiredAndNoRegisters() {
        return LocalDate.now().isAfter(getDate()) && getRegisters().isEmpty();
    }


    // --- Object methods ---
    @Override
    public String toString() {
        return "ExaminationAppeal [course=" + course.getName() +
            ", professor=" + professor.toString() +
            ", description=" + description +
            ", date=" + date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) +
            "]";
    }

    @Override
    public int hashCode() {
            return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExaminationAppeal ea)) return false;
        return Objects.equals(id, ea.id);
    }


    // initialization
    private void initializeRegisters(Set<Register> registers) {
        setRegisters(registers);
    }

}
