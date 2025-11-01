// upload-courses.js
/**
 * Module to fetch courses for a selected degree course and populate a select element.
 * Independent from the view; designed to interface with any script that handles the UI.
 */

/**
 * Fetch the list of courses for a given degree course from the backend.
 * @param {string} degreeCourseName - The degree course name to fetch.
 * @param {string} token - Authorization token.
 * @returns {Promise<Array>} - Array of courses ({name: string}).
 */
export async function fetchCourses(degreeCourseName, token) {
    if (!degreeCourseName) return [];

    const url = `/api/v1/degree-course/read/courses/ajax?name=${encodeURIComponent(degreeCourseName)}`;

    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Accept': 'application/json'
            }
        });

        if (!response.ok)
            throw new Error(`HTTP ${response.status} - ${response.statusText}`);

        const data = await response.json();

        if (!Array.isArray(data))
            throw new TypeError('Response is not a valid array of courses.');

        return data;
    } catch (error) {
        console.error('Error fetching courses:', error);
        return [];
    }
}

/**
 * Populate a select element with an array of courses.
 * @param {HTMLSelectElement} select - The select element to populate.
 * @param {Array} courses - Array of course objects {name: string}.
 */
export function populateCourseSelect(select, courses) {
    if (!select) return;

    select.disabled = true;
    select.innerHTML = '<option value="">Select a course</option>';

    for (const course of courses) {
        const option = document.createElement('option');
        option.value = course.name;
        option.textContent = course.name;
        select.appendChild(option);
    }

    select.disabled = false;
}

/**
 * Main function to fetch and populate courses for a given degree course select element.
 * @param {HTMLSelectElement} degreeSelect - The select for the degree course.
 * @param {HTMLSelectElement} courseSelect - The select to populate with courses.
 * @param {string} token - Authorization token.
 */
export async function uploadCourses(degreeSelect, courseSelect, token) {
    if (!degreeSelect || !courseSelect) return;

    const degreeCourseName = degreeSelect.value;

    // Optional: show temporary loading message
    courseSelect.innerHTML = '<option>Loading courses...</option>';

    const courses = await fetchCourses(degreeCourseName, token);
    populateCourseSelect(courseSelect, courses);
}




