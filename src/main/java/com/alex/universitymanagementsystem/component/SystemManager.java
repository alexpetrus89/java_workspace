package com.alex.universitymanagementsystem.component;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.alex.universitymanagementsystem.UniversityManagementSystemApplication;

@Component
public class SystemManager {


    private static final Logger logger = LoggerFactory.getLogger(SystemManager.class);

    private static final String APPLICATION_CONTEXT_ERROR = "No application context available to restart.";

    private static ConfigurableApplicationContext context;
    private static String[] args;

    private SystemManager() {
        // Private constructor to prevent instantiation
    }

    public static void setContext(ConfigurableApplicationContext ctx) {
        context = ctx;
    }

    public static void setArgs(String[] mainArgs) {
        args = mainArgs;
    }


    public static synchronized void restartByJVM() throws URISyntaxException, IOException {

        if (context == null) {
            logger.warn(APPLICATION_CONTEXT_ERROR);
            return;
        }

        try {
            File currentJar = new File(SystemManager.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI());

            boolean isJar = currentJar.getName().endsWith(".jar");

            if (!isJar) {
                logger.warn("Running from IDE (not a jar). Restarting in-process...");
                new Thread(() -> {
                    try {
                        SpringApplication app = new SpringApplication(UniversityManagementSystemApplication.class);
                        app.setAdditionalProfiles(context.getEnvironment().getActiveProfiles());
                        app.run(args);
                    } catch (Exception e) {
                        logger.error("Failed to restart application in IDE", e);
                    }
                }, "In-IDE-Restart-Thread").start();

                SpringApplication.exit(context, () -> 0);
                return;
            }

            logger.info("Running from jar. Restarting via new JVM process...");
            List<String> command = new ArrayList<>();
            command.add(System.getProperty("java.home") + "/bin/java");
            command.add("-jar");
            command.add(currentJar.getPath());
            command.addAll(Arrays.asList(args));

            new ProcessBuilder(command)
                .inheritIO() // mostra log della nuova JVM
                .start();

            SpringApplication.exit(context, () -> 0);

        } catch (URISyntaxException e) {
            logger.error("Failed to determine application jar location", e);
        } catch (IOException e) {
            logger.error("Failed to restart application", e);
        }
    }


    public static synchronized void restartWithScript() {
        if (context == null) {
            logger.warn(APPLICATION_CONTEXT_ERROR);
            return;
        }

        try {
            // chiudi il contesto Spring
            SpringApplication.exit(context, () -> 0);
            logger.info("Application context closed. Launching restart script...");

            // determina il comando in base al sistema operativo
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder pb;

            if (os.contains("win")) {
                // Windows PowerShell
                String command = "powershell.exe mvn clean spring-boot:run -X -e --debug";
                pb = new ProcessBuilder("cmd.exe", "/c", command);
            } else {
                // Linux/macOS
                String command = "mvn clean spring-boot:run -X -e --debug";
                pb = new ProcessBuilder("sh", "-c", command);
            }

            // opzionale: eredita output console
            pb.inheritIO();
            pb.start();

            logger.info("Restart script executed successfully.");
        } catch (Exception e) {
            logger.error("Failed to restart application via script", e);
        }
    }



    /**
     * Restarts the application gracefully.
     * Compatible with Spring Boot DevTools.
     */
    public static synchronized void restart() {
        if (context == null) {
            logger.warn(APPLICATION_CONTEXT_ERROR);
            return;
        }

        // Use SpringApplication.exit first, then start a new JVM thread if needed
        int exitCode = SpringApplication.exit(context, () -> 0);
        logger.info("Application context closed. Exit code: {}", exitCode);

        // Avoid DevTools classloader issues: start a new JVM process
        new Thread(() -> {
            try {
                logger.info("Starting new application instance...");
                SpringApplication app = new SpringApplication(UniversityManagementSystemApplication.class);

                // Mantieni i profili attivi
                if (context != null)
                    app.setAdditionalProfiles(context.getEnvironment().getActiveProfiles());

                // run app
                app.run(args); // args salvati in SystemManager

                logger.info("Application restarted successfully.");
            } catch (Exception e) {
                logger.error("Failed to restart application", e);
            }
        },
        "Spring Boot DevTools Restart Thread")
        .start();
    }


    /**
     * Shuts down the application.
     */
    public static void shutdown() {
        if (context != null) {
            logger.info("Shutting down application...");
            int exitCode = SpringApplication.exit(context, () -> 0);
            logger.info("Exit code: {}", exitCode);
            System.exit(exitCode);
        } else {
            logger.warn("No application context available to shut down.");
        }
    }


}

