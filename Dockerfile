FROM tomcat

ENV PASS_FEDORA_USER=${pass.fedora.user} \
PASS_FEDORA_PASSWORD=${pass.fedora.password} \
PASS_FEDORA_BASEURL=http://fcrepo:${fcrepo.http.port}/fcrepo/rest/ \
PASS_ELASTICSEARCH_URL=http://elasticsearch:9200/pass/ \
PASS_ELASTICSEARCH_LIMIT=${pass.elasticsearch.limit}

EXPOSE ${pass.doi.service.port}

ADD  target/pass-doi-service.war /usr/local/tomcat/webapps/

CMD ["catalina.sh", "run"]
