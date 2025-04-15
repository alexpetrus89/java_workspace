package com.alex.universitymanagementsystem.utils;

import java.io.IOException;

import org.springframework.boot.jackson.JsonComponent;

import com.alex.universitymanagementsystem.dto.CourseDto;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

@JsonComponent
public class CourseSerializer extends JsonSerializer<CourseDto> {

    // Constants
    private ObjectMapper objectMapper = new ObjectMapper();
    private SerializerProvider provider = objectMapper.getSerializerProvider();
    private String jsonCourseName;
    private String jsonDegreeCourseName;

    @Override
    public void serialize(CourseDto courseDto, JsonGenerator generator, SerializerProvider provider) throws IOException {
        try (generator) {
            // Inizio della scrittura dell'oggetto JSON
            generator.writeStartObject();

            // Serializzazione del nome del corso
            if(courseDto == null) {
                generator.writeNullField("course");
                // Fine della scrittura dell'oggetto JSON
                generator.writeEndObject();
                generator.flush(); // Svuota il buffer di output
                return;
            }

            // Specifica il nome del campo JSON per il corso
            generator.writeObjectFieldStart( "course");
            generator.writeStringField("name", courseDto.getName());
            this.jsonCourseName = objectMapper.writeValueAsString("name");


            // Serializzazione del corso di laurea
            if (courseDto.getDegreeCourse() != null) {
                // Specifica il nome del campo JSON per il corso di laurea
                generator.writeObjectFieldStart( "degreeCourse");
                generator.writeStringField("name", courseDto.getDegreeCourse().getName());
                this.jsonDegreeCourseName = objectMapper.writeValueAsString("name");
            } else {
                generator.writeNullField("degreeCourse");
            }

        }
    }

    public void serialize(CourseDto courseDto,  JsonGenerator generator) {
        try {
            this.serialize(courseDto, generator, this.provider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    // Getters
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public SerializerProvider getProvider() {
        return provider;
    }

    public String getJsonCourseName() {
        return jsonCourseName;
    }

    public String getJsonDegreeCourseName() {
        return jsonDegreeCourseName;
    }

    // Setters
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setProvider(SerializerProvider provider) {
        this.provider = provider;
    }

    public void setJsonCourseName(String jsonCourseName) {
        this.jsonCourseName = jsonCourseName;
    }

    public void setJsonDegreeCourseName(String jsonDegreeCourseName) {
        this.jsonDegreeCourseName = jsonDegreeCourseName;
    }



}