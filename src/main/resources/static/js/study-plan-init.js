// study-plan-init.js
import { uploadCourses } from './upload-courses.js';
import { COURSES_TOKEN } from './config.js';

document.addEventListener('DOMContentLoaded', () => {
    const degreeSelect = document.getElementById('degreeCourseOfNewCourse');
    const courseSelect = document.getElementById('courseToAdd');
    const oldDegreeInput = document.getElementById('degreeCourseOfOldCourse');

    if (!degreeSelect || !courseSelect) return;

    // Hide old degree course field
    if (oldDegreeInput) oldDegreeInput.hidden = true;

    // Load courses initially
    uploadCourses(degreeSelect, courseSelect, COURSES_TOKEN);

    // Reload courses when degree course changes
    degreeSelect.addEventListener('change', () => {
        uploadCourses(degreeSelect, courseSelect, COURSES_TOKEN);
    });
});
