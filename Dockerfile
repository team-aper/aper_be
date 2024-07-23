FROM openjdk:17
WORKDIR /app
COPY build/libs/*.jar /app/app.jar
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=dev
CMD ["java", "-jar", "app.jar"]
