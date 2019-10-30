# Project variables
PROJECT_NAME ?= edudoor-backend

# File names
DOCKER_DEV_COMPOSE_FILE := docker/docker-compose.yml

api:
	${INFO} "Building the backend API"
	@ mvn package -DskipTests=true
	${INFO} "Setting up the docker image"
	@ docker login
	@ docker-compose -f ${DOCKER_DEV_COMPOSE_FILE} build edudoor_backend
	@ ${INFO} "Tagging the docker image"
	@ docker tag docker_edudoor_backend:latest gitaumoses4/openolat-edudoor
	@ ${INFO} "Pushing the image to docker hub"
	@ docker push gitaumoses4/openolat-edudoor
	@ ${SUCCESS} "Image has been successfully deployed to dockerhub"


# COLORS
GREEN	:= $(shell tput -Txterm setaf 2)
YELLOW 	:= $(shell tput -Txterm setaf 3)
WHITE	:= $(shell tput -Txterm setaf 7)
NC		:= "\e[0m"
RESET 	:= $(shell tput -Txterm sgr0)

# SHELL FUNCTIONS
INFO 	:= @bash -c 'printf "\n"; printf $(YELLOW); echo "===> $$1"; printf "\n"; printf $(NC)' SOME_VALUE
SUCCESS	:= @bash -c 'printf "\n"; printf $(GREEN); echo "===> $$1"; printf "\n"; printf $(NC)' SOME_VALUE
