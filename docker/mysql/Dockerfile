FROM mysql:5.7

COPY src/main/resources/database/mysql/setupDatabase.sql .
RUN chmod 644 setupDatabase.sql

ENV MYSQL_DATABASE=openolat
ENV MYSQL_USER=openolat
ENV MYSQL_PASSWORD=openolat
ENV MYSQL_ROOT_PASSWORD=root

RUN /etc/init.d/mysql start && \
    mysql -u root --password=${MYSQL_ROOT_PASSWORD} -e "CREATE DATABASE IF NOT EXISTS ${MYSQL_DATABASE};" && \
    mysql -u root --password=${MYSQL_ROOT_PASSWORD} -e "GRANT ALL PRIVILEGES ON ${MYSQL_USER}.* TO '${MYSQL_DATABASE}' IDENTIFIED BY '${MYSQL_PASSWORD}';" && \
    mysql -u root --password=${MYSQL_ROOT_PASSWORD} -e "UPDATE mysql.user SET HOST='localhost' WHERE USER ='${MYSQL_USER}' AND HOST='%'" && \
    mysql -u root --password=${MYSQL_ROOT_PASSWORD} -e "FLUSH PRIVILEGES;" && \
    mysql -u ${MYSQL_USER} --password=${MYSQL_PASSWORD} -D ${MYSQL_DATABASE} < setupDatabase.sql