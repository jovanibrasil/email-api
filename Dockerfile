FROM payara/micro
#FROM tomcat
LABEL maintainer="jovanibrasil@gmail.com"

USER root

ARG SMTP_USER 
ARG SMTP_PASSWORD 
ARG SMTP_HOST
ARG POP3_USER
ARG POP3_PASSWORD
ARG POP3_HOST
ARG PORT=8080

ENV SMTP_USER=$SMTP_USER
ENV SMTP_PASSWORD=$SMTP_PASSWORD
ENV SMTP_HOST=$SMTP_HOST
ENV POP3_USER=$POP3_USER
ENV POP3_PASSWORD=$POP3_PASSWORD
ENV POP3_HOST=$POP3_HOST
ENV PORT=$PORT

ADD ./target/email-api.war /email-api.war

#RUN echo "Port:${PORT}"
#COPY ./target/email-api.war /usr/local/tomcat/webapps/email-api.war
   
EXPOSE 8080
   
RUN echo "java -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -jar payara-micro.jar --port $PORT --deploy /email-api.war" > /start.sh
 
ENTRYPOINT ["/bin/sh", "/start.sh"] 
	
#"-XshowSettings:vm"
CMD ["--deploy", "/email-api.war"]
