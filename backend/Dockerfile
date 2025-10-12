# ====== Build stage ======
FROM gradle:8.10.2-jdk21 AS build
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .
# Build a fat JAR (skip tests for speed)
RUN gradle clean bootJar -x test

# ====== Runtime stage ======
FROM eclipse-temurin:21-jre
WORKDIR /app
# Copy the built jar
COPY --from=build /home/gradle/project/build/libs/*SNAPSHOT.jar app.jar
# (Optional) JVM opts controlled via env
ENV JAVA_OPTS=""
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
