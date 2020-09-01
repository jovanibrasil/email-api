FROM adoptopenjdk/openjdk11:latest
# Run with non-root privileges to mitigate some risks
#RUN addgroup -S spring && adduser -S spring -G spring
#USER spring:spring
ARG JAR_FILE=target/*.jar

ENV EMAIL_SERVICE_USER=
ENV EMAIL_SERVICE_PASSWORD= 
ENV EMAIL_SERVICE_HOST=
ENV PORT=

COPY ${JAR_FILE} email-api.jar
ENTRYPOINT ["java","-jar","/email-api.jar"]
