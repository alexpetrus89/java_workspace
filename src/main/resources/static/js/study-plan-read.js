document.addEventListener("DOMContentLoaded", () => {
    const toggle = document.getElementById("theme-toggle");
    const root = document.documentElement;

    // === THEME HANDLING ===
    const darkMode = localStorage.getItem("theme") === "dark";
    root.classList.toggle("dark", darkMode);
    toggle.innerHTML = darkMode ? '🌞' : '🌙';

    toggle.addEventListener("click", () => {
        const isDark = root.classList.toggle("dark");
        localStorage.setItem("theme", isDark ? "dark" : "light");
        toggle.innerHTML = isDark ? '🌞' : '🌙';
    });

    // === PROGRESS BAR DYNAMIC LOAD ===
    const progressBar = document.getElementById("progress-bar");
    const progressInfo = document.getElementById("progress-info");
    const totalCFUElement = document.getElementById("total-cfu");

    const totalCFU = Number.parseInt(totalCFUElement?.textContent || "180", 10);
    progressInfo.textContent = "⏳ Loading your academic progress...";

    fetch("/api/v1/examination/read/student/register/ajax")
        .then(response => {
            if (!response.ok) throw new Error("HTTP error " + response.status);
            return response.json();
        })
        .then(examinations => {
            if (!Array.isArray(examinations)) throw new Error("Invalid JSON response");

            const completedCFU = examinations
                .filter(e => e.grade >= 18)
                .reduce((sum, e) => sum + (e.courseCfu || 0), 0);

            const percentage = Math.min((completedCFU / totalCFU) * 100, 100);

            // Reset width for animation
            progressBar.style.width = "0%";
            progressBar.style.transition = "width 1.5s ease-in-out";

            requestAnimationFrame(() => {
                progressBar.style.width = `${percentage}%`;
            });

            progressInfo.textContent = `🎓 Completed ${completedCFU}/${totalCFU} CFU (${percentage.toFixed(1)}%)`;

            if (percentage < 40) {
                progressBar.style.backgroundColor = "#ef4444"; // red
            } else if (percentage < 80) {
                progressBar.style.backgroundColor = "#f59e0b"; // orange
            } else {
                progressBar.style.backgroundColor = "#22c55e"; // green
            }
        })
        .catch(err => {
            console.error("Error fetching student examinations:", err);
            progressInfo.textContent = "⚠️ Unable to load progress. Please try again later.";
            progressBar.style.backgroundColor = "#6b7280";
        });
});



