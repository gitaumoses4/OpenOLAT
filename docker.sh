sudo docker-compose -f docker/docker-compose.yml build edudoor_backend
sudo docker volume create --name=edudoor_data
sudo docker-compose -f docker/docker-compose.yml up
