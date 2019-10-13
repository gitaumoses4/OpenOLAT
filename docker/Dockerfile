FROM tomcat:9-jdk8

LABEL MAINTAINER="gitaumoses4@gmail.com"
LABEL APPLICATION="edudoor_backend"

ENV TERM=xterm-256color

COPY target/openolat*.war $CATALINA_HOME/webapps/openolat.war

ENTRYPOINT ["catalina.sh", "run"]