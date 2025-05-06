package com.alex.universitymanagementsystem.domain;

import java.io.Serializable;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

// Oggetto Outcome
@Entity
@Table(name = "examination_outcome")
@Access(AccessType.PROPERTY)
public class ExaminationOutcome implements Serializable {

    // instance variables
    private Long id;
    private ExaminationAppeal examinationAppeal; // appello di riferimento
    private String register; // matricola dello studente
    private boolean present; // flag per indicare se lo studente si è presentato all'esame
    private int grade; // voto dell'esame
    private boolean withHonors; // flag per indicare se lo studente ha accettato l'esito con lode
    private boolean accepted; // flag per indicare se lo studente ha accettato l'esito
    private String message; // informazioni generiche


    // constructors
    public ExaminationOutcome() {}

    public ExaminationOutcome(ExaminationAppeal examinationAppeal, String register) {
        this.examinationAppeal = examinationAppeal;
        this.register = register;
    }

    public ExaminationOutcome(ExaminationAppeal examinationAppeal, String register, boolean present, int grade, boolean withHonors) {
        this.examinationAppeal = examinationAppeal;
        this.register = register;
        this.present = present;
        this.grade = grade;
        this.withHonors = withHonors;
    }

    // getters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "outcome_id")
    public Long getId() {
        return id;
    }


    @ManyToOne
    @JoinColumn(name = "examination_appeal_id")
    public ExaminationAppeal getExaminationAppeal() {
        return examinationAppeal;
    }

    @Column(name = "register")
    public String getRegister() {
        return register;
    }

    @Column(name = "present")
    public boolean isPresent() {
        return present;
    }

    @Min(0)
    @Max(30)
    @Column(name = "grade")
    public int getGrade() {
        return grade;
    }

    @Column(name = "with_honors")
    public boolean isWithHonors() {
        return withHonors;
    }

    @Column(name = "accepted")
    public boolean isAccepted() {
        return accepted;
    }

    @Column(name = "message")
    public String getMessage() {
        return message;
    }


    // setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setExaminationAppeal(ExaminationAppeal examinationAppeal) {
        this.examinationAppeal = examinationAppeal;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public void setGrade(int grade) {
        if (grade < 0 || grade > 30) {
            throw new IllegalArgumentException("Grade must be between 0 and 30");
        }
        this.grade = grade;
    }

    public void setWithHonors(boolean withHonors) {
        if(getGrade() != 30) {
            this.withHonors = false;
        }
        this.withHonors = withHonors;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
