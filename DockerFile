FROM gradle:7.6.1-jdk17 as builder
WORKDIR /app
COPY build.gradle settings.gradle /app/
COPY gradle /app/gradle
COPY src /app/src
RUN pwd && ls -la
RUN gradle build --no-daemon

FROM openjdk:17-jdk-slim-buster
WORKDIR /app
RUN pwd && ls -la
COPY --from=builder /app/build/libs/rurban-0.0.1-SNAPSHOT.jar /app/rurban.jar
EXPOSE 8080
CMD ["java", "-jar", "rurban.jar"]