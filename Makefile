#!/usr/bin/make

# Run the script with -E flag: to preserve current environment

SHELL := /bin/bash
RC := ~/.bashrc

stop:
	- docker stop email-api
clean: stop
	- docker rm email-api
build: clean
	mvn clean package -Dmaven.test.skip=true
	sudo docker build --build-arg SMTP_USER=$$SMTP_USER --build-arg BRANCH --build-arg SMTP_PASSWORD=$$SMTP_PASSWORD  \
	 --build-arg SMTP_HOST=$$SMTP_HOST --build-arg POP3_USER=$$POP3_USER --build-arg POP3_PASSWORD=$$POP3_PASSWORD \
	 --build-arg POP3_HOST=$$POP3_HOST --network net -t email-api .
	chmod -R ugo+rw target/	
run: clean
	docker run -p 8084:8084 -m 256M --name=email-api -d email-api
start:
	docker start email-api
bash:
	docker container exec -i -t --user root email-api bash
logs:
	docker logs email-api
deploy-jar:
	java -jar ~/servers/payara-micro-5.191.jar --deploy ./email-api/target/email-api.war 
package:
	mvn package
run-tests:
	docker run -t postman/newman_alpine33 run https://www.getpostman.com/collections/94994e4c0de7a688aa51

heroku-docker-deploy: heroku-create-app
	heroku container:push web --app=master-email-api
	heroku container:release web --app=master-email-api
heroku-maven-deploy:
	#heroku config:add java_opts='-Xmx384m -Xms384m -Xss512k -XX:+UseCompressedOops' --app=master-email-api
	#heroku config:add JAVA_OPTS='-Xmx384m -Xms384m -Xss512k -XX:+UseCompressedOops' --app=master-email-api
	mvn clean heroku:deploy-war 
	chmod -R ugo+rw target/
heroku-logs:
	heroku logs --app=jb-email-api
