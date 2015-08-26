# engine-service
#
# VERSION               1.0-SNAPSHOT
FROM relateiq/oracle-java8
MAINTAINER Cyril Neftali <cneftali@gmail.com>
LABEL Description="This image is used to start the engine-service executable" Vendor="" Version="1.0-SNAPSHOT"

VOLUME ["/tmp", "/data"]
ADD target/engine-service-1.0-SNAPSHOT.jar /opt/batch-import-service/engine-service.jar
EXPOSE 8080
EXPOSE 1221

WORKDIR /opt/engine-service/
RUN bash -c 'touch engine-service.jar'
CMD java -Djava.rmi.server.hostname=localhost -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=1221 -Dspring.profiles.active=docker -Xmx1024m -jar engine-service.jar