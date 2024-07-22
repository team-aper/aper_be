FROM openjdk:17
WORKDIR /app
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /app/app.jar
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=dev
CMD ["java","-jar","app.jar"]
