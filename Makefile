#!/usr/bin/make

# Run the script with -E flag: to preserve current environment

SHELL := /bin/bash
RC := ~/.bashrc

stop:
	- docker stop payara
clean: stop
	- docker rm payara
build: clean
	mvn clean package -Dmaven.test.skip=true
	sudo docker build --build-arg SMTP_USER --build-arg BRANCH --build-arg SMTP_PASSWORD \
	 --build-arg SMTP_HOST --build-arg POP3_USER=$$POP3_USER --build-arg POP3_PASSWORD=$$POP3_PASSWORD \
	 --build-arg POP3_HOST=$$POP3_HOST --build-arg PORT="8082" --network net -t payara .
	chmod -R ugo+rw target/	
run: clean
	docker run -p 8080:8082 -m 256M --name=payara -d payara
start:
	docker start payara 
bash:
	docker container exec -i -t --user root payara bash
logs:
	docker logs payara
deploy-jar:
	java -jar ~/servers/payara-micro-5.191.jar --deploy ./email-api/target/email-api.war 
package:
	mvn package
run-tests:
	docker run -t postman/newman_alpine33 run https://www.getpostman.com/collections/94994e4c0de7a688aa51
heroku-deploy:
	- heroku destroy master-email-api --confirm master-email-api
	heroku create master-email-api
	heroku container:push web --app=master-email-api
	heroku container:release web --app=master-email-api
heroku-logs:
	heroku logs --app=master-email-api
