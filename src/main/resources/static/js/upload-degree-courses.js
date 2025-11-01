// upload-degree-courses.js

/**
 * Waits until the HTML document is fully loaded, then loads degree courses.
 */
document.addEventListener('DOMContentLoaded', async () => {
    const select = document.getElementById('degreeCourse');
    if (!select) {
        console.warn('⚠️ <select id="degreeCourse"> not found in DOM.');
        return;
    }

    await loadDegreeCourses(select);
});

/**
 * Fetches degree courses from the backend and populates the select element.
 * Includes basic caching in sessionStorage to avoid redundant requests.
 *
 * @param {HTMLSelectElement} select - The select element to populate.
 */
async function loadDegreeCourses(select) {
    const cacheKey = 'degreeCourses_cache';
    const cachedData = sessionStorage.getItem(cacheKey);

    // ✅ Use cache if available
    if (cachedData) {
        console.info('📦 Using cached degree courses.');
        populateSelect(select, JSON.parse(cachedData));
        return;
    }

    // Disable and show loading message
    select.disabled = true;
    select.innerHTML = '<option>Loading degree courses...</option>';

    try {
        const response = await fetch('/api/v1/degree-course/read/all/ajax', {
            method: 'GET',
            headers: { 'Accept': 'application/json' }
        });

        if (!response.ok) {
            throw new Error(`HTTP ${response.status} - ${response.statusText}`);
        }

        const degreeCourses = await response.json();

        // Cache results for this session
        sessionStorage.setItem(cacheKey, JSON.stringify(degreeCourses));

        populateSelect(select, degreeCourses);

        console.info(`✅ Loaded ${degreeCourses.length} degree courses successfully.`);
    } catch (error) {
        console.error('❌ Failed to load degree courses:', error);
        select.innerHTML = '<option disabled>Error loading degree courses</option>';
    } finally {
        select.disabled = false;
    }
}

/**
 * Populates the given select element with degree course options.
 *
 * @param {HTMLSelectElement} select - The select element to populate.
 * @param {Array<Object>} degreeCourses - List of degree courses from the backend.
 */
function populateSelect(select, degreeCourses) {
    select.innerHTML = '<option value="">-- Select a degree course --</option>';

    if (!Array.isArray(degreeCourses) || degreeCourses.length === 0) {
        const option = document.createElement('option');
        option.textContent = 'No degree courses available';
        option.disabled = true;
        select.appendChild(option);
        return;
    }

    for (const course of degreeCourses) {
        const option = document.createElement('option');
        option.value = course.name;
        option.textContent = course.name;
        select.appendChild(option);
    }
}
