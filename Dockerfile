FROM maven:3.9.11-eclipse-temurin-24-noble AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src
RUN java -version
RUN javac -version
RUN mvn clean package -DskipTests
RUN ls -la .

FROM eclipse-temurin:24-jre-alpine AS runtime

WORKDIR /app

COPY --from=build  /app/target/*.jar ./app.jar

# Expose port 8080
EXPOSE 8080

# Health check for Spring Boot actuator
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# JVM optimizations for Java 24 + Spring Boot
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+UseZGC", \
    "-Dspring.profiles.active=container", \
    "-jar", "app.jar"]