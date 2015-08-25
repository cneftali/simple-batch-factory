# batch-import-service
#
# VERSION               1.0-SNAPSHOT
FROM relateiq/oracle-java8
MAINTAINER Cyril Neftali <cneftali@gmail.com>
LABEL Description="This image is used to start the batch-import-service executable" Vendor="" Version="1.0-SNAPSHOT"

VOLUME ["/tmp", "/data"]
ADD target/batch-import-service-1.0-SNAPSHOT.jar /opt/batch-import-service/batch-import-service.jar
EXPOSE 8080
EXPOSE 1221

WORKDIR /opt/batch-import-service/
RUN bash -c 'touch batch-import-service.jar'
CMD java -Djava.rmi.server.hostname=localhost -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=1221 -Dspring.profiles.active=docker -Xmx1024m -jar batch-import-service.jar