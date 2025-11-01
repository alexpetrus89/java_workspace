package com.alex.universitymanagementsystem.mapper;


import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import com.alex.universitymanagementsystem.enum_type.CourseType;
import com.alex.universitymanagementsystem.enum_type.MiurAcronymType;

public class CourseTypeMapper {

    private static final Map<CourseType, MiurAcronymType> COURSE_TO_MIUR_MAP;
    private static final Map<MiurAcronymType, CourseType> MIUR_TO_COURSE_MAP;

    static {
        Map<CourseType, MiurAcronymType> courseToMiur = new EnumMap<>(CourseType.class);

        // --- Main mapping: CourseType → MiurAcronymType ---
        courseToMiur.put(CourseType.MATEMATICA, MiurAcronymType.MAT);
        courseToMiur.put(CourseType.INFORMATICA, MiurAcronymType.INF);
        courseToMiur.put(CourseType.ECONOMIA, MiurAcronymType.ECO);
        courseToMiur.put(CourseType.FISICA, MiurAcronymType.FIS);
        courseToMiur.put(CourseType.CHIMICA, MiurAcronymType.CHI);
        courseToMiur.put(CourseType.ING_MECCANICA, MiurAcronymType.ING);
        courseToMiur.put(CourseType.ING_GESTIONALE, MiurAcronymType.ING);
        courseToMiur.put(CourseType.ING_ELETTRICA, MiurAcronymType.ING);
        courseToMiur.put(CourseType.ING_CIVILE, MiurAcronymType.ING);
        courseToMiur.put(CourseType.ING_INFORMATICA, MiurAcronymType.INF);
        courseToMiur.put(CourseType.ING_ELETTRONICA, MiurAcronymType.ING);
        courseToMiur.put(CourseType.LINGUA_STRANIERA, MiurAcronymType.LET);
        courseToMiur.put(CourseType.DISEGNO, MiurAcronymType.ING);
        courseToMiur.put(CourseType.IDRAULICA, MiurAcronymType.ING);
        courseToMiur.put(CourseType.ALTRO, MiurAcronymType.ING);

        COURSE_TO_MIUR_MAP = Collections.unmodifiableMap(courseToMiur);

        // --- Reverse mapping: MiurAcronymType → CourseType ---
        Map<MiurAcronymType, CourseType> miurToCourse = new EnumMap<>(MiurAcronymType.class);

        miurToCourse.put(MiurAcronymType.MAT, CourseType.MATEMATICA);
        miurToCourse.put(MiurAcronymType.INF, CourseType.INFORMATICA);
        miurToCourse.put(MiurAcronymType.ECO, CourseType.ECONOMIA);
        miurToCourse.put(MiurAcronymType.FIS, CourseType.FISICA);
        miurToCourse.put(MiurAcronymType.CHI, CourseType.CHIMICA);
        miurToCourse.put(MiurAcronymType.ING, CourseType.ING_MECCANICA);
        miurToCourse.put(MiurAcronymType.LET, CourseType.LINGUA_STRANIERA);

        // Remaining MIUR types not represented in CourseType → default to ALTRO
        miurToCourse.put(MiurAcronymType.BIO, CourseType.ALTRO);
        miurToCourse.put(MiurAcronymType.MED, CourseType.ALTRO);
        miurToCourse.put(MiurAcronymType.GIU, CourseType.ALTRO);
        miurToCourse.put(MiurAcronymType.PSI, CourseType.ALTRO);

        MIUR_TO_COURSE_MAP = Collections.unmodifiableMap(miurToCourse);
    }

    private CourseTypeMapper() {
        // Utility class: prevent instantiation
    }

    /**
     * Maps a {@link CourseType} to its corresponding {@link MiurAcronymType}.
     *
     * @param courseType the course type to map (may be null)
     * @return an {@link Optional} containing the corresponding {@link MiurAcronymType},
     *         or an empty {@link Optional} if no mapping is defined or the input is null
     */
    public static Optional<MiurAcronymType> toMiurAcronym(CourseType courseType) {
        return Optional.ofNullable(COURSE_TO_MIUR_MAP.get(courseType));
    }

    /**
     * Maps a {@link MiurAcronymType} to a representative {@link CourseType}.
     *
     * @param miurType the MIUR acronym type to map (may be null)
     * @return an {@link Optional} containing a representative {@link CourseType},
     *         or an empty {@link Optional} if no mapping is defined or the input is null
     */
    public static Optional<CourseType> toCourseType(MiurAcronymType miurType) {
        return Optional.ofNullable(MIUR_TO_COURSE_MAP.get(miurType));
    }

    /**
     * Returns the complete, unmodifiable mapping from {@link CourseType} to {@link MiurAcronymType}.
     *
     * @return an unmodifiable {@link Map} containing the CourseType → MiurAcronymType mapping
     */
    public static Map<CourseType, MiurAcronymType> getCourseToMiurMap() {
        return COURSE_TO_MIUR_MAP;
    }

    /**
     * Returns the complete, unmodifiable mapping from {@link MiurAcronymType} to {@link CourseType}.
     *
     * @return an unmodifiable {@link Map} containing the MiurAcronymType → CourseType mapping
     */
    public static Map<MiurAcronymType, CourseType> getMiurToCourseMap() {
        return MIUR_TO_COURSE_MAP;
    }
}

