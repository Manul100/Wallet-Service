FROM gradle:7.6-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle build --no-daemon -x test
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]