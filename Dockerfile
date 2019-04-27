FROM tomcat
ADD  target/pass-doi-service.war /usr/local/tomcat/webapps/
CMD ["catalina.sh", "run"]
EXPOSE 8080


