FROM tomcat:8.0-alpine
LABEL maintainer="jrm@jhu.edu"
ADD doi-service.war /usr/local/tomcat/webapps/
EXPOSE 8090
CMD ["catalina.sh", "run"]

