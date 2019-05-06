FROM tomcat

EXPOSE ${pass.doi.service.port}

ADD  target/pass-doi-service.war /usr/local/tomcat/webapps/

CMD ["catalina.sh", "run"]
