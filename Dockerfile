FROM openjdk:17
WORKDIR /app
COPY build/libs/*.jar /app/app.jar
COPY src/main/resources/yml/application-secret.yml src/main/resources/yml/application-secret.yml
COPY src/main/resources/yml/application-dev.yml src/main/resources/yml/application-dev.yml
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=dev
CMD ["java", "-jar", "app.jar"]
