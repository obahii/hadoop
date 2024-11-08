NN = 2

all : up


up : 
	docker-compose up -d --scale datanode=$(NN)

down :
	docker-compose down

clean : down
	docker container rm $$(docker container ls -aq)
	docker volume rm $$(docker volume ls -q)
	docker network rm $$(docker network ls -q)

fclean : down
	docker image rm  $$(docker image ls -aq)
	docker system prune -a --force

re : down fclean all