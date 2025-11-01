package com.alex.universitymanagementsystem.dto;

import java.util.ArrayList;
import java.util.List;


public class RoleBasedUrlMappings {

    private List<String> publicUrls = new ArrayList<>();
    private List<String> adminUrls = new ArrayList<>();
    private List<String> studentUrls = new ArrayList<>();
    private List<String> professorUrls = new ArrayList<>();

    // getters
    public List<String> getPublicUrls() { return publicUrls; }
    public String[] getPublicUrlsArray() { return publicUrls.toArray(String[]::new); }
    public List<String> getAdminUrls() { return adminUrls; }
    public String[] getAdminUrlsArray() { return adminUrls.toArray(String[]::new); }
    public List<String> getStudentUrls() { return studentUrls; }
    public String[] getStudentUrlsArray() { return studentUrls.toArray(String[]::new); }
    public List<String> getProfessorUrls() { return professorUrls; }
    public String[] getProfessorUrlsArray() { return professorUrls.toArray(String[]::new); }

    // setters
    public void setPublicUrls(List<String> publicUrls) { this.publicUrls = publicUrls; }
    public void setAdminUrls(List<String> adminUrls) { this.adminUrls = adminUrls; }
    public void setStudentUrls(List<String> studentUrls) { this.studentUrls = studentUrls; }
    public void setProfessorUrls(List<String> professorUrls) { this.professorUrls = professorUrls; }


}

