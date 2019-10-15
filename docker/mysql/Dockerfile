FROM mysql:5.7

COPY src/main/resources/database/mysql/setupDatabase.sql /docker-entrypoint-initdb.d/setupDatabase.sql

ENV MYSQL_DATABASE=openolat
ENV MYSQL_USER=openolat
ENV MYSQL_PASSWORD=openolat
ENV MYSQL_ROOT_PASSWORD=root
