// upload-professors.js

/**
 * Wait until the page is fully loaded before executing the function.
 */
document.addEventListener('DOMContentLoaded', async () => {
    const select = document.getElementById('professor');
    if (!select) {
        console.warn('⚠️ <select id="professor"> not found in DOM.');
        return;
    }

    await loadProfessors(select);
});

/**
 * Fetches professors from the backend and populates the select element.
 * @param {HTMLSelectElement} select - The select element to populate.
 */
async function loadProfessors(select) {
    // Disable select and show loading message
    select.disabled = true;
    select.innerHTML = '<option>Loading professors...</option>';

    try {
        const response = await fetch('/api/v1/professor/read/all/ajax', {
            method: 'GET',
            headers: { 'Accept': 'application/json' }
        });

        if (!response.ok) {
            throw new Error(`HTTP ${response.status} - ${response.statusText}`);
        }

        const professors = await response.json();

        // Clear and repopulate
        select.innerHTML = '<option value="">-- Select a professor --</option>';

        if (professors.length === 0) {
            const opt = document.createElement('option');
            opt.textContent = 'No professors available';
            opt.disabled = true;
            select.appendChild(opt);
            return;
        }

        for (const professor of professors) {
            const option = document.createElement('option');
            option.value = professor.professorCode;
            option.textContent = professor.fullName;
            select.appendChild(option);
        }

        console.info(`✅ Loaded ${professors.length} professors successfully.`);
    } catch (error) {
        console.error('❌ Failed to load professors:', error);
        select.innerHTML = '<option disabled>Error loading professors</option>';
    } finally {
        select.disabled = false;
    }
}


