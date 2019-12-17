FROM maven:3.6.3-slim as builder
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN mvn clean package

FROM adoptopenjdk:11-jre-hotspot
COPY --from=builder /usr/src/app/target/*.jar application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]