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
	docker build -t email-api .
	chmod -R ugo+rw target/	
run: clean
	docker run -p 8084:8084 -m 128M --network net -e JAVA_OPTS='-XX:+UseSerialGC -Xss512k -XX:MaxRAM=72m' \
		--env-file ./.env --name=email-api email-api
start:
	docker start email-api
bash:
	docker container exec -i -t --user root email-api bash
logs:
	docker logs email-api

heroku-docker-deploy: heroku-create-app
	heroku container:push web --app=master-email-api
	heroku container:release web --app=master-email-api
heroku-maven-deploy:
	mvn clean heroku:deploy-war 
	chmod -R ugo+rw target/
heroku-logs:
	heroku logs --app=jb-email-api
