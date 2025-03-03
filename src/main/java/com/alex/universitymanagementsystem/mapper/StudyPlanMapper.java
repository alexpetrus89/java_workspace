package com.alex.universitymanagementsystem.mapper;

import com.alex.universitymanagementsystem.domain.StudyPlan;
import com.alex.universitymanagementsystem.dto.StudyPlanDto;

public class StudyPlanMapper {

    private StudyPlanMapper() {}

    public static StudyPlan mapToStudyPlan(StudyPlanDto studyPlanDto) {
        return new StudyPlan(
            studyPlanDto.getStudent(),
            studyPlanDto.getOrdering(),
            studyPlanDto.getCourses()
        );
    }

    public static StudyPlanDto mapToStudyPlanDto(StudyPlan studyPlan) {
        return new StudyPlanDto(
            studyPlan.getStudent(),
            studyPlan.getOrdering(),
            studyPlan.getCourses()
        );
    }

}
