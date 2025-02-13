# Utilizza l'immagine di base per Java 22
FROM alpine/java:22-jdk

# Imposta la directory di lavoro
WORKDIR /app

# Copia il file pom.xml, il codice sorgente e la directory per maven
COPY pom.xml .
COPY src /app/src
COPY maven /usr/local/maven

# Imposta le variabili di ambiente
ENV MAVEN_HOME /usr/local/maven

ENV PATH ${MAVEN_HOME}/bin:${PATH}

# Esegui il comando Maven per compilare e pacchettizzare l'applicazione
RUN /usr/local/maven/apache-maven-3.9.9/bin/mvn clean package -Dmaven.test.skip=true

# Copia il file JAR compilato nella directory di lavoro
COPY target/student-management-system-0.0.1-SNAPSHOT.jar /app.jar

# Imposta le variabili di ambiente per la connessione al database
ENV DB_PASSWORD=${DB_PASSWORD}
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://127.0.0.1:5432/student
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}

# Imposta la variabile di ambiente per l'applicazione
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Esegui l'applicazione
CMD ["java", "-jar", "/app.jar"]