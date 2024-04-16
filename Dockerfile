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

# FROM gradle:7.6.1-alpine AS build
# COPY . .
# RUN gradle build --no-daemon
# FROM openjdk:17-jdk-slim-buster
# EXPOSE 8080
# RUN pwd
# RUN mkdir /app
# COPY --from=build /build/libs/rurban-0.0.1-SNAPSHOT.jar app.jar
# ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]

# FROM gradle:7.6.1-jdk17 as builder
# WORKDIR /app
# COPY build.gradle settings.gradle /app/
# COPY gradle /app/gradle
# COPY src /app/src
# RUN gradle build --no-daemon
#
# FROM openjdk:17-jdk-slim-buster
# WORKDIR /app
# COPY --from=builder /app/build/libs/expense-tracker-be-0.0.1-SNAPSHOT /app/tracker-expense-be.jar
# EXPOSE 8080
# CMD ["java", "-jar", "tracker-expense-be.jar"]
