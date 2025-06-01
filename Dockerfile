# This Dockerfile is only sample for dev environment
# please adjust it further using your own configurations
FROM openjdk:21-jdk-slim

WORKDIR /app
COPY target/*.jar /app/service.jar

EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=dev
#ENV SHOW_SQL_QUERIES=true

CMD ["java", "-jar", "service.jar", "--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
